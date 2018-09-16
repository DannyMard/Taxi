package diet.bus_tracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Signup extends Activity implements View.OnClickListener {
TextView login_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        login_tv = (TextView) findViewById(R.id.login_tv);
        login_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_tv:
                Intent login = new Intent(Signup.this,Login.class);
                startActivity(login);
                break;
        }
    }
}
