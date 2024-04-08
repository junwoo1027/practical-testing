package sample.cafekiosk.spring.domain.order.response;

import sample.cafekiosk.spring.domain.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderResponse(
        Long id,
        int totalPrice,
        LocalDateTime registeredDateTime,
        List<ProductResponse> products
) {
    public static OrderResponse of(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getRegisteredDateTime(),
                order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList())
                );
    }
}
