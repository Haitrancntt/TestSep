package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Adapter.TaskAdapter;
import Class.Task;
import Controller.Task_Control;

/**
 * Created by Thanh Huy on 7/23/2016.
 */
public class ChooseTaskFragment extends Fragment {
    private ArrayList<Task> arrayList;
    private Task_Control task_control;
    private int accountID;
    private TaskAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_task_for_time, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        task_control = new Task_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        listView = (ListView) getActivity().findViewById(R.id.listTaskChoosing);
        arrayList = task_control.LoadList(accountID);
        adapter = new TaskAdapter(getActivity(), R.layout.layout_task, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = arrayList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                CreateTimeFragment fragment = new CreateTimeFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });
    }
}
