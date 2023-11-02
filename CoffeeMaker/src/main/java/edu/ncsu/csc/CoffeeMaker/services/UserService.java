package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

@Component
@Transactional
public class UserService extends Service<User, Long> {

    /**
     * userRepository, to be autowired in by Spring and provide CRUD operations
     * on Recipe model.
     */
    @Autowired
    private UserRepository userRepository;

    @Override
    protected JpaRepository<User, Long> getRepository () {
        return userRepository;
    }

}
