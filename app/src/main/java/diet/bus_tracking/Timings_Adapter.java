package diet.bus_tracking;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Timings_Adapter extends RecyclerView.Adapter<Timings_Adapter.MyHolder> {
    BusTimings context;
    ArrayList<Timings> timings;
    int Rowlayout;

    public Timings_Adapter(BusTimings busTimings, ArrayList<Timings> timings, int bus_single) {
        this.context = busTimings;
        this.timings = timings;
        this.Rowlayout = bus_single;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(Rowlayout, viewGroup, false);
        return new MyHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

            holder.station_tv.setText(timings.get(position).getBusstop());
            holder.duration_tv.setText(timings.get(position).getTimings());

    }

    @Override
    public int getItemCount() {
        return timings.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView station_tv,duration_tv;
        View line_view;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            station_tv = itemView.findViewById(R.id.station_tv);
            line_view = itemView.findViewById(R.id.line_view);
            duration_tv = itemView.findViewById(R.id.duration_tv);
        }
    }
}
