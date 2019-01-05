package com.example.yohni.donorq;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Signup extends AppCompatActivity {

    private static final String TAG = "signup";
    private FirebaseAuth mAuth;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("users");

    @BindView(R.id.signup_name)
    EditText mName;

    @BindView(R.id.signup_email)
    EditText mEmail;

    @BindView(R.id.signup_noHP)
    EditText mNoHP;

    @BindView(R.id.signup_noPMI)
    EditText mNoPMI;

    @BindView(R.id.signup_password)
    EditText mPass;

    @BindView(R.id.type_group)
    RadioGroup typeGroup;

    @BindView(R.id.signup_login)
    TextView signLogin;

    private RadioButton radioType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mName.setHint("");
                return false;
            }
        });

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mName.setHint("Name");
            }
        });

        mEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEmail.setHint("");
                return false;
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmail.setHint("Email");
            }
        });

        mNoHP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mNoHP.setHint("");
                return false;
            }
        });

        mNoHP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mNoHP.setHint("No Handphone");
            }
        });

        mNoPMI.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mNoPMI.setHint("");
                return false;
            }
        });

        mNoPMI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mNoPMI.setHint("No PMI");
            }
        });

        mPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPass.setHint("");
                return false;
            }
        });

        mPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mPass.setHint("Password");
            }
        });

        signLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });
    }


    @OnClick(R.id.btn_signup)
    public void Submit() {

        creataAccount(mEmail.getText().toString(), mPass.getText().toString());


    }


    private void creataAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "createUser");
                            FirebaseUser user = mAuth.getCurrentUser();
                            ProfileQ uProfile = new ProfileQ();
                            uProfile.setName(mName.getText().toString());
                            uProfile.setEmail(mEmail.getText().toString());
                            uProfile.setNoHP(mNoHP.getText().toString());
                            uProfile.setNoPMI(mNoPMI.getText().toString());
                            uProfile.setPoints(0);
                            uProfile.setStatus(new Status("none","rilis"));

                            int selectedRadio = typeGroup.getCheckedRadioButtonId();

                            radioType = (RadioButton) findViewById(selectedRadio);

                            uProfile.setType(radioType.getText().toString());
                            ref.child(mAuth.getCurrentUser().getEmail().replace(".","")).setValue(uProfile);
                            updateUI(user);
                        }
                        else{
                            Log.w(TAG, "createUser:fail");
                            Toast.makeText(Signup.this, "Authenticatoin failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser mUser) {
        if (mUser != null){
            startActivity(new Intent(Signup.this, MainActivity.class));
        }

    }
}
