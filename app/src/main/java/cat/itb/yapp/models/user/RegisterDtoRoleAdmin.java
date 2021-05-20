package cat.itb.yapp.models.user;

import lombok.Data;

@Data
public class RegisterDtoRoleAdmin {
    private String username;
    private String email;
    private String password;
    private String password2;
    private String name;
    private String surnames;
    private String photoUrl;
    private String phone;
    private String specialistType;
    private int collegiateNumber;

    private String nameClinic;
    private String addressClinic;
    private String phoneNumberClinic;
    private String emailClinic;
}