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

import com.example.kleayurvedic.Model.Users;
import com.example.kleayurvedic.utils.AndroidUtility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UserAdapter extends FirestoreRecyclerAdapter<Users, UserAdapter.UserViewHolder> {
    Context context;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<Users> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users user) {
        holder.NameTextView.setText(user.getUsername());
        holder.OPD_no_TextView.setText(user.getUserOPDnumber());
        holder.ProblemTextView.setText(user.getUserProblem());
        holder.TimestampTextView.setText(AndroidUtility.TimestamptoString(user.getTimestamp()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("username", user.getUsername());
                intent.putExtra("userOPDnumber", user.getUserOPDnumber());
                intent.putExtra("usercontactNo", user.getUsercontactNo());
                intent.putExtra("userProblem", user.getUserProblem());
                intent.putExtra("timestamp", user.getTimestamp());
                String docId=getSnapshots().getSnapshot(position).getId();
                intent.putExtra("docId",docId);
                try {
                    if (!user.getUserIPDnumber().isEmpty()) {
                        intent.putExtra("userIPDnumber", user.getUserIPDnumber());
                    }
                }
                catch (Exception e) {
                    Log.i("not available", "na");
                }
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleruseritem,parent,false);
        return new UserViewHolder(view);
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        TextView NameTextView,OPD_no_TextView,ProblemTextView,TimestampTextView;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            NameTextView=itemView.findViewById(R.id.recycler_Name);
            OPD_no_TextView=itemView.findViewById(R.id.recycler_OPD);
            ProblemTextView=itemView.findViewById(R.id.recycler_problem);
            TimestampTextView=itemView.findViewById(R.id.recycler_timeStamp);
        }
    }
}
