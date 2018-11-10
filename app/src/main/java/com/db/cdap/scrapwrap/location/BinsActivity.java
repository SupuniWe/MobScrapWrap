package com.db.cdap.scrapwrap.location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.db.cdap.scrapwrap.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BinsActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;
    private Button btnSave;
    private EditText editTextName;
    private EditText editTextLatitude;
    private EditText editTextLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bins);

       // btnProceed = (Button)findViewById(R.id.btnProceed);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Bins");
        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextLatitude = (EditText)findViewById(R.id.editTextLatitude);
        editTextLongitude = (EditText)findViewById(R.id.editTextLongitude);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        /*btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent i =  new Intent(BinsActivity.this, LocationActivity.class);
                startActivity(i);*//*
            }
        });*/
    }

    private void saveBinInformation(){
        String name = editTextName.getText().toString().trim();
        Double latitude = Double.parseDouble(editTextLatitude.getText().toString().trim());
        Double longitude = Double.parseDouble(editTextLongitude.getText().toString().trim());

        BinInformation binInformation = new BinInformation(name, latitude, longitude);
        //mDatabase.child("Bins").setValue(binInformation);
        mDatabase.child(name).setValue(binInformation);
        //mDatabase.push().setValue(binInformation);
        Toast.makeText(this, "Saved Bin Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        /*if(v == btnProceed){
            finish();
        }*/

        if(v == btnSave){
            saveBinInformation();
            editTextName.getText().clear();
            editTextLatitude.getText().clear();
            editTextLongitude.getText().clear();
        }
    }
}
