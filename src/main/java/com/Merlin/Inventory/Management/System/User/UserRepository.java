package com.Merlin.Inventory.Management.System.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

    Optional<User> findByRole(ROLE role);
    Optional<User> findByEmail(String email);
    int countByRole(ROLE role);


}
