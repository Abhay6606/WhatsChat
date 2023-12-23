package com.example.whatschat;

import static com.example.whatschat.ChatWindow.recieverImage;
import static com.example.whatschat.ChatWindow.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MsgModelClass> messageArraylist;
    int ITEM_SEND = 1;
    int ITEM_RECEVE = 2;

    public MessageAdapter() {
    }

    public MessageAdapter(Context context, ArrayList<MsgModelClass> messageArraylist) {
        this.context = context;
        this.messageArraylist = messageArraylist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new senderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciever_layout, parent, false);
            return new recieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MsgModelClass message = messageArraylist.get(position);

        if (holder.getClass() == senderViewHolder.class) {
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.textView.setText(message.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.imageView);
        } else {
            recieverViewHolder recieverViewHolder = (recieverViewHolder) holder;
            recieverViewHolder.textView.setText(message.getMessage());
            Picasso.get().load(recieverImage).into(recieverViewHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return messageArraylist.size();
    }


    @Override
    public int getItemViewType(int position) {
        MsgModelClass message = messageArraylist.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.senderImage);
            textView = itemView.findViewById(R.id.senderText);

        }
    }

    class recieverViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView textView;

        public recieverViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recieverImage);
            textView = itemView.findViewById(R.id.recieverText);
        }
    }


}
