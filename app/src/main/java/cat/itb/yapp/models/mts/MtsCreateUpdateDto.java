package cat.itb.yapp.models.mts;

import lombok.Data;

@Data
public class MtsCreateUpdateDto {
    private int patientId;
    private Long specialistId;
    private int treatmentId;
    private String date;

}
