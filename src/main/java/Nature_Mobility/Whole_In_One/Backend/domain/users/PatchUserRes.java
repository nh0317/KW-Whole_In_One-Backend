package Nature_Mobility.Whole_In_One.Backend.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRes {
    private String email;
    private String nickname;
    private String name;
    private String userImage;
}
