package com.example.projektsmb;

public class ProductClassFirebase {
    private String name;
    private double price;
    private long productId;

    private String cartId;

    public ProductClassFirebase(String name, double price, long productId) {
        this.name = name;
        this.price = price;
        this.productId = productId;
    }

    public ProductClassFirebase(String name, double price, long productId, String cartId) {
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.cartId = cartId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
