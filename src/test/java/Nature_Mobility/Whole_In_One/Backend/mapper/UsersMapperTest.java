package Nature_Mobility.Whole_In_One.Backend.mapper;

import Nature_Mobility.Whole_In_One.Backend.domain.users.DTOUsers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static Nature_Mobility.Whole_In_One.Backend.domain.UserStatus.ACTIVE;
import static Nature_Mobility.Whole_In_One.Backend.domain.UserStatus.INACTIVE;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UsersMapperTest {
    @Autowired
    UsersMapper usersMapper;

    @Test
    void saveAndFindById() {
        DTOUsers users = new DTOUsers("kim@naver.com","aaaa123","1234","KIM","김하나");
        log.info(users.toString());
        usersMapper.save(users);
        DTOUsers result=usersMapper.findById(users.getUserId()).get();
        log.info(result.toString());
        assertThat(users.getUserIdx()).isEqualTo(result.getUserIdx());
    }

    @Test
    void delete() {
        DTOUsers users = new DTOUsers("kim@naver.com","ab123","1234","KIM","김하나");
        log.info(users.toString());
        usersMapper.save(users);
        DTOUsers result=usersMapper.findById(users.getUserId()).get();
        usersMapper.delete(result);
        DTOUsers result2=usersMapper.findById(users.getUserId()).get();
        assertThat(result2).isEqualTo(null);
    }

    @Test
    void findByStatus() {
        List<DTOUsers> users = new ArrayList<>();
        users.add(new DTOUsers("kim@naver.com", "aaaa123", "1234", "KIM", "김하나"));
        users.add(new DTOUsers("abcd@naver.com","dfd123","1234","KIM","김하나"));
        users.add(new DTOUsers("dfsf@naver.com","ooo123","1234","KIM","김하나"));
        users.add(new DTOUsers("erer@naver.com","vvvv123","1234","KIM","김하나"));
        for(DTOUsers u : users)
            usersMapper.save(u);
        DTOUsers updateUser=usersMapper.findById(users.get(2).getUserId()).get();
        updateUser.setUserStatus(INACTIVE.getValue());
        usersMapper.save(updateUser);

        List<DTOUsers> usersList = usersMapper.findByStatus(ACTIVE.getValue());
        int idx=0;
        for(DTOUsers u : usersList){
            assertThat(u.getUserStatus()).isEqualTo(ACTIVE.getValue());
            log.info(u.toString());
            idx++;
        }
    }

    @Test
    void findByEmailAndStatus() {
        List<DTOUsers> users = new ArrayList<>();
        users.add(new DTOUsers("kim@naver.com", "aaaa123", "1234", "KIM", "김하나"));
        users.add(new DTOUsers("abcd@naver.com","dfd123","1234","KIM","김하나"));
        users.add(new DTOUsers("dfsf@naver.com","ooo123","1234","KIM","김하나"));
        users.add(new DTOUsers("erer@naver.com","vvvv123","1234","KIM","김하나"));
        for(DTOUsers u : users)
            usersMapper.save(u);
        DTOUsers updateUser=usersMapper.findById(users.get(2).getUserId()).get();
        updateUser.setUserStatus(INACTIVE.getValue());
        usersMapper.save(updateUser);

       List<DTOUsers> usersList = usersMapper.findByEmail("kim@naver.com");
        for(DTOUsers user : usersList){
            assertThat(user.getUserEmail()).isEqualTo("kim@naver.com");
            log.info(user.toString());
        }
    }

    @Test
    void findByStatusAndNicknameIsContaining() {
        List<DTOUsers> users = new ArrayList<>();
        users.add(new DTOUsers("kim@naver.com", "aaaa123", "1234", "KIM", "김하나"));
        users.add(new DTOUsers("abcd@naver.com","dfd123","1234","Park","김하나"));
        users.add(new DTOUsers("dfsf@naver.com","ooo123","1234","HiH","김하나"));
        users.add(new DTOUsers("erer@naver.com","vvvv123","1234","iiii","김하나"));
        for(DTOUsers u : users)
            usersMapper.save(u);
        DTOUsers updateUser=usersMapper.findById(users.get(2).getUserId()).get();
        updateUser.setUserStatus(INACTIVE.getValue());
        usersMapper.save(updateUser);

        List<DTOUsers> usersList = usersMapper.findByStatusAndNicknameIsContaining(ACTIVE.getValue(),"i");
        for(DTOUsers u : usersList){
            assertThat(u.getUserNickname().contains("I")|u.getUserNickname().contains("i")).isTrue();
            assertThat(u.getUserStatus()).isEqualTo(ACTIVE.getValue());
            log.info(u.toString());
        }
    }


}