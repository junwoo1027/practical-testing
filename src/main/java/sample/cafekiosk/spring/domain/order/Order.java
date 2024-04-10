package sample.cafekiosk.spring.domain.order;

import jakarta.persistence.*;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;
import sample.cafekiosk.spring.domain.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    protected Order() {
    }

    public Order(OrderStatus orderStatus, int totalPrice, LocalDateTime registeredDateTime) {
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
    }

    public Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registeredDateTime) {
        this.orderStatus = orderStatus;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getRegisteredDateTime() {
        return registeredDateTime;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return new Order(products, OrderStatus.INIT, registeredDateTime);
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream().mapToInt(Product::getPrice).sum();
    }
}
