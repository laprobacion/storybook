package com.read.storybook;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.read.storybook.model.Level;
import com.read.storybook.util.AppConstants;

import java.util.List;

import static android.R.attr.level;

public class CustomAdapter extends BaseAdapter {
    List<Level> levels;
    Activity mainActivity;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Activity mainActivity, List<Level> levels) {
        // TODO Auto-generated constructor stub
        this.levels = levels;
        this.mainActivity = mainActivity;
        inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return levels.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        //holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(levels.get(position).getName().toString());
        //holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(mainActivity, StoriesActivity.class);
                myIntent.putExtra(AppConstants.LEVEL_NAME,levels.get(position));
                mainActivity.startActivity(myIntent);
                //mainActivity.finish();
            }
        });
        return rowView;
    }

}