package br.com.joao.orders_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProductNotFoundException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("Product not found.");
        pb.setDetail("This product was not found, try another one.");

        return pb;

    }
}
