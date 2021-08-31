package org.example.controller;

import org.example.Util.JwtUtil;
import org.example.system.pojo.Admin;
import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.example.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @GetMapping
    public Result findAll(){
        List<Admin> adminList= adminService.findAll();
        if(adminList.size()>0){
            return new Result(true, StatusCode.OK,"查询成功",adminList);
        }
        return new Result(false, StatusCode.OK,"没有数据",adminList);
    }
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){

        Admin admin = adminService.findOne(id);
        if(admin!=null){
            return new Result(true,StatusCode.OK,"查询成功",admin);
        }
        return new Result(false,StatusCode.OK,"没有数据");
    }
    @PostMapping
    public Result add(@RequestBody Admin admin){
        int num=adminService.register(admin);
        return new Result(true,StatusCode.OK,"已添加"+num+"条数据");
    }
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Admin admin,@PathVariable Integer id){
        admin.setId(id);
        int num=adminService.updateAdmin(admin);
        return new Result(true,StatusCode.OK,"已更新"+num+"条数据");
    }
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Integer id){
        int num=adminService.deleteAdmin(id);
        return new Result(true,StatusCode.OK,"已删除"+num+"条数据");
    }
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin){
        boolean login = adminService.login(admin);
        if(login){  //如果验证成功
            Map<String,String> info = new HashMap<>();
            info.put("username",admin.getLogin_name());
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), admin.getLogin_name(), null);
            info.put("token",token);
            return new Result(true, StatusCode.OK,"登录成功",info);
        }else{
            return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
        }
    }
}
