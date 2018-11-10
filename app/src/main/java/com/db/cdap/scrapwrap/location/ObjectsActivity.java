package com.db.cdap.scrapwrap.location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.db.cdap.scrapwrap.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ObjectsActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;
    private Button btnObjSave;
    private EditText editTextObjName;
    private EditText editTextObjLatitude;
    private EditText editTextObjLongitude;
    private EditText editTextObjStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Objects");
        editTextObjName = (EditText)findViewById(R.id.editTextObjName);
        editTextObjLatitude = (EditText)findViewById(R.id.editTextObjLatitude);
        editTextObjLongitude = (EditText)findViewById(R.id.editTextObjLongitude);
        editTextObjStatus = (EditText)findViewById(R.id.editTextObjStatus);
        btnObjSave = (Button)findViewById(R.id.btnObjSave);
        btnObjSave.setOnClickListener(this);
    }

    private void saveObjectInformation(){
        String name = editTextObjName.getText().toString().trim();
        double latitude = Double.parseDouble(editTextObjLatitude.getText().toString().trim());
        double longitude = Double.parseDouble(editTextObjLongitude.getText().toString().trim());
        String status = editTextObjStatus.getText().toString().trim();

        ObjectInformation objectInformation = new ObjectInformation(name, latitude, longitude, status);
        mDatabase.child(name).setValue(objectInformation);
        Toast.makeText(this, "Saved object successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v == btnObjSave){
            saveObjectInformation();
            editTextObjName.getText().clear();
            editTextObjLatitude.getText().clear();
            editTextObjLongitude.getText().clear();
            editTextObjStatus.getText().clear();
        }
    }
}
