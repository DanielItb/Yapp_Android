package cat.itb.yapp.models.treatment;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUpdateTreatmentDto {
    // treatment
    private String reason;
    private int sessionsFinished;
    private String startDate;
    private int patientId;
    private Long userId;
}
