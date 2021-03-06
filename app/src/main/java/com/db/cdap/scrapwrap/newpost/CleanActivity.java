package com.db.cdap.scrapwrap.newpost;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.cdap.scrapwrap.R;
import com.db.cdap.scrapwrap.models.PostModel;
import com.db.cdap.scrapwrap.utils.FileUtils;
import com.db.cdap.scrapwrap.utils.FirebaseUtils;
import com.db.cdap.scrapwrap.utils.GlobalClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.io.IOException;

public class CleanActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    ImageView imgBtnCleanImage;
    Button btnCleanCheckImage;
    Button btnCleanOpenMap;
    Button btnCleanChooseGallery;
    //    TextView txtCleanPhotoVerify;
    TextView txtCleanCommunityVerify;
    PostModel postModel;
    GlobalClass globalClass;
    FirebaseUtils firebaseUtils;
    Uri imageFileUri;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);

        imgBtnCleanImage = findViewById(R.id.imgBtnCleanImage);
        btnCleanCheckImage = findViewById(R.id.btnCleanCheckImage);
//        txtCleanPhotoVerify = findViewById(R.id.txtCleanPhotoVerify);
        txtCleanCommunityVerify = findViewById(R.id.txtCleanCommunityVerify);
        btnCleanOpenMap = findViewById(R.id.btnCleanOpenMap);
        btnCleanChooseGallery = findViewById(R.id.btnCleanChooseGallery);

        globalClass = (GlobalClass) getApplicationContext();
        firebaseUtils = new FirebaseUtils();

        postModel = globalClass.getSelectedPost();

        try {
            final File localFile = File.createTempFile("images", "jpeg");
            firebaseUtils.getStorageReference().child(GlobalClass.PATH_IMAGES + "/" + postModel.getId()).getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap image = FileUtils.getBitmapFromFile(localFile);
                            imgBtnCleanImage.setImageBitmap(image);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //set post without image
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (postModel.isVerified()) {
            txtCleanCommunityVerify.setText(getString(R.string.verified));

            btnCleanOpenMap.setVisibility(View.GONE);
            btnCleanCheckImage.setVisibility(View.GONE);
        } else {

            btnCleanOpenMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMap();
                }
            });

            imgBtnCleanImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });

            btnCleanCheckImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verify();
                }
            });
            txtCleanCommunityVerify.setText(getString(R.string.to_be_verified));
        }
    }

    private void openMap() {
//        Uri gmmIntentUri = Uri.parse("geo:"+postModel.getLatitude()+","+postModel.getLongitude());
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        if (mapIntent.resolveActivity(getPackageManager()) != null) {
//            startActivity(mapIntent);
//        }


        Uri.Builder directionsBuilder = new Uri.Builder()
                .scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("destination", postModel.getLatitude() + "," + postModel.getLongitude());

        startActivity(new Intent(Intent.ACTION_VIEW, directionsBuilder.build()));
    }

    private void verify() {
        postModel.setVerified(true);

        String userId = "verifierUserId";
        postModel.setVerifier(userId);

        if (postModel.getId() != null) {
            firebaseUtils.verifyPost(CleanActivity.this, postModel, imageFile);
        } else {
            postModel.setPublisher(userId);
            Long timestamp = System.currentTimeMillis();
            postModel.setTimestamp(timestamp);

            File file = new File(String.valueOf(globalClass.getSelectedPostImageUri()));
            firebaseUtils.uploadPostWithVerify(CleanActivity.this, postModel, file, imageFile);
        }

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpeg");
        imageFileUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
        CleanActivity.this.startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageFileUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFileUri);
                imgBtnCleanImage.setImageBitmap(bitmap);
                imageFile = FileUtils.getFileFromImageUri(CleanActivity.this, imageFileUri);

                imageFileUri = Uri.parse(FileUtils.getRealPathFromURI(CleanActivity.this, imageFileUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                super.onActivityResult(requestCode, resultCode, data);
//
                Log.d(GlobalClass.TAG, "Image : " + requestCode);
                Log.d(GlobalClass.TAG, "Image : " + resultCode);
                Log.d(GlobalClass.TAG, "Image : " + data);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFileUri);
                imgBtnCleanImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.d(GlobalClass.TAG, "Image Error : " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
