package sample.cafekiosk.spring.api.service.order;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.order.request.CreateOrderRequest;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private OrderService orderService;

    @AfterEach
    void clear() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
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
        OrderResponse result = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.registeredDateTime()).isEqualTo(registeredDateTime);
        assertThat(result.totalPrice()).isEqualTo(8500);
        assertThat(result.products()).hasSize(2);
        assertThat(result.products())
                .extracting("productNumber", "price")
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
        OrderResponse result = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.registeredDateTime()).isEqualTo(registeredDateTime);
        assertThat(result.totalPrice()).isEqualTo(8000);
        assertThat(result.products()).hasSize(2);
        assertThat(result.products())
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000)
                );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        Product product1 = new Product("001", ProductType.BOTTLE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.BAKERY, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = new Stock("001", 2);
        Stock stock2 = new Stock("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        CreateOrderRequest request = new CreateOrderRequest(List.of("001", "001", "002", "003"));
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when
        OrderResponse result = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

        // then
        assertThat(result.id()).isNotNull();
        assertThat(result.totalPrice()).isEqualTo(17500);
        assertThat(result.products()).hasSize( 4);
        assertThat(result.products())
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 4000),
                        tuple("001", 4000),
                        tuple("002", 4500),
                        tuple("003", 5000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2);
        assertThat(stocks)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock() {
        // given
        Product product1 = new Product("001", ProductType.BOTTLE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.BAKERY, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = new Stock("001", 1);
        Stock stock2 = new Stock("002", 1);
        stockRepository.saveAll(List.of(stock1, stock2));

        CreateOrderRequest request = new CreateOrderRequest(List.of("001", "001", "002", "003"));
        LocalDateTime registeredDateTime = LocalDateTime.now();

        // when && then
        assertThatThrownBy(() -> orderService.createOrder(request.toServiceRequest(), registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족한 상품이 있습니다.");
    }

}