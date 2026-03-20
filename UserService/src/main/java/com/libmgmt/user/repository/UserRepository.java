package com.libmgmt.user.repository;

import com.libmgmt.user.model.User;
import com.libmgmt.user.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByLibraryCardCardNumber(String cardNumber);

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByRole(UserRole role);

    List<User> findByNameContainingIgnoreCaseAndRole(String name, UserRole role);
}
