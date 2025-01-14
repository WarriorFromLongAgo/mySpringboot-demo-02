package com.java.myspringbootdemo02.Common.convert.user;

import com.java.myspringbootdemo02.Common.po.UserPo;
import com.java.myspringbootdemo02.Common.vo.UserVo;

public class UserPoConvert {

    public static UserPo getUserPo(UserVo user) {
        UserPo userPo = new UserPo();
        userPo.setUserName(user.getUserName());
        userPo.setPassword(user.getPassword());
        userPo.setPhone(user.getPhone());
        userPo.setDept(user.getDept());
        userPo.setStatus(user.getStatusCode());
        userPo.setHireTime(user.getHireTime());
        userPo.setState(user.getStateCode());
        return userPo;
    }
}
