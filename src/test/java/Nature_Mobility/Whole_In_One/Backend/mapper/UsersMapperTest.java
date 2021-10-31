package Nature_Mobility.Whole_In_One.Backend.mapper;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOUsers;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class UsersMapperTest {
    @Autowired
    UsersMapper usersMapper;

    @Test
    void saveAndFindById() {
        DTOUsers users = new DTOUsers("kim@naver.com","abc123","1234","KIM","김하나");
        log.info(users.toString());
        usersMapper.save(users);
        DTOUsers result=usersMapper.findById(users.getUserId()).get();
        log.info(result.toString());
        assertThat(users.getUserIdx()).isEqualTo(result.getUserIdx());

        //update 확인
        result.setUserName("Hana");
        result.setUserPoint(1);
        result.setUserStatus(1);
        usersMapper.save(result);
        DTOUsers update=usersMapper.findById(result.getUserId()).get();
        log.info(update.toString());
        assertThat(update.getUserIdx()).isEqualTo(result.getUserIdx());
        assertThat(update.getUserName()).isEqualTo(result.getUserName());
    }

    @Test
    void delete() {
        DTOUsers users = new DTOUsers("kim@naver.com","abc123","1234","KIM","김하나");
        log.info(users.toString());
        usersMapper.save(users);
        DTOUsers result=usersMapper.findById(users.getUserId()).orElseGet(()->null);
        usersMapper.delete(result);
        DTOUsers result2=usersMapper.findById(result.getUserId()).orElseGet(()->null);
        assertThat(result2).isEqualTo(null);
    }

    @Test
    void findByStatus() {
        List<DTOUsers> users = new ArrayList<>();
        users.add(new DTOUsers("kim@naver.com", "abc123", "1234", "KIM", "김하나"));
        users.add(new DTOUsers("abcd@naver.com","dfd123","1234","KIM","김하나"));
        users.add(new DTOUsers("dfsf@naver.com","ooo123","1234","KIM","김하나"));
        users.add(new DTOUsers("erer@naver.com","vvvv123","1234","KIM","김하나"));
        for(DTOUsers u : users)
            usersMapper.save(u);
        DTOUsers updateUser=usersMapper.findById(users.get(2).getUserId()).get();
        updateUser.setUserStatus(2);
        usersMapper.save(updateUser);

        List<DTOUsers> usersList = usersMapper.findByStatus(0);
        int idx=0;
        for(DTOUsers u : usersList){
            if(idx==2) idx++;

            assertThat(u.getUserId()).isEqualTo(users.get(idx).getUserId());
            assertThat(u.getUserIdx()).isEqualTo(users.get(idx).getUserIdx());
            log.info(u.toString());
            idx++;
        }
    }

    @Test
    void findByEmailAndStatus() {
        List<DTOUsers> users = new ArrayList<>();
        users.add(new DTOUsers("kim@naver.com", "abc123", "1234", "KIM", "김하나"));
        users.add(new DTOUsers("abcd@naver.com","dfd123","1234","KIM","김하나"));
        users.add(new DTOUsers("dfsf@naver.com","ooo123","1234","KIM","김하나"));
        users.add(new DTOUsers("erer@naver.com","vvvv123","1234","KIM","김하나"));
        for(DTOUsers u : users)
            usersMapper.save(u);
        DTOUsers updateUser=usersMapper.findById(users.get(2).getUserId()).get();
        updateUser.setUserStatus(2);
        usersMapper.save(updateUser);

        List<DTOUsers> usersList = usersMapper.findByEmailAndStatus("kim@naver.com",0);
        for(DTOUsers u : usersList){
            assertThat(u.getUserEmail()).isEqualTo("kim@naver.com");
            assertThat(u.getUserStatus()).isEqualTo(0);
            log.info(u.toString());
        }
    }

    @Test
    void findByStatusAndNicknameIsContaining() {
        List<DTOUsers> users = new ArrayList<>();
        users.add(new DTOUsers("kim@naver.com", "abc123", "1234", "KIM", "김하나"));
        users.add(new DTOUsers("abcd@naver.com","dfd123","1234","Park","김하나"));
        users.add(new DTOUsers("dfsf@naver.com","ooo123","1234","HiH","김하나"));
        users.add(new DTOUsers("erer@naver.com","vvvv123","1234","iiii","김하나"));
        for(DTOUsers u : users)
            usersMapper.save(u);
        DTOUsers updateUser=usersMapper.findById(users.get(2).getUserId()).get();
        updateUser.setUserStatus(2);
        usersMapper.save(updateUser);

        List<DTOUsers> usersList = usersMapper.findByStatusAndNicknameIsContaining(0,"i");
        for(DTOUsers u : usersList){
            assertThat(u.getUserNickname().contains("I")|u.getUserNickname().contains("i")).isTrue();
            assertThat(u.getUserStatus()).isEqualTo(0);
            log.info(u.toString());
        }
    }


}