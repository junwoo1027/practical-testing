package sample.cafekiosk.spring.domain.product;

public enum ProductType {

    HANDMADE("제조 음료"),
    BOTTLE("병 음료"),
    BAKERY("베이커리");

    private String text;

    ProductType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
