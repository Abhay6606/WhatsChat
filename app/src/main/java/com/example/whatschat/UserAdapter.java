package com.example.whatschat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    ArrayList<UsserGettersetter> userArrayList;


      UserAdapter(Context context, ArrayList<UsserGettersetter> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ussers_items,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        UsserGettersetter usserGettersetter=userArrayList.get(position);
        holder.userName.setText(usserGettersetter.name);
        holder.userStatus.setText(usserGettersetter.status);
        Picasso.get().load(userArrayList.get(position).imageuri).into(holder.userImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChatWindow.class);
                intent.putExtra("nameee",usserGettersetter.getName());
                intent.putExtra("usserImg",usserGettersetter.getImageuri());
                intent.putExtra("uid",usserGettersetter.getUserId());
                context.startActivity(intent);
            }
        });

    }
    public UserAdapter() {
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName,userStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.item_imageView);
            userName=itemView.findViewById(R.id.usserNameId);
            userStatus=itemView.findViewById(R.id.statusId);

        }
    }
}
