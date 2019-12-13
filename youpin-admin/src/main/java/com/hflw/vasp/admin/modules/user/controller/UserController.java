package com.hflw.vasp.admin.modules.user.controller;


import com.hflw.vasp.admin.modules.AbstractController;
import com.hflw.vasp.admin.modules.user.service.UserService;
import com.hflw.vasp.system.entity.SysUser;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyf
 * @Title UserController.java
 * @Package com.hflw.vasp.controller
 * @Description 登录
 * @date 2019年10月24日 下午2:02:54
 */
@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/info")
    public R index() {
        SysUser user = getSessionUser();
        return R.ok().data(user);
    }

}
