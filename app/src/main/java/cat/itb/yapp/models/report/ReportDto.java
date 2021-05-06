package cat.itb.yapp.models.report;

import lombok.Data;

@Data
public class ReportDto {
    private String id;
    private String diagnosis;
    private String objectives;
    private String date;
    private String patientId;
    private String patientFullName;
    private String specialistId;
    private String specialistFullName;
    private String specialistType;
}
