package cat.itb.yapp.models.clinic;

import lombok.Data;

@Data
public class ClinicDto {
    private Integer id;
    private String name;
    private String address;
    private String photo;
    private String phoneNumber;
    private String email;
    private String registerDate;

}
