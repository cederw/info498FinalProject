package com.info498.bestgroup.tindar;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public CustomArrayAdapter(Context context, ArrayList<String> values) {
        super(context, android.R.layout.simple_list_item_1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView rowView = (TextView)inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        // Customization to your textView here
        Typeface robotoFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

        String lines[] = values.get(position).split("\\r?\\n");

//        String s = "Bluetooth Name: " + lines[0] + "\r\n"+ "MAC: " + lines[1];
//
//        rowView.setText(s);
        rowView.setText(values.get(position));
        rowView.setTypeface(robotoFont);
        rowView.setTextSize(14);

        return rowView;
    }
}