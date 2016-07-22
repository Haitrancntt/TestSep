package Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Controller.Tag_Control;
import Controller.Task_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTaskFragment extends Fragment {
    private ImageButton btnExit;
    private Spinner spinner;
    private int accountID;
    private EditText editText;
    private Tag_Control tag_control;
    private Task_Control task_control;
    private ArrayList<String> arrayList;
    private ArrayAdapter adapter;

    public CreateTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnExit = (ImageButton) getActivity().findViewById(R.id.imageButton_CreateNewTask);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        editText = (EditText) getActivity().findViewById(R.id.editText3);
        accountID = LoginActivity.Account_Id;
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        task_control = new Task_Control(LoginActivity.db.getConnection());
        arrayList = tag_control.LoadList(accountID);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner.setAdapter(adapter);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = editText.getText().toString();
                String tagName = spinner.getSelectedItem().toString();
                int tagId = 0;
                try {
                    tagId = tag_control.GetTagIDByName(tagName, accountID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boolean b = task_control.CheckExistedTask(accountID, taskName);
                if (b) {
                    // do something
                } else {
                    boolean b1 = task_control.AddTask(taskName, tagId, accountID);
                    if (b1) {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new TaskFragment()).commit();
                        Toast.makeText(getActivity(), "Add Task Successfully", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    //ON BACK PRESSED
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, new TaskFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

}