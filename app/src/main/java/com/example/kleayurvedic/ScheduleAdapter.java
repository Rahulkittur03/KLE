package com.example.kleayurvedic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kleayurvedic.Model.Schedule;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ScheduleAdapter extends FirestoreRecyclerAdapter<Schedule,ScheduleAdapter.ScheduleViewHolder> {
    Context context;

    public ScheduleAdapter(@NonNull FirestoreRecyclerOptions<Schedule> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position, @NonNull Schedule schedule) {
        if (schedule.getSchedule_Status().equals("1")) {
            holder.itemView.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setVisibility(View.GONE);
        }
        holder.UsernametextView.setText(schedule.getUsername());
        holder.OPD_numbertextView.setText(schedule.getUserOPD_Number());
        holder.DescriptiontextView.setText(schedule.getDescription());
        holder.DatetextView.setText(schedule.getSchedule_Date());

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, Schedule_SMS_details.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Username",schedule.getUsername());
            intent.putExtra("OPD_number",schedule.getUserOPD_Number());
            intent.putExtra("Schedule_Date",schedule.getSchedule_Date());
            intent.putExtra("Schedule_Time",schedule.getSchedule_Text_time());
            intent.putExtra("Contact_no",schedule.getUserContactNo());
            String docId=getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            // **  IPD NUMBER  **
           try {
                if (!schedule.getUserIPD_Number().isEmpty()) {
                    intent.putExtra("userIPDnumber", schedule.getUserIPD_Number());
               }
            }
            catch (Exception e) {
               Log.i("not available", "na");
            }
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleruseritem,parent,false);
        return new ScheduleAdapter.ScheduleViewHolder(view);
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder{
        TextView UsernametextView,OPD_numbertextView,DescriptiontextView,DatetextView;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            UsernametextView=itemView.findViewById(R.id.recycler_Name);
            OPD_numbertextView=itemView.findViewById(R.id.recycler_OPD);
            DescriptiontextView=itemView.findViewById(R.id.recycler_problem);
            DatetextView=itemView.findViewById(R.id.recycler_timeStamp);
        }
    }
}
