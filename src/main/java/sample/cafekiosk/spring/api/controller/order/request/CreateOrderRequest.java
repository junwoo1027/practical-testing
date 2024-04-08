package sample.cafekiosk.spring.api.controller.order.request;

import jakarta.validation.constraints.NotEmpty;
import sample.cafekiosk.spring.api.service.order.request.CreateOrderServiceRequest;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "상품 번호 리스트는 필수입니다.")
        List<String> productNumbers
) {

        public CreateOrderServiceRequest toServiceRequest() {
                return new CreateOrderServiceRequest(
                        this.productNumbers
                );
        }

}
