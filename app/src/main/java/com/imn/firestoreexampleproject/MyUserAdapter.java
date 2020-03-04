package com.imn.firestoreexampleproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyUserAdapter extends RecyclerView.Adapter<MyUserAdapter.MyUserHolder> {

    private ArrayList<User> list;
    Context context;

    public MyUserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new MyUserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserAdapter.MyUserHolder holder, final int position) {
        holder.First.setText(list.get(position).firstName);
        holder.Last.setText(list.get(position).lastName);

        holder.Ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "GO", Toast.LENGTH_SHORT).show();
                Intent intentt = new Intent(v.getContext(), EditUser.class);
                intentt.putExtra("firstt", list.get(position).firstName);
                intentt.putExtra("lastt", list.get(position).lastName);
                v.getContext().startActivity(intentt);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyUserHolder extends RecyclerView.ViewHolder {
        public TextView First;
        public TextView Last;
        public LinearLayout Ll;

        public MyUserHolder(View itemView) {
            super(itemView);
            this.First = itemView.findViewById(R.id.first);
            this.Last = itemView.findViewById(R.id.last);
            this.Ll = itemView.findViewById(R.id.ll);
        }
    }
}
