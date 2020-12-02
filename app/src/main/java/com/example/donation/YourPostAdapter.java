package com.example.donation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;

public class YourPostAdapter  extends RecyclerView.Adapter<YourPostAdapter.YourPostHolder> {

    ArrayList<String> subject;
    ArrayList<String> requirement;
    ArrayList<String> requirement_type;
    ArrayList<byte[]> images;
    ArrayList<String> postStatus;
    ArrayList<String> donorMail;
    ArrayList<String> postCount;
    ArrayList<Integer> postIdArray;
    String sub,requirements,req_type,status,postcount;
    int postid;
    byte img[];


    public YourPostAdapter( ArrayList<String> subject, ArrayList<String> requirement_type,ArrayList<String> requirement,ArrayList<String> postStatus,ArrayList<String> postCount,ArrayList<String> donorMail,ArrayList<byte[]> images,ArrayList<Integer> postId){

        this.subject=subject;
        this.requirement_type=requirement_type;
        this.requirement=requirement;
        this.postStatus=postStatus;
        this.postCount=postCount;
        this.donorMail=donorMail;
        this.images=images;
        this.postIdArray=postId;
    }

    @NonNull
    @Override
    public YourPostAdapter.YourPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.yourpost_row,parent,false);
        return new YourPostAdapter.YourPostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final YourPostAdapter.YourPostHolder holder, final int position) {

        req_type=requirement_type.get(position);
        requirements=requirement.get(position);
        sub=subject.get(position);
        status=postStatus.get(position);
        postcount=postCount.get(position);
        img=images.get(position);
        postid=postIdArray.get(position);
        holder.complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB_handling db=new DB_handling(view.getContext());
                Log.i("postcompleted func",String.valueOf(postIdArray.get(position)));
                if(db.updatePostStatus(postIdArray.get(position),"Completed") ){
                    holder.statusImage.setImageResource(R.drawable.greentick);
                    holder.complete.setVisibility(View.GONE);

                    Toast.makeText(view.getContext(),"Post Completed",Toast.LENGTH_SHORT).show();
                }


            }
        });
        Log.i("postcompleted func",String.valueOf(postid));
        holder.requirementType.setText(req_type);
        holder.subject.setText(sub);


        holder.requirements.setText(requirements);
        if(Integer.parseInt(postcount)>1){
            holder.count.setText(postcount+ " responses");
        }
        else{
            holder.count.setText(postcount+ " response");
        }
        if(status.equals("pending")){
            holder.statusImage.setImageResource(R.drawable.pending);
        }
        else if(status.equals("Completed")){
            holder.statusImage.setImageResource(R.drawable.greentick);
            holder.complete.setVisibility(View.GONE);
        }
        else {
            holder.statusImage.setImageResource(R.drawable.workinprogress);
        }
        try{
        Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
        holder.image.setImageBitmap(bmp);
        Log.i("yourpostimage", String.valueOf(img.length));
        }
        catch (Exception e){
            holder.image.setImageResource(R.drawable.profile_pic_dummy);
        }
    }

    @Override
    public int getItemCount() {
         return requirement.size();
    }

    public class YourPostHolder extends RecyclerView.ViewHolder{
        TextView subject,requirements,requirementType,count;
        ImageView image,statusImage;
        Button complete;
        public YourPostHolder(@NonNull final View itemView) {
            super(itemView);
            requirementType=(TextView)itemView.findViewById(R.id.requirement_type);
            subject=(TextView)itemView.findViewById(R.id.subject);
            requirements=(TextView)itemView.findViewById(R.id.requirement);
            count=(TextView)itemView.findViewById(R.id.postCount);
            image=itemView.findViewById(R.id.post_image);
            statusImage=(ImageView) itemView.findViewById(R.id.tick);
            complete=(Button) itemView.findViewById(R.id.completeButton);

        }
    }
}

