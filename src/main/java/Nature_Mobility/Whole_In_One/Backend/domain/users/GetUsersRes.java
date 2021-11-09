package Nature_Mobility.Whole_In_One.Backend.domain.users;

import lombok.*;

@Getter
@AllArgsConstructor
public class GetUsersRes {
    private final Long userId;
    private final String email;
}
