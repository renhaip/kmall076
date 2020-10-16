package com.kgc.kmall.interceptors;

import com.kgc.kmall.annotations.LoginRequired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shkstart
 * @create 2020-10-15 19:21
 */
@Component
public class AuthInterceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //确保请求是访问控制器的请求
        if (handler.getClass().equals(org.springframework.web.method.HandlerMethod.class)) {
            //获取注解信息
            HandlerMethod hm = (HandlerMethod) handler;
            LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);
            // 没有LoginRequired注解不拦截
            if (methodAnnotation == null) {
                System.out.println("拦截器直接放行");
                return true;
            }else{
                //判断value的值是true还是false
                boolean loginSuccess = methodAnnotation.value();// 获得该请求是否必登录成功
                if (loginSuccess) {
                    System.out.println("必须登录的");
                }else{
                    System.out.println("没有登陆,也可以放行");
                }

            }
        }
        System.out.println("测试拦截器");
        return true;
    }

}
