package br.com.joao.orders_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class InsufficientStockException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("There's not enough stock of this product.");
        pb.setDetail("There's not enough stock of this product, try buying less.");

        return pb;

    }
}
