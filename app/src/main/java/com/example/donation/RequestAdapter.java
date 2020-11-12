package com.example.donation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestHolder>{
    ArrayList<String> ngo_names;
    ArrayList<String> titles;
    ArrayList<String> status;
    ArrayList<String> items_list;
    public RequestAdapter(ArrayList<String> ngo_names,ArrayList<String> titles,ArrayList<String> status,ArrayList<String> items_list){
        this.ngo_names=ngo_names;
        this.titles=titles;
        this.status=status;
        this.items_list=items_list;
    }
    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.request_item,parent,false);
        return new RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        String ngoname=ngo_names.get(position);
        String donation_title=titles.get(position);
        String stat=status.get(position);
        String items_data=items_list.get(position);
        holder.ngo_name.setText(ngoname);
        holder.title.setText(donation_title);
        holder.status.setText(stat);
        holder.items.setText(items_data);
    }

    @Override
    public int getItemCount() {
        return ngo_names.size();
    }

    public class RequestHolder extends RecyclerView.ViewHolder{
        TextView ngo_name,title,status,items;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            ngo_name=(TextView)itemView.findViewById(R.id.ngo_name);
            title=(TextView)itemView.findViewById(R.id.title);
            status=(TextView)itemView.findViewById(R.id.status);
            items=(TextView)itemView.findViewById(R.id.items);
        }
    }
}
