package com.naturemobility.seoul.service;

import com.naturemobility.seoul.domain.CustomUserDetails;
import com.naturemobility.seoul.domain.partners.PartnerInfo;
import com.naturemobility.seoul.domain.partners.PartnerStatus;
import com.naturemobility.seoul.domain.users.UserInfo;
import com.naturemobility.seoul.domain.users.UserStatus;
import com.naturemobility.seoul.mapper.PartnerMapper;
import com.naturemobility.seoul.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersMapper userMapper;
    @Autowired
    private PartnerMapper partnerMapper;

    public CustomUserDetailsService (UsersMapper usersMapper){
        this.userMapper = usersMapper;
        log.info("유저 디테일 서비스 생성");
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        //사용자의 정보를 CustomUserDetails 형으로 가져온다.
//        log.info("loadUserByUsername 시작");
        String[] strings = userEmail.split(":");
        if (strings.length <2)
            throw new UsernameNotFoundException(userEmail);
        userEmail = strings[0];
        log.info("이메일 : "+userEmail);
        if (strings[1].equals("user")) {
            //사용자 정보를 가져옴
            UserInfo user;
            List<UserInfo> userList = userMapper.findByEmailAndStatus(userEmail, UserStatus.ACTIVE.getValue());
            if (userList.size() > 0)
                user = userList.get(0);
                //만약 해당 username의 사용자 정보가 없다면 예외처리
            else throw new UsernameNotFoundException(userEmail);
            //성공이면 사용자의 정보가 담긴 user 리턴
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

            CustomUserDetails c = new CustomUserDetails(user.getUserEmail(), user.getUserPassword(), user.getIsAccountNonExpired(), user.getIsEnabled(), authorities);
            return c;
        }
        else if (strings[1].equals("partner")){
            PartnerInfo partnerInfo;
            List<PartnerInfo> partnerList = partnerMapper.findByEmailAndStatus(userEmail, PartnerStatus.ACTIVE.getValue());
            if (partnerList.size() > 0)
                partnerInfo = partnerList.get(0);
                //만약 해당 username의 사용자 정보가 없다면 예외처리
            else throw new UsernameNotFoundException(userEmail);

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

            CustomUserDetails c = new CustomUserDetails(partnerInfo.getPartnerEmail(), partnerInfo.getPartnerPassword(), partnerInfo.getIsAccountNonExpired(), partnerInfo.getIsEnabled(), authorities);
            return c;
        }
        else throw new UsernameNotFoundException(userEmail);
    }
}