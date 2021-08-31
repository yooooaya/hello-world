package org.example.service;


import org.example.system.pojo.Admin;

import java.util.List;

public interface AdminService {
    public List<Admin> findAll();

    public Admin findOne(Integer id);

    public int register(Admin admin);

    public int updateAdmin(Admin admin);

    public int deleteAdmin(Integer id);

    public boolean login(Admin admin);
}
