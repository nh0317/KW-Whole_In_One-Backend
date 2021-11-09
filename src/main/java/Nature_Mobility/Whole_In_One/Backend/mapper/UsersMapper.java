package Nature_Mobility.Whole_In_One.Backend.mapper;

import Nature_Mobility.Whole_In_One.Backend.domain.users.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UsersMapper {
    int save(UserInfo userInfo);
    int update(UserInfo userInfo);
    int delete(UserInfo userInfo);
    Optional<UserInfo> findByIdx(@Param("userIdx") Long userIdx);
    List<UserInfo> findByStatus(@Param("status") Integer status);
    List<UserInfo> findById(@Param("userId") String userId);
    List<UserInfo> findByEmail(@Param("email") String email);
    List<UserInfo> findByIdAndStatus(@Param("userId") String userId, @Param("status") Integer status);
    List<UserInfo> findByEmailAndStatus(@Param("email") String email, @Param("status") Integer status);
    List<UserInfo> findByStatusAndNicknameIsContaining(@Param("status") Integer status, @Param("word") String word);

    Optional<Integer> cntCoupon(@Param("userIdx") Long userIdx);
    Optional<Integer> cntStoreLike(@Param("userIdx") Long userIdx);
    Optional<Integer> cntReservation(@Param("userIdx") Long userIdx);
}
