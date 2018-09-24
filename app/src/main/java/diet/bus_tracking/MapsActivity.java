package diet.bus_tracking;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener{

    private GoogleMap mMap;
    ImageView back_gps;
    TextView bus_tv;
    SharedPreferences details;
    DatabaseReference myRef;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        back_gps = (ImageView) findViewById(R.id.back_gps);
        back_gps.setOnClickListener(this);
        bus_tv = (TextView) findViewById(R.id.bus_tv);
        bus_tv.setText(getIntent().getStringExtra("busno"));

        details = getSharedPreferences("Details",MODE_PRIVATE);
        gpsTracker = new GPSTracker(MapsActivity.this);

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (details.getString("name","").equals("admin")){
            if (gpsTracker.isNetworkEnabled || gpsTracker.isGPSEnabled){
                LatLng sydney = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());

                mMap.addMarker(new MarkerOptions().position(sydney).title("Bus Exact Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );

                mMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title("Diet College Bus")
                        .snippet("Population: 4,627,300")

                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_map)));
            }
        }else {

            Query query = myRef.child("Bus").orderByChild("busno").equalTo(getIntent().getStringExtra("busno"));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //TODO get the data here
                            Bus buses = postSnapshot.getValue(Bus.class);
                            System.out.println("dadi" + buses.getStatus());
                            LatLng sydney = new LatLng(buses.getLat(),buses.getLongi());

                            mMap.addMarker(new MarkerOptions().position(sydney).title("Bus Exact Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );

                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title("Diet College Bus")
                                    .snippet("Population: 4,627,300")

                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_map)));

                        }

                    } else {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    System.out.print("Error mama " + databaseError.getMessage().toString());
                }
            });


        }
        // Add a marker in Sydney and move the camera

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_gps:
                   finish();
                break;
        }
    }
}
