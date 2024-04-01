package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LatteTest {

    @Test
    void getName() {
        Latte latte = new Latte();

        assertThat(latte.getName()).isEqualTo("라뗴");
    }

    @Test
    void getPrice() {
        Latte latte = new Latte();

        assertThat(latte.getPrice()).isEqualTo(4500);
    }
}