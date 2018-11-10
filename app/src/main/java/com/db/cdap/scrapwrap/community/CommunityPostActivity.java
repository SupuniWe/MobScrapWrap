package com.db.cdap.scrapwrap.community;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.db.cdap.scrapwrap.R;
import com.db.cdap.scrapwrap.utils.GlobalClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommunityPostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton btnChoosePost;
    private Button btnPost;
    private Button btnCancel;
    private EditText editTxtpostDesc;
    private static final int Gallery_Pick = 1;
    private Uri imageUri;
    private String postDesc;
    private StorageReference postStorageRef;
    private DatabaseReference postDatabaseRef;

    private String saveCurrDate, saveCurrTime, randomPostName;
    private String url;

    private ProgressDialog loadPost;

    public static String storagePath = GlobalClass.PATH_IMAGES;
    public static String databasePath = GlobalClass.PATH_POSTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post);

        //get a reference to an instance of the firebase storage
        postStorageRef = FirebaseStorage.getInstance().getReference();
        //posts node reference
        postDatabaseRef = FirebaseDatabase.getInstance().getReference(databasePath);

        btnChoosePost = (ImageButton)findViewById(R.id.community_btnChoosePost);
        btnPost = (Button) findViewById(R.id.community_btnPost);
        btnCancel = (Button)findViewById(R.id.community_btnCancel);
        editTxtpostDesc = (EditText)findViewById(R.id.community_editTxt_post_desc);

        loadPost = new ProgressDialog(this);


        toolbar = (Toolbar)findViewById(R.id.community_post_toolbar);
        //set the toolbar to act as an action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //for the back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Post");


        //open the phone gallery when the add post button is clicked
        btnChoosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoneGallery();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 validatePostDetails();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMainActivity();
            }
        });

    }

    private void validatePostDetails() {

        if(imageUri == null) {
            Toast.makeText(this, "Please select a post", Toast.LENGTH_SHORT).show();
        }
        //loading dialog which will open up when the new post is saving
//        loadPost.setTitle("Add Post");
//        loadPost.setMessage("Post is getting added...");
//        loadPost.show();
//        loadPost.setCanceledOnTouchOutside(true);

        StoreImageInStorage();
    }

    //upload image to the storage
    private void StoreImageInStorage() {

        if(imageUri != null)
        {
            //set progress dialog title
            loadPost.setTitle("Post is uploading...");
            loadPost.show();

            //create another storage reference
            final StorageReference tempPostStorageRef = postStorageRef.child(storagePath + System.currentTimeMillis() + "." +GetFileExtension(imageUri));

            //Add addOnSuccessListener to the temp storage
            tempPostStorageRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return tempPostStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        //get the image description
                        postDesc = editTxtpostDesc.getText().toString().trim();

                        //To generate a random name to a post
                        Calendar date = Calendar.getInstance();
                        SimpleDateFormat currDate = new SimpleDateFormat("dd-MM-yyyy");
                        saveCurrDate = currDate.format(date.getTime());

                        Calendar time = Calendar.getInstance();
                        SimpleDateFormat currTime = new SimpleDateFormat("HH:mm");
                        saveCurrTime = currTime.format(time.getTime());

                        //hide progress dialog after uploading
                        loadPost.dismiss();

                        //show a toast message after done uploading
                        Toast.makeText(getApplicationContext(), "Post uploaded successfully!", Toast.LENGTH_LONG).show();

                        Posts post = new Posts("u124", saveCurrTime, saveCurrDate, postDesc, "Lakmini Perera", downloadUri.toString());

                        //get post upload Id
                        String postUploadId = postDatabaseRef.push().getKey();

                        //add post upload Id as a child element into database reference
                        postDatabaseRef.child(postUploadId).setValue(post);

                        sendUserToMainActivity();
                    }
                    else
                    {
                        loadPost.dismiss();
                        Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


//        //
//        //try adding some random characters in between
//        //
//

//
//        randomPostName = saveCurrDate+saveCurrTime;
//
//        //In the firebase storage, a Posts folder will be created where all the posts will be saved
//        //image name - real name + curr date + curr time + .jpg
//        StorageReference postPath = postStorageRef.child(storagePath).child(imageUri.getLastPathSegment() + randomPostName + ".jpg");
//
//        //store the post in the firebase storage
//        postPath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                if(task.isSuccessful())
//                {
//                    //get the link of the image from the firebase storage and save that in url variable
//                   // url = task.getResult().getDownloadUrl().toString();
//                    Toast.makeText(CommunityPostActivity.this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
//
//                    SavePostInDatabase();
//                }
//                else{
//                    String message = task.getException().getMessage();
//                    Toast.makeText(CommunityPostActivity.this, "Error occured : " + message, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

//    private void SavePostInDatabase() {
//        String userName = "Anjali Perera";
//
//        HashMap postMap = new HashMap();
//        postMap.put("uid", "u123");
//        postMap.put("date", saveCurrDate);
//        postMap.put("time", saveCurrTime);
//        postMap.put("description", postDesc);
//        postMap.put("post", url);
//        postMap.put("fullName", userName);
//
//        //save in the database
//        postRef.child(randomPostName).updateChildren(postMap)
//                .addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if(task.isSuccessful())
//                        {
//
//                            Toast.makeText(CommunityPostActivity.this, "Post is added successfully!", Toast.LENGTH_SHORT).show();
//                            loadPost.dismiss();
//                            sendUserToMainActivity();
//                        }
//                        else
//                        {
//                            Toast.makeText(CommunityPostActivity.this, "Error occured while adding the post!", Toast.LENGTH_SHORT).show();
//                            loadPost.dismiss();
//                        }
//                    }
//                });
//
//    }

    //open the gallery of the phone
    private void openPhoneGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Please select an image!"), Gallery_Pick);
    }

    //set the chosen image inside the imagebutton
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null   && data.getData() != null)
        {
            imageUri = data.getData();

            try
            {
                //Getting selected image into bitmap
                Bitmap postBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                //Seting the selected image into the imageButton
                btnChoosePost.setImageBitmap(postBitmap);

            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            btnChoosePost.setImageURI(imageUri);
        }
    }

    //get the selected image file extension from file path URI
    public String GetFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            sendUserToMainActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(CommunityPostActivity.this, CommunityMainActivity.class);
        startActivity(intent);
    }
}
