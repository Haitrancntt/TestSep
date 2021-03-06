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
 * A simple {@link Fragment} subclass.
 */
public class CreateNewAccountFragment extends Fragment {
    private EditText txtName, txtEmail, txtPassword;
    private String sName, sEmail, sPassword, sPassEncrypt;
    private TextView txtErrorName, txtErrorEmail, txtErrorPass;
    private ImageButton btnCreate;
    private Encryption encryption;
    private FragmentManager fragmentManager = getFragmentManager();
    private Account_Control account_control;

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
        View view = inflater.inflate(R.layout.fragment_create_new_account, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        encryption = new Encryption();
        txtEmail = (EditText) getActivity().findViewById(R.id.editText_Email);
        txtName = (EditText) getActivity().findViewById(R.id.editText_Name);
        txtPassword = (EditText) getActivity().findViewById(R.id.editText_Password);
        txtErrorName = (TextView) getActivity().findViewById(R.id.textview_errorname);
        txtErrorEmail = (TextView) getActivity().findViewById(R.id.textview_erroremail);
        txtErrorPass = (TextView) getActivity().findViewById(R.id.textview_errorpass);
        btnCreate = (ImageButton) getActivity().findViewById(R.id.imagebutton_Create);
        account_control = new Account_Control(LoginActivity.db.getConnection());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtErrorEmail.setText("");
                txtErrorPass.setText("");
                txtErrorName.setText("");
                sName = txtName.getText().toString().trim();
                sEmail = txtEmail.getText().toString().trim();
                sPassword = txtPassword.getText().toString().trim();
                if (sName.equals("") || sEmail.equals("") || sPassword.equals("")) {
                    if (sName.equals("")) {
                        txtErrorName.setText(R.string.null_space);
                    }
                    if (sEmail.equals("")) {
                        txtErrorEmail.setText(R.string.null_space);
                    }
                    if (sPassword.equals("")) {
                        txtErrorPass.setText(R.string.null_space);
                    }
                } else {
                    try {
                        if (account_control.CheckEmail(sEmail)) {
                            if (account_control.CheckExisted(sEmail) == true) {
                                txtErrorEmail.setText(R.string.existed_email);
                            } else {
                                sPassEncrypt = Encryption.bytesToHex(encryption.encrypt(sPassword));
                                boolean b = account_control.AddNewAccount(sEmail, sName, sPassEncrypt);
                                if (b) {
                                    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, new AccountFragment()).commit();
                                }
                                //Toast.makeText(getContext(), encryption.Encryption(sPassword), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            txtErrorEmail.setText(R.string.error_email_login);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Toast.makeText(getContext(), "Fill all field please", Toast.LENGTH_SHORT).show();
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
                    fm.beginTransaction().replace(R.id.content_frame, new AccountFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }

}
