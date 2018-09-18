package diet.bus_tracking;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends Activity implements View.OnClickListener {
    TextView login_tv;
    EditText name_et, email_et, phoneno_et, password_et;
    Button continue_btn;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        progressDialog = new ProgressDialog(this);

        login_tv = (TextView) findViewById(R.id.login_tv);
        login_tv.setOnClickListener(this);

        continue_btn = (Button) findViewById(R.id.continue_btn);
        continue_btn.setOnClickListener(this);


        name_et = (EditText) findViewById(R.id.name_et);
        email_et = (EditText) findViewById(R.id.email_et);
        phoneno_et = (EditText) findViewById(R.id.phoneno_et);
        password_et = (EditText) findViewById(R.id.password_et);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("details");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv:
                Intent login = new Intent(Signup.this, Login.class);
                startActivity(login);
                break;
            case R.id.continue_btn:
                if (name_et.getText().toString().length() == 0 ){
                     showDialog(Signup.this,"Please Enter Name ","no");
                }else if (email_et.getText().toString().length() == 0){
                    showDialog(Signup.this,"Please Enter Valid Email ","no");
                }else if (isValidEmail(email_et.getText().toString())){
                    showDialog(Signup.this,"Please Enter Valid Email Address ","no");
                }else if (phoneno_et.getText().toString().length() == 0 ){
                    showDialog(Signup.this,"Please Enter Valid Phone Number ","no");
                }else if (password_et.getText().toString().length() == 0){
                    showDialog(Signup.this,"Please Enter Password ","no");
                }else {


                    String id = myRef.push().getKey();
                    Details details = new Details(name_et.getText().toString(),email_et.getText().toString(),phoneno_et.getText().toString(),password_et.getText().toString());
                    myRef.child(id).setValue(details);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email_et.getText().toString(),password_et.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        //finish();


                                    } else {
                                        progressDialog.dismiss();
                                        // If sign in fails, display a message to the user.
                                        showDialog(Signup.this,"Sorry Email Already Exists !!", "no");
                                        // Toast.makeText(RegisterActivity.this, "Email Already Exists.",
                                        //        Toast.LENGTH_SHORT).show();

                                    }


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
                    Intent dashboard = new Intent(Signup.this, Home.class);
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
