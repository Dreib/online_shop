package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistence.ProductRepository;
import org.fasttrackit.onlineshop.transfer.product.GetProductsRequest;
import org.fasttrackit.onlineshop.transfer.product.ProductResponse;
import org.fasttrackit.onlineshop.transfer.product.SaveProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(SaveProductRequest request) {
        LOGGER.info("Creating product {}", request);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImageUrl(request.getImageUrl());
        product.setQuantity(request.getQuantity());

        Product savedProduct = productRepository.save(product);

        return mapProductResponse(savedProduct);
    }

    public Product getProduct(long id) {
        LOGGER.info("Retrieving product {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product " + id + " does not exist"));
    }

    public ProductResponse getProductResponse(long id) {
        Product product = getProduct(id);
        return mapProductResponse(product);
    }

    public Page<ProductResponse> getProducts(GetProductsRequest request, Pageable pageable) {
        LOGGER.info("Retrieving products: {}", request);

        Product exampleProduct = new Product();
        exampleProduct.setName(request.getPartialName());
        exampleProduct.setDescription(request.getPartialDescription());
        exampleProduct.setQuantity(request.getMinQuantity());
        exampleProduct.setPrice(request.getPrice());
        exampleProduct.setCarts(null);

        Example<Product> example = Example.of(exampleProduct,
                ExampleMatcher.matchingAny()
                        .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()));
        //todo: add matcher for minimum quantity

        //query by example
        Page<Product> productsPage = productRepository.findAll(example, pageable);

        List<ProductResponse> productDTOs = new ArrayList<>();

        for(Product product: productsPage.getContent()) {
            ProductResponse productResponse = mapProductResponse(product);
            productDTOs.add(productResponse);
        }

        return new PageImpl<>(productDTOs, pageable, productsPage.getTotalElements());
    }

    private ProductResponse mapProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();

        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setPrice(product.getPrice());
        productResponse.setDescription(product.getDescription());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setQuantity(product.getQuantity());

        return productResponse;
    }
}
