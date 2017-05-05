package com.example.root.newapplayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by root on 23/4/17.
 */

public class DeviceDetailsPopulator extends BaseAdapter {

    private Context context;
    private final ArrayList ids;
    private final ArrayList details;
    private String curr_name;
    int dist_size=0;

    public DeviceDetailsPopulator(DeviceDetails dd, ArrayList ids, ArrayList details, String sname)
    {
        this.context = dd;
        this.ids = ids;
        this.details = details;
        this.curr_name = sname;
    }


    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem;

        if (convertView == null) {
            listViewItem = new View(context);
            listViewItem = inflater.inflate(R.layout.list_dev_details,null);

            TextView id = (TextView)listViewItem.findViewById(R.id.id);
            TextView detail = (TextView)listViewItem.findViewById(R.id.details);

            id.setText((CharSequence) ids.get(position));
            detail.setText((CharSequence) details.get(position));
        }
        else
            listViewItem = convertView;
        return listViewItem;

    }
}
