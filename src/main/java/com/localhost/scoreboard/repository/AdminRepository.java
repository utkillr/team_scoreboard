package com.localhost.scoreboard.repository;

import com.localhost.scoreboard.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findById(int id);
    Admin findByLogin(String login);
    List<Admin> findAll();
}