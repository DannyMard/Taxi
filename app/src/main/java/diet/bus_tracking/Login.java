package diet.bus_tracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
