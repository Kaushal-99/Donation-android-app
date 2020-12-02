package com.example.donation;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
public class RequestNGOAdapter extends RecyclerView.Adapter<RequestNGOAdapter.RequestNGOHolder>{
    ArrayList<String> donor_names;
    ArrayList<String> subject;
    ArrayList<String> items;
    ArrayList<String> post;
    ArrayList<String> user_req;
    ArrayList<String> donor_email;
    Context context;

    public RequestNGOAdapter(ArrayList<String> donor_names,ArrayList<String> subject,ArrayList<String> items,ArrayList<String> post,ArrayList<String> user_req,ArrayList<String> donor_email){
        this.donor_names=donor_names;
        this.subject=subject;
        this.items=items;
        this.post=post;
        this.user_req=user_req;
        this.donor_email=donor_email;
    }
    @NonNull
    @Override
    public RequestNGOHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        context=parent.getContext();
        View view=inflater.inflate(R.layout.request_item_ngo,parent,false);
        return new RequestNGOHolder(view);
    }

    @Override
    @SuppressLint("WrongConstant")
    public void onBindViewHolder(@NonNull RequestNGOHolder holder, final int position) {
        String donorname=donor_names.get(position);
        String donation_title=subject.get(position);
        String items_donate=items.get(position);
        //int postid=Integer.parseInt(post.get(position));

        if(post.get(position).equals(" ")){
            holder.subject.setText(donation_title);
            holder.acc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DB_handling db=new DB_handling(view.getContext());
                    db.updateUserRequest(Integer.parseInt(user_req.get(position)),"accepted");
                    Toast.makeText(view.getContext(),"Request accepted",Toast.LENGTH_SHORT).show();
                    donor_names.remove(position);
                    subject.remove(position);
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,donor_names.size());
                }
            });
            holder.rej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DB_handling db=new DB_handling(view.getContext());
                    db.updateUserRequest(Integer.parseInt(user_req.get(position)),"rejected");
                    Toast.makeText(view.getContext(),"Request rejected",Toast.LENGTH_SHORT).show();
                    donor_names.remove(position);
                    subject.remove(position);
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,donor_names.size());
                }
            });
        }
        else{
            SpannableString content = new SpannableString(donation_title);
            content.setSpan(new UnderlineSpan(), 0, donation_title.length(), 0);
            holder.subject.setText(content);
            holder.subject.setTextColor(context.getResources().getColor(R.color.link_color));
            holder.subject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent view_post=new Intent(view.getContext(),View_Post.class);
                    view_post.putExtra("post id",post.get(position));
                    view.getContext().startActivity(view_post);
                }
            });
            DB_handling db=new DB_handling(context);
           // Log.i("postID",String.valueOf(db.getPostIsCompleted(Integer.parseInt(post.get(position)))));
            if(db.getPostIsCompleted(Integer.parseInt(post.get(position))) ){
                Log.i("position","entered complete post");
                holder.acc.setVisibility(View.GONE);
                holder.rej.setVisibility(View.GONE);
                holder.completeText.setVisibility(0);

            }
            else{
                holder.acc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DB_handling db=new DB_handling(view.getContext());
                        db.updatePostUserRequest(Integer.parseInt(post.get(position)),"accepted");
                        if(db.updatePostStatus(Integer.parseInt(post.get(position)),"Work in Progress")){
                            Toast.makeText(view.getContext(),"workinprogress",Toast.LENGTH_SHORT).show();
                        }
                        donor_names.remove(position);
                        subject.remove(position);
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,donor_names.size());
                        Toast.makeText(view.getContext(),"Request accepted",Toast.LENGTH_SHORT).show();
                    }
                });
                holder.rej.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DB_handling db=new DB_handling(view.getContext());
                        db.updatePostUserRequest(Integer.parseInt(post.get(position)),"rejected");
                        donor_names.remove(position);
                        subject.remove(position);
                        items.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,donor_names.size());
                        Toast.makeText(view.getContext(),"Request rejeted",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        holder.donor_name.setText(donorname);
        holder.item.setText(items_donate);
    }

    @Override
    public int getItemCount() {
        return donor_names.size();
    }

    public class RequestNGOHolder extends RecyclerView.ViewHolder{
        TextView donor_name,subject,item,completeText;
        Button acc,rej;

        public RequestNGOHolder(@NonNull View itemView) {
            super(itemView);
            donor_name=(TextView)itemView.findViewById(R.id.donor_name);
            subject=(TextView)itemView.findViewById(R.id.title);
            item=(TextView)itemView.findViewById(R.id.items_list);
            acc=(Button)itemView.findViewById(R.id.accept_btn);
            rej=(Button)itemView.findViewById(R.id.reject_btn);
            completeText=(TextView)itemView.findViewById(R.id.completeStatusText);
        }
    }
}
