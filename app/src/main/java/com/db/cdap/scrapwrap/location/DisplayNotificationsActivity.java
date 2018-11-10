package com.db.cdap.scrapwrap.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.db.cdap.scrapwrap.R;

public class DisplayNotificationsActivity extends AppCompatActivity {

    private TextView mNotifData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notifications);

        String dataMessage = getIntent().getStringExtra("dataMessage");
        String dataFrom = getIntent().getStringExtra("dataFrom");

        mNotifData = (TextView)findViewById(R.id.notif_text);

        mNotifData.setText("FROM : " + dataFrom + " | MESSAGE : " + dataMessage);
    }
}
