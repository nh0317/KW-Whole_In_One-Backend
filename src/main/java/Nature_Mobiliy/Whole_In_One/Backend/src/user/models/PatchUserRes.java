package Nature_Mobiliy.Whole_In_One.Backend.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchUserRes {
    private final String email;
    private final String nickname;
    private final String phoneNumber;
}
