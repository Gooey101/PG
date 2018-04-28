package com.example.christophergu.pg.data.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.christophergu.pg.R;
import com.example.christophergu.pg.data.Game;
import com.example.christophergu.pg.data.Team;

import java.util.List;

public class TeamArrayAdapter extends ArrayAdapter<Team> {
    private final List<Team> Teams;
    private final Context context;

    public TeamArrayAdapter(@NonNull Context context, int resource, @NonNull List<Team> Teams) {
        super(context, resource, Teams);
        this.context = context;
        this.Teams = Teams;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_team_list, parent, false);
        TextView tvTeamName = rowView.findViewById(R.id.tvTeamName);
        TextView tvTeamNum = rowView.findViewById(R.id.tvTeamNum);
        TextView tvTeamDescription = rowView.findViewById(R.id.tvTeamDescription);

        tvTeamName.setText("Team "+Teams.get(position).gettName());
        tvTeamNum.setText("# of Members: "+Teams.get(position).getNumMembers());
        tvTeamDescription.setText(Teams.get(position).getDescription());
        return rowView;
    }

}