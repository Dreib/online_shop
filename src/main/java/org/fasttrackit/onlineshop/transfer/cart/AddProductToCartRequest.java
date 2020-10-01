package org.fasttrackit.onlineshop.transfer.cart;

import javax.validation.constraints.NotNull;

public class AddProductToCartRequest {

    @NotNull
    private Long userID;
    @NotNull
    private Long productID;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    @Override
    public String toString() {
        return "AddProductToCartRequest{" +
                "userID=" + userID +
                ", productID=" + productID +
                '}';
    }
}
