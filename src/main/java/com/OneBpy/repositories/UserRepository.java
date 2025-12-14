package com.OneBpy.repositories;

import com.OneBpy.models.Role;
import com.OneBpy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    User findByRole(Role role);
}
