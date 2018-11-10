package com.db.cdap.scrapwrap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.db.cdap.scrapwrap.chatbot.ChatbotMainActivity;
import com.db.cdap.scrapwrap.community.CommunityMainActivity;
import com.db.cdap.scrapwrap.game.StoreActivity;
import com.db.cdap.scrapwrap.location.MapsActivity;
import com.db.cdap.scrapwrap.newpost.AddPostActivity;

public class CategoryActivity extends AppCompatActivity {

    private Button mLocationBtn;
    private Button mCommunityBtn;
    private Button mStoreBtn;
    private Button mChatbotBtn;
    private Button btnDetectObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mLocationBtn = (Button)findViewById(R.id.btnLocation);
        mCommunityBtn = (Button)findViewById(R.id.btnCommunity);
        mStoreBtn = (Button)findViewById(R.id.btnAvatar);
        mChatbotBtn = (Button)findViewById(R.id.btnChatbot);
        btnDetectObjects = (Button)findViewById(R.id.btnDetectObjects);

        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btnDetectObjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        });

        mCommunityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : Goto Post list
                Intent intent = new Intent(CategoryActivity.this, CommunityMainActivity.class);
                startActivity(intent);
            }
        });

        mStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, StoreActivity.class);
                startActivity(intent);
            }
        });

        mChatbotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, ChatbotMainActivity.class);
                startActivity(intent);
            }
        });

    }
}
