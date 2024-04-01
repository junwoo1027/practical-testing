package sample.cafekiosk.unit.order;

import sample.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private LocalDateTime orderAt;
    private List<Beverage> beverages;

    public Order(LocalDateTime orderAt, List<Beverage> beverages) {
        this.orderAt = orderAt;
        this.beverages = beverages;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public List<Beverage> getBeverages() {
        return beverages;
    }
}
