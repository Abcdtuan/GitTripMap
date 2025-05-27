package com.codeWithProject.TripServer.repository;

import com.codeWithProject.TripServer.entity.User;
import com.codeWithProject.TripServer.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUserRole(UserRole userRole);
    Optional<User> findByEmail(String email);
    Optional<User> findFirstByEmail(String email) ;
}
