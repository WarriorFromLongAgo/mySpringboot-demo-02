package com.java.myspringbootdemo02.Common.convert.user;

import com.alibaba.excel.util.StringUtils;
import com.java.myspringbootdemo02.Common.entity.User;
import com.java.myspringbootdemo02.Common.enums.user.UserStateEnum;
import com.java.myspringbootdemo02.Common.enums.user.UserStatusEnum;
import com.java.myspringbootdemo02.Common.po.UserPo;
import com.java.myspringbootdemo02.Common.vo.UserVo;

import java.text.SimpleDateFormat;

public class UserVoConvert {

    public static UserVo getUserVo(UserPo user) {
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setUserName(user.getUserName());
        userVo.setPhone(user.getPhone());
        userVo.setDept(user.getDept());
        userVo.setStatusCode(UserStatusEnum.getUserStatusByCode(user.getStatus()).getCode());
        userVo.setStatus(UserStatusEnum.getUserStatusByCode(user.getStatus()).getStatus());
        userVo.setHireTime(user.getHireTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        userVo.setCreateTime(simpleDateFormat.format(user.getCreateTime()));
        userVo.setUpdateTime(simpleDateFormat.format(user.getUpdateTime()));
        userVo.setStateCode(UserStateEnum.getUserStatusByCode(user.getState()).getCode());
        userVo.setState(UserStateEnum.getUserStatusByCode(user.getState()).getState());
        return userVo;
    }

    public static UserVo getUserVo(User user) {
        UserVo userVo = new UserVo();
        userVo.setUserName(user.getUserName());
        setPassword(user, userVo);
        userVo.setPhone(user.getPhone());
        userVo.setDept(user.getDept());
        setState(user, userVo);
        setStatus(user, userVo);
        userVo.setHireTime(user.getHireTime());
        userVo.setCreateTime(user.getCreateTime());
        userVo.setUpdateTime(user.getUpdateTime());
        return userVo;
    }

    private static void setPassword(User user, UserVo userVo) {
        if (!StringUtils.isEmpty(user.getPassword())){
            userVo.setPassword(user.getPassword());
        }else {
            userVo.setPassword("112233");
        }
    }

    private static void setStatus(User user, UserVo userVo) {
        if (UserStateEnum.USING.getState().equals(user.getStatus()) && !StringUtils.isEmpty(user.getStatus())){
            userVo.setStatusCode(1);
        }else {
            userVo.setStatusCode(-1);
        }
    }

    private static void setState(User user, UserVo userVo) {
        if (UserStatusEnum.MANAGER.getStatus().equals(user.getState()) && !StringUtils.isEmpty(user.getState())){
            userVo.setStateCode(1);
        }else {
            userVo.setStateCode(2);
        }
    }
}
