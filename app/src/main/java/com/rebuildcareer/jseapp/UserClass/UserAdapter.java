package com.rebuildcareer.jseapp.UserClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rebuildcareer.jseapp.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{


    private Context context;
    private ArrayList<Member> members;

    public UserAdapter(Context c, ArrayList<Member> members){

        this.context = c;
        this.members = members;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Member member = this.members.get(position);

        holder.txtPersonName.setText(member.getFullName());
        holder.txtQualificationName.setText(member.getQualification());
        holder.txtExperienceNumber.setText(member.getExperience());
        holder.txtExpectedSalaryNumber.setText(member.getExpectedSalary());
        holder.txtLocationName.setText(member.getCurrentLocation());


    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPersonName,txtQualificationName, txtExperienceNumber, txtExpectedSalaryNumber, txtLocationName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPersonName = itemView.findViewById(R.id.txtPersonName);
            txtQualificationName = itemView.findViewById(R.id.txtQualificationName);
            txtExperienceNumber = itemView.findViewById(R.id.txtExperienceNumber);
            txtExpectedSalaryNumber = itemView.findViewById(R.id.txtExpectedSalaryNumber);
            txtLocationName = itemView.findViewById(R.id.txtLocationName);

        }
    }
}
