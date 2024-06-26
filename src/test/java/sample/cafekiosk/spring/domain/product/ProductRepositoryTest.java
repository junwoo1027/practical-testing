package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("원하는 판매상태를 가진 상품을 조회한다.")
    void findAllBySellingStatusIn() {
        // given
        Product product1 = new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> results = productRepository.findAllBySellingStatusIn(List.of(ProductSellingStatus.SELLING, ProductSellingStatus.HOLD));

        // then
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                        tuple("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500)
                );
    }

    @Test
    @DisplayName("상품번호 리스트로 상품을 조회한다.")
    void findAllByProductNumberIn() {
        // given
        Product product1 = new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = new Product("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> results = productRepository.findAllByProductNumberIn(List.of("001", "003"));

        // then
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                        tuple("003", ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000)
                );
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    void findLatestProductNumber() {
        // given
        String targetProductNumber = "003";

        Product product1 = new Product("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 4500);
        Product product3 = new Product(targetProductNumber, ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "빙수", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String result = productRepository.findLatestProductNumber();

        // then
        assertThat(result).isEqualTo(targetProductNumber);
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    void findLatestProductNumberWhenProductIsEmpty() {
        // given

        // when
        String result = productRepository.findLatestProductNumber();

        // then
        assertThat(result).isNull();
    }

}