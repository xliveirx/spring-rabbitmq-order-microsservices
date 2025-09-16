package br.com.joao.orders_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class OrderNotFoundException extends DomainException{
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("Order not found.");
        pb.setDetail("This order was not found, try another one.");

        return pb;

    }
}
