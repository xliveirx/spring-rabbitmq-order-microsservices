package br.com.joao.users_service.service;

import br.com.joao.users_service.dto.UserEditRequest;
import br.com.joao.users_service.domain.User;
import br.com.joao.users_service.dto.UserCreateRequest;
import br.com.joao.users_service.exceptions.EmailAlreadyRegisteredException;
import br.com.joao.users_service.exceptions.PasswordsDontMatchException;
import br.com.joao.users_service.exceptions.UserNotFoundException;
import br.com.joao.users_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(UserCreateRequest req) {

        if(userRepository.findByEmailIgnoreCaseAndActiveTrue(req.email()).isPresent()){
            throw new EmailAlreadyRegisteredException();
        }

        if(!req.password().equals(req.confirmPassword())){
            throw new PasswordsDontMatchException();
        }

        var encodedPassword = passwordEncoder.encode(req.password());

        var user = new User(req.fullName(), req.email(), encodedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public void disableUser(User logged) {

        var user = userRepository.findById(logged.getId())
                .orElseThrow(UserNotFoundException::new);

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

    }

    @Transactional
    public User editUser(User logged, UserEditRequest req) {

        var user = userRepository.findById(logged.getId())
                .orElseThrow(UserNotFoundException::new);

        if(req.fullName() != null){
            user.setFullName(req.fullName());
        }

        if(req.password() != null && req.confirmPassword() != null){
            if(req.password().equals(req.confirmPassword())){
                var encodedPassword = passwordEncoder.encode(req.password());
                user.setPassword(encodedPassword);
            }
        }

        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void disableUserById(Long id) {

        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void enableUserById(Long id) {

        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCaseAndActiveTrue(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public Page<User> getUsers(PageRequest pageable) {

        return userRepository.findAll(pageable);
    }
}
