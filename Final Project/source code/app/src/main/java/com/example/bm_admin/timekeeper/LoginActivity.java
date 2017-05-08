package com.example.bm_admin.timekeeper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.bean.Converter;
import com.example.bm_admin.timekeeper.bean.RegisterBean;
import com.example.bm_admin.timekeeper.sqlite.DBHandler;
import com.example.bm_admin.timekeeper.sqlite.DatabaseHelper;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.PreferenceManager;
import com.example.bm_admin.timekeeper.utility.Utils;
import com.example.bm_admin.timekeeper.utility.Validation;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private Button login, loginGoogle, loginFacebook;
    private TextView createaccount, forgot_password;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private EditText userName, password;
    private DatabaseHelper dbHelper;
    private AppConfig appConfig;
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;
    private Activity activity;
    private DBHandler dbHandler;
    private boolean mShouldResolve = false;
    private String TAG;
    private ProgressDialog progressDialog;

    public LoginActivity() {
        appConfig = AppConfig.getInstance();
        dbHandler = DBHandler.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);

        //  AppEventsLogger.activateApp(this);

        login = (Button) findViewById(R.id.login);
        createaccount = (TextView) findViewById(R.id.createaccount);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        loginGoogle = (Button) findViewById(R.id.loginGoogle);
        loginFacebook = (Button) findViewById(R.id.loginFacebook);
        loginButton = (LoginButton) findViewById(R.id.connectWithFbButton);
        // signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        // signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.loginGoogle).setOnClickListener(this);


        final String user_Name = userName.getText().toString();
        String pass = password.getText().toString();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        activity = this;

        try {
            DatabaseHelper dbHelper = null;

            if (Build.VERSION.SDK_INT >= 23) {
                if (Utils.checkPermission(getApplicationContext())) {
                    dbHelper = new DatabaseHelper(getApplicationContext());
                } else {
                    dbHelper = new DatabaseHelper(getApplicationContext(), true);
                }
            } else {
                dbHelper = new DatabaseHelper(getApplicationContext());
            }
            // open to read and write
            dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FB", "FB");
                if (isFBLoggedIn()) {
                    showAlertDialog();
                }else {
                    loginButton.performClick();
                }
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);
                if (Validation.isValidEmailAddress(userName)) {
                    if (checkUserExists() == 0) {
                        if (progressDialog != null) progressDialog.dismiss();
                        Snackbar.make(view, "Email ID not registered", Snackbar.LENGTH_LONG).show();
                    } else {
                        getUserBasedOnMailId();
                        //  Snackbar.make(view, "", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(activity);

                if (checkUserLogin() == 0) {
                    Snackbar bar = Snackbar.make(view, "Incorrect User ID or Password", Snackbar.LENGTH_LONG);
                    bar.show();
                } else {
                    appConfig.loginMail = userName.getText().toString();
                    PreferenceManager.storeIntoSharedPrefrence(getApplicationContext(), appConfig.MAIL, appConfig.loginMail);
                    Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                    intent.putExtra("ID", 1);
                    startActivity(intent);
                }

            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });




        callbackManager = CallbackManager.Factory.create();


        //loginButton.setReadPermissions(Arrays.asList( "email","public_profile",
             //   "user_friends"));
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, mFacebookCallback);


        Log.d("onSuccess", "");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("onSuccess", "facebook" + loginResult.getAccessToken().getUserId());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                // Application code
                                try {
                                    String email = object.getString("email");
                                    Log.d("onSuccess", "email" + email);

                                    if (!checkUserIsExist(email)) {
                                        insertRegisterData("", email);
                                    }

                                    appConfig.loginMail = email;
                                    PreferenceManager.storeIntoSharedPrefrence(getApplicationContext(), appConfig.MAIL, email);
                                    PreferenceManager.storeIntoSharedPrefrence(getApplicationContext(), appConfig.FBMAIL, email);
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

        String serverClientId = getString(R.string.server_client_id);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, LoginActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private Integer checkUserLogin() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.REGISTERTB, null, appConfig.GETLOGINUSER,
                    new String[]{userName.getText().toString(), password.getText().toString()},
                    appConfig.RawQuery, false, true, false, getApplicationContext());
            return Integer.parseInt(dbStr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Integer checkUserExists() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.REGISTERTB, null, appConfig.CHECKUSEREXIST,
                    new String[]{userName.getText().toString()},
                    appConfig.RawQuery, false, true, false, getApplicationContext());
            return Integer.parseInt(dbStr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("Shreks Fragment", "onSuccess");

            Profile profile = Profile.getCurrentProfile();
            Log.d("onSuccess", "" + profile);

        }

        @Override
        public void onCancel() {
            Log.d("Shreks Fragmnt", "onCancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("Shreks Fragment", "onError " + e);
        }
    };

    private void signIn() {
        //Creating an intent
        Log.d("signIn", "Entered");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
            Log.d("signIn", "Entered");


      /*      GoogleSignInAccount acc = result.getSignInAccount();
            Log.d("","emailid" + acc.getEmail());
            Log.d("","emailid" + acc.getDisplayName());
            Log.d("","emailid" + acc.getId());
*/
          /*  Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            Log.i(TAG, "--------------------------------");
            Log.i(TAG, "Display Name: " + person.getDisplayName());
            Log.i(TAG, "Gender: " + person.getGender());
            Log.i(TAG, "AboutMe: " + person.getAboutMe());
            Log.i(TAG, "Birthday: " + person.getBirthday());
            Log.i(TAG, "Current Location: " + person.getCurrentLocation());
            Log.i(TAG, "Language: " + person.getLanguage());*/


        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        Log.d("GoogleSignInResult", "" + Converter.gson.toJson(result));
        Log.d("GoogleSignInResult", "" + result.isSuccess());
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            //Displaying name and email
            String name = acct.getDisplayName();
            Log.d("email", "" + name);
            String email = acct.getEmail();
            Log.d("email", "" + email);

            appConfig.loginMail = email;
            PreferenceManager.storeIntoSharedPrefrence(getApplicationContext(), appConfig.MAIL, email);

            if (!email.equals("")) {
                if (!checkUserIsExist(email)) {
                    insertRegisterData(name, email);
                }
                Log.d("email", "" + email);
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    private void insertRegisterData(String name, String email) {
        try {
            RegisterBean registerBean = new RegisterBean();
            registerBean.setEmail(email);
            registerBean.setName(name);
            if (dbHandler.insertHandler(AppConfig.REGISTERTB, Converter.gson.toJson(registerBean), getApplicationContext())) {
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkUserIsExist(String email) {
        try {
            if (getRegisterUsers() != null) {
                for (RegisterBean bean : getRegisterUsers()) {
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


    @Override
    public void onClick(View v) {
        if (v == loginGoogle) {
            //Calling signin
            signIn();
        }
    }

    public boolean isFBLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.facebook);
        builder.setTitle("Facebook");
        builder.setMessage("Already logged in with Facebook. Do you want to continue?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                appConfig.loginMail = PreferenceManager.retrieveFromSharedPrefrence(getApplicationContext(), appConfig.FBMAIL);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }

        });
        builder.show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void startMail(RegisterBean registerBean) {
        if (isOnline()) {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Sending mail...");
            progressDialog.show();
            new SendMailAsync(registerBean).execute();
        } else {
            Toast.makeText(getApplicationContext(), "Offline", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public class SendMailAsync extends AsyncTask<Boolean, String, Boolean> {

        private RegisterBean registerBean;

        public SendMailAsync(RegisterBean registerBean) {
            this.registerBean = registerBean;
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            return sendMail(registerBean);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean)
                Snackbar.make(forgot_password, "Password has been sent to the registered Email ID", Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(forgot_password, "Mail sending failed", Snackbar.LENGTH_LONG).show();

            super.onPostExecute(aBoolean);
        }

    }

    public boolean sendMail(RegisterBean registerBean) {

        final String username = "timekeeper070@gmail.com";
        final String password = "Pass123!@#";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(registerBean.getEmail()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(registerBean.getEmail()));
            message.setSubject("Mail From TimeKeeper");
            System.out.println("MAIL PWD :" + registerBean.getPassword());
            System.out.println("MAIL ID:" + registerBean.getEmail());
            message.setText("Your password is " + registerBean.getPassword());

          /*  //for attachment
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            if (picturePath != null) {
                messageBodyPart = new MimeBodyPart();
                File file = new File(picturePath);
                if (file.exists()) {
                    DataSource source = new FileDataSource(picturePath);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(pictureName);
                    multipart.addBodyPart(messageBodyPart);

                    message.setContent(multipart);
                } else {
                    Log.d("Mail", "File not found");
                }
            }*/

            Transport.send(message);

            System.out.println("Done");
            progressDialog.dismiss();
            return true;
        } catch (Exception e) {
            Snackbar.make(forgot_password, "Mail sending failed", Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void getUserBasedOnMailId() {
        try {
            String dbStr = DBHandler.selectHandler(AppConfig.REGISTERTB, null, "email=?",
                    new String[]{userName.getText().toString()}, null, true, false, false, getApplicationContext());
            System.out.println("MAIL 1:" + dbStr);
            RegisterBean registerBean = Converter.gson.fromJson(dbStr, RegisterBean.class);
            if (registerBean != null) {
                if (registerBean.getEmail() != null)
                    startMail(registerBean);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
