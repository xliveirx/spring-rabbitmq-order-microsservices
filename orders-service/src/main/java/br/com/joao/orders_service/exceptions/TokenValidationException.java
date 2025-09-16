package br.com.joao.orders_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TokenValidationException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        pb.setTitle("Token validation error.");
        pb.setDetail("There was an error while validating your token, try again.");

        return pb;

    }
}
