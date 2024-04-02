package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    @Test
    @DisplayName("음료 1개를 추가하면 주문 목록에 담긴다.")
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
    @DisplayName("음료를 여러개 씩 주문 목록에 담을 수 있다.")
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
    @DisplayName("음료는 1잔 이상 주문해아한다.")
    void addZeroBeverages() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();

        // when && then
        assertThatThrownBy(() -> cafeKiosk.add(new Americano(), 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    @Test
    @DisplayName("주문 목록에서 음료를 제거한다.")
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
    @DisplayName("주문 목록을 초기화한다.")
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
    @DisplayName("주문 목록의 총 금액을 계산한다.")
    void calculateTotalPrice() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        // when
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(8500);
    }

    @Test
    @DisplayName("영업 시간에 주문을 생성한다.")
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
    @DisplayName("영업 시작 시간 이전에는 주문을 생성할 수 없다.")
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