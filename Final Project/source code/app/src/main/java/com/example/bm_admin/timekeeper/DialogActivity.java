package com.example.bm_admin.timekeeper;

/**
 * Created by suresh on 30/12/15.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.IdRes;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bm_admin.timekeeper.alarm.AlarmReceiver;
import com.example.bm_admin.timekeeper.bean.PuzzleBean;
import com.example.bm_admin.timekeeper.utility.AppConfig;
import com.example.bm_admin.timekeeper.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class DialogActivity extends Activity {

    private String msgTxt;
    private String snooze;
    private Integer reqCode;
    private TextView title, subTitle, stopBtn, snoozeBtn, puzzleDesc, puzzleNo;
    private LinearLayout puzzleLayout;
    private RadioButton option1, option2, option3, option4;
    private RadioGroup radioGroup;
    private ArrayList<PuzzleBean> puzzleBeen, tmpPuzzleBeen;
    private int puzzleCount = 0;
    private AppConfig appConfig;
    private LinearLayout buttonLayout;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    public DialogActivity() {
        puzzleBeen = new ArrayList<>();
        tmpPuzzleBeen = new ArrayList<>();
        appConfig = AppConfig.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);

        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.desc);
        stopBtn = (TextView) findViewById(R.id.stopBtn);
        snoozeBtn = (TextView) findViewById(R.id.snoozeBtn);
        puzzleLayout = (LinearLayout) findViewById(R.id.puzzleLayout);
        option1 = (RadioButton) findViewById(R.id.puzzle_option1);
        option2 = (RadioButton) findViewById(R.id.puzzle_option2);
        option3 = (RadioButton) findViewById(R.id.puzzle_option3);
        option4 = (RadioButton) findViewById(R.id.puzzle_option4);
        radioGroup = (RadioGroup) findViewById(R.id.puzzleRadioGroup);
        puzzleDesc = (TextView) findViewById(R.id.puzzle_desc);
        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        puzzleNo = (TextView) findViewById(R.id.puzzleNo);
        puzzleLayout = (LinearLayout) findViewById(R.id.puzzleLayout);

        boolean isReminder = getIntent().getBooleanExtra("ACTION", false);
        snooze =  getIntent().getStringExtra("SNOOZE");
        reqCode = getIntent().getIntExtra("CODE", 0);

        tmpPuzzleBeen = Utils.setPuzzleBean();

        int i = 0;
        while (i < 3) {
            int idx = new Random().nextInt(tmpPuzzleBeen.size());
            puzzleBeen.add(tmpPuzzleBeen.get(idx));
            i++;
        }


        setPuzzle();

        if (isReminder) {
            msgTxt = getIntent().getStringExtra("REMIND");
            subTitle.setText(msgTxt);
            subTitle.setVisibility(View.VISIBLE);
            snoozeBtn.setVisibility(View.GONE);
            stopBtn.setText("CLOSE");
        } else {
            if(snooze.toLowerCase().equals("speech")) {
                startVoiceRecognitionActivity();
            }
            else {


                subTitle.setVisibility(View.GONE);
                if (getIntent().getBooleanExtra("ALARM", false)) {
                    puzzleLayout.setVisibility(View.VISIBLE);
                } else {
                    if (isOnline()) {
                        puzzleLayout.setVisibility(View.GONE);
                        buttonLayout.setVisibility(View.VISIBLE);
                        // startVoiceRecognitionActivity();
                    } else {
                        puzzleLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        }


        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(reqCode);
            }
        });

        snoozeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snoozeAlarm(reqCode);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                try {
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        ((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
                    }

                    switch (checkedId) {
                        case R.id.puzzle_option1:
                            System.out.println("OPTION 1:" + puzzleBeen.get(puzzleCount).getPuzz_option1());
                            System.out.println("VALUE 1:" + appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()));
                            if (puzzleBeen.get(puzzleCount).getPuzz_option1().equalsIgnoreCase(appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()))) {
                                buttonLayout.setVisibility(View.VISIBLE);
                                radioGroup.setEnabled(true);
                            } else {
                                moveToNxtPuzzle();
                            }
                            break;
                        case R.id.puzzle_option2:
                            System.out.println("OPTION 2:" + puzzleBeen.get(puzzleCount).getPuzz_option2());
                            System.out.println("VALUE 2:" + appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()));
                            if (puzzleBeen.get(puzzleCount).getPuzz_option2().equalsIgnoreCase(appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()))) {
                                buttonLayout.setVisibility(View.VISIBLE);
                                radioGroup.setEnabled(true);
                            } else {
                                moveToNxtPuzzle();
                            }
                            break;
                        case R.id.puzzle_option3:
                            System.out.println("OPTION 3:" + puzzleBeen.get(puzzleCount).getPuzz_option3());
                            System.out.println("VALUE 3:" + appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()));
                            if (puzzleBeen.get(puzzleCount).getPuzz_option3().equalsIgnoreCase(appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()))) {
                                buttonLayout.setVisibility(View.VISIBLE);
                                radioGroup.setEnabled(true);
                            } else {
                                moveToNxtPuzzle();
                            }
                            break;
                        case R.id.puzzle_option4:
                            System.out.println("OPTION 4:" + puzzleBeen.get(puzzleCount).getPuzz_option4());
                            System.out.println("VALUE 4:" + appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()));
                            if (puzzleBeen.get(puzzleCount).getPuzz_option4().equalsIgnoreCase(appConfig.answerMap.get(puzzleBeen.get(puzzleCount).getPuzz_id()))) {
                                buttonLayout.setVisibility(View.VISIBLE);
                                radioGroup.setEnabled(true);
                            } else {
                                moveToNxtPuzzle();
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void startVoiceRecognitionActivity() {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "SLEEP TRACKER");
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
            startActivity(browserIntent);
        }
    }


    private void cancelAlarm(int notificationId) {
        try {
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pintent = PendingIntent.getService(getApplicationContext(), notificationId, intent, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pintent);

            if (AlarmReceiver.ringtone != null)
                if (AlarmReceiver.ringtone.isPlaying()) AlarmReceiver.ringtone.stop();

            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void snoozeAlarm(int reqCode) {
        try {
            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this.getApplicationContext(), reqCode, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Utils.getCurrentDayWithTime());
            calendar.add(Calendar.MINUTE, 5);

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToNxtPuzzle() {
        try {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(true);
            }
            option1.setChecked(false);
            option2.setChecked(false);
            option3.setChecked(false);
            option4.setChecked(false);
            buttonLayout.setVisibility(View.GONE);
            if (puzzleCount < 2) {
                puzzleCount += 1;
                setPuzzle();
                radioGroup.setEnabled(true);
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPuzzle() {
        try {
            puzzleNo.setText("PUZZLE " + (puzzleCount + 1));
            puzzleDesc.setText(puzzleBeen.get(puzzleCount).getPuzz_quiz());
            System.out.println("OPTION TXT1:" + puzzleBeen.get(puzzleCount).getPuzz_option1());
            System.out.println("OPTION TXT2:" + puzzleBeen.get(puzzleCount).getPuzz_option2());
            System.out.println("OPTION TXT3:" + puzzleBeen.get(puzzleCount).getPuzz_option3());
            System.out.println("OPTION TXT4:" + puzzleBeen.get(puzzleCount).getPuzz_option4());
            option1.setText(puzzleBeen.get(puzzleCount).getPuzz_option1());
            option2.setText(puzzleBeen.get(puzzleCount).getPuzz_option2());
            option3.setText(puzzleBeen.get(puzzleCount).getPuzz_option3());
            option4.setText(puzzleBeen.get(puzzleCount).getPuzz_option4());
        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {


            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            String words = "";
            for (Object word : matches) {
                words = words + word.toString() + "," +
                        Log.w("#WORD :", word.toString());
            }

            if (matches.contains("stop")) {
                Toast.makeText(getApplicationContext(), "ALARM STOPPED", Toast.LENGTH_SHORT).show();
                cancelAlarm(reqCode);
            }if (matches.contains("snooze")) {
                Toast.makeText(getApplicationContext(), "ALARM SNOOZE...5 MINS", Toast.LENGTH_SHORT).show();
                snoozeAlarm(reqCode);
            }
        }
    }

}