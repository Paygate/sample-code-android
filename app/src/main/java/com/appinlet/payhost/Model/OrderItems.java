package com.appinlet.payhost.Model;

import lombok.Builder;

@Builder
public class OrderItems {

    private String ProductCode;
    private String ProductDescription;
    private String ProductCategory;
    private String ProductRisk;
    private String OrderQuantity;
    private String UnitPrice;
    private String Currency;

    public OrderItems(String productCode, String productDescription, String productCategory, String productRisk, String orderQuantity, String unitPrice, String currency) {
        ProductCode = productCode;
        ProductDescription = productDescription;
        ProductCategory = productCategory;
        ProductRisk = productRisk;
        OrderQuantity = orderQuantity;
        UnitPrice = unitPrice;
        Currency = currency;
    }

    // Getter Methods

    public String getProductCode() {
        return ProductCode;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public String getProductRisk() {
        return ProductRisk;
    }

    public String getOrderQuantity() {
        return OrderQuantity;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public String getCurrency() {
        return Currency;
    }

    // Setter Methods

    public void setProductCode(String ProductCode) {
        this.ProductCode = ProductCode;
    }

    public void setProductDescription(String ProductDescription) {
        this.ProductDescription = ProductDescription;
    }

    public void setProductCategory(String ProductCategory) {
        this.ProductCategory = ProductCategory;
    }

    public void setProductRisk(String ProductRisk) {
        this.ProductRisk = ProductRisk;
    }

    public void setOrderQuantity(String OrderQuantity) {
        this.OrderQuantity = OrderQuantity;
    }

    public void setUnitPrice(String UnitPrice) {
        this.UnitPrice = UnitPrice;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }
}