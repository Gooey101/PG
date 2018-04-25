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

import java.util.List;

public class GameArrayAdapter extends ArrayAdapter<Game> {
    private final List<Game> Games;
    private final Context context;

    public GameArrayAdapter(@NonNull Context context, int resource, @NonNull List<Game> Games) {
        super(context, resource, Games);
        this.context = context;
        this.Games = Games;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_game_list, parent, false);
        TextView tvSport = rowView.findViewById(R.id.tvSport);
        TextView tvDate = rowView.findViewById(R.id.tvDate);

        tvSport.setText(Games.get(position).getSport());
        tvDate.setText(Games.get(position).getGameDate().substring(0,10));

        return rowView;
    }

}