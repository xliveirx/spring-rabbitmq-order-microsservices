package br.com.joao.users_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UserNotFoundException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {

        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("User not found.");
        pb.setDetail("This user was not found, try another one.");

        return pb;

    }
}
