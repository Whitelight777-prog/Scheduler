package com.example.androidstudioproject_tomroche.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.androidstudioproject_tomroche.R;
import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.dao.TermDAO;
import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.entities.Course;
import com.example.androidstudioproject_tomroche.entities.Term;
import com.example.androidstudioproject_tomroche.entities.TermCourses;

import java.util.ArrayList;
import java.util.List;

public class EditTermsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Database database = null;
    TermDAO termDAO = null;
    ArrayList<Term> terms;
    Term term = null;
    Term selected = null;
    TermSpinnerAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_terms);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        database = Room.databaseBuilder(getApplicationContext(), Database.class, Database.DATABASE_NAME).allowMainThreadQueries().build();

        termDAO = database.termDAO();
        terms = (ArrayList<Term>) termDAO.getAll();
        adapter = new TermSpinnerAdapter(this, android.R.layout.simple_spinner_item, terms);
        spinner.setAdapter(adapter);

        EditText startDate = findViewById(R.id.term_startdate_txt);

        Button termStartDateBtn = findViewById(R.id.term_stardate_btn);
        termStartDateBtn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditTermsActivity.this,
                    (view, year12, month12, dayOfMonth) -> startDate.setText((month12 + 1) + "/" + dayOfMonth + "/" + year12), year, month, day);

            datePickerDialog.show();

        });

        TextView endDate = findViewById(R.id.term_enddate_txt);

        Button termEndDateBtn = findViewById(R.id.term_enddate_btn);
        termEndDateBtn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(EditTermsActivity.this,
                    (view, year1, month1, dayOfMonth) -> endDate.setText((month1 + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);

            datePickerDialog.show();

        });

        Button saveButton = findViewById(R.id.edit_terms_add_terms_btn);
        TextView nameTextView = findViewById(R.id.term_name_txt2);

        saveButton.setOnClickListener(v -> {
            if (!(nameTextView.getText().length() == 0 || startDate.getText().length() == 0 ||
                endDate.getText().length() == 0)) {
                Term term = new Term(nameTextView.getText().toString(), startDate.getText().toString(), endDate.getText().toString());
                if (!adapter.contains(term.getName()))
                {
                    termDAO.insertAll(term);
                    adapter.add(term);
                    spinner.setSelection(terms.indexOf(term));
                }
                else {
                    Toast.makeText(this,"Term with matching name already found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button updateButton = findViewById(R.id.activity_edit_terms_update_btn);
        updateButton.setOnClickListener(v -> {
            if (selected != null){
                adapter.remove(selected);
                List<Term> currentCourses = adapter.getAll();
                adapter.clear();

                selected.setName(nameTextView.getText().toString());
                if (!startDate.getText().toString().equals(selected.getStartDate())){
                    selected.setStartDate(startDate.getText().toString());
                    // Update alarm
                }
                if (!endDate.getText().toString().equals(selected.getEndDate())){
                    selected.setEndDate(endDate.getText().toString());
                    // Update end alarm
                }
                database.termDAO().update(selected);
                currentCourses.add(selected);
                adapter.addAll(currentCourses);
                spinner.setSelection(adapter.getPosition(selected));
            }
        });

        Button removeButton = findViewById(R.id.edit_terms_remove_term_btn);
        removeButton.setOnClickListener(v -> {
            List<TermCourses> termCourses = database.termCourseDAO().getTermsWithCourses();
            for(TermCourses tc:termCourses){
                if(tc.term.getTermId() == selected.getTermId()){
                    boolean foundCourse = false;
                    for (Course course : tc.courses) {
                        if (course.getTermId() == selected.getTermId()) {
                            foundCourse = true;
                            break;
                        }
                    }
                    if (foundCourse){
                        Toast toast = Toast.makeText(getApplicationContext(), "Cannot delete a term if courses are assigned to it", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        adapter.remove(selected);
                        spinner.setSelection(0);
                        termDAO.delete(selected);
                        if (adapter.isEmpty()) {
                            nameTextView.setText("");
                            startDate.setText("");
                            endDate.setText("");
                        }
                        Toast toast = Toast.makeText(getApplicationContext(),"Successfully deleted", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                }
            }
        });

        setAppBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (database != null){
            if (database.isOpen()){
                database.close();
            }
        }
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.edit_terms_tb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Button saveUpdateButton = findViewById(R.id.edit_terms_add_terms_btn);
        selected = adapter.getItem(position);

        if (position >= 0 && selected != null) {
            TextView textView = findViewById(R.id.term_name_txt2);
            textView.setText(selected.getName());

            textView = findViewById(R.id.term_startdate_txt);
            textView.setText(selected.getStartDate());

            textView = findViewById(R.id.term_enddate_txt);
            textView.setText(selected.getEndDate());

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}