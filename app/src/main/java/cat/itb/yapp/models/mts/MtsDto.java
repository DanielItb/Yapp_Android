package cat.itb.yapp.models.mts;

import java.io.Serializable;

import lombok.Data;

@Data
public class MtsDto implements Serializable {
    /// mts
    private String id;
    private String date;
    // patient
    private Integer patientId;
    private String patientFullName;
    private String patientAge;
    private String patientPhone;
    private String patientPhoto;
    // specialist
    private Long specialistId;
    private String specialistFullName;
    private String specialistType;
    private String reason;

    //treatment
    private int tratmentId;
    //clinic
    private Integer clinicId;
    private String clinicName;
}
