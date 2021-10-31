package Nature_Mobility.Whole_In_One.Backend.src.user;//package com.softsquared.template.src.user;
//
//import com.softsquared.template.src.user.models.UserInfo;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
//public interface UserInfoRepository extends CrudRepository<UserInfo, Integer> {
//    List<UserInfo> findByStatus(String status);
//    List<UserInfo> findByEmailAndStatus(String email, String status);
//    List<UserInfo> findByStatusAndNicknameIsContaining(String status, String word);
//}