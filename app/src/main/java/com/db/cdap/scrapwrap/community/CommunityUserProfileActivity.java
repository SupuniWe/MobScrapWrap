package com.db.cdap.scrapwrap.community;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.db.cdap.scrapwrap.R;
import com.db.cdap.scrapwrap.user.User;
import com.db.cdap.scrapwrap.user.UserActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CommunityUserProfileActivity extends AppCompatActivity implements ProgressFragment.OnFragmentInteractionListener, RewardFragment.OnFragmentInteractionListener, EventFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;

    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    DatabaseReference databaseReference;

    Bundle bundle;
    ProgressFragment progressFragment;
    RewardFragment rewardFragment;
    EventFragment eventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_user_profile);


        toolbar = (Toolbar)findViewById(R.id.community_user_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPager)findViewById(R.id.community_user_profile_viewPager);
        tabLayout = (TabLayout)findViewById(R.id.community_user_profile_tablayout);

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new ProgressFragment(), "PROGRESS");
        tabAdapter.addFragment(new RewardFragment(), "REWARDS");
        tabAdapter.addFragment(new EventFragment(), "EVENTS");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        databaseReference = FirebaseDatabase.getInstance().getReference(UserActivity.databasePath).child("Users");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot userSnapShot : dataSnapshot.getChildren())
//                {
//                    dataSnapshot.getChildrenCount();
//                    User user = userSnapShot.getValue(User.class);
//
//                    String progress = user.getUserlastPlayed();
//
//                    bundle = new Bundle();
//                    bundle.putString("LastPlayed", progress);
//                    progressFragment = new ProgressFragment();
//                    progressFragment.setArguments(bundle);
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });


    }

    @Override
    public void onEventFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProgressFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRewardFragmentInteraction(Uri uri) {

    }
}
