package Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;

import Adapter.TaskAdapter;
import Class.Task;
import Controller.Tag_Control;
import Controller.Task_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {
    private ImageButton btnadd;
    private ArrayList<Task> arrayList;
    private ArrayList<String> stringArrayList;
    private TaskAdapter adapter;
    private ListView listView;
    private Task_Control task_control;
    private Tag_Control tag_control;
    private int accountID, tagId, taskId;
    private Adapter sAdapter;
    private Task task;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tagId = tag_control.GetTagId(arrayList.get(i).getTag_name(), accountID);

            }
        });*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnadd = (ImageButton) getActivity().findViewById(R.id.imageButtonTask);
        listView = (ListView) getActivity().findViewById(R.id.listview_Task);
        task_control = new Task_Control(LoginActivity.db.getConnection());
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        LoadList();
        registerForContextMenu(listView);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateTaskFragment()).commit();

            }
        });
    }

    //LOAD LIST
    public void LoadList() {
        arrayList = task_control.LoadList(accountID);
        adapter = new TaskAdapter(getActivity(), R.layout.layout_task, arrayList);
        listView.setAdapter(adapter);
    }

    // CREATE CONTEXT MENU FOR DELETE / EDIT
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == listView.getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(arrayList.get(info.position).getName());
            tagId = tag_control.GetTagId(arrayList.get(info.position).getTag_name(), accountID);
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    // SHOW POP UP FOR EDIT TASK

    /**
     * @param item
     * @param sEdit // name of task
     * @param ID    // id of task
     * @param TagID // id of tag
     */
    public void AlertEdit(String item, final String sEdit, int ID, int TagID) throws Exception {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(item);
        // GET NAME
        final EditText EditText = new EditText(getContext());
        EditText.setText(sEdit);
        alert.setView(EditText);
        //GET TAG ID

        //GET TASK ID
        taskId = task_control.GetTaskIDByName(sEdit, accountID);
        //CREATE SPINNER
        AlertDialog alertDialog = alert.create();
       Spinner spinner = (Spinner) alertDialog.findViewById(R.id.Spinner);
        stringArrayList = tag_control.LoadList(accountID);
        sAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList);
        spinner.setAdapter(adapter);
        alert.setView(spinner);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //tag_control.EditTag(EditText.getText().toString(), tagId);
                task_control.EditTask(EditText.getText().toString(), taskId, tagId);
                Toast.makeText(getContext(), R.string.edit_tag_success, Toast.LENGTH_LONG).show();
                LoadList();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    // SHOW POP UP FOR DELETE
    public void AlertDelete(String title, String sNameTask, int ID) throws Exception {
        final AlertDialog.Builder alertdelete = new AlertDialog.Builder(getContext());
        alertdelete.setTitle(title);
        taskId = task_control.GetTaskIDByName(sNameTask, accountID);
        alertdelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task_control.DeleteTag(taskId);
                LoadList();
                Toast.makeText(getContext(), R.string.delete_tag_success, Toast.LENGTH_SHORT).show();
            }
        });
        alertdelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertdelete.create();
        alertDialog.show();
    }

    // EVENT FOR CLICK ITEM CONTEXT
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String sEdit = arrayList.get(info.position).getName();
        String[] menuItems = getResources().getStringArray(R.array.menu);

        try {
            //GET TAG ID
            // tagId = tag_control.GetTagId(sEdit, accountID);
            //GET TASK ID
            taskId = task_control.GetTaskIDByName(sEdit, accountID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (menuItemIndex) {
            case 0:
                try {
                    AlertEdit(menuItems[0], sEdit, taskId, tagId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    AlertDelete(menuItems[1], sEdit,taskId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }
}
