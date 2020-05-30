package com.example.bakethis.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakethis.Object.StepsObject;
import com.example.bakethis.R;
import com.example.bakethis.databinding.StepsSingleBinding;

import java.util.ArrayList;

public class StepsAdapter extends  RecyclerView.Adapter<StepsAdapter.SingleStepView> {
    private StepsSingleBinding rvLayout;
    private Context context;
    private ArrayList<StepsObject> stepsList;
    private StepsClickListener stepsClickListener;

    public interface StepsClickListener{
        void onStepClick(int position);
    }

    public StepsAdapter(Context context, ArrayList<StepsObject> stepsList, StepsClickListener stepsClickListener) {
        this.context = context;
        this.stepsList = stepsList;
        this.stepsClickListener = stepsClickListener;
    }

    @NonNull
    @Override
    public SingleStepView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StepsSingleBinding view = StepsSingleBinding.inflate(LayoutInflater.from(context));
        return new SingleStepView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleStepView holder, int position) {
        StepsObject currentStep = stepsList.get(position);
        holder.tvStepDesc.setText((position+1) + ". " + currentStep.getShortDesc());
        if(!currentStep.getVideoUrl().isEmpty() || !currentStep.getThumbnailUrl().isEmpty()){
            holder.tvReadWatch.setText(R.string.watch_more);
        }
        else{
            holder.tvReadWatch.setText(R.string.read_more);
        }
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class SingleStepView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvStepDesc;
        TextView tvReadWatch;

        public SingleStepView(@NonNull StepsSingleBinding itemView) {
            super(itemView.getRoot());
            rvLayout = itemView;
            tvStepDesc = rvLayout.tvStepDesc;
            tvReadWatch = rvLayout.tvReadWatch;
            itemView.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            stepsClickListener.onStepClick(getAdapterPosition());
        }
    }
}
