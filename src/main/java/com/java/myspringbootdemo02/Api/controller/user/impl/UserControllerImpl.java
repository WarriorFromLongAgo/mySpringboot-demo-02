package com.java.myspringbootdemo02.Api.controller.user.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.java.myspringbootdemo02.Api.controller.user.UserController;
import com.java.myspringbootdemo02.Api.result.Result;
import com.java.myspringbootdemo02.App.exception.MyApplicationException;
import com.java.myspringbootdemo02.App.service.user.impl.UserServiceImpl;
import com.java.myspringbootdemo02.Common.convert.user.UserConvert;
import com.java.myspringbootdemo02.Common.convert.user.UserVoConvert;
import com.java.myspringbootdemo02.Common.entity.User;
import com.java.myspringbootdemo02.Common.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
public class UserControllerImpl implements UserController {

    private static final Logger log= LoggerFactory.getLogger(UserControllerImpl.class);
    @Autowired
    @Qualifier(value = "userService")
    private UserServiceImpl userService;

    public Result findAllUser() {
        List<UserVo> userVos = userService.findAll();
        return Result.success(userVos);
    }

    @Override
    public Result addUser(@RequestBody UserVo userVo) {
        int i = userService.addUser(userVo);
        return Result.getResult(i);
    }


    @Override
    public Result updateUserById(UserVo userPo) {
        int i = userService.updateUserById(userPo);
        return Result.getResult(i);
    }

    @Override
    public Result deleteUserById(UserVo user) {
        int i = userService.deleteUserById(user);
        return Result.getResult(i);
    }

    @Override
    public Result findByPage(int currentPage, int pageSize) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("startIndex", (currentPage - 1) * pageSize);
        map.put("pageSize", pageSize);
        return Result.success(userService.findByPage(map));
    }

    @Override
    public Result batchAdd(List<UserVo> list) {
        int i = userService.batchAdd(list);
        return Result.getResult(i);
    }


    public Result ExportExcel() {
        // 设置文件导出的路径
        String realPath = "E://wsfile/";
        File folder = new File(realPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        String fileName = realPath + "User" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为用户表 然后文件流会自动关闭
        try {
            ExcelWriterBuilder write = EasyExcel.write(fileName, User.class);
            ExcelWriterSheetBuilder userTable = write.sheet("用户表");
            List<User> data = getData();
            log.info("用户表导出数量："+data.size());
            System.err.println("用户表导出开始............");
            long start = System.currentTimeMillis();
            userTable.doWrite(data);
            long end = System.currentTimeMillis();
            long result=end-start;
            log.info("用户表导出所用到的时间："+result);
        } catch (Exception e) {
            throw new MyApplicationException("导出失败！");
        }
        return Result.success("下载成功，文件已存入：E:/wsfile/");
    }

    private List<User> getData() {
        // 查询用户表
        System.err.println("用户表查询开始............");
        long start = System.currentTimeMillis();
        List<UserVo> all = userService.findAll();
        long end = System.currentTimeMillis();
        long result=end-start;
        log.info("用户表查询所用到的时间："+result);
        ArrayList<User> users = new ArrayList<>();
        for (UserVo userVo : all) {
            users.add(UserConvert.getUser(userVo));
        }
        return users;
    }


    public Result ImportExcel(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return Result.fail();
        }
        // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        try {
            ArrayList<UserVo> userVos = new ArrayList<>();
            EasyExcel.read(multipartFile.getInputStream(), User.class, new PageReadListener<User>(dataList -> {
                for (User user : dataList) {
                    UserVo userVo = UserVoConvert.getUserVo(user);
                    userVos.add(userVo);
                }
                // 将导入的数据用mybatis添加进数据库
                userService.batchAdd(userVos);
            })).sheet().doRead();
        } catch (Exception e) {
            throw new MyApplicationException("导入失败！");
        }
        return Result.success();
    }
}
