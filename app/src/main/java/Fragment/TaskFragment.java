package Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.KeyEvent;
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
import android.widget.SpinnerAdapter;
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
    private int accountID, tagId, taskId, tagIdCurrent, iStatus;
    private Adapter sAdapter;
    private String sSelectedItem, sNametag;
    private Spinner spinner;
    private EditText editText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() == false & m3g.isConnected() == false) {
            new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage("Please make sure that your device is already connected to the Internet!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finishAffinity();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnadd = (ImageButton) getActivity().findViewById(R.id.imageButtonTag);
        listView = (ListView) getActivity().findViewById(R.id.listview_Task);
        task_control = new Task_Control(LoginActivity.db.getConnection());
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        registerForContextMenu(listView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadList();
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadTaskInBackGround().execute();
            }
        });
    }

    // CREATE CONTEXT MENU FOR DELETE / EDIT
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == listView.getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(arrayList.get(info.position).getName());
            sNametag = arrayList.get(info.position).getTag_name();
            // tagId = tag_control.GetTagId(arrayList.get(info.position).getTag_name(), accountID);
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    /**
     * @param item
     * @param sEdit // name of task
     * @param ID    // id of task
     * @param TagID // id of tag
     */
    public void AlertEdit(String item, final String sEdit, int ID, int TagID) throws Exception {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView = inflater.inflate(R.layout.dialog_edit, null);

        // GET NAME
        editText = (EditText) alertView.findViewById(R.id.editTag);
        spinner = (Spinner) alertView.findViewById(R.id.spinner);
        editText.setText(sEdit);

        //GET TASK ID
        taskId = task_control.GetTaskIDByName(sEdit, accountID);

        //GET VALUE INTO SPINNER
        stringArrayList = tag_control.LoadList(accountID);
        tagIdCurrent = stringArrayList.indexOf(sNametag);
        sAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringArrayList);
        spinner.setAdapter((SpinnerAdapter) sAdapter);
        spinner.setSelection(tagIdCurrent);
        //sSelectedItem = spinner.getSelectedItem().toString();
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(item);
        alert.setView(alertView);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tagId = tag_control.GetTagId(spinner.getSelectedItem().toString(), accountID);
                task_control.EditTask(editText.getText().toString(), taskId, tagId);
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
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    // SHOW POP UP FOR EDIT TASK

    // SHOW POP UP FOR DELETE
    public void AlertDelete(String title, String sNameTask, int ID) throws Exception {
        final AlertDialog.Builder alertdelete = new AlertDialog.Builder(getContext());
        alertdelete.setTitle(title);
        taskId = task_control.GetTaskIDByName(sNameTask, accountID);
        iStatus = task_control.SelectStatus(taskId);
        alertdelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (iStatus == 0) {
                    task_control.DeleteTag(taskId);
                    LoadList();
                    Toast.makeText(getContext(), R.string.delete_tag_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Not Permission", Toast.LENGTH_SHORT).show();
                }
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
                    AlertDelete(menuItems[1], sEdit, taskId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
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
                    fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private class LoadTaskInBackGround extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            arrayList = task_control.LoadList(accountID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new TaskAdapter(getActivity(), R.layout.layout_task, arrayList);
            listView.setAdapter(adapter);
        }
    }
}
