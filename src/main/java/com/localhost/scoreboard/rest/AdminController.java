package com.localhost.scoreboard.rest;

import com.localhost.scoreboard.model.*;
import com.localhost.scoreboard.service.AdminService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = {"", "/"})
    @ResponseStatus(value = HttpStatus.OK)
    public String login(@RequestBody AdminDAO adminDAO) throws NotFoundException {
        Admin admin = adminService.init(adminDAO);
        if (admin != null) return admin.getPwdHash();
        admin = adminService.authorized(adminDAO);
        if (admin != null) return admin.getPwdHash();
        else return "";
    }
}
