package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Adapter.TaskAdapter;
import Class.Task;
import Controller.Task_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    private ImageButton btnadd;
    private ArrayList<Task> arrayList;
    private TaskAdapter adapter;
    private ListView listView;
    private Task_Control task_control;
    private int accountID;

    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnadd = (ImageButton) getActivity().findViewById(R.id.imageButtonTask);
        listView = (ListView) getActivity().findViewById(R.id.listview_Task);
        task_control = new Task_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        LoadList();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateTaskFragment()).commit();

            }
        });
    }

    public void LoadList() {
        arrayList = task_control.LoadList(accountID);
        adapter = new TaskAdapter(getActivity(), R.layout.layout_task, arrayList);
        listView.setAdapter(adapter);
    }
}
