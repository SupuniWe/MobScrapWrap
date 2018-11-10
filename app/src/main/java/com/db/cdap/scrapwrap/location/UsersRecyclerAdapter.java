package com.db.cdap.scrapwrap.location;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.db.cdap.scrapwrap.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by panalk on 9/27/2018.
 */

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private List<UserInformation> usersList;
    private Context context;

    public UsersRecyclerAdapter(Context context, List<UserInformation> usersList) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String user_name = usersList.get(position).getName();

        holder.user_name_view.setText(user_name);

        CircleImageView user_image_view = holder.user_image_view;
        Glide.with(context).load(usersList.get(position).getImage()).into(user_image_view);

        final String user_id = usersList.get(position).userId;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(context, SendActivity.class);
                sendIntent.putExtra("user_id", user_id);
                sendIntent.putExtra("user_name", user_name);
                context.startActivity(sendIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private CircleImageView user_image_view;
        private TextView user_name_view;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            user_image_view = (CircleImageView)mView.findViewById(R.id.user_list_image);
            user_name_view = (TextView) mView.findViewById(R.id.user_list_name);

        }
    }
}