package diet.bus_tracking;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.support.constraint.Constraints.TAG;

public class MyProfile extends Activity implements View.OnClickListener {
    TextView header_txt_plain,username_tv,phone_tv;
    ImageView back_plain, threedots_plain, profile_img;
    DatabaseReference myRef;
    private Bitmap bitmap;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String userChoosenTask;
    SharedPreferences.Editor editprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);
        username_tv = (TextView) findViewById(R.id.username_tv);
        phone_tv = (TextView) findViewById(R.id.phone_tv);
        profile_img = (ImageView) findViewById(R.id.profile_img);
        profile_img.setOnClickListener(this);
        header_txt_plain = (TextView) findViewById(R.id.header_txt_plain);
        header_txt_plain.setText("Profile");
        threedots_plain = (ImageView) findViewById(R.id.threedots_plain);
        threedots_plain.setOnClickListener(this);
        back_plain = (ImageView) findViewById(R.id.back_plain);
        back_plain.setOnClickListener(this);
        myRef = FirebaseDatabase.getInstance().getReference();



        SharedPreferences users = getSharedPreferences("Details", MODE_PRIVATE);
        phone_tv.setText(users.getString("phone",""));
        username_tv.setText(users.getString("name",""));

        Query query = myRef.child("details").orderByChild("email").equalTo(users.getString("email", ""));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //TODO get the data here
                        Details models = postSnapshot.getValue(Details.class);
                        System.out.println("dadi" + models.getEmail());

                    }

                } else {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.print("Error mama " + databaseError.getMessage().toString());
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        }

        editprofile = getSharedPreferences("Myprofile", MODE_PRIVATE).edit();
        SharedPreferences ss = getSharedPreferences("Myprofile", MODE_PRIVATE);
        if (ss.getString("photopath", "").toString().length() > 0) {
            getBitmap(ss.getString("photopath", ""));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap;
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            profile_img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void onSelectFromGalleryResult(Intent data) {
         if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

              /*  Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                File finalFile = new File(getRealPathFromURI(tempUri));
                //  compressImage(finalFile.getAbsolutePath().toString());*/

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    editprofile.putString("photopath", destination.getAbsolutePath().toString());
                    editprofile.commit();
                    Toast.makeText(getBaseContext(),"onselectimage",Toast.LENGTH_SHORT).show();
                    profile_img.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"ext",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"exception "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    userChoosenTask = "Take Photo";
                    cameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Library";
                    galleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        bitmap = (Bitmap) data.getExtras().get("data");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();

        File destination = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

            editprofile.putString("photopath", destination.getAbsolutePath().toString());
            editprofile.commit();
            profile_img.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_plain:
                finish();
                break;
            case R.id.threedots_plain:

                break;
            case R.id.profile_img:
                selectImage();
                break;
        }
    }
}
