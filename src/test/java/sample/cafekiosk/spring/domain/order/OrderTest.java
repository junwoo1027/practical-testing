package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = List.of(
                new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                new Product("002", ProductType.HANDMADE, ProductSellingStatus.SELLING, "카페라떼", 4500)
        );

        // when
        Order result = Order.create(products, now);

        // then
        assertThat(result.getTotalPrice()).isEqualTo(8500);
    }

    @DisplayName("주문 생성 시 주문상태는 INIT 이다.")
    @Test
    void init() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = List.of(
                new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                new Product("002", ProductType.HANDMADE, ProductSellingStatus.SELLING, "카페라떼", 4500)
        );

        // when
        Order result = Order.create(products, now);

        // then
        assertThat(result.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTime() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = List.of(
                new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                new Product("002", ProductType.HANDMADE, ProductSellingStatus.SELLING, "카페라떼", 4500)
        );

        // when
        Order result = Order.create(products, now);

        // then
        assertThat(result.getRegisteredDateTime()).isEqualTo(now);
    }

}