package com.db.cdap.scrapwrap.user;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.db.cdap.scrapwrap.MainActivity;
import com.db.cdap.scrapwrap.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private Toolbar userPageToolebar;
    private Spinner userSecurityQuestion1;
    private Spinner userSecurityQuestion2;
    private Button userBtnDone;
    private EditText userEditTxtGameName;
    private EditText userEditTxtAnswer1;
    private EditText userEditTxtAnswer2;

    private String securityQues1;
    private String securityQues2;
    private int secQuesPos1;
    private int secQuesPos2;

    private StorageReference postStorageRef;
    private DatabaseReference postDatabaseRef;
    public static String storagePath = "/Users";
    public static String databasePath = "Users_Database";

    private String deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //get a reference to an instance of the firebase storage
        postStorageRef = FirebaseStorage.getInstance().getReference();
        //posts node reference
        postDatabaseRef = FirebaseDatabase.getInstance().getReference(databasePath);

        userPageToolebar = (Toolbar)findViewById(R.id.user_page_toolbar);
        userSecurityQuestion1 = (Spinner)findViewById(R.id.user_spinner_security_ques1);
        userSecurityQuestion2 = (Spinner)findViewById(R.id.user_spinner_security_ques2);
        userBtnDone = (Button)findViewById(R.id.user_btnDone);
        userEditTxtGameName = (EditText)findViewById(R.id.user_edittxt_game_name);
        userEditTxtAnswer1 = (EditText)findViewById(R.id.user_edittxt_security_ans1);
        userEditTxtAnswer2 = (EditText)findViewById(R.id.user_edittxt_security_ans2);

        //toolbar start
        setSupportActionBar(userPageToolebar);
        getSupportActionBar().setTitle("Your Profile");
        //toolbar end

        //spinner start
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                R.array.security_questions, android.R.layout.simple_spinner_dropdown_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        userSecurityQuestion1.setAdapter(adapter);
        userSecurityQuestion2.setAdapter(adapter);

        userSecurityQuestion1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                secQuesPos1 = position;
                if(position == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please pick a security question.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    securityQues1 = adapterView.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(getApplicationContext(), "Please pick a security question.", Toast.LENGTH_LONG).show();
            }
        });

        userSecurityQuestion2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                secQuesPos2 = position;
                if(position == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please pick a security question.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    securityQues2 = adapterView.getItemAtPosition(position).toString();
                    if (securityQues2.equals(securityQues1)) {
                        Toast.makeText(getApplicationContext(), "Please pick another security question.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Please pick a security question.", Toast.LENGTH_LONG).show();
            }
        });
        //spinner end

        //btnDone start
        userBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }
        });
        //btnDone end
    }

    public void saveUser(String userGameName, String userSecurityQues1, String userAns1, String userSecurityQues2, String userAns2)
    {
        //get deviceId Start
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //get deviceid end
        DatabaseReference tempDatabaseRef = postDatabaseRef.child("Users");

        Map<String, User> user = new HashMap<>();
        //user.put(userGameName, new User(userGameName, userSecurityQues1, userAns1, userSecurityQues2, userAns2, deviceId));
        user.put(userGameName, new UserBuilder()
                .name(userGameName)
                .question1(userSecurityQues1)
                .answer1(userAns1)
                .question2(userSecurityQues2)
                .answer2(userAns2)
                .userDeviceId(deviceId)
                .buildUser());
        tempDatabaseRef.setValue(user);
        Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_SHORT).show();
        sendUsertoMainActivity();
    }

    public void validateUser()
    {
        if(TextUtils.isEmpty(userEditTxtGameName.getText().toString()))
        {
            userEditTxtGameName.setError("Please enter a Game name.");
        }
        else if (TextUtils.isEmpty(userEditTxtAnswer1.getText().toString()))
        {
            userEditTxtAnswer1.setError("Please enter an answer for question 1.");
        }
        else if (TextUtils.isEmpty(userEditTxtAnswer2.getText().toString()))
        {
            userEditTxtAnswer2.setError("Please enter an answer for question 2.");
        }
        if(secQuesPos1 == 0 || secQuesPos2 == 0)
        {
            Toast.makeText(getApplicationContext(), "Please pick a security question.", Toast.LENGTH_LONG).show();
        }

        saveUser(userEditTxtGameName.getText().toString(), securityQues1, userEditTxtAnswer1.getText().toString(), securityQues2, userEditTxtAnswer2.getText().toString());
    }

    public void sendUsertoMainActivity()
    {
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
