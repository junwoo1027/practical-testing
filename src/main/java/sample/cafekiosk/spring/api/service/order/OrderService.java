package sample.cafekiosk.spring.api.service.order;

import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderProductRepository orderProductRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.productRepository = productRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.productNumbers();
        List<Product> products = findProdutsBy(productNumbers);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = this.orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

    private List<Product> findProdutsBy(List<String> productNumbers) {
        List<Product> products = this.productRepository.findAllByProductNumberIn(productNumbers);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumbers.stream()
                .map(number -> productMap.get(number))
                .collect(Collectors.toList());
    }
}
