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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;
import java.util.Calendar;

import Adapter.TimeAdapter;
import Class.Time;
import Class.TimePassData;
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
    private int accountID;
    private Time_Control time_control;
    private ArrayList<Time> arrayList;
    private TimeAdapter adapter;
    private ListView listView;
    private Calendar calendar;
    private int mMonth, mYear, mDay;
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
        btnCalendar = (ImageButton) getActivity().findViewById(R.id.imageButtonTime_Calendar);
        btnAdd = (ImageButton) getActivity().findViewById(R.id.imageButtonTime);
        txtTime = (TextView) getActivity().findViewById(R.id.textViewTime);
        txtTime.setText("");
        listView = (ListView) getActivity().findViewById(R.id.listView);
        time_control = new Time_Control(LoginActivity.db.getConnection());
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


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ChooseTaskFragment()).commit();

            }
        });


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
                    // mListener.onFragmentInteraction(sNameTask, sNameTag, sStart, sEnd);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, runTaskFragment).commit();
                } else {
                    Toast.makeText(getActivity(), R.string.runnable, Toast.LENGTH_SHORT).show();
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
                    fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }*/
/*
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String nameTask, String nameTag, String Start, String End);
    }*/

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


}
