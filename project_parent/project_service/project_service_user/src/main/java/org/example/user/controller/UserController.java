package org.example.user.controller;

import com.github.pagehelper.Page;
import org.example.pojo.PageResult;
import org.example.pojo.Result;
import org.example.pojo.StatusCode;
import org.example.user.pojo.User;
import org.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    /**
     * 查询全部数据
     * @return
     */
    //对该方法设置访问权限
    @PreAuthorize("hasAnyAuthority('accountant')")
    @GetMapping
    public Result findAll(){
        List<User> userList = userService.findAll();
        System.out.println(userList);
        return new Result(true, StatusCode.OK,"查询成功",userList) ;
    }

    /***
     * 根据ID查询数据
     * @param username
     * @return
     */
    @PostAuthorize("hasAnyAuthority('accountant')")
    @GetMapping("/{username}")
    public Result findById(@PathVariable String username){
        User user = userService.findById(username);
        return new Result(true,StatusCode.OK,"查询成功",user);
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username){
        User user = userService.findById(username);
        return user;
    }



    /***
     * 新增数据
     * @param user
     * @return
     */
    @PostMapping
    public Result add(@RequestBody User user){
        userService.add(user);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param user
     * @param username
     * @return
     */
    @PutMapping(value="/{username}")
    public Result update(@RequestBody User user,@PathVariable String username){
        user.setUsername(username);
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param username
     * @return
     */
    @DeleteMapping(value = "/{username}" )
    public Result delete(@PathVariable String username){
        userService.delete(username);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<User> list = userService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<User> pageList = userService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


}
