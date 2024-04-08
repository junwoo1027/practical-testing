package sample.cafekiosk.spring.api.service.order.request;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

public record CreateProductRequest(
        ProductType type,
        ProductSellingStatus sellingStatus,
        String name,
        int price
) {

    public Product toEntity(String nextProductNumber) {
        return new Product(
                nextProductNumber,
                this.type(),
                this.sellingStatus(),
                this.name(),
                this.price()
        );
    }
}
