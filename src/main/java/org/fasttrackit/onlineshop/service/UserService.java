package org.fasttrackit.onlineshop.service;

import org.fasttrackit.onlineshop.domain.User;
import org.fasttrackit.onlineshop.persistence.UserRepository;
import org.fasttrackit.onlineshop.transfer.SaveUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//spring bean (object controlled by Spring Boot)
//IoC container (context of all spring beans)
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    //Inversion of control (IoC)
    private final UserRepository userRepository;

    //Dependency injection
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(SaveUserRequest request) {
        LOGGER.info("Creating user: {}", request);

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }
}
