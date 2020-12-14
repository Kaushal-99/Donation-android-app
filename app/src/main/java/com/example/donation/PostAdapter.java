package com.example.donation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    ArrayList<String> ngo_names;
    ArrayList<String> subject;
    ArrayList<String> requirement;
    ArrayList<String> requirement_type;
    ArrayList<Integer> postId;
    ArrayList<byte[]> images;
    String ngoname,sub,requirements,req_type;
    byte img[];

    public PostAdapter(ArrayList<String> ngo_names, ArrayList<String> subject, ArrayList<String> requirement_type,ArrayList<String> requirement,ArrayList<byte[]> images,ArrayList<Integer> postId){
        this.ngo_names=ngo_names;
        this.subject=subject;
        this.requirement_type=requirement_type;
        this.requirement=requirement;
        this.images=images;
        this.postId=postId;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.feed_item,parent,false);
        return new PostAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        ngoname=ngo_names.get(position);
        sub=subject.get(position);
        requirements=requirement.get(position);
        img=images.get(position);
        req_type=requirement_type.get(position);
        if(req_type.equalsIgnoreCase("Volunteer")){
            holder.btn.setText("Volunteer");
        }
        else if(req_type.equalsIgnoreCase("Donation")){
            holder.btn.setText("Donate");
        }
        holder.ngo_name.setText(ngoname);
        holder.subject_tv.setText(sub);
        holder.requirements.setText(requirements);
        try{
            Bitmap bmp = BitmapFactory.decodeByteArray(images.get(position), 0, images.get(position).length);
            holder.image.setImageBitmap(bmp);
            Log.i("feedimage", Arrays.toString(img));
        }
        catch (Exception e){
            holder.image.setImageResource(R.drawable.profile_pic_dummy);
        }


    }

    @Override
    public int getItemCount() {
        return ngo_names.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        TextView ngo_name,subject_tv,requirements;
        ImageView image;
        Button btn;
        public PostHolder(@NonNull final View itemView) {
            super(itemView);
            ngo_name=(TextView)itemView.findViewById(R.id.ngo_name);
            subject_tv=(TextView)itemView.findViewById(R.id.title);
            requirements=(TextView)itemView.findViewById(R.id.requirement_type);
            image=(ImageView) itemView.findViewById(R.id.post_image);
            btn=itemView.findViewById(R.id.post_request_btn);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int i=getAdapterPosition();
                    Intent post_form=new Intent(v.getContext(),Post_Form.class);
                    post_form.putExtra("NGO Name",ngoname);
                    post_form.putExtra("index",String.valueOf(postId.get(i)));
                    post_form.putExtra("subject",subject.get(i));
                    post_form.putExtra("req_type",requirement_type.get(i));
                    v.getContext().startActivity(post_form);
                }
            });

        }
    }
}
