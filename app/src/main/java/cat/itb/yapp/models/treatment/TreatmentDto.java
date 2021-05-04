package cat.itb.yapp.models.treatment;

import lombok.Data;

@Data
public class TreatmentDto {
    private String id;
    private String reason;
    private String sessionsFinished;
    private String startDate;
    private String patientId;
    private String patientFullName;
    private String patientAge;
    private String patientPhone;
    private String patientPhoto;
    private String specialistId;
    private String specialistFullName;
    private String specialistType;
}
