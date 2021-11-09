package Nature_Mobility.Whole_In_One.Backend.domain.users;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PatchUserReq {
    private String email;
    private String nickname;
    private String name;
    private String userImage;
}
