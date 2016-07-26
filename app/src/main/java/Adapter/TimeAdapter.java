package Adapter;

import android.content.Context;
import android.graphics.Color;
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

    public TimeAdapter(Context context, int layout, ArrayList<Time> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

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
        TextView txtEsHour = (TextView) convertView.findViewById(R.id.iEsHour);
        TextView txtEsMin = (TextView) convertView.findViewById(R.id.iEsMin);
        TextView txtAcHour = (TextView) convertView.findViewById(R.id.acHour);
        TextView txtAcMin = (TextView) convertView.findViewById(R.id.acMin);
        TextView txtStart = (TextView) convertView.findViewById(R.id.startTime);
        TextView txtEnd = (TextView) convertView.findViewById(R.id.endTime);
        TextView txtId = (TextView) convertView.findViewById(R.id.id);
        TextView txtRun = (TextView) convertView.findViewById(R.id.run);
        TextView txtDone = (TextView) convertView.findViewById(R.id.done);
        // dau -
        TextView txt = (TextView) convertView.findViewById(R.id.textView12);
        //
        txtTag.setText(arrayList.get(position).getTagName());
        txtTask.setText(arrayList.get(position).getTaskName());
        txtEsHour.setText(String.format("%02d", arrayList.get(position).getEsHour()));
        txtEsMin.setText(String.format("%02d", arrayList.get(position).getEsMin()));
        txtAcHour.setText(String.format("%02d", arrayList.get(position).getAcHour()));
        txtAcMin.setText(String.format("%02d", arrayList.get(position).getAcMin()));
        /*txtEsHour.setText(String.valueOf(arrayList.get(position).getiEsHour()));
        txtEsMin.setText(String.valueOf(arrayList.get(position).getiEsMin()));
        txtAcHour.setText(String.valueOf(arrayList.get(position).getAcHour()));
        txtAcMin.setText(String.valueOf(arrayList.get(position).getAcMin()));*/
        txtStart.setText(arrayList.get(position).getStart());
        txtEnd.setText(arrayList.get(position).getEnd());
        //INVISIBLE ID
        txtId.setText(String.valueOf(arrayList.get(position).getId()));
        txtId.setVisibility(View.INVISIBLE);
        //INVISIBLE RUNNING STATUS
        txtRun.setText(String.valueOf(arrayList.get(position).getRun()));
        txtRun.setVisibility(View.INVISIBLE);
        if (txtRun.getText().toString().equals("1")) {
            txtTag.setTextColor(Color.rgb(198,34,34));
            txt.setTextColor(Color.rgb(198,34,34));
            txtTask.setTextColor(Color.rgb(198,34,34));
        }
        txtDone.setText(String.valueOf(arrayList.get(position).getDone()));
        txtDone.setVisibility(View.INVISIBLE);
        if (txtDone.getText().toString().equals("1")) {
            txtTag.setTextColor(Color.rgb(16,254,241));
            txt.setTextColor(Color.rgb(16,254,241));
            txtTask.setTextColor(Color.rgb(16,254,241));
        }
        return convertView;
    }
}
