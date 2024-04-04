package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;
    
    @Autowired
    private OrderService orderService;

    @AfterEach
    void clear() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        Product product1 = new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        CreateOrderRequest request = new CreateOrderRequest(List.of("001", "002"));
        LocalDateTime registeredDateTime = LocalDateTime.of(2024, 4, 4, 0 ,0);

        // when
        OrderResponse result = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.registeredDateTime()).isEqualTo(registeredDateTime);
        assertThat(result.totalPrice()).isEqualTo(8500);
        assertThat(result.products()).hasSize(2);
        assertThat(result.products())
                .extracting("ProductNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("002", 4500)
                );
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        Product product1 = new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        CreateOrderRequest request = new CreateOrderRequest(List.of("001", "001"));
        LocalDateTime registeredDateTime = LocalDateTime.of(2024, 4, 4, 0 ,0);

        // when
        OrderResponse result = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.registeredDateTime()).isEqualTo(registeredDateTime);
        assertThat(result.totalPrice()).isEqualTo(8000);
        assertThat(result.products()).hasSize(2);
        assertThat(result.products())
                .extracting("ProductNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
                );
    }

}