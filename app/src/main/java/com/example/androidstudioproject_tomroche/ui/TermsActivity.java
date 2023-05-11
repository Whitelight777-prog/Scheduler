package com.example.androidstudioproject_tomroche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidstudioproject_tomroche.dao.Database;
import com.example.androidstudioproject_tomroche.dao.TermDAO;
import com.example.androidstudioproject_tomroche.entities.Term;
import com.example.androidstudioproject_tomroche.R;

import java.util.ArrayList;

public class TermsActivity extends AppCompatActivity implements TermsListAdapter.TermClickHandler{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        Database database = Room.databaseBuilder(getApplicationContext(),Database.class,Database.DATABASE_NAME).allowMainThreadQueries().build();

        TermDAO termDAO = database.termDAO();

        ArrayList<Term> itemterms = new ArrayList<>(termDAO.getAll());
        TermsListAdapter termsListAdapter = new TermsListAdapter(itemterms);
        termsListAdapter.addTermClickHandler(this);
        RecyclerView recyclerView = findViewById(R.id.termView);
        recyclerView.setAdapter(termsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       Button manageButton = findViewById(R.id.manage_terms_btn);
       manageButton.setOnClickListener(view -> {
           Intent intent = new Intent(this,EditTermsActivity.class);
           startActivity(intent);
       });

       setAppBar();

       database.close();
    }

    @Override
    public void onResume(){
        super.onResume();
        Database database = Room.databaseBuilder(getApplicationContext(),Database.class,Database.DATABASE_NAME).allowMainThreadQueries().build();

        TermDAO termDAO = database.termDAO();

        ArrayList<Term> itemterms = new ArrayList<>(termDAO.getAll());
        TermsListAdapter termsListAdapter = new TermsListAdapter(itemterms);
        termsListAdapter.addTermClickHandler(this);
        RecyclerView recyclerView = findViewById(R.id.termView);
        recyclerView.setAdapter(termsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setAppBar();

        database.close();
    }

    private void setAppBar() {
        Toolbar toolbar = findViewById(R.id.terms_tb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onTermClicked(Term term) {
        Intent intent = new Intent(getApplicationContext(), DetailedTermActivity.class);
        intent.putExtra(Term.TERM_ACCESS,term);
        startActivity(intent);
    }
}
