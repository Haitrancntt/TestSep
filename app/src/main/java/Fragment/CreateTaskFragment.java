package Fragment;


import android.app.AlertDialog;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3g = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected() == false & m3g.isConnected() == false) {
            new AlertDialog.Builder(getActivity())
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnExit = (ImageButton) getActivity().findViewById(R.id.imageButton_CreateNewTask);
        spinner = (Spinner) getActivity().findViewById(R.id.spinner);
        editText = (EditText) getActivity().findViewById(R.id.editText3);
        accountID = LoginActivity.Account_Id;
        tag_control = new Tag_Control(LoginActivity.db.getConnection());
        task_control = new Task_Control(LoginActivity.db.getConnection());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadTagInBackGround().execute();
            }
        });
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
                    Toast.makeText(getActivity(), R.string.existed_tag, Toast.LENGTH_SHORT).show();
                } else {
                    if (taskName.equals("")) {
                        Toast.makeText(getActivity(), R.string.null_space, Toast.LENGTH_SHORT).show();
                    } else {
                        boolean b1 = task_control.AddTask(taskName, tagId, accountID);
                        if (b1) {
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, new TaskFragment()).commit();
                            Toast.makeText(getActivity(), "Add Task Successfully", Toast.LENGTH_SHORT).show();
                        }
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

    private class LoadTagInBackGround extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            arrayList = tag_control.LoadList(accountID);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, arrayList);
            spinner.setAdapter(adapter);
        }
    }

}