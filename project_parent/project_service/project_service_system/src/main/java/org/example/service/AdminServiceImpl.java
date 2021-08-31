package org.example.service;

import org.example.dao.AdminDao;
import org.example.system.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{
    @Autowired
    private AdminDao adminDao;
    @Override
    public List<Admin> findAll() {
        return adminDao.selectAll();
    }

    @Override
    public Admin findOne(Integer id) {
        return adminDao.selectByPrimaryKey(id);
    }

    @Override
    public int register(Admin admin) {
        String str=BCrypt.gensalt();
        String password=BCrypt.hashpw(admin.getPassword(),str);
        admin.setPassword(password);
        System.out.println(password);
        return adminDao.insertSelective(admin);
    }

    @Override
    public int updateAdmin(Admin admin) {
        return adminDao.updateByPrimaryKey(admin);
    }

    @Override
    public int deleteAdmin(Integer id) {
        return adminDao.deleteByPrimaryKey(id);
    }

    @Override
    public boolean login(Admin admin) {
        //根据登录名查询管理员
        Admin admin1=new Admin();
        admin1.setLogin_name(admin.getLogin_name());
        admin1.setStatus(1);
        System.out.println(admin1);
        Admin admin2 = adminDao.selectOne(admin1);//数据库查询出的对象
        if(admin2==null){
            return false;
        }else{
            //验证密码, Bcrypt为spring的包, 第一个参数为明文密码, 第二个参数为密文密码
            return BCrypt.checkpw(admin.getPassword(),admin2.getPassword());
        }
    }
}
