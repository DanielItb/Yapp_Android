package cat.itb.yapp.models.clinic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUpdateClinicDto {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
}
