package com.db.cdap.scrapwrap.community;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.db.cdap.scrapwrap.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PostsViewHolder>
{

    Context context;
    List<Posts> postsList;

    public RecyclerViewAdapter(Context context, List<Posts> tempPostList)
    {
        this.context = context;
        this.postsList = tempPostList;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_post_display_layout, parent, false);
        PostsViewHolder postsViewHolder = new PostsViewHolder(view);
        return postsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        Posts post = postsList.get(position);

        holder.username.setText(post.getFullName());
        holder.date.setText(post.getDate());
        holder.time.setText(post.getTime());
        holder.postDesc.setText(post.getDescription());

        Glide.with(context).load(post.getPostUrl()).into(holder.postImage);


    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username;
        public TextView date;
        public TextView time;
        public TextView postDesc;
        public ImageView postImage;
        public CircleImageView userImage;

        public PostsViewHolder(View itemView) {
            super(itemView);
            username = (TextView)itemView.findViewById(R.id.community_post_username);
            date = (TextView)itemView.findViewById(R.id.community_post_date);
            time = (TextView)itemView.findViewById(R.id.community_post_time);
            postDesc = (TextView)itemView.findViewById(R.id.community_post_description);
            postImage = (ImageView) itemView.findViewById(R.id.community_post_image);
            userImage = (CircleImageView) itemView.findViewById(R.id.community_post_profile_image);

        }
    }
}
