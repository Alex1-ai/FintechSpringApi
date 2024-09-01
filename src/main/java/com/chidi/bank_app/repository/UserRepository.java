package com.chidi.bank_app.repository;

import com.chidi.bank_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail( String email);
    Boolean existsByAccountNumber( String phoneNumber);
    User findByAccountNumber(String accountNumber);
    Optional<User> findByEmail(String email);

}
