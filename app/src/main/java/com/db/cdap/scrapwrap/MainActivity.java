package com.db.cdap.scrapwrap;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.db.cdap.scrapwrap.community.CommunityMainActivity;
import com.db.cdap.scrapwrap.game.BaseActivity;
import com.db.cdap.scrapwrap.game.MainMenuActivity;
import com.db.cdap.scrapwrap.game.StoreActivity;
import com.db.cdap.scrapwrap.location.LocationActivity;
import com.db.cdap.scrapwrap.location.LoginActivity;
import com.db.cdap.scrapwrap.location.MapsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Intent mapIntent = new Intent(this, MapsActivity.class);

        //Intent mapIntent = new Intent(this, MainMenuActivity.class);

        //startActivity(mapIntent);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        startActivity(intent);
    }
}
