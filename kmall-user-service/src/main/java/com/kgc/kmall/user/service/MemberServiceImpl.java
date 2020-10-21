package com.kgc.kmall.user.service;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.bean.Member;
import com.kgc.kmall.bean.MemberExample;
import com.kgc.kmall.bean.MemberReceiveAddress;
import com.kgc.kmall.bean.MemberReceiveAddressExample;
import com.kgc.kmall.service.MemberService;
import com.kgc.kmall.user.mapper.MemberMapper;
import com.kgc.kmall.user.mapper.MemberReceiveAddressMapper;
import com.kgc.kmall.user.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-08-19 17:37
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    MemberMapper memberMapper;

    @Resource
    RedisUtil redisUtil;

    @Resource
    MemberReceiveAddressMapper memberReceiveAddressMapper;

    @Override
    public List<Member> selectAllMember() {
        List<Member> members = memberMapper.selectByExample(null);
        return members;
    }

    @Override
    public Member login(String username,String password) {

        //先从redis中进行查询
        Jedis jedis=null;
        try {
            jedis=redisUtil.getJedis();
            if(jedis!=null){
                String userInfo = jedis.get("user:" + username + ":info");
                    if (StringUtils.isNotBlank(userInfo)){
                        Member member = JSON.parseObject(userInfo, Member.class);
                        //判断  存在redis中的密码和 刚刚输入的密码进行比较
                        if(member.getPassword().equals(password)){
                            return member;
                        }else{
                            return null;
                        }
                    }else{ //缓存中如果没有的话 则在数据中进行查询
                        Member member = getMember(username, password);
                        jedis.setex("user:"+username+":info",60*60*24,JSON.toJSONString(member));
                        return member;
                    }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addUserToken(String token, Long memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+memberId+":token",60*60*2,token);
        jedis.close();
    }

    @Override
    public Member checkOauthUser(Long sourceUid) {
        MemberExample example=new MemberExample();
        example.createCriteria().andSourceUidEqualTo(sourceUid);
        List<Member> members = memberMapper.selectByExample(example);
        if(members.size()>0){
            return  members.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Integer addOauthUser(Member member) {
        return memberMapper.insertSelective(member);
    }

    @Override
    public List<MemberReceiveAddress> getReceiveAddressByMemberId(Long memberId) {
        MemberReceiveAddressExample example=new MemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        List<MemberReceiveAddress> members = memberReceiveAddressMapper.selectByExample(example);
        return members;
    }

    @Override
    public MemberReceiveAddress getReceiveAddressById(Long receiveAddressId) {

        return  memberReceiveAddressMapper.selectByPrimaryKey(receiveAddressId);
    }

    public Member getMember(String username,String password){
        MemberExample example=new MemberExample();
        example.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
        List<Member> members = memberMapper.selectByExample(example);

        if(members!=null&&members.size()>0){
            return  members.get(0);
        }
        return null;
    }
}
