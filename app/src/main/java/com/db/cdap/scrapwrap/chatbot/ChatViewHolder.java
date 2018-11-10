package com.db.cdap.scrapwrap.chatbot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.db.cdap.scrapwrap.R;

public class ChatViewHolder extends RecyclerView.ViewHolder
{
    TextView txtLeft;
    TextView txtRight;

    public ChatViewHolder(View itemView) {
        super(itemView);

        txtLeft = (TextView)itemView.findViewById(R.id.chatbot_msgList_txtLeft);
        txtRight = (TextView)itemView.findViewById(R.id.chatbot_msgList_txtRight);
    }
}
