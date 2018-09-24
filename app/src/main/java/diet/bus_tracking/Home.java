package diet.bus_tracking;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Home extends Activity implements View.OnClickListener {
    DrawerLayout home;
    ImageView menu_img, map_logo, photo_img;
    RelativeLayout myll;
    Spinner bus_spinner;
    RecyclerView stations_rv;
    ArrayList<Stations> stations;
    Station_Adapter station_adapter;
    Button logout_btn;
    LinearLayout bustimng_ll, shareapp_ll, suggest_ll, logout_ll, profile_ll, setalarm_ll, stopalarm_ll;
    String busno, state_button;
    TextView name_tv, start_tv;
    SharedPreferences editprofile;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    GPSTracker gpsTracker;
    SharedPreferences se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);
        se = getSharedPreferences("Details", MODE_PRIVATE);
        gpsTracker = new GPSTracker(Home.this);
        name_tv = (TextView) findViewById(R.id.name_tv);
        start_tv = (TextView) findViewById(R.id.start_tv);
        start_tv.setOnClickListener(this);
        photo_img = (ImageView) findViewById(R.id.photo_img);
        photo_img.setOnClickListener(this);
        map_logo = (ImageView) findViewById(R.id.map_logo);
        map_logo.setOnClickListener(this);
        menu_img = (ImageView) findViewById(R.id.menu_img);
        menu_img.setOnClickListener(this);
        suggest_ll = (LinearLayout) findViewById(R.id.suggest_ll);
        suggest_ll.setOnClickListener(this);
        shareapp_ll = (LinearLayout) findViewById(R.id.shareapp_ll);
        shareapp_ll.setOnClickListener(this);
        profile_ll = (LinearLayout) findViewById(R.id.profile_ll);
        profile_ll.setOnClickListener(this);
        logout_ll = (LinearLayout) findViewById(R.id.logout_ll);
        logout_ll.setOnClickListener(this);
        bustimng_ll = (LinearLayout) findViewById(R.id.bustimng_ll);
        bustimng_ll.setOnClickListener(this);
        setalarm_ll = (LinearLayout) findViewById(R.id.setalarm_ll);
        setalarm_ll.setOnClickListener(this);
        home = (DrawerLayout) findViewById(R.id.home);
        stopalarm_ll = (LinearLayout) findViewById(R.id.stopalarm_ll);
        stopalarm_ll.setOnClickListener(this);
        myll = (RelativeLayout) findViewById(R.id.myll);
        bus_spinner = (Spinner) findViewById(R.id.bus_spinner);
        stations_rv = (RecyclerView) findViewById(R.id.stations_rv);
        if (se.getString("name", "").equals("admin")) {
            setalarm_ll.setVisibility(View.GONE);
            stopalarm_ll.setVisibility(View.GONE);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.buses)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        bus_spinner.setAdapter(spinnerArrayAdapter);
        stations_rv = (RecyclerView) findViewById(R.id.stations_rv);
        stations_rv.setLayoutManager(new LinearLayoutManager(Home.this));
        stations = new ArrayList<Stations>();
        bus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                busno = adapterView.getSelectedItem().toString();
                SharedPreferences.Editor star = getSharedPreferences("STATUS", MODE_PRIVATE).edit();
                star.putString("status", "start");
                star.putString("busno", busno);
                star.commit();
                // Toast.makeText(getBaseContext(), busno, Toast.LENGTH_SHORT).show();
                switch (busno) {
                    case "BUS-01":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-02":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-03":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-04":
                        stations.clear();
                        stations.add(new Stations("Pendurthi ", "7:05 AM", "4"));
                        stations.add(new Stations("Chinnamusidiwada ", "7:08 AM", "4"));
                        stations.add(new Stations("Sujatha Nagar  ", "7:10 AM", "4"));
                        stations.add(new Stations("Krishnarai Puram  ", "7:16 AM", "4"));
                        stations.add(new Stations("Vepagunta Jnc  ", "7:18 AM", "4"));
                        stations.add(new Stations("Naiduthota  ", "7:19 AM", "4"));
                        stations.add(new Stations("Gopalapatnam Bunk  ", "7:25 AM", "4"));
                        stations.add(new Stations("Baji Jnc  ", "7:27 AM", "4"));
                        stations.add(new Stations("NAD Jnc", "7:30 AM", "4"));
                        stations.add(new Stations("Gajuwaka \n(Gantyada ) ", "7:55 AM", "4"));
                        stations.add(new Stations("DIET College  ", "8:25 AM", "4"));
                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-05":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-06":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-07":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-08":
                        stations.clear();
                        stations.add(new Stations("Scindia ", "7:30 AM", "4"));
                        stations.add(new Stations("Malkapuram ", "7:33 AM", "4"));
                        stations.add(new Stations("R.K.Puram  ", "7:34 AM", "4"));
                        stations.add(new Stations("Sriharipuram  ", "7:35 AM", "4"));
                        stations.add(new Stations("Coromandel \nGate  ", "7:36 AM", "4"));
                        stations.add(new Stations("GWK-Depo  ", "7:38 AM", "4"));
                        stations.add(new Stations("Zink Gate  ", "7:40 AM", "4"));
                        stations.add(new Stations("Old Gajuwaka ", "7:45 AM", "4"));
                        stations.add(new Stations("Srinagar", "7:50 AM", "4"));
                        stations.add(new Stations("DIET College  ", "8:30 AM", "4"));
                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-09":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-10":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-11":
                        stations.clear();
                        stations.add(new Stations("Narsipatnam ", "7:05 AM", "4"));
                        stations.add(new Stations("Pedaboddepalli ", "7:10 AM", "4"));
                        stations.add(new Stations("Kondalagraharam ", "7:25 AM", "4"));
                        stations.add(new Stations("Makavarapalem  ", "7:30 AM", "4"));
                        stations.add(new Stations("Kannurupalem ", "7:50 AM", "4"));
                        stations.add(new Stations("Thallapalem", "8:00 AM", "4"));
                        stations.add(new Stations("Bayyavaram ", "8:10 AM", "4"));
                        stations.add(new Stations("Kasimkota", "8:15 AM", "4"));
                        stations.add(new Stations("College Jn", "8:20 AM", "4"));
                        stations.add(new Stations("DIET College  ", "8:30 AM", "4"));
                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-12":
                        stations.clear();
                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-13":
                        stations.clear();
                        stations.add(new Stations("NADUPURU ", "7:25 AM", "4"));
                        stations.add(new Stations("HOUSING BOARD ", "7:27 AM", "4"));
                        stations.add(new Stations("BALACHERUVU \nARCH GATE ", "7:29 AM", "4"));
                        stations.add(new Stations("BULLI REDDY \nSTOP ", "7:31 AM", "4"));
                        stations.add(new Stations("NEW POLICE STATION\n (B.C.ROAD) ", "7:33 AM", "4"));
                        stations.add(new Stations("FIRE STATION \n (B.C.ROAD)", "7:35 AM", "4"));
                        stations.add(new Stations("TNR STOP\n (B.C.ROAD)", "7:37 AM", "4"));
                        stations.add(new Stations("DAYAL NAGAR\n (B.C.ROAD) ", "7:39 AM", "4"));
                        stations.add(new Stations("TNR FUNCTION HALL\n (B.C.ROAD) ", "7:41 AM", "4"));
                        stations.add(new Stations("KAKATIYA STOP \n (B.C.ROAD)", "7:43 AM", "4"));
                        stations.add(new Stations("CMR SHOPPING MALL \n (NEW GAJUWAKA)", "7:45 AM", "4"));
                        stations.add(new Stations("SRI KANYA THEATHRE \n (NEW GAJUWAKA)", "7:47 AM", "4"));
                        stations.add(new Stations("RK HOSPITAL \n (NEW GAJUWAKA) ", "7:55 AM", "4"));
                        stations.add(new Stations("DIET ", "8:30 AM", "4"));
                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-14":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-15":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-16":
                        stations.clear();

                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;
                    case "BUS-17":
                        stations.clear();
                        stations.add(new Stations("yellamanchili ", "7:30 AM", "17"));
                        stations.add(new Stations("nasingaballi  ", "7:45 AM", "17"));
                        stations.add(new Stations("A.S PETA ", "7:50 AM", "17"));
                        stations.add(new Stations("KSIMIKOTA ", "8:00 AM", "17"));
                        stations.add(new Stations("SARDANAGAR ", "8:15 AM", "17"));
                        stations.add(new Stations("COLLEGE ", "8:30 AM", "17"));
                        station_adapter = new Station_Adapter(Home.this, stations, R.layout.studysingle);
                        stations_rv.setAdapter(station_adapter);
                        station_adapter.notifyDataSetChanged();
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences star = getSharedPreferences("STATUS", MODE_PRIVATE);
        if (star.getString("status", "").length() > 0) {
            start_tv.setText(star.getString("status", "").toString());
        } else {
            start_tv.setText("Start");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_img:
                home.openDrawer(myll);
                break;
            case R.id.bustimng_ll:
                /*Intent bustimings = new Intent(Home.this, BusTimings.class);
                startActivity(bustimings);*/
                home.closeDrawer(myll);
                break;
            case R.id.logout_ll:
                SharedPreferences.Editor users = getSharedPreferences("Details", MODE_PRIVATE).edit();
                users.putString("email", "");
                users.commit();
                finish();
                break;
            case R.id.map_logo:
                Intent maps = new Intent(Home.this, MapsActivity.class);
                maps.putExtra("busno", busno);
                startActivity(maps);
                break;
            case R.id.profile_ll:
                Intent profile = new Intent(Home.this, MyProfile.class);
                startActivity(profile);
                break;
            case R.id.start_tv:
                SharedPreferences.Editor alarm = getSharedPreferences("Alarm", MODE_PRIVATE).edit();
                alarm.putString("alarmstopped", "no");
                alarm.commit();
                if (busno.equals("Select Bus")) {
                    showDialog(Home.this, "Please Select Bus", "no");
                } else {
                    SharedPreferences.Editor star = getSharedPreferences("STATUS", MODE_PRIVATE).edit();

                    if (start_tv.getText().equals("Start")) {
                        state_button = "started";
                        start_tv.setText("Stop");
                        start_tv.setTextColor(Color.GREEN);
                        star.putString("status", "stop");
                        star.putString("busno", busno);
                        star.commit();
                        AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                        Intent w = new Intent(this, LocationUpdatesBroadcastReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, w, 0);
                        Calendar cal2 = Calendar.getInstance();

                        // cal.add(Calendar.SECOND, 5);
                        // alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                        //alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,cal.getTimeInMillis(), pendingIntent);

                        int timeinterval = 30000;
                        System.out.println("Geo Milliseconds " + timeinterval);
                        if (android.os.Build.MANUFACTURER.equals("LeMobile")) {
                            //  alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), 60000, pendingIntent);
                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), timeinterval, pendingIntent);
                        } else if (android.os.Build.MANUFACTURER.equals("vivo")) {
                            //  Toast.makeText(getBaseContext(), "vivo mobile", Toast.LENGTH_SHORT).show();
                            //  alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60000, pendingIntent);
                            //  alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), 30000, pendingIntent);
                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), timeinterval, pendingIntent);
                        } else {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                //   alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60000, pendingIntent);
                                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeinterval, pendingIntent);
                                // only for gingerbread and newer versions
                            } else {
                                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), timeinterval, pendingIntent);
                            }
                        }

                        updatestatus();
                    } else {
                        state_button = "stopped";
                        start_tv.setText("Start");
                        start_tv.setTextColor(Color.WHITE);
                        star.putString("status", "start");
                        star.putString("busno", busno);
                        star.commit();
                        clearApplicationData();
                    }



               /* for (int i=10;i<21;i++){
                    String id = myRef.push().getKey();
                    Bus details = new Bus("BUS-"+i,"start",format,"22.22","22.22");
                    myRef.child(id).setValue(details);

                }*/

                }


                break;
            case R.id.stopalarm_ll:
                SharedPreferences.Editor aa = getSharedPreferences("Alarm", MODE_PRIVATE).edit();
                aa.putString("alarmstopped", "no");
                aa.commit();

                break;
            case R.id.setalarm_ll:
                SharedPreferences.Editor sd = getSharedPreferences("Alarm", MODE_PRIVATE).edit();
                sd.putString("alarmstopped", "yes");
                sd.commit();
                if (busno.equals("Select Bus")) {
                    showDialog(Home.this, "Please select your bus before Enabled Alarm. It will remains you before 10 minutes and depends on bus speed and location", "no");
                } else {
                    SharedPreferences.Editor star = getSharedPreferences("STATUS", MODE_PRIVATE).edit();
                    star.putString("status", "start");
                    star.putString("busno", busno);
                    star.commit();
                    showDialog(Home.this, "Enabled Alarm Enabling. It will remains you before 10 minutes and depends on bus speed and location", "yes");
                    final AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                    Intent everyfiveminutes = new Intent(this, EveryFiveMInutes.class);
                    final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, everyfiveminutes, 0);
                    final Calendar cal2 = Calendar.getInstance();
                    int timeinterval = 30000;
                    System.out.println("Geo Milliseconds " + timeinterval);
                    if (android.os.Build.MANUFACTURER.equals("LeMobile")) {
                        //  alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), 60000, pendingIntent);
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), timeinterval, pendingIntent);
                    } else if (android.os.Build.MANUFACTURER.equals("vivo")) {
                        //  Toast.makeText(getBaseContext(), "vivo mobile", Toast.LENGTH_SHORT).show();
                        //  alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60000, pendingIntent);
                        //  alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), 30000, pendingIntent);
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), timeinterval, pendingIntent);
                    } else {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            //   alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60000, pendingIntent);
                            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeinterval, pendingIntent);
                            // only for gingerbread and newer versions
                        } else {
                            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), timeinterval, pendingIntent);
                        }
                    }

                }


                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editprofile = getSharedPreferences("Myprofile", MODE_PRIVATE);

        getimage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getimage();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getimage();
    }

    public void getimage() {
        name_tv.setText(se.getString("name", ""));
        editprofile = getSharedPreferences("Myprofile", MODE_PRIVATE);
        if (se.getString("name", "").equals("admin")) {
           // stations_rv.setVisibility(View.GONE);
        }else {
            start_tv.setVisibility(View.GONE);
        }
        SharedPreferences ss = getSharedPreferences("Myprofile", MODE_PRIVATE);
        if (ss.getString("photopath", "").toString().length() > 0) {
            try {
                Bitmap bitmap;
                File f = new File(ss.getString("photopath", "").toString());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                photo_img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public void updatestatus() {
        gpsTracker = new GPSTracker(Home.this);
        if (gpsTracker.isGPSEnabled || gpsTracker.isNetworkEnabled) {

            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            final String format = s.format(new Date());
            Query query = myRef.child("Bus").orderByChild("busno").equalTo(busno);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(getBaseContext(), "hello if case", Toast.LENGTH_SHORT).show();
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //TODO get the data here
                            Bus buses = postSnapshot.getValue(Bus.class);
                            System.out.println("dadi" + buses.getStatus());

                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                            String path = "/" + dataSnapshot.getKey() + "/" + key;
                            HashMap<String, Object> result = new HashMap<>();
                            gpsTracker = new GPSTracker(Home.this);
                            if (gpsTracker.canGetLocation()) {
                                if (gpsTracker.getLatitude() != 0.0 && gpsTracker.getLongitude() != 0.0) {
                                    result.put("lat", gpsTracker.getLatitude());
                                    result.put("longi", gpsTracker.getLongitude());
                                    result.put("dateandtime", format);
                                    result.put("status", state_button);
                                }
                            }

                            myRef.child(path).updateChildren(result);
                        }

                    } else {
                        Toast.makeText(getBaseContext(), "hello else case", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getBaseContext(), "hello " + databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    System.out.print("Error mama " + databaseError.getMessage().toString());
                }
            });

        } else {

        }

    }

    public void clearApplicationData() {
        getApplicationContext().getSharedPreferences("START_STOP", MODE_PRIVATE).edit().clear().commit();
        getApplicationContext().getSharedPreferences("location_date_storage", MODE_PRIVATE).edit().clear().commit();
        getApplicationContext().getSharedPreferences("Userdetails", MODE_PRIVATE).edit().clear().commit();

        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }

        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
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
                } else {
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }

}
