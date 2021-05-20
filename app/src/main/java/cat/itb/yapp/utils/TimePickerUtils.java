package cat.itb.yapp.utils;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

public class TimePickerUtils {

    public static String timePicker(String date, Context context) {
        final String[] finalDate = new String[1];
        String dateTime;
        // Get Current Time
        int mHour = 0;
        int mMinute = 0;

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String finalHour;
                String finalMinute;

                if (hourOfDay < 10){
                    finalHour = "0" + hourOfDay;
                }else{
                    finalHour = String.valueOf(hourOfDay);
                }

                if (minute < 10){
                    finalMinute = "0" + minute;
                }else{
                    finalMinute = String.valueOf(minute);
                }

                finalDate[0] = date + "T" + finalHour + ":" + finalMinute + ":00" ;


            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
        dateTime = finalDate[0];
        return dateTime;
    }
}
