package com.kgc.kmall.kmallseckill.controller;

import com.kgc.kmall.kmallseckill.util.RedisUtil;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-10-28 16:27
 */
@Controller
public class SeckillController {


    @Resource
    RedisUtil redisUtil;

    @Resource
    RedissonClient  redissonClient;

    @RequestMapping("kill")
    @ResponseBody
    public String  kill(){
        Jedis jedis = redisUtil.getJedis();
        jedis.watch("116");
        int stock=Integer.parseInt(jedis.get("116"));
        if(stock>0){
            Transaction multi = jedis.multi();
            multi.incrBy("116",-1);
            List<Object> exec = multi.exec();
            if(exec!=null&&exec.size()>0){
                System.out.println("===========抢购成功==========,当前库存剩余数量:"+stock+"");
            }else{
                System.out.println("抢购失败,当前库存剩余数量:"+stock+"");
            }
        }else{
            System.out.println("抢购活动已结束");
        }
        return "1";
    }

    @RequestMapping("kill2")
    @ResponseBody
    public String secKill(){
        Jedis jedis = redisUtil.getJedis();

        RSemaphore semaphore = redissonClient.getSemaphore("116");
        boolean b = semaphore.tryAcquire();
        if(b){
            int stock = Integer.parseInt(jedis.get("116"));
            System.out.println("===========抢购成功==========");
            // 用消息队列发出订单消息
            System.out.println("发出订单的消息队列，由订单系统对当前抢购生成订单");
        }else {
            System.out.println("抢购结束");
        }

        jedis.close();
        return "1";
    }
}
