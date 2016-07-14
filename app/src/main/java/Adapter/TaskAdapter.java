package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Class.Task;

/**
 * Created by Thanh Huy on 7/14/2016.
 */
public class TaskAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Task> arrayList;

    public TaskAdapter(Context context, int layout, ArrayList<Task> arrayList) {
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
        convertView = inflater.inflate(R.layout.layout_task, null);
        TextView txtName = (TextView) convertView.findViewById(R.id.textView9);
        txtName.setText(arrayList.get(position).getName());
        TextView txtTagName = (TextView) convertView.findViewById(R.id.textView10);
        txtTagName.setText(arrayList.get(position).getTag_name());
        return convertView;
    }
}
