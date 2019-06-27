package app.techsol.smartlock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.victor.loading.rotate.RotateLoading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import app.techsol.smartlock.Models.ProductModel;

public class BarCodeActicity extends AppCompatActivity {


    FirebaseAuth auth;
    private EditText mEdtTxtCategory;
    private Button mCVAddCategory;
    StorageReference profilePicRef;
    Bitmap bitmapbarCode;

    private String mStrCatTitle;
    private StorageReference mProfilePicStorageReference;
    private static final int RC_PHOTO_PICKER = 1;
    private Uri selectedProfileImageUri;

    Button mSelectedImgBtn;
    ImageView profileImageView;
    String downloadUri;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private ProductModel muploadProcut;
    RotateLoading loading;
    LinearLayout view;

    EditText mProductKeyET;


    private String ProducKeyStr = "";
    private String type = "";
    private Button button_generate;
    private EditText ProductKeyET;
    private Spinner type_spinner;
    private ImageView imageView;
    private int size = 660;
    private int size_width = 660;
    private int size_height = 264;

    private TextView success_text;
    private ImageView success_imageview;

    private String time;

    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_acticity);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
        mProfilePicStorageReference = FirebaseStorage.getInstance().getReference().child("pictures");
        ProducKeyStr = "";
        type = "BarCode";
        button_generate = (Button) findViewById(R.id.generate_button);
        ProductKeyET = (EditText) findViewById(R.id.edittext1);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        imageView = (ImageView) findViewById(R.id.image_imageview);
        view=findViewById(R.id.getView);
        loading=findViewById(R.id.rotateloading);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0:
                        type = "Barcode";
                        break;
                    case 1:
                        type = "PDF 417";
                        break;
                    case 2:
                        type = "Barcode-39";
                        break;
                    case 3:
                        type = "Barcode-93";
                        break;
                }
                Log.d("type", type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProducKeyStr = ProductKeyET.getText().toString();

                if (ProducKeyStr.equals("") || type.equals("")) {
                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(BarCodeActicity.this);
                    dialog.setTitle("Error");
                    dialog.setMessage("Invalid input!");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  do nothing
                        }
                    });
                    dialog.create();
                    dialog.show();
                } else {
                    Bitmap bitmap = null;
                    try {
                        bitmap = CreateImage(ProducKeyStr, type);
                        myBitmap = bitmap;
                    } catch (WriterException we) {
                        we.printStackTrace();
                    }
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        uploadBardCode();
                    }
                }
            }
        });

    }

    public Bitmap CreateImage(String ProducKeyStr, String type) throws WriterException {
        BitMatrix bitMatrix = null;
        // BitMatrix bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.QR_CODE, size, size);
        switch (type) {
            //  case "QR Code": bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.QR_CODE, size, size);break;
            case "Barcode":
                bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.CODE_128, size_width, size_height);
                break;
            //  case "Data Matrix": bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.DATA_MATRIX, size, size);break;
            case "PDF 417":
                bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.PDF_417, size_width, size_height);
                break;
            case "Barcode-39":
                bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.CODE_39, size_width, size_height);
                break;
            case "Barcode-93":
                bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.CODE_93, size_width, size_height);
                break;
            //  case "AZTEC": bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.AZTEC, size, size);break;
            // default: bitMatrix = new MultiFormatWriter().encode(ProducKeyStr, BarcodeFormat.QR_CODE, size, size);break;
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (bitMatrix.get(j, i)) {
                    pixels[i * width + j] = 0xff000000;
                } else {
                    pixels[i * width + j] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public void saveBitmap(Bitmap bitmap, String ProducKeyStr, String bitName) {

        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"};
        int permission = ContextCompat.checkSelfPermission(this,
                "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        String fileName = ProducKeyStr + "_at_" + String.valueOf(year) + "_" + String.valueOf(month) + "_" + String.valueOf(day) + "_" + String.valueOf(hour) + "_" + String.valueOf(minute) + "_" + String.valueOf(second) + "_" + String.valueOf(millisecond);
        time = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + " " + String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second) + "." + String.valueOf(millisecond);
        File file;

        String fileLocation;

        String folderLocation;

        if (Build.BRAND.equals("Xiaomi")) {
            fileLocation = Environment.getExternalStorageDirectory().getPath() + "/DCIM/AndroidBarcodeGenerator/" + fileName + bitName;
            folderLocation = Environment.getExternalStorageDirectory().getPath() + "/DCIM/AndroidBarcodeGenerator/";
        } else {
            fileLocation = Environment.getExternalStorageDirectory().getPath() + "/DCIM/AndroidBarcodeGenerator/" + fileName + bitName;
            folderLocation = Environment.getExternalStorageDirectory().getPath() + "/DCIM/AndroidBarcodeGenerator/";
        }

        Log.d("file_location", fileLocation);

        file = new File(fileLocation);

        File folder = new File(folderLocation);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (file.exists()) {
            file.delete();
        }


        FileOutputStream out;

        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.optLogout) {
            // Save image
            saveBitmap(myBitmap, ProducKeyStr, ".jpg");

            LayoutInflater layoutInflater = LayoutInflater.from(BarCodeActicity.this);
            View view = layoutInflater.inflate(R.layout.success_dialog, null);
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BarCodeActicity.this);
            builder.setTitle("Summary");
            builder.setCancelable(false);
            builder.setView(view);
            success_text = (TextView) view.findViewById(R.id.success_text);
            success_text.setText(ProducKeyStr + "\n\n" + time);
            success_imageview = (ImageView) view.findViewById(R.id.success_imageview);
            success_imageview.setImageBitmap(myBitmap);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            builder.create();
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    void uploadBardCode() {
        loading.start();
        profilePicRef = mProfilePicStorageReference.child(getImageUri(getApplicationContext(), myBitmap).getLastPathSegment());
        Uri uri = getImageUri(getApplicationContext(), myBitmap);
        profilePicRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUri = uri.toString();
                        uploadProduct(downloadUri);
                        imageView.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);

                    }
                });
            }
        });
    }

    public void uploadProduct(String ImageUrl) {
        muploadProcut = new ProductModel(ProducKeyStr, ImageUrl);
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
