package br.com.joao.orders_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ProductAlreadyRegisteredException extends DomainException{

    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Product already registered.");
        pb.setDetail("This product has already been registered, try another one.");

        return pb;

    }
}
