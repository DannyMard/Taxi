package diet.bus_tracking;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.constraint.Constraints.TAG;

public class Login extends Activity implements View.OnClickListener {
    TextView signup_tv, forgotpassword_tv;
    FloatingActionButton login_btn;
    EditText password_et_login, email_et_login;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progressDialog;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email_et_login = (EditText) findViewById(R.id.email_et_login);
        password_et_login = (EditText) findViewById(R.id.password_et_login);
        login_btn = (FloatingActionButton) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        forgotpassword_tv = (TextView) findViewById(R.id.forgotpassword_tv);
        forgotpassword_tv.setOnClickListener(this);
        signup_tv = (TextView) findViewById(R.id.signup_tv);
        signup_tv.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_NETWORK_STATE}, 0);
        }

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_tv:
                Intent signup = new Intent(Login.this, Signup.class);
                startActivity(signup);
                break;
            case R.id.forgotpassword_tv:
                Intent forgotpassword_tv = new Intent(Login.this, Forgotpassword.class);
                startActivity(forgotpassword_tv);
                break;
            case R.id.login_btn:

                if (!isValidEmail(email_et_login.getText().toString())) {
                    showDialog(Login.this, "Please Enter valid Email ", "no");
                } else if (email_et_login.getText().toString().length() == 0){
                    showDialog(Login.this, "Please Enter valid Email ", "no");
                }else if (password_et_login.getText().toString().length() == 0) {
                    showDialog(Login.this, "Please Enter Valid Email ", "no");
                }else {

                    progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setMessage("Please wait authenticating user");
                    progressDialog.show();

                    Query query = myRef.child("details").orderByChild("email").equalTo(email_et_login.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressDialog.dismiss();
                            if (dataSnapshot.exists()) {
                                // dataSnapshot is the "issue" node with all children with id 0
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    //TODO get the data here
                                    Details models = postSnapshot.getValue(Details.class);
                                    System.out.println("dadi" + models.getEmail());
                                    SharedPreferences.Editor users = getSharedPreferences("Details", MODE_PRIVATE).edit();
                                    users.putString("email", models.getEmail());
                                    users.commit();
                                    if (models.getPassword().toString().equals(password_et_login.getText().toString())){
                                        showDialog(Login.this, "Sucessfully Login", "yes");
                                    }else {
                                        showDialog(Login.this, " Entered password incorrect ", "no");
                                    }
                                }

                            } else {
                                showDialog(Login.this, "Your are not Authenticated user", "no");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                            showDialog(Login.this, "Sorry Unexpected Error", "no");
                            System.out.print("Error mama " + databaseError.getMessage().toString());
                        }
                    });
                }


                break;
        }
    }

    public void showDialog(Activity activity, String msg, final String status) {
        final Dialog dialog = new Dialog(activity, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        ImageView b = dialog.findViewById(R.id.b);
        if (status.equals("yes")) {
            b.setVisibility(View.VISIBLE);
        } else {
            b.setVisibility(View.GONE);
        }
        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("yes")) {
                    dialog.dismiss();
                    Intent dashboard = new Intent(Login.this, Home.class);
                    startActivity(dashboard);
                    finish();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    private boolean isValidEmail(String Emailid) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Emailid);
        return matcher.matches();
    }

}
