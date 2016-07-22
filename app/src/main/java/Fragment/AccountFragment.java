package Fragment;


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
        account_control = new Account_Control(LoginActivity.db.getConnection());
        iAccountId = LoginActivity.Account_Id;
        iPermission = account_control.GetPermission(iAccountId);
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
