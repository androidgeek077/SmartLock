package app.techsol.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.techsol.smartlock.Models.uploadProduct;

public class BarCodeActicity extends AppCompatActivity {


    private EditText mEdtTxtCategory;
    private CardView mCVAddCategory;
    StorageReference profilePicRef;

    private String mStrCatTitle;
    private StorageReference mProfilePicStorageReference;
    private static final int RC_PHOTO_PICKER = 1;
    private Uri selectedProfileImageUri;

    Button mSelectedImgBtn;
    ImageView profileImageView;
    String downloadUri;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


    private uploadProduct muploadProcut;
LinearLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_acticity);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Categories");
        mProfilePicStorageReference = FirebaseStorage.getInstance().getReference().child("pictures");
        view=findViewById(R.id.getView);

        //Registering EditText Of Category Here
        mEdtTxtCategory=findViewById(R.id.et_cat_title);
        profileImageView = findViewById(R.id.selected_img_vw_cat);

        mSelectedImgBtn = findViewById(R.id.select_iamge_btn_cat);

        mSelectedImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfilePicture();
            }
        });
        //Registering CardView As Button Here
        mCVAddCategory=view.findViewById(R.id.cv_add_category_btn);
        mCVAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting Edit Text Values To Strings
                mStrCatTitle=mEdtTxtCategory.getText().toString();

                if(mStrCatTitle.isEmpty()){
                    mEdtTxtCategory.setError("Please Fill This Field");
                }else{

                    profilePicRef = mProfilePicStorageReference.child(selectedProfileImageUri.getLastPathSegment());
                    profilePicRef.putFile(selectedProfileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(BarCodeActicity.this, "image uploaded", Toast.LENGTH_SHORT).show();

                            profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUri=uri.toString();
                                    uploadProduct(downloadUri);
                                }
                            });
                        }
                    });

                }
            }
        });
    }

    public void getProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedProfileImageUri = selectedImageUri;
            profileImageView.setImageURI(selectedImageUri);
            profileImageView.setVisibility(View.VISIBLE);
        }

    }
    public void uploadProduct(String ImageUrl){
        muploadProcut = new uploadProduct( mStrCatTitle,ImageUrl);
        databaseReference.push().setValue(muploadProcut).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(view, "Product Added", Snackbar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


    }
}
