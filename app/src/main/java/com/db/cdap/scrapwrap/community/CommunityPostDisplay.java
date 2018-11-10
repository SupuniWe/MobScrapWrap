package com.db.cdap.scrapwrap.community;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.db.cdap.scrapwrap.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityPostDisplay extends AppCompatActivity {

    //Database reference
    DatabaseReference databaseReference;

    //Creating RecyclerView
    RecyclerView postRecyclerView;

    //Creting Recycler view adapter
    RecyclerView.Adapter adapter;

    ProgressDialog progressDialog;
    List<Posts> postsList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post_display);

        postRecyclerView = (RecyclerView)findViewById(R.id.community_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(CommunityPostDisplay.this));

        progressDialog = new ProgressDialog(CommunityPostDisplay.this);
        progressDialog.setTitle("Posts are loading...");
        progressDialog.show();

        //setting up firebase image uploader path to the databasereference
        databaseReference = FirebaseDatabase.getInstance().getReference(CommunityPostActivity.databasePath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren())
                {
                    dataSnapshot.getChildrenCount();
                    Posts post = postSnapShot.getValue(Posts.class);
                    postsList.add(post);
                }

                adapter = new RecyclerViewAdapter(getApplicationContext(), postsList);

                postRecyclerView.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }
}
