package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    @Test
    void add() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());

        // then
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);
        assertThat(cafeKiosk.getBeverages()).extracting("name").containsExactly("아메리카노", "라떼");
        assertThat(cafeKiosk.getBeverages()).extracting("price").containsExactly(4000, 4500);
    }

    @Test
    void addSeveralBeverages() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when
        cafeKiosk.add(new Americano(), 2);
        cafeKiosk.add(new Latte(), 2);

        // then
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(4);
        assertThat(cafeKiosk.getBeverages()).extracting("name").containsExactly("아메리카노", "아메리카노", "라떼", "라떼");
        assertThat(cafeKiosk.getBeverages()).extracting("price").containsExactly(4000, 4000, 4500, 4500);
    }

    @Test
    void addZeroBeverages() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when && then
        assertThatThrownBy(() -> cafeKiosk.add(new Americano(), 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    @Test
    void remove() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);

        //when
        cafeKiosk.remove(americano);

        //then
        assertThat(cafeKiosk.getBeverages().isEmpty());
    }

    @Test
    void clear() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        //when
        cafeKiosk.clear();

        //then
        assertThat(cafeKiosk.getBeverages().isEmpty());
    }

    @Test
    void createOrder() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 4, 2, 10, 0, 0);

        // when
        Order order = cafeKiosk.createOrder(currentDateTime);

        // then
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages()).extracting("name").containsExactly("아메리카노");
    }

    @Test
    void createOrderOutsizeOpenTime() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);
        LocalDateTime currentDateTime = LocalDateTime.of(2024, 4, 2, 9, 59, 0);

        // when && then
        assertThatThrownBy(() -> cafeKiosk.createOrder(currentDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }
}