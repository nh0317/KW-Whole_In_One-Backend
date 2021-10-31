package Nature_Mobility.Whole_In_One.Backend.mapper;

import Nature_Mobility.Whole_In_One.Backend.domain.DTOUsers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UsersMapper {
    int save(DTOUsers users);
    int delete(DTOUsers users);
    Optional<DTOUsers> findById(@Param("userId") String userId);
    List<DTOUsers> findByStatus(@Param("status") Integer status);
    List<DTOUsers> findByEmailAndStatus(@Param("email") String email, @Param("status") Integer status);
    List<DTOUsers> findByStatusAndNicknameIsContaining(@Param("status") Integer status, @Param("word") String word);
}
