package com.example.androidstudioproject_tomroche.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudioproject_tomroche.entities.Term;
import com.example.androidstudioproject_tomroche.R;

import java.util.ArrayList;
import java.util.List;

public class TermsListAdapter extends RecyclerView.Adapter<TermsListAdapter.TermViewHolder>{
    ArrayList<Term> terms;
    ArrayList<TermClickHandler> termClickHandlers = new ArrayList<>();

    public TermsListAdapter(List<Term> terms){
        this.terms = (ArrayList<Term>) terms;
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_list_item,parent,false);
        TermViewHolder termViewHolder = new TermViewHolder(view);
        return termViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        holder.termText.setText(terms.get(position).getName());
        holder.layout.setOnClickListener(view -> {

            for(TermClickHandler termClickHandler:termClickHandlers){
                termClickHandler.onTermClicked(terms.get(position));
            }

        });
    }

    public class TermViewHolder extends RecyclerView.ViewHolder{
        TextView termText;
        ConstraintLayout layout;

        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            termText = itemView.findViewById(R.id.item_name_txt);
            layout = itemView.findViewById(R.id.list_item);
        }
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void addTermClickHandler(TermClickHandler termClickHandler){
        termClickHandlers.add(termClickHandler);
    }

    public interface TermClickHandler{
        void onTermClicked(Term term);
    }
}