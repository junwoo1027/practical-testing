package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("해당 일자에 결제완료 된 주문을 조회한다.")
    void findOrdersBy() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 04, 11, 0, 0);
        Order order1 = new Order(OrderStatus.INIT, 1000, now);
        Order order2 = new Order(OrderStatus.PAYMENT_COMPLETED, 2000, now);
        Order order3 = new Order(OrderStatus.PAYMENT_COMPLETED, 3000, now.plusDays(1));
        orderRepository.saveAll(List.of(order1, order2, order3));

        // when
        List<Order> results = orderRepository.findOrdersBy(now, now.plusDays(1), OrderStatus.PAYMENT_COMPLETED);

        // then
        assertThat(results).hasSize(1);
        assertThat(results).extracting("orderStatus", "totalPrice")
                .containsExactlyInAnyOrder(
                        tuple(OrderStatus.PAYMENT_COMPLETED, 2000)
                );
    }

}