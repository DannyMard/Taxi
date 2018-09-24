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

public class Station_Adapter extends RecyclerView.Adapter<Station_Adapter.MyHolder> {
    Home context;
    ArrayList<Stations> stations;
    int Rowlayout;

    public Station_Adapter(Home home, ArrayList<Stations> stations, int station_single) {
        this.context = home;
        this.stations = stations;
        this.Rowlayout = station_single;
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
       /* if (stations.get(position).getDestinaation().equals("vizag")) {
            holder.group_title_tv.setBackground(context.getResources().getDrawable(R.drawable.circlegreen));
            holder.group_title_tv.setText(stations.get(position).getDestinaation());
            holder.line_view.setBackgroundColor(Color.GREEN);
        }else {
            holder.group_title_tv.setText(stations.get(position).getDestinaation());
            holder.duration_tv.setText(stations.get(position).getStarttime());
        }*/
        holder.group_title_tv.setText(stations.get(position).getDestinaation());
        holder.duration_tv.setText(stations.get(position).getStarttime());
        if (position % 2 == 0) {

            holder.left_duration.setBackground(context.getResources().getDrawable(R.drawable.timings_bg));
            holder.left_duration.setText(stations.get(position).getStarttime());

            holder.duration_tv.setBackgroundColor(Color.WHITE);
            holder.duration_tv.setText("");
            if (stations.size()-1 == position){
                holder.duration_tv.setBackground(context.getResources().getDrawable(R.drawable.timings_bg));
                holder.duration_tv.setText("Goal Reached");
            }
        } else {
            holder.left_duration.setBackgroundColor(Color.WHITE);
            holder.left_duration.setText("");
            holder.duration_tv.setBackground(context.getResources().getDrawable(R.drawable.timings_bg));
            holder.duration_tv.setText(stations.get(position).getStarttime());
            if (stations.size()-1 == position){
                holder.left_duration.setBackground(context.getResources().getDrawable(R.drawable.timings_bg));
                holder.left_duration.setText("Goal Reached");
            }
        }
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView group_title_tv, duration_tv,left_duration;
        View line_view;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
          /*  group_title_tv = itemView.findViewById(R.id.study_hor_tv);
            line_view = itemView.findViewById(R.id.line_view);
            duration_tv = itemView.findViewById(R.id.duration_tv);*/
            left_duration = itemView.findViewById(R.id.left_duration);
            group_title_tv = itemView.findViewById(R.id.study_hor_tv);
            line_view = itemView.findViewById(R.id.line_view);
            duration_tv = itemView.findViewById(R.id.duration_tv);
        }
    }
}
