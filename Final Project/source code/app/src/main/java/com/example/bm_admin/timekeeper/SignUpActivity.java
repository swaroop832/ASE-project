package com.example.bm_admin.timekeeper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.RegisterBean;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.Utils;
import com.example.bm_admin.timekeeper.utility.Validation;

import java.util.ArrayList;

/**
 * Created by bm-admin on 25/3/17.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText nameET, emailET, pwdET;
    private Button regButton;
    private TextView loginView;
    private DBHandler dbHandler;
    private Activity activity;

    public SignUpActivity() {
        dbHandler = DBHandler.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        nameET = (EditText) findViewById(R.id.reg_name);
        emailET = (EditText) findViewById(R.id.reg_email);
        pwdET = (EditText) findViewById(R.id.reg_password);
        regButton = (Button) findViewById(R.id.reg_signup);
        loginView = (TextView) findViewById(R.id.login1);
        activity = this;

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);

                if (Validation.isNonEmpty(nameET) && Validation.isValidEmailAddress(emailET) && Validation.isValidPassword(pwdET)) {
                    if (!checkUserIsExist(emailET.getText().toString())) {
                        insertRegisterData();
                    } else {
                        Snackbar.make(view, "Already registered", Snackbar.LENGTH_LONG).show();
                        //  Toast.makeText(getApplicationContext(), "Already registered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void insertRegisterData() {

        try {
            RegisterBean registerBean = new RegisterBean();
            registerBean.setEmail(emailET.getText().toString());
            registerBean.setName(nameET.getText().toString());
            registerBean.setPassword(pwdET.getText().toString());

            if (dbHandler.insertHandler(AppConfig.REGISTERTB, Converter.gson.toJson(registerBean), getApplicationContext())) {
                //System.out.println("Insert success");
                Snackbar.make(regButton, "Successful", Snackbar.LENGTH_LONG).show();
                // Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                clearFields();
                closeActivity();
            } else {
                //    System.out.println("Insert failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkUserIsExist(String email) {
        try {
            if (getRegisterUsers() != null) {
                // System.out.println("TST USRS 2:"+getRegisterUsers().size());
                for (RegisterBean bean : getRegisterUsers()) {
                    //   System.out.println("TST USRS 3:"+bean.getEmail());
                    if (bean.getEmail().equalsIgnoreCase(email)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private ArrayList<RegisterBean> getRegisterUsers() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.REGISTERTB, null, null,
                    null, null, false, false, false, getApplicationContext());
            //  System.out.println("TST USRS 1:"+dbStr);
            return Converter.convertJsonToRegisterBeans(dbStr);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void clearFields() {
        nameET.setText("");
        emailET.setText("");
        pwdET.setText("");
    }

    private void closeActivity() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
