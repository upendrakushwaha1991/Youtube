package youtube.cpm.com.youtube.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import youtube.cpm.com.youtube.GetterSetter.CustomerDetailsSettrGetter;
import youtube.cpm.com.youtube.R;

public class CustomerViewAdapter extends ArrayAdapter<CustomerDetailsSettrGetter> {
    Context context;
    int layoutResourceId;
    ArrayList<CustomerDetailsSettrGetter> data = new ArrayList<CustomerDetailsSettrGetter>();

    public CustomerViewAdapter(Context context, int layoutResourceId, ArrayList<CustomerDetailsSettrGetter> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list, null);

            holder = new ViewHolder();

            holder.listcotmnameview = (TextView) convertView.findViewById(R.id.listcotmnameview);
            holder.listcontectview = (TextView) convertView.findViewById(R.id.listcontectview);
            holder.listemailview = (TextView) convertView.findViewById(R.id.listemailview);
            holder.c1 = (CardView) convertView.findViewById(R.id.c1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CustomerDetailsSettrGetter customerDetailsSettrGetter = data.get(position);

        holder.listcotmnameview.setText(customerDetailsSettrGetter.getCustomername());
        holder.listcontectview.setText(customerDetailsSettrGetter.getContectnumber());
        holder.listemailview.setText(customerDetailsSettrGetter.getEmailaddress());


        if (customerDetailsSettrGetter.getN().equalsIgnoreCase("Y"))
            holder.c1.setBackgroundColor(Color.parseColor("#b2ff9a"));

        else
            holder.c1.setBackgroundColor(Color.WHITE);

        return convertView;
    }

    static class ViewHolder {
        TextView listcotmnameview;
        TextView listcontectview;
        TextView listemailview;
        CardView c1;


    }
}



