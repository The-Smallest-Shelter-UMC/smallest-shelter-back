package umc_sjs.smallestShelter.user.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOrganizationPageRes {

    private Long userIdx;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private String role;
    private Integer profileImgUrl;
    private String OrganizationName;
}
