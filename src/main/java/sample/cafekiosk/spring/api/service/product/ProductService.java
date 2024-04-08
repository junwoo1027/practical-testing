package sample.cafekiosk.spring.api.service.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.order.request.CreateProductRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = this.productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        String nextProductNumber = createNextProductNumber();
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = this.productRepository.save(product);
        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String productNumber = this.productRepository.findLatestProductNumber();
        if (productNumber == null) {
            return "001";
        }
        int latestProductNumber = Integer.parseInt(productNumber);
        int nextProductNumberInt = latestProductNumber + 1;
        String nextProductNumber = String.format("%03d", nextProductNumberInt);
        return nextProductNumber;
    }

}
