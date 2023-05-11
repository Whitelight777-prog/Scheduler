package com.example.androidstudioproject_tomroche.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.androidstudioproject_tomroche.entities.Term;

import java.util.ArrayList;
import java.util.List;

public class TermSpinnerAdapter extends ArrayAdapter<Term> {

    public TermSpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.setDropDownViewResource(android.R.layout.simple_spinner_item);
    }

    @Override
    public void add(Term term){
        super.add(term);
    }

    public boolean contains(String termName){
        for(int i = 0; i < getCount(); i++){
            if (termName.equals(getItem(i).getName())){
                return true;
            }
        }
        return false;
    }

    public List<Term> getAll(){
        List<Term> terms = new ArrayList<>();
        for (int i = 0; i < getCount(); i++){
            terms.add(getItem(i));
        }
        return terms;
    }

}
