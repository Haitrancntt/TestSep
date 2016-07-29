package Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haitr.planed_12062016.Encryption;
import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import Controller.Account_Control;

/**
 * Created by Thanh Huy on 7/28/2016.
 */
public class ChangePasswordFragment extends Fragment {
    private EditText current, txtnewPass, confrmPass;
    private Account_Control account_control;
    private int accountID;
    private Encryption encryption;
    private String oldPass, newPass, newPass2;
    private ImageButton btnSave;
    private TextView lblold, lblnew, lblnew2;

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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        current = (EditText) getActivity().findViewById(R.id.currentPW);
        txtnewPass = (EditText) getActivity().findViewById(R.id.newPW);
        confrmPass = (EditText) getActivity().findViewById(R.id.confrPW);
        btnSave = (ImageButton) getActivity().findViewById(R.id.imageButton);
        lblold = (TextView) getActivity().findViewById(R.id.textView39);
        lblnew = (TextView) getActivity().findViewById(R.id.textView40);
        lblnew2 = (TextView) getActivity().findViewById(R.id.textView41);
        encryption = new Encryption();
        accountID = LoginActivity.Account_Id;
        account_control = new Account_Control(LoginActivity.db.getConnection());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblnew2.setText("");
                lblold.setText("");
                lblnew.setText("");
                if (current.getText().toString().equals("")) {
                } else if (txtnewPass.getText().toString().equals("")) {
                } else if (txtnewPass.getText().toString().equals("")) {
                } else {
                    oldPass = current.getText().toString();
                    newPass = txtnewPass.getText().toString();
                    newPass2 = confrmPass.getText().toString();
                    try {
                        String enPass = Encryption.bytesToHex(encryption.encrypt(oldPass));
                        if (enPass.equals(account_control.GetPassword(accountID))) {
                            if (newPass.equals(oldPass)) {
                                lblnew.setText("Equal old password");
                            } else {
                                if (newPass.equals(newPass2)) {
                                    String newpassEn = Encryption.bytesToHex(encryption.encrypt(newPass));
                                    boolean b = account_control.ChangePassword(accountID, newpassEn);
                                    if (b) {
                                        getFragmentManager().beginTransaction().replace(R.id.content_frame, new AccountFragment()).commit();
                                        Toast.makeText(getActivity(), "Changed Password Successfully", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    lblnew2.setText("Not equal");
                                }
                            }
                        } else {
                            lblold.setText("Current password is wrong");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //ON BACKED PRESS
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
                    fm.beginTransaction().replace(R.id.content_frame, new AccountFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}

