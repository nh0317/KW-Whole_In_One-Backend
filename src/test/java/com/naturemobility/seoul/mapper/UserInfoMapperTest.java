package com.naturemobility.seoul.mapper;

import com.naturemobility.seoul.domain.users.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.naturemobility.seoul.domain.users.UserStatus.ACTIVE;
import static com.naturemobility.seoul.domain.users.UserStatus.INACTIVE;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UserInfoMapperTest {
    @Autowired
    UsersMapper usersMapper;

    @Test
    void saveAndFindById() {
        UserInfo userInfo = new UserInfo("kim@naver.com","1234","KIM","김하나");
        log.info(userInfo.toString());
        usersMapper.save(userInfo);
        UserInfo result=usersMapper.findByIdx(userInfo.getUserIdx()).get();
        log.info(result.toString());
        assertThat(userInfo.getUserIdx()).isEqualTo(result.getUserIdx());
    }

    @Test
    void delete() {
        UserInfo userInfo = new UserInfo("kim@naver.com","1234","KIM","김하나");
        log.info(userInfo.toString());
        usersMapper.save(userInfo);
        UserInfo result=usersMapper.findByIdx(userInfo.getUserIdx()).get();
        usersMapper.delete(result);
        UserInfo result2=usersMapper.findByIdx(userInfo.getUserIdx()).orElseGet(()->null);
        assertThat(result2).isEqualTo(null);
    }

    @Test
    void findByStatus() {
        List<UserInfo> users = new ArrayList<>();
        users.add(new UserInfo("kim@naver.com", "1234", "KIM", "김하나"));
        users.add(new UserInfo("abcd@naver.com","1234","KIM","김하나"));
        users.add(new UserInfo("dfsf@naver.com","1234","KIM","김하나"));
        users.add(new UserInfo("erer@naver.com","1234","KIM","김하나"));
        for(UserInfo u : users)
            usersMapper.save(u);
        UserInfo updateUser=usersMapper.findByIdx(users.get(2).getUserIdx()).get();
        updateUser.setUserStatus(INACTIVE.getValue());
        usersMapper.save(updateUser);

        List<UserInfo> userInfoList = usersMapper.findByStatus(ACTIVE.getValue());
        int idx=0;
        for(UserInfo u : userInfoList){
            assertThat(u.getUserStatus()).isEqualTo(ACTIVE.getValue());
            log.info(u.toString());
            idx++;
        }
    }

    @Test
    void findByEmailAndStatus() {
        List<UserInfo> users = new ArrayList<>();
        users.add(new UserInfo("kim@naver.com", "1234", "KIM", "김하나"));
        users.add(new UserInfo("abcd@naver.com","1234","KIM","김하나"));
        users.add(new UserInfo("dfsf@naver.com","1234","KIM","김하나"));
        users.add(new UserInfo("erer@naver.com","1234","KIM","김하나"));
        for(UserInfo u : users)
            usersMapper.save(u);
        UserInfo updateUser=usersMapper.findByIdx(users.get(2).getUserIdx()).get();
        updateUser.setUserStatus(INACTIVE.getValue());
        usersMapper.save(updateUser);

       List<UserInfo> userInfoList = usersMapper.findByEmail("kim@naver.com");
        for(UserInfo user : userInfoList){
            assertThat(user.getUserEmail()).isEqualTo("kim@naver.com");
            log.info(user.toString());
        }
    }

    @Test
    void findByStatusAndNicknameIsContaining() {
        List<UserInfo> users = new ArrayList<>();
        users.add(new UserInfo("kim@naver.com", "1234", "KIM", "김하나"));
        users.add(new UserInfo("abcd@naver.com","1234","Park","김하나"));
        users.add(new UserInfo("dfsf@naver.com","1234","HiH","김하나"));
        users.add(new UserInfo("erer@naver.com","1234","iiii","김하나"));
        for(UserInfo u : users)
            usersMapper.save(u);
        UserInfo updateUser=usersMapper.findByIdx(users.get(2).getUserIdx()).get();
        updateUser.setUserStatus(INACTIVE.getValue());
        usersMapper.save(updateUser);

        List<UserInfo> userInfoList = usersMapper.findByStatusAndNicknameIsContaining(ACTIVE.getValue(),"i");
        for(UserInfo u : userInfoList){
            assertThat(u.getUserNickname().contains("I")|u.getUserNickname().contains("i")).isTrue();
            assertThat(u.getUserStatus()).isEqualTo(ACTIVE.getValue());
            log.info(u.toString());
        }
    }


}