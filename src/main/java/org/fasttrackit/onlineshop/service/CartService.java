package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.Cart;
import org.fasttrackit.onlineshop.domain.Product;
import org.fasttrackit.onlineshop.domain.User;
import org.fasttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fasttrackit.onlineshop.persistence.CartRepository;
import org.fasttrackit.onlineshop.transfer.cart.AddProductToCartRequest;
import org.fasttrackit.onlineshop.transfer.cart.CartResponse;
import org.fasttrackit.onlineshop.transfer.cart.ProductInCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, UserService userService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional
    public void addProductToCart(AddProductToCartRequest request) {
        LOGGER.info("Adding product to cart: {}", request);

        Cart cart = cartRepository.findById(request.getUserID()).orElse(new Cart());

        if(cart.getUser() == null) {
            User user = userService.getUser(request.getUserID());
            cart.setUser(user);
        }

        Product product = productService.getProduct(request.getProductID());
        cart.addProduct(product);

        cartRepository.save(cart);
    }

    @Transactional
    public CartResponse getCart(long userId) {
        LOGGER.info("Retrieving cart for user {}", userId);

        Cart cart = cartRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart " + userId + " does not exist."));

        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());

        Set<ProductInCart> products = new HashSet<>();

        for(Product product: cart.getProducts()) {
            ProductInCart productInCart = new ProductInCart();
            productInCart.setId(product.getId());
            productInCart.setName(product.getName());
            productInCart.setPrice(product.getPrice());
            productInCart.setImageUrl(product.getImageUrl());

            products.add(productInCart);
        }

        cartResponse.setProducts(products);

        return cartResponse;
    }
}
