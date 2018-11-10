package com.db.cdap.scrapwrap.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.cdap.scrapwrap.R;
import com.db.cdap.scrapwrap.models.PostModel;
import com.db.cdap.scrapwrap.newpost.CleanActivity;
import com.db.cdap.scrapwrap.utils.DateTimeUtils;
import com.db.cdap.scrapwrap.utils.FileUtils;
import com.db.cdap.scrapwrap.utils.FirebaseUtils;
import com.db.cdap.scrapwrap.utils.GlobalClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.util.List;


public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    FirebaseUtils firebaseUtils;
    private List<PostModel> postModelList;

    public PostListAdapter(List<PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        firebaseUtils = new FirebaseUtils();

        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, final int position) {
        try {
            Log.d(GlobalClass.TAG, "Position : " + position);
            final Context context = holder.postView.getContext();
            final PostModel post = postModelList.get(position);
            String time = DateTimeUtils.getRelativeDateTime(context, post.getTimestamp());
            holder.txtPostPublished.setText(time);
            holder.txtPostPublisher.setText(context.getString(R.string.format_post_list_published_by, post.getPublisher()));
            holder.txtPostDesc.setText(post.getDescription());

            holder.postView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalClass globalClass = (GlobalClass) context.getApplicationContext();
                    globalClass.setSelectedPost(post);
                    if(post.getType().equalsIgnoreCase(GlobalClass.TYPE_GARBAGE_POST)){
                        Intent intent = new Intent(context.getApplicationContext(), CleanActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }else{
                        //TODO : GoTo Community Post
                    }
                }
            });

            if (post.isVerified()) {
//                holder.txtPostVerifier.setText(context.getString(R.string.format_post_list_verified_by, post.getVerifier()));
                holder.txtPostVerifier.setText("Status : CLEANED");
                try {
                    Log.d(GlobalClass.TAG, " FIREBASEUTIL : LOAD : POST : POST_ID : " + post.getId());
                    final File localFile = File.createTempFile("confirm", "jpeg");
                    firebaseUtils.getStorageReference().child(GlobalClass.PATH_IMAGES + "/" + post.getId()).getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d(GlobalClass.TAG, " FIREBASEUTIL : LOAD : DOWNLOADED CONFIRM : POST_ID : " + post.getId());
                                    Bitmap image = FileUtils.getBitmapFromFile(localFile);
                                    holder.imgPostImage.setImageBitmap(image);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //set post without image
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                holder.txtPostVerifier.setVisibility(View.GONE);
                holder.txtPostVerifier.setText("Status : NOT CLEANED");
                try {
                    Log.d(GlobalClass.TAG, " FIREBASEUTIL : LOAD : POST : POST_ID : " + post.getId());
                    final File localFile = File.createTempFile("images", "jpeg");
                    firebaseUtils.getStorageReference().child(GlobalClass.PATH_IMAGES + "/" + post.getId()).getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.d(GlobalClass.TAG, " FIREBASEUTIL : LOAD : DOWNLOADED : POST_ID : " + post.getId());
                                    Bitmap image = FileUtils.getBitmapFromFile(localFile);
                                    holder.imgPostImage.setImageBitmap(image);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //set post without image
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception ignored){
        }
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView txtPostPublished, txtPostPublisher, txtPostVerifier,txtPostDesc;
        public View postView;
        public ImageView imgPostImage;
//        public ImageView imgVerifiedImage;

        public PostViewHolder(final View view) {
            super(view);
            postView = view;
            txtPostPublished = view.findViewById(R.id.txtPostPublished);
            txtPostPublisher = view.findViewById(R.id.txtPostPublisher);
            txtPostVerifier = view.findViewById(R.id.txtPostVerifier);
            imgPostImage = view.findViewById(R.id.imgPostImage);
            txtPostDesc = view.findViewById(R.id.txtPostDesc);
//            imgVerifiedImage = view.findViewById(R.id.imgVerifiedImage);

        }
    }
}
