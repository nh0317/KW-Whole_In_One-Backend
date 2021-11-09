package Nature_Mobility.Whole_In_One.Backend.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLoginRes {
    private final Long userIdx;
    private final String jwt;
}
