package com.example.myblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myblogapp.sharedpreferences.BlogPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    private TextInputLayout textUserNameLayout;
    private TextInputLayout textPasswordLayout;
    private MaterialButton logInBtn;
    private ProgressBar progressIndicator;
    private BlogPreferences blogPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this code needs to be write here because we have to check that user is logged in or not check before logIn activity launches
        blogPreferences = new BlogPreferences(LogInActivity.this);
        if (blogPreferences.isLoggedIn()) {
            startMainActivity();
            finish();
            return;
        }

        setContentView(R.layout.activity_log_in);
        //setting hints to edittext
        textUserNameLayout = findViewById(R.id.textUserName);
        textUserNameLayout.setHint("Username");
        textPasswordLayout = findViewById(R.id.password);
        textPasswordLayout.setHint("Password");
        logInBtn = findViewById(R.id.login_btn);
        progressIndicator = findViewById(R.id.progress_circular);

        //adding textWatcher to remove error msg if user enter any thing inside Edittext
        Objects.requireNonNull(textUserNameLayout.getEditText()).addTextChangedListener(createTextWatcher(textUserNameLayout));
        Objects.requireNonNull(textPasswordLayout.getEditText()).addTextChangedListener(createTextWatcher(textPasswordLayout));

        //logInButton
        logInBtn.setOnClickListener(view -> LogInActivity.this.logInClicked());


    }

    //textWatcher
    private TextWatcher createTextWatcher(TextInputLayout textUserNameLayout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //no needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textUserNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //no needed
            }
        };
    }

    //logInClicked Method
    private void logInClicked() {
        String userName = textUserNameLayout.getEditText().getText().toString();
        String password = textPasswordLayout.getEditText().getText().toString();
        if (userName.isEmpty()) {
            textUserNameLayout.setError("Please ! Enter your E-mail");
        } else if (password.isEmpty()) {
            textPasswordLayout.setError("Enter your password");
        } else if (!userName.equals("admin") || !password.equals("admin")) {
            showDialog();
        } else {
            perFromLogIn();
        }
    }

    // to proceed further login
    private void perFromLogIn() {
        //here setting user LoggedIn
        blogPreferences.setLoggedIn(true);

        //set textView false so that user can't edit text after clicking  logIn btn
        textUserNameLayout.setEnabled(false);
        textPasswordLayout.setEnabled(false);
        logInBtn.setVisibility(View.INVISIBLE);
        progressIndicator.setVisibility(View.VISIBLE);

        //Handler
        //used to delay or at the specific time
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //method to move to MainActivity
                startMainActivity();
                //now calling finish() to finish the LogInActivity Method
                finish();
            }
        }, 2000);
    }

    //method startMainActivity
    private void startMainActivity() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //showDialog method to show dialog box if userName and password is not equal to "admin"
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("LogIn Failed")
                .setMessage("Username or password is not correct. Please try again.")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }
}