package com.example.bm_admin.timekeeper.utility;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

/**
 * Created by suresh on 18/6/16.
 */
public class Validation {

    public static boolean isNonEmpty(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Empty field");
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidEmailAddress(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher
                (editText.getText().toString()).matches()) {
            return true;
        } else {
            editText.setError("Please enter valid Email Address");
            return false;
        }
    }

    public static boolean isValidPassword(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            if (editText.getText().toString().length() >= 4) {
                // editText.setError("Empty Password");
                return true;
            } else {
                editText.setError("Please enter valid Password (Minimum 4 digits)");
                return false;
            }
        } else {
            editText.setError("Please enter Password");
            return false;
        }
    }

    public static boolean isValidConfirmPassword(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            if (editText.getText().toString().length() >= 6) {
                // editText.setError("Empty Password");
                return true;
            } else {
                editText.setError("Please enter valid Confirm Password (Minimum 8 digits)");
                return false;
            }
        } else {
            editText.setError("Please enter Confirm Password");
            return false;
        }
    }

    public static boolean isValidPhoneNumber(EditText editText) {

        if (editText.getText().toString().trim().length() == 10) {
            return true;
        } else {
            editText.setError("Please enter 10 digit mobile Number");
            return false;
        }
        /*if (!TextUtils.isEmpty(editText.getText().toString()) && Patterns.PHONE.matcher
                (editText.getText().toString()).matches()) {
            return true;
        } else {
            editText.setError("Please enter valid Phone Number");
            return false;
        }*/
    }
}
