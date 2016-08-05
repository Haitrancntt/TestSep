package Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

/**
 * Created by Thanh Huy on 8/4/2016.
 */
public class ChooseReportFragment extends Fragment {
    int mYear, mMonth, mDay;
    private RadioButton btndate, btnweek, btnmonth, btnmany;
    private EditText editText;
    private Spinner spFrom, spTo;
    private TextView txt;
    private Button btnGo;
    private ImageButton btnCal;
    private RadioGroup group;
    private int iMode = 1;
    private String[] sMonth = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private ArrayAdapter adapter;
    private String sDate;
    private int accountID;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choosetype, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        group = (RadioGroup) getActivity().findViewById(R.id.radio_group);
        btndate = (RadioButton) getActivity().findViewById(R.id.radio_date);
        btnweek = (RadioButton) getActivity().findViewById(R.id.radio_week);
        btnmonth = (RadioButton) getActivity().findViewById(R.id.radio_month);
        btnmany = (RadioButton) getActivity().findViewById(R.id.radio_many_months);
        editText = (EditText) getActivity().findViewById(R.id.editText);
        btnCal = (ImageButton) getActivity().findViewById(R.id.imageButton2);
        spFrom = (Spinner) getActivity().findViewById(R.id.spinner2);
        spTo = (Spinner) getActivity().findViewById(R.id.spinner3);
        txt = (TextView) getActivity().findViewById(R.id.txtTO);
        btnGo = (Button) getActivity().findViewById(R.id.buttonGO);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sMonth);
        spFrom.setAdapter(adapter);
        spTo.setAdapter(adapter);
        accountID = LoginActivity.Account_Id;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                             @Override
                                             public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                 switch (checkedId) {
                                                     case R.id.radio_date:
                                                         iMode = 1;
                                                         editText.setVisibility(View.VISIBLE);
                                                         btnCal.setVisibility(View.VISIBLE);
                                                         spFrom.setVisibility(View.GONE);
                                                         txt.setVisibility(View.GONE);
                                                         spTo.setVisibility(View.GONE);
                                                         break;
                                                     case R.id.radio_week:
                                                         iMode = 2;
                                                         editText.setVisibility(View.VISIBLE);
                                                         btnCal.setVisibility(View.VISIBLE);
                                                         spFrom.setVisibility(View.GONE);
                                                         txt.setVisibility(View.GONE);
                                                         spTo.setVisibility(View.GONE);
                                                         break;
                                                     case R.id.radio_month:

                                                         iMode = 3;
                                                         editText.setVisibility(View.GONE);
                                                         btnCal.setVisibility(View.GONE);
                                                         spFrom.setVisibility(View.VISIBLE);
                                                         txt.setVisibility(View.GONE);
                                                         spTo.setVisibility(View.GONE);
                                                         break;
                                                     case R.id.radio_many_months:

                                                         iMode = 3;
                                                         editText.setVisibility(View.GONE);
                                                         btnCal.setVisibility(View.GONE);
                                                         spFrom.setVisibility(View.VISIBLE);
                                                         txt.setVisibility(View.VISIBLE);
                                                         spTo.setVisibility(View.VISIBLE);
                                                         break;
                                                 }
                                             }
                                         }

        );
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                switch (iMode) {
                    case 1:
                        fm.beginTransaction().replace(R.id.content_frame, new DailyReportFragment()).commit();
                        break;
                }
            }
        });

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);
                        String sDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        // LoadList(accountID, sDate);
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

    }
}
