package com.example.christophergu.pg.data.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.christophergu.pg.CourtListActivity;
import com.example.christophergu.pg.R;
import com.example.christophergu.pg.data.Court;

import java.util.List;

public class CourtArrayAdapter extends ArrayAdapter<Court> {
    private final List<Court> courts;
    private final Context context;

    public CourtArrayAdapter(@NonNull Context context, int resource, @NonNull List<Court> courts) {
        super(context, resource, courts);
        this.context = context;
        this.courts = courts;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_court_list, parent, false);

        TextView tvLocation = rowView.findViewById(R.id.tvCourtLocation);
        TextView tvPublic = rowView.findViewById(R.id.tvIsPublic);
        TextView tvOutdoor = rowView.findViewById(R.id.tvIsOutdoor);
        TextView tvTimeRange = rowView.findViewById(R.id.tvTimeRange);
        TextView tvSpecification = rowView.findViewById(R.id.tvSpecification);

        tvLocation.setText(courts.get(position).getAddress());
        tvPublic.setText(courts.get(position).ismPublic()?"True":"False");
        tvOutdoor.setText(courts.get(position).ismOutside()?"True":"False");
        tvTimeRange.setText(courts.get(position).getOpenTime()+" ~ "+courts.get(position).getCloseTime());

        String descriptions="";
        if(courts.get(position).getNumHoops()!=0){
            descriptions += "Number of hoops: "+String.valueOf(courts.get(position).getNumHoops());
        }
        if(courts.get(position).getNumLanes()!=0){
            descriptions += "Number of lanes: "+String.valueOf(courts.get(position).getNumLanes());
        }
        if(courts.get(position).getNumNets()!=0){
            descriptions += "Number of nets: "+String.valueOf(courts.get(position).getNumNets());
        }


        if(courts.get(position).isGrass() == 1)
            descriptions += "Grass field: True";
        else if(courts.get(position).isGrass() == 0 && CourtListActivity.sportType.equals("soccer-fields"))
            descriptions += "Grass field: False";

        if(descriptions==""){
            descriptions = "None";
        }
        tvSpecification.setText(descriptions);

        return rowView;
    }

}
