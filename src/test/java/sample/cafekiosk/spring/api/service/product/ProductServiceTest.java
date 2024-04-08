package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.request.CreateProductRequest;
import sample.cafekiosk.spring.domain.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @AfterEach
    void clean() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("판매 중인 상품 리스트를 조회한다.")
    void getSellingProducts() {
        // given
        Product product1 = new Product("001", ProductType.BOTTLE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = new Product("002", ProductType.BOTTLE, ProductSellingStatus.SELLING, "카페라떼", 4500);
        Product product3 = new Product("003", ProductType.BAKERY, ProductSellingStatus.STOP_SELLING, "케이크", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<ProductResponse> result = productService.getSellingProducts();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("002", "카페라떼", 4500)
                );
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    void createProduct() {
        // given
        Product product = new Product("001", ProductType.BOTTLE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        productRepository.save(product);

        CreateProductRequest request = new CreateProductRequest(ProductType.BAKERY, ProductSellingStatus.SELLING, "카페라떼", 4500);

        // when
        ProductResponse result = productService.createProduct(request.toServiceRequest());

        // then
        assertThat(result)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("002", ProductType.BAKERY, ProductSellingStatus.SELLING, "카페라떼", 4500);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
        assertThat(products)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", ProductType.BOTTLE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                        tuple("002", ProductType.BAKERY, ProductSellingStatus.SELLING, "카페라떼", 4500)
                );
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
    void createProductWhenProductsIsEmpty() {
        // given
        CreateProductRequest request = new CreateProductRequest(ProductType.BAKERY, ProductSellingStatus.SELLING, "카페라떼", 4500);

        // when
        ProductResponse result = productService.createProduct(request.toServiceRequest());

        // then
        assertThat(result)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("001", ProductType.BAKERY, ProductSellingStatus.SELLING, "카페라떼", 4500);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", ProductType.BAKERY, ProductSellingStatus.SELLING, "카페라떼", 4500)
                );
    }

}