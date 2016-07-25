package Fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import java.util.ArrayList;
import java.util.Calendar;

import Adapter.TimeAdapter;
import Class.Time;
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
    public TimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_time, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAdd = (ImageButton) getActivity().findViewById(R.id.imageButtonTime);
        txtTime = (TextView) getActivity().findViewById(R.id.textViewTime);
        txtTime.setText("");
        listView = (ListView) getActivity().findViewById(R.id.listView);
        time_control = new Time_Control(LoginActivity.db.getConnection());
        accountID = LoginActivity.Account_Id;
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ChooseTaskFragment()).commit();

            }
        });

        btnCalendar = (ImageButton) getActivity().findViewById(R.id.imageButtonTime_Calendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR); // current year
                int mMonth = calendar.get(Calendar.MONTH); // current month
                int mDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtTime.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);
                        sDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        LoadList(accountID, sDate);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

    }

 /*   //CLICK CALENDAR
    public void onDateClicked(View v) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDate.set(Calendar.YEAR, year);
                mDate.set(Calendar.MONTH, monthOfYear);
                mDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };

        new DatePickerDialog(getContext(), mDateListener,
                mDate.get(Calendar.YEAR),
                mDate.get(Calendar.MONTH),
                mDate.get(Calendar.DAY_OF_MONTH)).show();

    }*/

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

    public void LoadList(int accId, String date) {
        arrayList = time_control.LoadTime(accId, date);
        adapter = new TimeAdapter(getActivity(), R.layout.layout_time, arrayList);
        listView.setAdapter(adapter);

    }
}
