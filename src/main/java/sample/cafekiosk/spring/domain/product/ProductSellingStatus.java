package sample.cafekiosk.spring.domain.product;

import java.util.List;

public enum ProductSellingStatus {

    SELLING("판매중"),
    HOLD("판매 보류"),
    STOP_SELLING("판매 중지");

    private String text;

    ProductSellingStatus(String text) {
        this.text = text;
    }

    public static List<ProductSellingStatus> forDisplay() {
        return List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD);
    }

    public String getText() {
        return text;
    }

}
