package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Class.Time;

/**
 * Created by Thanh Huy on 7/22/2016.
 */
public class TimeAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Time> arrayList;

    @Override
    public int getCount() {
        return arrayList.size();
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
        convertView = inflater.inflate(R.layout.layout_time, null);
        TextView txtTag = (TextView) convertView.findViewById(R.id.tagName);
        TextView txtTask = (TextView) convertView.findViewById(R.id.taskName);
        TextView txtEsHour = (TextView) convertView.findViewById(R.id.esHour);
        TextView txtEsMin = (TextView) convertView.findViewById(R.id.esMin);
        TextView txtAcHour = (TextView) convertView.findViewById(R.id.acHour);
        TextView txtAcMin = (TextView) convertView.findViewById(R.id.acMin);
        TextView txtStart = (TextView) convertView.findViewById(R.id.startTime);
        TextView txtEnd = (TextView) convertView.findViewById(R.id.endTime);
        txtTag.setText(arrayList.get(position).getTagName());
        txtTask.setText(arrayList.get(position).getTaskName());
        txtEsHour.setText(arrayList.get(position).getEsHour());
        txtEsMin.setText(arrayList.get(position).getEsMin());
        txtAcHour.setText(arrayList.get(position).getAcHour());
        txtAcMin.setText(arrayList.get(position).getAcMin());
        txtStart.setText(arrayList.get(position).getStart());
        txtEnd.setText(arrayList.get(position).getEnd());
        return convertView;
    }
}
