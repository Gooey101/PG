package com.example.christophergu.pg.data.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.christophergu.pg.CourtsListActivity;
import com.example.christophergu.pg.R;
import com.example.christophergu.pg.data.Courts;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CourtArrayAdapter extends ArrayAdapter<Courts> {
    private final List<Courts> courts;
    private final Context context;

    public CourtArrayAdapter(@NonNull Context context, int resource, @NonNull List<Courts> courts) {
        super(context, resource, courts);
        this.context = context;
        this.courts = courts;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.court_row, parent, false);

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
        else if(courts.get(position).isGrass() == 0 && CourtsListActivity.sportType.equals("soccer-fields"))
            descriptions += "Grass field: False";

        if(descriptions==""){
            descriptions = "None";
        }
        tvSpecification.setText(descriptions);

        return rowView;
    }

}
