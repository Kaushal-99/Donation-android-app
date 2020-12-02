package com.example.donation;

import android.content.Context;
import android.util.Log;
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
    ArrayList<Integer> postId;
    Context context;
    ArrayList<String[]> allval;
    public RequestAdapter(ArrayList<String> ngo_names,ArrayList<String> titles,ArrayList<String> status,ArrayList<String> items_list, ArrayList<Integer> postId,ArrayList<String []>allval){
        this.ngo_names=ngo_names;
        this.titles=titles;
        this.status=status;
        this.items_list=items_list;
        this.postId=postId;
        this.allval=allval;
    }
    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        context=parent.getContext();
        View view=inflater.inflate(R.layout.request_item,parent,false);
        return new RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        String ngoname=allval.get(position)[1];
        String donation_title=allval.get(position)[0];
        String stat=allval.get(position)[2];
        String items_data=allval.get(position)[3];
        holder.ngo_name.setText(ngoname);
        holder.title.setText(donation_title);
        DB_handling db=new DB_handling(context);

        if(allval.get(position).length>5 && !stat.equalsIgnoreCase("accepted")&&/*(postId.get(position)!=-1)*/ db.getPostIsCompleted(Integer.parseInt(allval.get(position)[4]))){
            holder.status.setText("This Request have already been completed");
        }
        else{
            holder.status.setText(stat);
        }
        holder.items.setText(items_data);
    }

    @Override
    public int getItemCount() {
        return allval.size();
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
