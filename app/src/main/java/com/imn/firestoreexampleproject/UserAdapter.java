package com.imn.firestoreexampleproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private Context context;
    private ArrayList<User> list;
    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.UserHolder holder, final int position) {
        holder.First.setText(list.get(position).firstName);
        holder.Last.setText(list.get(position).lastName);

        Glide.with(context).load(list.get(position).imageUrl).into(holder.imageView);

        holder.Ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "GO", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), DummyActivity.class);
                intent.putExtra("first", list.get(position).firstName);
                intent.putExtra("last", list.get(position).lastName);
                v.getContext().startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        public TextView First;
        public TextView Last;
        public LinearLayout Ll;
        public ImageView imageView;

        public UserHolder(View itemView) {
            super(itemView);
            this.First = itemView.findViewById(R.id.first);
            this.Last = itemView.findViewById(R.id.last);
            this.Ll = itemView.findViewById(R.id.ll);
            this.imageView = itemView.findViewById(R.id.recyclerimage);
        }
    }

}


