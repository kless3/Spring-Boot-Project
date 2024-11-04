package com.harmyFounder.SpringBootProject.repo;

import com.harmyFounder.SpringBootProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    public User findByName(String username);
}
