package Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;

import Controller.Account_Control;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private ImageButton imgbutton_CreateAccount, imgbutton_ChangePass, imgbutton_ResetPass;
    private FragmentManager fragmentManager;
    private Account_Control account_control;
    private int iPermission, iAccountId;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        } else {
            account_control = new Account_Control(LoginActivity.db.getConnection());
            iAccountId = LoginActivity.Account_Id;
            iPermission = LoginActivity.iPermission;
            imgbutton_CreateAccount = (ImageButton) getActivity().findViewById(R.id.imagebutton_createaccount);
            imgbutton_ResetPass = (ImageButton) getActivity().findViewById(R.id.imagebutton_resetpass);
            imgbutton_ChangePass = (ImageButton) getActivity().findViewById(R.id.imagebutton_changepass);
            imgbutton_CreateAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iPermission == 1) {
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new CreateNewAccountFragment()).commit();
                    } else {
                        Toast.makeText(getContext(), "You are not allowed to do this\nPlease contact your Administator", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            imgbutton_ResetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iPermission == 1) {
                        fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new ResetPassFragment()).commit();
                    } else {
                        Toast.makeText(getContext(), "You are not allowed to do this\nPlease contact your Administator", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            imgbutton_ChangePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
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
                    fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
                    return true;
                }
                return false;
            }
        });
    }
}
