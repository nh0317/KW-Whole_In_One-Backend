package Nature_Mobility.Whole_In_One.Backend.src.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserRes {
    private final int userId;
    private final String email;
    private final String nickname;
    private final String phoneNumber;
}