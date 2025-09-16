package br.com.joao.orders_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class NullPriceException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pb.setTitle("Price can't be null.");
        pb.setDetail("Price must be positive, try again with a value greater than zero.");

        return pb;

    }
}
