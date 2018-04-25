package com.example.christophergu.pg.data.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.christophergu.pg.R;
import com.example.christophergu.pg.data.Account;

import java.util.List;

public class AccountArrayAdapter extends ArrayAdapter<Account> {
    private final List<Account> accounts;
    private final Context context;

    public AccountArrayAdapter(@NonNull Context context, int resource, @NonNull List<Account> accounts) {
        super(context, resource, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_player_list, parent, false);
        TextView tvUserName = rowView.findViewById(R.id.tvPlayerUserName);



        tvUserName.setText(accounts.get(position).getUsername());

        return rowView;
    }

}
