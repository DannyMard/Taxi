package diet.bus_tracking;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Forgotpassword extends Activity implements View.OnClickListener {
    FloatingActionButton forgot_btn;
    TextView login_tv;
    EditText email_phone_et;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progressDialog;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);
        email_phone_et = (EditText) findViewById(R.id.email_phone_et);
        forgot_btn = (FloatingActionButton) findViewById(R.id.forgot_btn);
        login_tv = (TextView) findViewById(R.id.login_tv);
        login_tv.setOnClickListener(this);
        forgot_btn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_btn:
                if (email_phone_et.getText().toString().length() == 0) {
                    showDialog(Forgotpassword.this, "Please Enter Valid Email ", "no");
                } else {

                    progressDialog = new ProgressDialog(Forgotpassword.this);
                    progressDialog.setMessage("Please wait authenticating user");
                    progressDialog.show();

                    Query query = myRef.child("details").orderByChild("email").equalTo(email_phone_et.getText().toString());
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

                                    showDialog(Forgotpassword.this, "Your password :  "+models.getPassword(), "yes");
                                }

                            } else {
                                showDialog(Forgotpassword.this, "Your Record not found in our database please contact admin", "no");
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                            showDialog(Forgotpassword.this, "Sorry Unexpected Error", "no");
                            System.out.print("Error mama " + databaseError.getMessage().toString());
                        }
                    });
                }
                break;
            case R.id.login_tv:
                finish();
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
                    Intent dashboard = new Intent(Forgotpassword.this, Login.class);
                    startActivity(dashboard);
                    finish();
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

}
