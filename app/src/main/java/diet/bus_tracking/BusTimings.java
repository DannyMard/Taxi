package diet.bus_tracking;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class BusTimings extends Activity {
    RecyclerView bustimings_rv;
    ArrayList<Timings> timings;
    Timings_Adapter timings_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_timings);
        bustimings_rv = (RecyclerView) findViewById(R.id.bustimings_rv);
        bustimings_rv.setLayoutManager(new LinearLayoutManager(BusTimings.this));

        timings = new ArrayList<Timings>();
        timings.add(new Timings("NADUPURU","7:25 AM"));
        timings.add(new Timings("HOUSING BOARD","7:27 AM"));
        timings.add(new Timings("BALACHERUVU ARCH GATE","7:29 AM"));
        timings.add(new Timings("BULLI REDDY STOP","7:31 AM"));
        timings.add(new Timings("NEW POLICE STATION\n (B.C.ROAD)","7:33 AM"));
        timings.add(new Timings("FIRE STATION \n(B.C.ROAD)","7:35 AM"));
        timings.add(new Timings("TNR STOP \n(B.C.ROAD)","7:37 AM"));
        timings.add(new Timings("DAYAL NAGAR \n(B.C.ROAD)","7:39 AM"));
        timings.add(new Timings("DAYAL NAGAR\n (B.C.ROAD)","7:41 AM"));
        timings.add(new Timings("KAKATIYA STOP \n(B.C.ROAD)","7:43 AM"));
        timings.add(new Timings("CMR SHOPPING MALL \n(NEW GAJUWAKA)","7:45 AM"));
        timings.add(new Timings("CMR SHOPPING \nMALL (NEW GAJUWAKA)","7:47 AM"));
        timings.add(new Timings("RK HOSPITAL \n(CHINA GANTAYADA)","7:55 AM"));
        timings.add(new Timings("DIET \n(ANAKAPALLE)","8:30 AM"));
        timings_adapter = new Timings_Adapter(BusTimings.this,timings,R.layout.timings_single);
        bustimings_rv.setAdapter(timings_adapter);
    }
}
