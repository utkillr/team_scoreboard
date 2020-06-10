package com.localhost.scoreboard.service;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.repository.AdminRepository;
import com.localhost.scoreboard.repository.GameRepository;
import com.localhost.scoreboard.util.HashUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private AdminRepository adminRepository;

    @Autowired
    public void setAdminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Admin findById(int id) {
        return adminRepository.findById(id);
    }

    public Admin findByLogin(String login) {
        return adminRepository.findByLogin(login);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public void delete(Admin admin) {
        adminRepository.delete(admin);
    }

    public Admin authorized(AdminDAO adminDAO) {
        Admin admin = adminRepository.findByLogin(adminDAO.getLogin());
        if (admin != null && admin.getPwdHash().equals(HashUtilities.getHash(adminDAO.getPwd()))) return admin;
        else return null;
    }

    public Boolean isAdmin(String hash) {
        return hash != null && findAll().stream().map(Admin::getPwdHash).collect(Collectors.toList()).contains(hash);
    }

    public Admin init(AdminDAO adminDAO) {
        if (!adminRepository.findAll().isEmpty()) return null;
        Admin admin = new Admin();
        admin.setLogin(adminDAO.getLogin());
        admin.setPwdHash(HashUtilities.getHash(adminDAO.getPwd()));
        return adminRepository.save(admin);
    }
}
