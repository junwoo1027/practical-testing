package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.*;
import sample.cafekiosk.spring.domain.BaseEntity;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    protected Product() {
    }

    public Product(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public ProductType getType() {
        return type;
    }

    public ProductSellingStatus getSellingStatus() {
        return sellingStatus;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

}
