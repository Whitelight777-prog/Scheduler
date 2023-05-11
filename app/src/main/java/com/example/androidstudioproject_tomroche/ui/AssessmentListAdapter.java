package com.example.androidstudioproject_tomroche.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidstudioproject_tomroche.entities.Assessment;
import com.example.androidstudioproject_tomroche.R;

import java.util.ArrayList;
import java.util.List;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.AssessmentViewHolder>{
    ArrayList<Assessment> assessments;
    ArrayList<AssessmentClickHandler> assessmentClickHandlers = new ArrayList<>();

    public AssessmentListAdapter(List<Assessment> assessments){
        this.assessments = (ArrayList<Assessment>) assessments;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_list_item,parent,false);
        AssessmentViewHolder assessmentViewHolder = new AssessmentViewHolder(view);
        return assessmentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        holder.assessmentText.setText(assessments.get(position).getName());
        holder.layout.setOnClickListener(view -> {

            for(AssessmentClickHandler assessmentClickHandler:assessmentClickHandlers){
                assessmentClickHandler.onAssessmentClicked(assessments.get(position));
            }

        });
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder{
        TextView assessmentText;
        ConstraintLayout layout;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentText = itemView.findViewById(R.id.item_name_txt);
            layout = itemView.findViewById(R.id.list_item);
        }
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public void addAssessmentClickHandler(AssessmentClickHandler assessmentClickHandler){
        assessmentClickHandlers.add(assessmentClickHandler);
    }

    public interface AssessmentClickHandler{
        void onAssessmentClicked(Assessment assessment);
    }
}

