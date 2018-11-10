package com.db.cdap.scrapwrap.community;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.db.cdap.scrapwrap.*;
import com.db.cdap.scrapwrap.adapters.PostListAdapter;
import com.db.cdap.scrapwrap.chatbot.ChatbotMainActivity;
import com.db.cdap.scrapwrap.models.PostModel;
import com.db.cdap.scrapwrap.newpost.AddPostActivity;
import com.db.cdap.scrapwrap.utils.GlobalClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityMainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView userPostList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ImageButton btnAddPost;

    public DatabaseReference postsRef;

    private Context appContext;
    //public List<Posts> postsList;

    private Button btnDisplayPosts;

    //Display posts
    //Database reference
    DatabaseReference databaseReference;

    //Creating RecyclerView
    RecyclerView postRecyclerView;

    //Creting Recycler view adapter
    RecyclerView.Adapter adapter;

    ProgressDialog progressDialog;
    List<PostModel> postsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        //Adding the toolbar to the main activity
        toolbar = (Toolbar)findViewById(R.id.community_main_page_toolbar);
        setSupportActionBar(toolbar);
        //Assign a title to the main Activity
        getSupportActionBar().setTitle("Home");

        drawerLayout = (DrawerLayout) findViewById(R.id.community_drawable_layout);
        //Adding a hamburgur icon to the toolbar and set the click action to open the drawer layout
        actionBarDrawerToggle = new ActionBarDrawerToggle(CommunityMainActivity.this, drawerLayout, R.string.community_drawer_open, R.string.community_drawer_close);
        //add a drawerlistener on the actionbarDrawerToggle
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //sync the indicator with the drawer layout
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postsRef = FirebaseDatabase.getInstance().getReference().child(GlobalClass.PATH_POSTS);

        //add post button initialization
        btnAddPost = (ImageButton)findViewById(R.id.community_btn_add_post);


        //Display posts start
        postRecyclerView = (RecyclerView)findViewById(R.id.community_recycler_view);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //setting up firebase image uploader path to the databasereference
        databaseReference = FirebaseDatabase.getInstance().getReference(CommunityPostActivity.databasePath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren())
                {
                    dataSnapshot.getChildrenCount();
                    PostModel post = postSnapShot.getValue(PostModel.class);
                    postsList.add(post);
                }

                adapter = new PostListAdapter(postsList);

                postRecyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        //Display posts end

        navigationView = (NavigationView) findViewById(R.id.community_navigation_view);
        View communityNavHeader = navigationView.inflateHeaderView(R.layout.community_navigation_header);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                return false;
            }
        });

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPostActivity();
            }
        });

    }



    private void SendUserToPostActivity() {
//        Intent intent = new Intent(CommunityMainActivity.this, CommunityPostActivity.class);
        Intent intent = new Intent(CommunityMainActivity.this, AddPostActivity.class);
        startActivity(intent);
    }

    //icon hamburgur click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void userMenuSelector(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.community_nav_home:
                Intent intentHome = new Intent(getApplicationContext(), CommunityMainActivity.class);
                startActivity(intentHome);
                break;
            case R.id.community_nav_profile:
                Intent intentProfile = new Intent(getApplicationContext(), CommunityUserProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.community_nav_progress:
                Toast.makeText(this, "Progress", Toast.LENGTH_SHORT).show();
                break;
            case R.id.community_nav_friends:
                Toast.makeText(this, "Friends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.community_nav_find_friends:
                Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                break;
            case R.id.community_nav_message:
                Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
                break;
            case R.id.community_nav_settings:
                Intent intentCot = new Intent(CommunityMainActivity.this, CommunityPostActivity.class);
                startActivity(intentCot);
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.community_nav_chatbot:
                Intent intentChatbot = new Intent(CommunityMainActivity.this, ChatbotMainActivity.class);
                startActivity(intentChatbot);

        }
    }
}
