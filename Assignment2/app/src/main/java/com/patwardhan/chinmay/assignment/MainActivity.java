package com.patwardhan.chinmay.assignment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.patwardhan.chinmay.assignment.R;
import com.patwardhan.chinmay.assignment.helpers.InputValidation;
import com.patwardhan.chinmay.assignment.sql.DatabaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = MainActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutNumber;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextNumber;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutNumber = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextNumber = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextNumber, textInputLayoutNumber, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextNumber(textInputEditTextNumber, textInputLayoutNumber, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextNumber.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {


//            Intent accountsIntent = new Intent(activity, HomeActivity.class);
//            accountsIntent.putExtra("NUMBER", textInputEditTextNumber.getText().toString().trim());
//            emptyInputEditText();
//            startActivity(accountsIntent);


        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextNumber.setText(null);
        textInputEditTextPassword.setText(null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initTasks();

        checkTimer();
    }

    private void initTasks() {
        logInButton = (Button) findViewById(R.id.appCompatButtonLogin);

        SharedPreferences prefs = getSharedPreferences("file", Context.MODE_PRIVATE);
    }

    private void checkTimer() {
        if (prefs.contains("time"))
            setTimer();
        else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("time", -1L);
            editor.apply();
        }
    }

    private void setTimer() {
        timeLeft = prefs.getLong("time", -1L);
        if (timeLeft != -1L)
            startTimer(timeLeft);
        else
            logInButton.setEnabled(true);

    }

    private void startTimer(long time) {
        logInButton.setEnabled(false);
        timer = new CountDownTimer(time, 1000) {

            @Override
            public void onFinish() {
                logInButton.setEnabled(true);
                saveToPref(-1L);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                //update UI, if required
                timeLeft = millisUntilFinished;
                saveToPref(timeLeft);
            }
        };
    }

    private void saveToPref(long timeLeft){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("time", timeLeft);
        editor.apply();
    }


}
