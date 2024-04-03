package sample.cafekiosk.spring.api.service.product.response;

import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

public record ProductResponse(
        Long id,
        String ProductNumber,
        ProductType type,
        ProductSellingStatus sellingStatus,
        String name,
        int price
) {

    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getProductNumber(),
                product.getType(),
                product.getSellingStatus(),
                product.getName(),
                product.getPrice()
        );
    }

}
