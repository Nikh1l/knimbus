package com.knimbus.elib;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    private Spinner spSem;
    private EditText etUsn, etPhone;
    private String selectedSem = "", uid, mUsn, mPhone;
    private ArrayAdapter<CharSequence> adapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressDialog mProgress;
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInactivity";
    boolean isDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        spSem = findViewById(R.id.spinner_registration_sem);
        etUsn = findViewById(R.id.et_registration_usn);
        etPhone = findViewById(R.id.et_registration_phone);

        final LinearLayout llSignIn = findViewById(R.id.ll_sign_in_gmail);
        final LinearLayout llSignInDone = findViewById(R.id.ll_sign_in_done);
        final LinearLayout llUsn = findViewById(R.id.ll_registration_usn);
        final LinearLayout llPhone = findViewById(R.id.ll_registration_phone);
        final LinearLayout llSem = findViewById(R.id.ll_registration_sem);

        final TextView regDone = findViewById(R.id.tv_registration_done);


        regDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llUsn.setVisibility(View.INVISIBLE);
                llPhone.setVisibility(View.INVISIBLE);
                llSem.setVisibility(View.INVISIBLE);
                llSignIn.setVisibility(View.INVISIBLE);
                llSignInDone.setVisibility(View.VISIBLE);
                regDone.setVisibility(View.INVISIBLE);
            }
        });

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Signing in...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        adapter = ArrayAdapter.createFromResource(this, R.array.semesters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSem.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    mProgress.show();
                    Log.d(TAG, "onAuthStateChanged: Intent from here");
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                    mProgress.dismiss();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        llSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        llSignInDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDone = true;
                mProgress.show();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    private void registerUser() {
        selectedSem = spSem.getSelectedItem().toString().trim();
        mUsn = etUsn.getText().toString().trim();
        mPhone = etPhone.getText().toString();
        if (mPhone.isEmpty() || !mPhone.matches("(\\+91)?[6-9][0-9]{9}")) {
            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
        } else if (mUsn.isEmpty() || !mUsn.matches("[1-4][A-Za-z][A-Za-z][0-9][0-9][A-Za-z][A-Za-z][0-9][0-9][0-9]")) {
            Toast.makeText(this, "Please enter a valid USN number", Toast.LENGTH_SHORT).show();
        } else if (selectedSem.equals("") || selectedSem.matches("-Select Sem-")) {
            Toast.makeText(this, "Please select a Sem", Toast.LENGTH_SHORT).show();
        } else {
            mProgress.show();
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                mProgress.dismiss();
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Something went wrong.. Please try again", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!isDone) {
                                mProgress.dismiss();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                                HashMap<String, Object> childMap = new HashMap<>();
                                String key = userRef.push().getKey();
                                childMap.put("usn", mUsn);
                                childMap.put("phone", mPhone);
                                childMap.put("email", mAuth.getCurrentUser().getEmail());
                                childMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userRef.child(key).updateChildren(childMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(SignInActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
