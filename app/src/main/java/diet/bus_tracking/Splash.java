package diet.bus_tracking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Splash extends Activity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    LinearLayout splash_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        splash_ll = (LinearLayout) findViewById(R.id.splash_ll);
        splash_ll.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences users = getSharedPreferences("Details", MODE_PRIVATE);
                if (users.getString("email", "").length() > 0) {

                    Intent login = new Intent(Splash.this, Home.class);
                    startActivity(login);
                } else {

                    Intent login = new Intent(Splash.this, Login.class);
                    startActivity(login);
                }

            }
        }, 2000);

        mAuth = FirebaseAuth.getInstance();


        myRef = FirebaseDatabase.getInstance().getReference("Sams");

/*
        mAuth.createUserWithEmailAndPassword("dadi@gmail.com", "dadi@123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        } else {
                              Toast.makeText(Splash.this, "Email Already Exists.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
*/


        myRef = myRef.child("second").child("name");
        String id = myRef.push().getKey();
        EventItem aUser = new EventItem("dadi two Nilsson");
        myRef.child(id).setValue(aUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash_ll:
                SharedPreferences users = getSharedPreferences("Details", MODE_PRIVATE);
                if (users.getString("email", "").length() > 0) {

                    Intent login = new Intent(Splash.this, Home.class);
                    startActivity(login);
                } else {

                    Intent login = new Intent(Splash.this, Login.class);
                    startActivity(login);
                }
                break;
        }
    }
}
