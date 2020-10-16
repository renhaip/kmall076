package com.kgc.kmall.interceptors;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.annotations.LoginRequired;
import com.kgc.kmall.util.CookieUtil;
import com.kgc.kmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-10-15 19:21
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //确保请求是访问控制器的请求
        if (handler.getClass().equals(org.springframework.web.method.HandlerMethod.class)) {
            //获取注解信息
            HandlerMethod hm = (HandlerMethod) handler;
            LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);


            //获取注解
            //从request中可以获取token
            //从cookie中获取到token
            //1.都没有token 都没有登陆
            //2.俩个都有 主动登陆  第二次访问(登陆成功,并登陆状态还没有消失)
            //3.request中有,cookie中没有(曾经登陆成功!,但是长时间不操作 登陆状态失败)
            //4.request中没有,cookie中有(被动登陆)

            String token = "";

            //从cookie中获取token
            String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
            if (StringUtils.isNotBlank(oldToken)) {
                token = oldToken;
            }

            //从request中获取token
            String newToken = request.getParameter("token");
            if (StringUtils.isNotBlank(newToken)) {
                token = newToken;
            }


            //验证token  为空不通过
            String result = "fail";
            Map<String, String> successMap = new HashMap<>();
            if (StringUtils.isNotBlank(token)) {
                //调用验证中心的验证方法进行验证
                String ip = request.getRemoteAddr();//从request中获取ip
                if(StringUtils.isBlank(ip)||ip.equals("0:0:0:0:0:0:0:1")){
                    ip="127.0.0.1";
                }
                String successJson  = HttpclientUtil.doGet("http://127.0.0.1:8086/verify?token="+token+"&currentIp="+ip);
                successMap = JSON.parseObject(successJson, Map.class);
                result=successMap.get("status");
            }




            // 没有LoginRequired注解不拦截
            if (methodAnnotation == null) {
                System.out.println("拦截器直接放行");
                return true;
            } else {
                //判断value的值是true还是false
                boolean loginSuccess = methodAnnotation.value();// 获得该请求是否必登录成功
                if (loginSuccess) {
                    if(!result.equals("success")){
                        //重定向会passport登录
                        StringBuffer requestURL = request.getRequestURL();
                        response.sendRedirect("http://localhost:8086/index?ReturnUrl="+requestURL);
                        return false;
                    }

                    //需要将token携带的用户信息写入
                    request.setAttribute("memberId",successMap.get("memberId"));
                    request.setAttribute("nickname",successMap.get("nickname"));

                    System.out.println("必须登录的");
                } else {
                    System.out.println("没有登陆,也可以放行");
                    if(result.equals("success")){
                        // 需要将token携带的用户信息写入
                        request.setAttribute("memberId",successMap.get("memberId"));
                        request.setAttribute("nickname",successMap.get("nickname"));
                    }
                }
                //通过验证,覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60*24,true);
                }
            }
        }
        System.out.println("测试拦截器");
        return true;
    }

}
