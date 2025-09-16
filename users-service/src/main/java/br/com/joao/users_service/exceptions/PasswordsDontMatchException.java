package br.com.joao.users_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class PasswordsDontMatchException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Passwords don't match.");
        pb.setDetail("Passwords don't match, try again.");

        return pb;

    }

}
