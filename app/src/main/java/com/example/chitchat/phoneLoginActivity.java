package com.example.chitchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phoneLoginActivity extends AppCompatActivity {

    private EditText phone_no;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private  String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private TextView sendOtpButton;
    private  TextView otp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        phone_no=findViewById(R.id.phone);
       sendOtpButton=findViewById(R.id.send_otp);
        otp=findViewById(R.id.otp);
        otp.setVisibility(View.INVISIBLE);

        mAuth=FirebaseAuth.getInstance();

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                Toast.makeText(phoneLoginActivity.this,"Phone verification failed",Toast.LENGTH_SHORT);

            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token)
            {
                mVerificationId = verificationId;
                mResendToken = token;
                if(!TextUtils.isEmpty(verificationId))
                    Log.d("send code", "code sent");
                Log.d("send code", "code sent");


            }
        };

    }
    public void sendOtp(View view)
    {
        String phone=phone_no.getText().toString();
        if(!TextUtils.isEmpty(phone))
        {
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+phone,120,TimeUnit.SECONDS,phoneLoginActivity.this,mCallbacks);

        }
        sendOtpButton.setVisibility(View.INVISIBLE);
        otp.setVisibility(View.VISIBLE);
        new CountDownTimer(120000,1000)
        {

            @Override
            public void onTick(long l)
            {
                TextView time= findViewById(R.id.time);
                time.setText(" "+l/1000);
            }

            @Override
            public void onFinish()
            {
                sendOtpButton.setVisibility(View.VISIBLE);
                otp.setVisibility(View.INVISIBLE);
            }
        }.start();


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent mainActivityIntent = new Intent(phoneLoginActivity.this, MainActivity.class);
                            startActivity(mainActivityIntent);
                        }
                        else
                        {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(phoneLoginActivity.this,"invalid Otp",Toast.LENGTH_SHORT);
                            }
                        }
                    }
                });
    }
}