package Nature_Mobility.Whole_In_One.Backend.mapper;

import Nature_Mobility.Whole_In_One.Backend.domain.users.DTOUsers;
import Nature_Mobility.Whole_In_One.Backend.domain.users.GetMyPageRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UsersMapper {
    int save(DTOUsers users);
    int update(DTOUsers users);
    int delete(DTOUsers users);
    Optional<DTOUsers> findByIdx(@Param("userIdx") Long userIdx);
    List<DTOUsers> findByStatus(@Param("status") Integer status);
    List<DTOUsers> findById(@Param("userId") String userId);
    List<DTOUsers> findByEmail(@Param("email") String email);
    List<DTOUsers> findByIdAndStatus(@Param("userId") String userId, @Param("status") Integer status);
    List<DTOUsers> findByEmailAndStatus(@Param("email") String email, @Param("status") Integer status);
    List<DTOUsers> findByStatusAndNicknameIsContaining(@Param("status") Integer status, @Param("word") String word);

    Optional<Integer> cntCoupon(@Param("userIdx") Long userIdx);
    Optional<Integer> cntStoreLike(@Param("userIdx") Long userIdx);
    Optional<Integer> cntReservation(@Param("userIdx") Long userIdx);
}
