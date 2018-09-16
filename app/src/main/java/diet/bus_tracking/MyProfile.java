package diet.bus_tracking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProfile extends Activity implements View.OnClickListener {
TextView header_txt_plain;
ImageView back_plain,threedots_plain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        header_txt_plain = (TextView) findViewById(R.id.header_txt_plain);
        header_txt_plain.setText("Profile");
        threedots_plain = (ImageView) findViewById(R.id.threedots_plain);
        threedots_plain.setOnClickListener(this);
        back_plain = (ImageView) findViewById(R.id.back_plain);
        back_plain.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_plain:
                  finish();
                break;
            case R.id.threedots_plain:

                break;
        }
    }
}
