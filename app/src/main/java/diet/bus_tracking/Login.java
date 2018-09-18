package diet.bus_tracking;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.support.constraint.Constraints.TAG;

public class Login extends Activity implements View.OnClickListener {
    TextView signup_tv, forgotpassword_tv;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login_btn = (Button) findViewById(R.id.login_btn);
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
                Intent login_btn = new Intent(Login.this, Home.class);
                startActivity(login_btn);
                break;
        }
    }
}
