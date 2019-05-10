package com.hanjixin.core.service;

import com.hanjixin.core.pojo.seller.Seller;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

public class UserDetailServiceImpl implements UserDetailsService {
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名 查询数据库中  用户对象  //要求：必须是审核通过的 如果审核不通过或是未审核 不允许登陆
        try {
            Seller seller = sellerService.findOne(username);
            if(seller!=null){
                //判断此用户的状态是否为审核通过
                if("1".equals(seller.getStatus())){
                    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
                    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
                    //  参数1：用户名  参数2：加密后的密码
                    return new User(username,seller.getPassword(),grantedAuthorities);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
