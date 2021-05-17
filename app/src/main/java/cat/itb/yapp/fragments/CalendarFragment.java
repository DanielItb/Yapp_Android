package cat.itb.yapp.fragments;

import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cat.itb.yapp.R;
import cat.itb.yapp.activities.MainActivity;
import cat.itb.yapp.fragments.forms.ReportFormFragmentArgs;
import cat.itb.yapp.fragments.list.ReportListFragmentDirections;
import cat.itb.yapp.models.mts.MtsDto;
import cat.itb.yapp.models.report.ReportDto;
import cat.itb.yapp.models.user.UserDto;
import cat.itb.yapp.retrofit.RetrofitHttp;
import cat.itb.yapp.utils.UtilsAuth;
import cat.itb.yapp.webservices.MtsServiceClient;
import cat.itb.yapp.webservices.UserWebServiceClient;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener, View.OnClickListener {
    private NavController navController;
    private WeekView mWeekView;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    int cont = 0;

    private MaterialButton oneDayButton, threeDayButton, oneWeekButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        mWeekView = (WeekView) v.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(true);

        oneDayButton = v.findViewById(R.id.oneDayButton);
        threeDayButton = v.findViewById(R.id.threeDayButton);
        oneWeekButton = v.findViewById(R.id.oneWeekButton);

        oneDayButton.setOnClickListener(this);
        threeDayButton.setOnClickListener(this);
        oneWeekButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oneDayButton:
                mWeekViewType = TYPE_DAY_VIEW;
                mWeekView.setNumberOfVisibleDays(1);

                // Lets change some dimensions to best fit the view.
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                break;
            case R.id.threeDayButton:
                mWeekViewType = TYPE_THREE_DAY_VIEW;
                mWeekView.setNumberOfVisibleDays(3);

                // Lets change some dimensions to best fit the view.
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                break;
            case R.id.oneWeekButton:
                mWeekViewType = TYPE_WEEK_VIEW;
                mWeekView.setNumberOfVisibleDays(7);

                // Lets change some dimensions to best fit the view.
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                break;

        }
    }


    @Override
    public void onEmptyViewLongPress(java.util.Calendar time) {
        Toast.makeText(getContext(), "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        CalendarFragmentDirections.ActionCalendarFragmentToMtsFormFragment dir =
                CalendarFragmentDirections.actionCalendarFragmentToMtsFormFragment();
        dir.setMtsDto(MainFragment.listMts.get(Integer.parseInt(String.valueOf(event.getId()))));

        navController.navigate(dir);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getContext(), "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }
        private HashMap<Integer, MtsDto> allEvents = new HashMap<>() ;



    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        while (MainFragment.listMts == null){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (cont < 1) {
            for (int j = 0; j < MainFragment.listMts.size(); j++) {
                MtsDto mts = MainFragment.listMts.get(j);
                String date = mts.getDate().replace('T', ' ');
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime mtsDate = LocalDateTime.parse(date, formatter);

                LocalDateTime todayDate = LocalDateTime.now();

                int hour = mtsDate.getHour();
                int min = mtsDate.getMinute();
                int month = mtsDate.getMonthValue();
                int year = mtsDate.getYear();
                int day = mtsDate.getDayOfMonth();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, min);
                startTime.set(Calendar.DAY_OF_MONTH, day);
                startTime.set(Calendar.MONTH, month - 1);
                startTime.set(Calendar.YEAR, year);

                Calendar endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.HOUR_OF_DAY, hour);
                endTime.set(Calendar.MINUTE, 45);
                endTime.set(Calendar.MONTH, month - 1);

                WeekViewEvent event = new WeekViewEvent(j, mts.getPatientFullName(), startTime, endTime);

                if (mtsDate.isBefore(todayDate)) {
                    event.setColor(getResources().getColor(R.color.mtsBefore));
                } else {
                    event.setColor(getResources().getColor(R.color.mtsAfter));
                }

                events.add(event);
                cont++;
            }
        }
        return events;
    }




    protected String getEventTitle(java.util.Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(java.util.Calendar.HOUR_OF_DAY), time.get(java.util.Calendar.MINUTE), time.get(java.util.Calendar.MONTH) + 1, time.get(java.util.Calendar.DAY_OF_MONTH));
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(java.util.Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }





    /*https://github.com/alamkanak/Android-Week-View/tree/develop/sample/src/main*/

}