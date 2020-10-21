package com.kgc.kmall.service;

import com.kgc.kmall.bean.Member;
import com.kgc.kmall.bean.MemberReceiveAddress;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-08-19 17:37
 */
public interface MemberService {

    public List<Member> selectAllMember();


    //登陆
    Member login(String username, String password);

    //增加user用户的token
    void addUserToken(String token, Long memberId);


    //查询
    Member checkOauthUser(Long sourceUid);

    //增加  动态增加
    Integer  addOauthUser(Member member);


    List<MemberReceiveAddress> getReceiveAddressByMemberId(Long memberId);

    MemberReceiveAddress getReceiveAddressById(Long receiveAddressId);
}
