package diet.bus_tracking;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class EveryFiveMInutes extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";
    Context context;
    static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.backgroundlocationupdates.action" +
                    ".PROCESS_UPDATES";
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    GPSTracker gpsTracker;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.keepSynced(true);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent w = new Intent(context, EveryFiveMInutes.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, w, 0);

        int timeinterval = (int) 30000;
        System.out.println("Geo Milliseconds at receiver " + timeinterval);
        if (Build.MANUFACTURER.equals("LeMobile")) {

        } else if (Build.MANUFACTURER.equals("vivo")) {
            //alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, 30000, pendingIntent);
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeinterval, pendingIntent);
            //alarmMgr.set(android.app.AlarmManager.RTC_WAKEUP,30000, pendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.MANUFACTURER.equals("LeMobile")) {

                } else {
                    //   alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, 30000, pendingIntent);
                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeinterval, pendingIntent);
                }
                // only for gingerbread and newer versions
            }
        }
        Log.d(TAG, "onReceive");
        if (intent != null) {
            Log.d(TAG, "onReceive entered if");
            updatestatus();
            //  Log.d("addresss ",getaddressFromGEO(17.7167105,83.306409));
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                Log.d(TAG, "onReceive entered ACTION_PROCESS_UPDATES");


            }
        }

    }

    public void updatestatus() {


        gpsTracker = new GPSTracker(context);
        if (gpsTracker.isGPSEnabled || gpsTracker.isNetworkEnabled) {

            SharedPreferences sd = context.getSharedPreferences("STATUS", MODE_PRIVATE);
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            final String format = s.format(new Date());
            Query query = myRef.child("Bus").orderByChild("busno").equalTo(sd.getString("busno", ""));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //TODO get the data here
                            Bus buses = postSnapshot.getValue(Bus.class);
                            System.out.println("dadi" + buses.getStatus());

                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`
                            String path = "/" + dataSnapshot.getKey() + "/" + key;
                            HashMap<String, Object> result = new HashMap<>();
                            gpsTracker = new GPSTracker(context);
                            if (gpsTracker.canGetLocation()) {
                                if (gpsTracker.getLatitude() != 0.0 && gpsTracker.getLongitude() != 0.0) {
                                    result.put("lat", gpsTracker.getLatitude());
                                    result.put("longi", gpsTracker.getLongitude());
                                    result.put("dateandtime", format);
                                    result.put("status", "RUNNING");

                                   /* Location startPoint=new Location("locationA");
                                    startPoint.setLatitude(gpsTracker.getLatitude());
                                    startPoint.setLongitude(gpsTracker.getLongitude());

                                    Location endPoint=new Location("locationA");
                                    endPoint.setLatitude(buses.getLat());
                                    endPoint.setLongitude(buses.getLongi());*/
                                    double distance = distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), buses.getLat(), buses.getLongi());
                                    System.out.print("helloldistance " + distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), buses.getLat(), buses.getLongi()));

                                    // double distance=startPoint.distanceTo(endPoint);
                                    if (distance < 4.0) {
                                        System.out.print("distance4.0<than " + distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), buses.getLat(), buses.getLongi()));
                                        MediaPlayer mPlayer2 = MediaPlayer.create(context, R.raw.alarm);
                                        if (mPlayer2.isPlaying()) {
                                            mPlayer2.stop();
                                        } else {
                                            System.out.print("elseparn " + distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), buses.getLat(), buses.getLongi()));
                                            SharedPreferences alarm = context.getSharedPreferences("Alarm", MODE_PRIVATE);
                                            if (alarm.getString("alarmstopped", "").equals("no")) {
                                                mPlayer2.stop();
                                            }else {
                                                mPlayer2.start();
                                            }

                                        }


                                    }else {
                                        System.out.print("hellolllllelsepart " + distance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), buses.getLat(), buses.getLongi()));

                                    }
                                   /* MediaPlayer mPlayer2 = MediaPlayer.create(context, R.raw.alarm);
                                    mPlayer2.start();*/

                                }
                            }

                            // myRef.child(path).updateChildren(result);
                        }

                    } else {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    System.out.print("Error mama " + databaseError.getMessage().toString());
                }
            });

        } else {

        }

    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double dist = earthRadius * c;

        return dist; // output distance, in MILES
    }


}