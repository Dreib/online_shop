package org.fasttrackit.onlineshop.persistence;

import org.fasttrackit.onlineshop.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //query derived from method name
    Page<User> findByFirstNameContains(String partialFirstName, Pageable pageable);

    Page<User> findByLastNameContains(String partialLastName, Pageable pageable);

    Page<User> findByFirstNameContainsAndLastNameContains(String partialFirstName,
                                                          String partialLastName, Pageable pageable);

}
