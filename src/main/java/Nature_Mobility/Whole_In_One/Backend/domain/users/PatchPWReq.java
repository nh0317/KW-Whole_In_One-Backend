package Nature_Mobility.Whole_In_One.Backend.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatchPWReq {
    String password;
    String newPassword;
    String confirmNewPassword;
    public PatchPWReq(){}
}
