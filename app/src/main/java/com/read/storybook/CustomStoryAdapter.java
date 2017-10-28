package com.read.storybook;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.read.storybook.model.Level;
import com.read.storybook.model.Story;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;

import org.json.JSONObject;

import java.util.List;

public class CustomStoryAdapter extends BaseAdapter {
    List<Story> stories;
    Activity mainActivity;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public CustomStoryAdapter(Activity mainActivity, List<Story> stories) {
        // TODO Auto-generated constructor stub
        this.stories = stories;
        this.mainActivity = mainActivity;
        inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return stories.size();
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
        rowView = inflater.inflate(R.layout.list_story_row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img = (ImageView) rowView.findViewById(R.id.default_icon);
        holder.img.setImageBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
                R.drawable.icons_open_book));
        //holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(stories.get(position).getTitle().toString());
        //holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(mainActivity, StoryActivity.class);
                myIntent.putExtra(AppConstants.STORY_OBJ,stories.get(position));
                mainActivity.startActivity(myIntent);
                //mainActivity.finish();
            }
        });
        if(AppCache.getInstance().getUser().isAdmin()){
            rowView.setLongClickable(true);
            rowView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    Service service = new Service("Deleting Story...", mainActivity, new ServiceResponse() {
                        @Override
                        public void postExecute(JSONObject resp) {
                        }
                    });
                    StoryService.delete(mainActivity, stories.get(position).getId(), service);
                    return true;
                }
            });
        }

        return rowView;
    }

}
