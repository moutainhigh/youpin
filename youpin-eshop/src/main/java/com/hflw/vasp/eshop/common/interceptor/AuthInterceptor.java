package com.hflw.vasp.eshop.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.hflw.vasp.annotation.AccessNoSession;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.common.exception.ResultCodeEnum;
import com.hflw.vasp.eshop.common.utils.UserUtils;
import com.hflw.vasp.eshop.modules.user.service.UserService;
import com.hflw.vasp.modules.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyf
 * @Title AuthInterceptor.java
 * @Package com.hflw.vasp.credit.sp.interceptor
 * @Description 登录校验拦截器
 * @date 2019年7月18日 下午4:20:58
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

//        String getServerName();//获取服务器名，localhost；
//        String getServerPort();//获取服务器端口号，8080；
//        String getContextPath();//获取项目名，/Example；
//        String getServletPath();//获取Servlet路径，/AServlet；
//        String getQueryString();//获取参数部分，即问号后面的部分：username=zhangsan
//        String getRequestURI();//获取请求URI，等于项目名+Servlet路径：/Example/AServlet
//        String getRequestURL();//获取请求URL，等于不包含参数的整个请求路径：http://localhost:8080/Example/AServlet
//        String getRemoteAddr();//获取服务器的IP，如localhost对应ip为127.0.0.1
//
        HttpSession session = request.getSession();

//        String requestURL = request.getRequestURI();
//        String remoteAddr = request.getRequestURI();
//        String sessionId = session.getId();
//        log.info("requestURL:{},sessionId:{}", requestURL, sessionId);

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        boolean needLoginFlag = true;
        //检查类上是否有校验登陆标注
        Class<?> targetBean = handlerMethod.getBeanType();
        AccessNoSession authCheck = targetBean.getAnnotation(AccessNoSession.class);
        //添加AccessNoSession注解的类，无需登录校验
        if (authCheck != null) needLoginFlag = !authCheck.value();

        //检查方法上是否有校验登陆标注,方法@AccessNoSession覆盖类@AccessNoSession
        authCheck = handlerMethod.getMethodAnnotation(AccessNoSession.class);
        //添加AccessNoSession注解的类，无需登录校验
        if (authCheck != null) needLoginFlag = !authCheck.value();

        if (needLoginFlag && !sessionVerify(request)) {
            printNotLogin(response, request);
            return false;
        }
        return true;
    }

    private boolean sessionVerify(HttpServletRequest request, Customer user) {
        HttpSession session = request.getSession();
        if (user.getDelFlag() == Constants.NOT_DEL && user.getEnableStatus() != Constants.ENABLE_STATUS_INVALID) {
            log.info(user.getId() + "状态正常");
            return true;
        } else {
            log.info("状态异常");
            UserUtils.clearSession(session);
            return false;
        }
    }

    private boolean sessionVerify(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Customer user = (Customer) session.getAttribute(Constants.SESSION_LOGIN_USER);
        if (null != user) {
            return true;
        } else {
            UserUtils.clearSession(session);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
    }

    private void printNotLogin(HttpServletResponse response, HttpServletRequest request) {
        PrintWriter out = null;
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", ResultCodeEnum.NOT_LOGIN.getCode());
        result.put("msg", ResultCodeEnum.NOT_LOGIN.getMsg());
        try {
            response.setContentType("application/json;charset=utf-8");
            out = response.getWriter();
            out.print(JSON.toJSON(result));
        } catch (IOException e) {
            log.error("获取PrintWriter异常", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
