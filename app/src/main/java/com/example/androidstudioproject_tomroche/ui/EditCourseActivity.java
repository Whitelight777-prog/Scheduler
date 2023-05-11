package com.example.androidstudioproject_tomroche.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.androidstudioproject_tomroche.R;
import com.example.androidstudioproject_tomroche.dao.CourseDAO;
import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.entities.CourseInstructor;
import com.example.androidstudioproject_tomroche.entities.Term;
import com.example.androidstudioproject_tomroche.entities.TermCourses;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EditCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {
    public static final String COURSE_NOTIFICATION = "CourseNotification";

    public final static String CHANNEL_ID = "CHANNEL_ID";
    public final static String NOTIFICATION_START_CHANNEL_ID = "Course Start Reminder Notifications";
    public final static String NOTIFICATION_END_CHANNEL_ID = "Course End Reminder Notifications";
    public final static String CONTENT_TITLE = "CONTENT_TITLE";
    public final static String CONTENT_TEXT = "CONTENT_TEXT";
    public final static  String PRIORITY = "PRIORITY";
    public final static String SMALL_ICON = "SMALL_ICON";
    Database database = null;
    CourseDAO courseDAO = null;
    TimePickerDialog timePicker;
    TimePickerDialog endTimePicker;
    NotificationType notType = null;
    long alarmTime;
    long endTime;
    Course selected;
    Calendar now;
    int dayOfMonth;

    Spinner spinner = null;
    Button courseStartDateBtn = null;
    Button endDateButton = null;
    Button saveButton = null;
    TextView courseNameText = null;
    TextView nameTextView = null;
    TextView startDateTextView = null;
    TextView endDateTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        setFields();

        Term term = (Term) getIntent().getSerializableExtra(Term.TERM_ACCESS);

        spinner.setOnItemSelectedListener(this);
        database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();

        courseDAO = database.courseDAO();

        List<TermCourses> termCourses = database.termCourseDAO().getTermsWithCourses();
        List<Course> courses = new ArrayList<>();

        for (TermCourses tc : termCourses) {
            if (tc.term.getTermId() == term.getTermId()) {
                courses = tc.courses;
                break;
            }
        }

        CourseSpinnerAdapter courseSpinnerAdapter = new CourseSpinnerAdapter(this, android.R.layout.simple_spinner_item, courses);
        spinner.setAdapter(courseSpinnerAdapter);

        now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));

        courseStartDateBtn.setOnClickListener(v -> {
            notType = NotificationType.START;
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH);
            int month = calendar.get(android.icu.util.Calendar.MONTH);
            int year = calendar.get(android.icu.util.Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditCourseActivity.this,
                    (view, year1, month1, dayOfMonth) -> startDateTextView.setText((month1 + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
            datePickerDialog.setOnDismissListener(dialog -> {
/*                timePicker = new TimePickerDialog(this, this, now.getTime().getHours(), now.getTime().getMinutes(), false);
                timePicker.show();*/
                Calendar c = Calendar.getInstance();
                int currentDate = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("Current Date: " + currentDate);
                System.out.println("Calendar Date" + dayOfMonth);
            });
            datePickerDialog.show();
        });

        endDateButton.setOnClickListener(v -> {
            notType = NotificationType.END;
            android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
            int day = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH);
            int month = calendar.get(android.icu.util.Calendar.MONTH);
            int year = calendar.get(android.icu.util.Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditCourseActivity.this,
                    (view, year12, month12, dayOfMonth) ->
                        endDateTextView.setText((month12 + 1) + "/" + dayOfMonth + "/" + year12), year, month, day);

            datePickerDialog.setOnDismissListener(dialog -> {
/*                endTimePicker = new TimePickerDialog(this, this, now.getTime().getHours(), now.getTime().getMinutes(), false);
                endTimePicker.show();*/
                //
            });
            datePickerDialog.show();
        });

        saveButton.setOnClickListener(v -> {
            if (!(nameTextView.getText().length() == 0 || startDateTextView.getText().length() == 0 ||
                    endDateTextView.getText().length() == 0)) {
                Course course = new Course(nameTextView.getText().toString(), startDateTextView.getText().toString(), endDateTextView.getText().toString(), term.getTermId(), CourseStatus.UNKNOWN.toString());
                if (!courseSpinnerAdapter.contains(course.getName())){
                    database.courseDAO().insertAll(course);
                    courseSpinnerAdapter.add(course);
                    spinner.setSelection(courseSpinnerAdapter.getPosition(course));

                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(Course.COURSE_ACCESS, course.getName());
                    intent.putExtra(CHANNEL_ID, NOTIFICATION_START_CHANNEL_ID);
                    intent.putExtra(CONTENT_TEXT, course.getName() + " starts in 24 hours");
                    intent.putExtra(CONTENT_TITLE, "Course Start Date Reminder");
                    intent.putExtra(SMALL_ICON, android.R.drawable.sym_def_app_icon);
                    intent.putExtra(PRIORITY, 0);
                    intent.setAction("Set start alarm");
                    PendingIntent sender = PendingIntent.getBroadcast(EditCourseActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, sender);

                    intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(Course.COURSE_ACCESS, course.getName());
                    intent.putExtra(CHANNEL_ID, NOTIFICATION_END_CHANNEL_ID);
                    intent.putExtra(CONTENT_TEXT, course.getName() + " ends in 24 hours");
                    intent.putExtra(CONTENT_TITLE, "Course End Date Reminder");
                    intent.putExtra(SMALL_ICON, android.R.drawable.btn_star);
                    intent.putExtra(PRIORITY, 0);
                    intent.setAction("Set end alarm");

                    sender = PendingIntent.getBroadcast(EditCourseActivity.this, 1, intent, PendingIntent.FLAG_MUTABLE);
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, sender);

                    Toast.makeText(getApplicationContext(), course.getName() + " start and end alarms set", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"Course with matching name already found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button updateBtn = findViewById(R.id.activity_edit_course_update_btn);
        updateBtn.setOnClickListener(v -> {
            if (selected != null){
                courseSpinnerAdapter.remove(selected);
                List<Course> currentCourses = courseSpinnerAdapter.getAll();
                courseSpinnerAdapter.clear();

                selected.setName(nameTextView.getText().toString());
                if (!startDateTextView.getText().toString().equals(selected.getStartDate())){
                    selected.setStartDate(startDateTextView.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent sender = PendingIntent.getBroadcast(EditCourseActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(sender);

                    intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(Course.COURSE_ACCESS, selected.getName());
                    intent.putExtra(CHANNEL_ID, NOTIFICATION_END_CHANNEL_ID);
                    intent.putExtra(CONTENT_TEXT, selected.getName() + " begins in 24 hours");
                    intent.putExtra(CONTENT_TITLE, "Course Start Date Reminder");
                    intent.putExtra(SMALL_ICON, android.R.drawable.btn_star);
                    intent.putExtra(PRIORITY, 0);
                    intent.setAction("Set start alarm");

                    PendingIntent updated = PendingIntent.getBroadcast(EditCourseActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, updated);

                    Toast.makeText(getApplicationContext(), selected.getName() + " start date alarm updated", Toast.LENGTH_SHORT).show();
                }
                if (!endDateTextView.getText().toString().equals(selected.getEndDate())){
                    selected.setEndDate(endDateTextView.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent sender = PendingIntent.getBroadcast(EditCourseActivity.this, 1, intent, PendingIntent.FLAG_MUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(sender);

                    intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(Course.COURSE_ACCESS, selected.getName());
                    intent.putExtra(CHANNEL_ID, NOTIFICATION_END_CHANNEL_ID);
                    intent.putExtra(CONTENT_TEXT, selected.getName() + " ends in 24 hours");
                    intent.putExtra(CONTENT_TITLE, "Course End Date Reminder");
                    intent.putExtra(SMALL_ICON, android.R.drawable.btn_star);
                    intent.putExtra(PRIORITY, 0);
                    intent.setAction("Set end alarm");
                    PendingIntent updated = PendingIntent.getBroadcast(EditCourseActivity.this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, updated);

                    Toast.makeText(getApplicationContext(), selected.getName() + " end date alarm updated", Toast.LENGTH_SHORT).show();

                }
                database.courseDAO().update(selected);
                currentCourses.add(selected);
                courseSpinnerAdapter.addAll(currentCourses);
                spinner.setSelection(courseSpinnerAdapter.getPosition(selected));
            }
        });

        Button removeButton = findViewById(R.id.activity_edit_course_remove_course_btn);
        removeButton.setOnClickListener(v -> {
            Course course = (Course) spinner.getSelectedItem();
            if (course != null) {
                List<CourseInstructor> courseInstructors = database.courseInstructorDAO().getCourseInstructors();
                for(CourseInstructor ci : courseInstructors){
                    if (ci.course.getCourseId() == course.getCourseId()){
                        if (ci.instructor != null){
                            database.instructorDAO().delete(ci.instructor);
                        }
                    }
                }
                courseDAO.delete(course);
                courseSpinnerAdapter.remove(course);
                spinner.setSelection(0);
                if (courseSpinnerAdapter.isEmpty()) {
                    courseNameText.setText(selected.getName());
                    startDateTextView.setText(selected.getStartDate());
                    endDateTextView.setText(selected.getEndDate());
                }
            }
        });
        setAppBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database != null) {
            if (database.isOpen()) {
                database.close();
            }
        }
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.edit_courses_tb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position >= 0) {
            Course course = (Course) spinner.getAdapter().getItem(position);
            selected = course;
            TextView textView = findViewById(R.id.activity_edit_course_name_tf);
            textView.setText(course.getName());

            textView = findViewById(R.id.activity_edit_course_startdate_tf);
            textView.setText(course.getStartDate());

            textView = findViewById(R.id.activity_edit_course_enddate_tf);
            textView.setText(course.getEndDate());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setFields(){
        spinner = findViewById(R.id.activity_edit_course_spinner);
        courseStartDateBtn = findViewById(R.id.activity_edit_course_startdate_btn);
        endDateButton = findViewById(R.id.activity_edit_course_enddate_btn);
        saveButton = findViewById(R.id.activity_edit_course_save_btn);
        courseNameText = findViewById(R.id.activity_edit_course_name_tf);
        nameTextView = findViewById((R.id.activity_edit_course_name_tf));
        startDateTextView = findViewById(R.id.activity_edit_course_startdate_tf);
        endDateTextView = findViewById(R.id.activity_edit_course_enddate_tf);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = now;
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        if (notType == NotificationType.START){
            alarmTime = cal.getTimeInMillis();
        }
        else {
            endTime = cal.getTimeInMillis();
        }
    }

    enum NotificationType {
        START, END
    }
}