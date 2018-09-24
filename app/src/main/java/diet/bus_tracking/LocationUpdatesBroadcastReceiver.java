package diet.bus_tracking;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

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
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
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
        Intent w = new Intent(context, LocationUpdatesBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, w, 0);
        // cal.add(Calendar.SECOND, 5);
        // alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        //alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,cal.getTimeInMillis(), pendingIntent);

        // alarmMgr.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, 60000, pendingIntent);

        int timeinterval = (int) 30000;
        System.out.println("Geo Milliseconds at receiver " + timeinterval);
        if (android.os.Build.MANUFACTURER.equals("LeMobile")) {

        } else if (android.os.Build.MANUFACTURER.equals("vivo")) {
            //alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, 30000, pendingIntent);
            alarmMgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeinterval, pendingIntent);
            //alarmMgr.set(android.app.AlarmManager.RTC_WAKEUP,30000, pendingIntent);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (android.os.Build.MANUFACTURER.equals("LeMobile")) {

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

            SharedPreferences sd = context.getSharedPreferences("STATUS",MODE_PRIVATE);
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            final String format = s.format(new Date());
            Query query = myRef.child("Bus").orderByChild("busno").equalTo(sd.getString("busno",""));
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
                                }
                            }/*else {
                                result.put("lat", 17.6603895);
                                result.put("longi", 83.0196629);
                                result.put("dateandtime", format);
                                result.put("status", "NOTRUNNING");
                            }*/

                            myRef.child(path).updateChildren(result);
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



}