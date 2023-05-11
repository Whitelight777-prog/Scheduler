package com.example.androidstudioproject_tomroche.ui;

import static com.example.androidstudioproject_tomroche.ui.EditCourseActivity.CHANNEL_ID;
import static com.example.androidstudioproject_tomroche.ui.EditCourseActivity.CONTENT_TEXT;
import static com.example.androidstudioproject_tomroche.ui.EditCourseActivity.CONTENT_TITLE;
import static com.example.androidstudioproject_tomroche.ui.EditCourseActivity.NOTIFICATION_END_CHANNEL_ID;
import static com.example.androidstudioproject_tomroche.ui.EditCourseActivity.NOTIFICATION_START_CHANNEL_ID;
import static com.example.androidstudioproject_tomroche.ui.EditCourseActivity.PRIORITY;
import static com.example.androidstudioproject_tomroche.ui.EditCourseActivity.SMALL_ICON;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.entities.CourseAssessments;
import com.example.androidstudioproject_tomroche.R;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EditAssessmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {

    Database database = null;
    AssessmentSpinnerAdapter adapter;
    Course course = null;
    long startTime;
    long endTime;
    Calendar now;
    TimePickerDialog startTimePicker;
    TimePickerDialog endTimePicker;
    static int assRequestId = 100;
    Assessment selected;
    EditCourseActivity.NotificationType notType;
    String assessmentType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);

        course = (Course) getIntent().getSerializableExtra(Course.COURSE_ACCESS);
        Button endDateButton = findViewById(R.id.activity_edit_assessment_enddate_btn);
        Button assessmentStartDateBtn = findViewById(R.id.activity_edit_assessment_startdate_btn);
        Button saveButton = findViewById(R.id.activity_edit_assessment_add_assmnt_btn);
        TextView assessmentNameText = findViewById(R.id.activity_edit_assessment_name_tf);
        Spinner spinner = findViewById(R.id.activity_edit_assessment_spinner);
        TextView nameTextView = findViewById((R.id.activity_edit_assessment_name_tf));
        TextView startDate = findViewById(R.id.activity_edit_assessment_startdate_tf);
        TextView endDate = findViewById(R.id.activity_edit_assessment_enddate_tf);
        Button removeButton = findViewById(R.id.activity_edit_assessment_remove_assmnt_btn);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.test_type_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("checked Id " + checkedId );
                if(checkedId == R.id.edit_assessment_performance_btn){
                    assessmentType = "Performance";
                    System.out.println("Performance");
                } else {
                    assessmentType = "Objective";
                    System.out.println("Objective");

                }
            }
        });

        spinner.setOnItemSelectedListener(this);
        database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();

        List<Assessment> assessments = getAssessments();

        adapter = new AssessmentSpinnerAdapter(this, android.R.layout.simple_spinner_item, assessments);
        spinner.setAdapter(adapter);

        now = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()));

        assessmentStartDateBtn.setOnClickListener(v -> {
            notType = EditCourseActivity.NotificationType.START;
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH);
            int month = calendar.get(android.icu.util.Calendar.MONTH);
            int year = calendar.get(android.icu.util.Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditAssessmentActivity.this,
                    (view, year1, month1, dayOfMonth) -> startDate.setText((month1 + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
            datePickerDialog.setOnDismissListener(dialog -> {
                startTimePicker = new TimePickerDialog(this, this, now.getTime().getHours(), now.getTime().getMinutes(), false);
                startTimePicker.show();
            });
            datePickerDialog.show();
        });

        endDateButton.setOnClickListener(v -> {
            notType = EditCourseActivity.NotificationType.END;
            android.icu.util.Calendar calendar = android.icu.util.Calendar.getInstance();
            int day = calendar.get(android.icu.util.Calendar.DAY_OF_MONTH);
            int month = calendar.get(android.icu.util.Calendar.MONTH);
            int year = calendar.get(android.icu.util.Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditAssessmentActivity.this,
                    (view, year2, month2, dayOfMonth) -> endDate.setText((month2 + 1) + "/" + dayOfMonth + "/" + year2), year, month, day);
            datePickerDialog.setOnDismissListener(dialog -> {
                endTimePicker = new TimePickerDialog(this, this, now.getTime().getHours(), now.getTime().getMinutes(), false);
                endTimePicker.show();
            });
            datePickerDialog.show();
        });

        saveButton.setOnClickListener(v -> {
            if (!(nameTextView.getText().length() == 0 || startDate.getText().length() == 0 || endDate.getText().length() == 0)) {
                Assessment assessment = new Assessment(nameTextView.getText().toString(), startDate.getText().toString(), endDate.getText().toString(), course.getCourseId(), assessmentType);
                if (!adapter.contains(assessment.getName())) {
                    database.assessmentDAO().insertAll(assessment);
                    adapter.add(assessment);
                    spinner.setSelection(adapter.getPosition(assessment));

                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(Assessment.ASS_ACCESS, assessment.getName());
                    intent.putExtra(CHANNEL_ID, NOTIFICATION_START_CHANNEL_ID);
                    intent.putExtra(CONTENT_TEXT, assessment.getName() + " starts in 24 hours");
                    intent.putExtra(CONTENT_TITLE, "Assessment Start Date Reminder");
                    intent.putExtra(SMALL_ICON, android.R.drawable.sym_def_app_icon);
                    intent.putExtra(PRIORITY, 0);
                    intent.setAction("Set start alarm");

                    PendingIntent sender = PendingIntent.getBroadcast(EditAssessmentActivity.this, 2, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, sender);

                    intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra(Assessment.ASS_ACCESS, assessment.getName());
                    intent.putExtra(CHANNEL_ID, NOTIFICATION_END_CHANNEL_ID);
                    intent.putExtra(CONTENT_TEXT, assessment.getName() + " ends in 24 hours");
                    intent.putExtra(CONTENT_TITLE, "Assessment End Date Reminder");
                    intent.putExtra(SMALL_ICON, android.R.drawable.btn_star);
                    intent.putExtra(PRIORITY, 0);
                    intent.setAction("Set end alarm");

                    sender = PendingIntent.getBroadcast(EditAssessmentActivity.this, 3, intent, PendingIntent.FLAG_MUTABLE);
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, sender);

                    Toast.makeText(getApplicationContext(), assessment.getName() + " start and end alarms set", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Assessment with matching name already found", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button updateBtn = findViewById(R.id.activity_edit_assessment_update_btn);

        updateBtn.setOnClickListener(v -> {
            adapter.remove(selected);
            List<Assessment> currentAssessments = adapter.getAll();
            adapter.clear();

            selected.setName(nameTextView.getText().toString());
            if (!startDate.getText().toString().equals(selected.getStartDate())){
                selected.setStartDate(startDate.getText().toString());

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(EditAssessmentActivity.this, 2, intent, PendingIntent.FLAG_MUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(sender);

                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra(Assessment.ASS_ACCESS, selected.getName());
                intent.putExtra(CHANNEL_ID, NOTIFICATION_END_CHANNEL_ID);
                intent.putExtra(CONTENT_TEXT, selected.getName() + " begins in 24 hours");
                intent.putExtra(CONTENT_TITLE, "Assessment Start Date Reminder");
                intent.putExtra(SMALL_ICON, android.R.drawable.btn_star);
                intent.putExtra(PRIORITY, 0);
                intent.setAction("Set start alarm");

                PendingIntent updated = PendingIntent.getBroadcast(EditAssessmentActivity.this, 2, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, updated);

                Toast.makeText(getApplicationContext(), selected.getName() + " start date alarm updated", Toast.LENGTH_SHORT).show();
            }
            if (!endDate.getText().toString().equals(selected.getEndDate())){
                selected.setEndDate(endDate.getText().toString());

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(EditAssessmentActivity.this, 3, intent, PendingIntent.FLAG_MUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(sender);

                intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                intent.putExtra(Assessment.ASS_ACCESS, selected.getName());
                intent.putExtra(CHANNEL_ID, NOTIFICATION_END_CHANNEL_ID);
                intent.putExtra(CONTENT_TEXT, selected.getName() + " begins in 24 hours");
                intent.putExtra(CONTENT_TITLE, "Assessment Start Date Reminder");
                intent.putExtra(SMALL_ICON, android.R.drawable.btn_star);
                intent.putExtra(PRIORITY, 0);
                intent.setAction("Set start alarm");

                PendingIntent updated = PendingIntent.getBroadcast(EditAssessmentActivity.this, 3, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime, updated);

                Toast.makeText(getApplicationContext(), selected.getName() + " start date alarm updated", Toast.LENGTH_SHORT).show();
            }
            database.assessmentDAO().update(selected);
            currentAssessments.add(selected);
            adapter.addAll(currentAssessments);
            spinner.setSelection(adapter.getPosition(selected));
        });


        Assessment assessment = (Assessment) spinner.getSelectedItem();
        removeButton.setOnClickListener(v -> {
            database.assessmentDAO().delete(assessment);
            adapter.remove(assessment);
            spinner.setSelection(0);
            if (adapter.isEmpty()) {
                startDate.setText(selected.getStartDate());
                endDate.setText(selected.getEndDate());
                assessmentNameText.setText(selected.getName());
            }

            //TODO: I'm not sure that this is required and I don't remember
            //      why I put it here in the first place. I'm sure there was
            //      a reason so I'm just going to comment it out and we'll
            //      see what breaks if anything breaks
//            adapter.clear();
//            List<CourseAssessments> courseAssessments =
//                    database.courseAssDAO().getCourseAssessments();
//            for(CourseAssessments ca : courseAssessments){
//                if (ca.course.getCourseId() == course.getCourseId()){
//                    adapter.addAll(ca.assessments);
//                }
//            }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Button saveUpdateButton = findViewById(R.id.activity_edit_assessment_add_assmnt_btn);
        adapter = (AssessmentSpinnerAdapter) ((Spinner)(findViewById(R.id.activity_edit_assessment_spinner))).getAdapter();
        Assessment assessment = adapter.getItem(position);

        if (position >= 0) {
            TextView textView = findViewById(R.id.activity_edit_assessment_name_tf);
            textView.setText(assessment.getName());

            textView = findViewById(R.id.activity_edit_assessment_startdate_tf);
            textView.setText(assessment.getStartDate());

            textView = findViewById(R.id.activity_edit_assessment_enddate_tf);
            textView.setText(assessment.getEndDate());

        }

        selected = assessment;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar cal = now;
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        if (notType == EditCourseActivity.NotificationType.START){
            startTime = cal.getTimeInMillis();
        }
        else {
            endTime = cal.getTimeInMillis();
        }
    }

    private List<Assessment> getAssessments() {
        List<CourseAssessments> courseAssessments = database.courseAssDAO().getCourseAssessments();
        List<Assessment> assessments = new ArrayList<>();
        for (CourseAssessments ca : courseAssessments) {
            if (ca.course.getCourseId() == course.getCourseId()) {
                assessments = ca.assessments;
            }
        }
        return assessments;
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.edit_assessment_tb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}