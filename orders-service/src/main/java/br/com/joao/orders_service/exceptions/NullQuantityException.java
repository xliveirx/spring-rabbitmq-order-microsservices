package br.com.joao.orders_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class NullQuantityException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pb.setTitle("Quantity can't be null.");
        pb.setDetail("Quantity must be positive, try again with a value greater than zero.");

        return pb;

    }
}
