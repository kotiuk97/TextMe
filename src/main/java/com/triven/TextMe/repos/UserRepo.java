package com.triven.TextMe.repos;

import com.triven.TextMe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long>{

    User findByUsername(String username);

    User findByActivationCode(String code);
}
