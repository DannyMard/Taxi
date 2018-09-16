package diet.bus_tracking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Forgotpassword extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
