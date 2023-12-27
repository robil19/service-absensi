package com.intelsysdata.absensi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intelsysdata.absensi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    boolean existsByUsernameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneIgnoreCase(String phone);

    User findByEmail(String email);

    User findByPhone(String phone);

    User findUserByPhoneOrEmail(String phone, String email);

}
