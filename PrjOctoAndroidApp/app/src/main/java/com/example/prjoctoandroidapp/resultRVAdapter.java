package com.example.prjoctoandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class resultRVAdapter extends RecyclerView.Adapter<resultRVAdapter.ViewHolder> {

    // arraylist for storing our data and context
    private ArrayList<DataModal> dataModalArrayList;
    private Context context;

    // constructor for our variables
    public resultRVAdapter(ArrayList<DataModal> dataModalArrayList, Context context) {
        this.dataModalArrayList = dataModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inside on create view holder method we are inflating our layout file which we created.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // inside on bind view holder method we are setting
        // data to each item of recycler view.
        DataModal modal = dataModalArrayList.get(position);
        holder.resultTV.setText(modal.getResult());
        holder.confidenceTV.setText("" + modal.getConfidence());
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return dataModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text view.
        private TextView resultTV, confidenceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            resultTV = itemView.findViewById(R.id.idTVResult);
            confidenceTV = itemView.findViewById(R.id.idTVConfidence);
        }
    }
}
