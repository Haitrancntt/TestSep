package Fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;
import java.util.Calendar;

import Adapter.TimeAdapter;
import Class.Time;
import Class.TimePassData;
import Controller.Task_Control;
import Controller.Time_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeFragment extends Fragment {
    private ImageButton btnCalendar;
    private DatePickerDialog datePickerDialog;
    private ImageButton btnAdd;
    private TextView txtTime;
    private String sDate;
    private String sNametag;
    private String sNameTask;
    private int accountID, iExTaskId, iStatus;
    private Time_Control time_control;
    private Task_Control task_control;
    private ArrayList<Time> arrayList;
    private TimeAdapter adapter;
    private ListView listView;
    private Calendar calendar;
    private int mMonth, mYear, mDay, iHour, iMinute;
    private EditText editTextNameTask, editTextNameTag, editTextDate, editTextEstimate;
    private NumberPicker numberPickerHour, numberPickerMinute;
    // private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MAPPING FROM .XML
        btnCalendar = (ImageButton) getActivity().findViewById(R.id.imageButtonTime_Calendar);
        btnAdd = (ImageButton) getActivity().findViewById(R.id.imageButtonTime);
        txtTime = (TextView) getActivity().findViewById(R.id.textViewTime);
        listView = (ListView) getActivity().findViewById(R.id.listView);
        txtTime.setText("");

        //CONNECT
        time_control = new Time_Control(LoginActivity.db.getConnection());
        task_control = new Task_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR); // current year
        mMonth = calendar.get(Calendar.MONTH); // current month
        mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
        txtTime.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        sDate = mYear + "-" + (mMonth + 1) + "-" + mDay;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadListinBackGround().execute();

            }
        });

        //CHOOSE TASK WANT TO ADD TIME
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ChooseTaskFragment()).commit();

            }
        });

        //CHOOSE ONE DAY FOR DISPLAY TASK
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtTime.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);
                        sDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new LoadListinBackGround().execute();
                            }
                        });
                        // LoadList(accountID, sDate);
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        //CHOOSE ONE ITEM FOR DISPLAY RUN TASK
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sNameTask = arrayList.get(i).getTaskName();
                String sNameTag = arrayList.get(i).getTagName();
                String sStart = arrayList.get(i).getStart();
                String sEnd = arrayList.get(i).getEnd();
                int iId = arrayList.get(i).getId();
                int iEsMinute = arrayList.get(i).getEsMin();
                int iEsHour = arrayList.get(i).getEsHour();
                int iRun = arrayList.get(i).getRun();
                int iDone = arrayList.get(i).getDone();
                if (iDone != 1) {
                    Bundle bPassData = new Bundle();
                    TimePassData tPassData = new TimePassData(iId, sNameTag, sNameTask, sStart, sEnd, iEsMinute, iEsHour, iRun, iDone);
                    bPassData.putSerializable("time", tPassData);
                    RunTaskFragment runTaskFragment = new RunTaskFragment();
                    runTaskFragment.setArguments(bPassData);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, runTaskFragment).commit();
                } else {
                    Toast.makeText(getActivity(), R.string.runnable, Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerForContextMenu(listView);
    }

    //CREATE CONTEXT MENU
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == listView.getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(arrayList.get(info.position).getTaskName());

            sNametag = arrayList.get(info.position).getTagName();
            iExTaskId = arrayList.get(info.position).getId();
            sNameTask = arrayList.get(info.position).getTaskName();
            iHour = arrayList.get(info.position).getEsHour();
            iMinute = arrayList.get(info.position).getEsMin();
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }


    // SHOW POP UP FOR DELETE
    public void AlertDelete(String title) throws Exception {
        final android.support.v7.app.AlertDialog.Builder alertdelete = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertdelete.setTitle(title);
        iStatus = time_control.SelectStatus(iExTaskId);
        alertdelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (iStatus == 0) {
                    time_control.DeleteExecutedTask(iExTaskId);
                    LoadList();
                    Toast.makeText(getContext(), R.string.delete_tag_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.runnable, Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertdelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = alertdelete.create();
        alertDialog.show();
    }

    // SHOW POP UP FOR EDIT
    public void AlertEdit(String title) throws Exception {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertView = inflater.inflate(R.layout.dialog_edit_time, null);

        // GET NAME
        editTextNameTask = (EditText) alertView.findViewById(R.id.editTaskName_Time);
        editTextNameTag = (EditText) alertView.findViewById(R.id.editTag_Time);
        editTextDate = (EditText) alertView.findViewById(R.id.edit_Date);
        numberPickerHour = (NumberPicker) alertView.findViewById(R.id.picker_Hour);
        numberPickerMinute = (NumberPicker) alertView.findViewById(R.id.picker_Minute);
        iStatus = time_control.SelectStatus(iExTaskId);
        //SET VALUE
        editTextNameTask.setText(sNameTask);
        editTextNameTag.setText(sNametag);
        editTextDate.setText(sDate);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(8);
        numberPickerMinute.setMinValue(00);
        numberPickerMinute.setMaxValue(59);

        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(title);
        alert.setView(alertView);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (iStatus == 0) {
                    time_control.EditExecutedTask(iExTaskId, numberPickerHour.getValue() * 60 + numberPickerMinute.getValue());
                    Toast.makeText(getContext(), R.string.edit_tag_success, Toast.LENGTH_LONG).show();
                    LoadList();
                } else {
                    Toast.makeText(getContext(), R.string.runnable, Toast.LENGTH_SHORT).show();
                }
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

    // EVENT FOR CLICK ITEM CONTEXT
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        // String sEdit = arrayList.get(info.position).getName();
        String[] menuItems = getResources().getStringArray(R.array.menu);

        try {
            //GET TASK ID
            //  taskId = task_control.GetTaskIDByName(sEdit, accountID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (menuItemIndex) {
            case 0:
                try {
                    AlertEdit(menuItems[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    AlertDelete(menuItems[1]);
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

    //LOAD BACKGROUND
    private class LoadListinBackGround extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            arrayList = time_control.LoadTime(accountID, sDate);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new TimeAdapter(getActivity(), R.layout.layout_time, arrayList);
            listView.setAdapter(adapter);
        }
    }

    //LOAD LIST
    public void LoadList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadListinBackGround().execute();
            }
        });
    }

}
