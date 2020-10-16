package com.kgc.kmall.kmallpassportweb.test;

import com.kgc.kmall.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-10-14 16:32
 */
public class Test01 {
    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<>();
        map.put("memberId","1");
        map.put("nickname","zhangsan");
        String encode = JwtUtil.encode("2020kmall076", map,"127.0.0.1");
        System.err.println(encode);

        Map<String, Object> decode = JwtUtil.decode(encode, "2020kmall076", "127.0.0.1");
        System.out.println(decode.toString());
    }
}
