package diet.bus_tracking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class Home extends Activity implements View.OnClickListener {
    DrawerLayout home;
    ImageView menu_img;
    RelativeLayout myll;
    Spinner bus_spinner;
    RecyclerView stations_rv;
    ArrayList<Stations> stations;
    Station_Adapter station_adapter;
    Button logout_btn;
    LinearLayout bustimng_ll,shareapp_ll,suggest_ll,logout_ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        menu_img = (ImageView) findViewById(R.id.menu_img);
        menu_img.setOnClickListener(this);
        suggest_ll = (LinearLayout) findViewById(R.id.suggest_ll);
        suggest_ll.setOnClickListener(this);
        shareapp_ll = (LinearLayout) findViewById(R.id.shareapp_ll);
        shareapp_ll.setOnClickListener(this);
        logout_ll = (LinearLayout) findViewById(R.id.logout_ll);
        logout_ll.setOnClickListener(this);
        bustimng_ll  = (LinearLayout) findViewById(R.id.bustimng_ll);
        bustimng_ll.setOnClickListener(this);
        home = (DrawerLayout) findViewById(R.id.home);

        myll = (RelativeLayout) findViewById(R.id.myll);
        bus_spinner = (Spinner) findViewById(R.id.bus_spinner);
        stations_rv = (RecyclerView) findViewById(R.id.stations_rv);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.buses)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        bus_spinner.setAdapter(spinnerArrayAdapter);

        stations_rv = (RecyclerView) findViewById(R.id.stations_rv);
        stations_rv.setLayoutManager(new LinearLayoutManager(Home.this));
        stations = new ArrayList<Stations>();
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "start", "0 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "vizag", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "gurdwara", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "kancharapalem", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "politechical college", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "Nad", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "Srinagar", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "Gajuwaka", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "Nathayapalem", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "Kurmanapalem", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "lankelapalem", "20 Km"));
        stations.add(new Stations("12:00", "1:00", ".05", ".10", "Anakapalle", "20 Km"));

        station_adapter = new Station_Adapter(Home.this, stations, R.layout.station_single);
        stations_rv.setAdapter(station_adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_img:
                home.openDrawer(myll);
                break;
            case R.id.bustimng_ll:
                Intent bustimings = new Intent(Home.this,BusTimings.class);
                startActivity(bustimings);
                break;
            case R.id.logout_ll:
                SharedPreferences.Editor users = getSharedPreferences("Details", MODE_PRIVATE).edit();
                users.putString("email", "");
                users.commit();
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences users = getSharedPreferences("Details", MODE_PRIVATE);
        users.getString("email", "");
    }
}
