package cat.itb.yapp.models.patient;

import lombok.Data;

@Data
public class PatientDto {
    private Integer id;
    private String name;
    private String surname;
    private String reason;
    private String phoneNumber;
    private String email;
    private String urlPhoto;
    //
    private Float age;
    private String homeAddress;
    private String schoolName;
    private String course;
    private String paymentType;
    private boolean active = true;
    //clinic
    private Integer clinicId;
    private String clinicName;

}
