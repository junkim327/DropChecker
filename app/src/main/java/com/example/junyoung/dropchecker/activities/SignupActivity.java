package com.example.junyoung.dropchecker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.junyoung.dropchecker.R;
import com.example.junyoung.dropchecker.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private String userName;

    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    @BindView(R.id.edittext_signup_email) EditText editTextEmail;
    @BindView(R.id.edittext_signup_username) EditText editTextUserName;
    @BindView(R.id.edittext_signup_password) EditText editTextPassword;
    @BindView(R.id.text_input_layout_email) TextInputLayout textInputLayoutEmail;
    @BindView(R.id.text_input_layout_username) TextInputLayout textInputLayoutUsername;
    @BindView(R.id.text_input_layout_password) TextInputLayout textInputLayoutPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar_signup);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        updateUi(currentUser);
    }

    /**
     * Launch the main activity if the currently signed-in user exists.
     *
     * @param user the currently signed-in user.
     */
    private void updateUi(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.button_signup)
    public void signup() {
        userName = editTextUserName.getText().toString();
        textInputLayoutEmail.setErrorEnabled(false);
        textInputLayoutPassword.setErrorEnabled(false);
        textInputLayoutUsername.setErrorEnabled(false);
        checkIfUsernameAlreadyExists(userName);
    }

    /**
     * Check if the user's display name exists in the Firebase database.
     * <p>
     * If it exists, set an error message.<br>
     * If it doesn't exist, start registering the user's new email address and password.
     *
     * @param userName the currently signed-in user's display name.
     * @see #registerNewEmailAndPassword()
     */
    private void checkIfUsernameAlreadyExists(String userName) {
        usersRef.orderByChild("userName").equalTo(userName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.v(TAG, "onDataChange: Username exists.");

                            textInputLayoutUsername.setError("Sorry, that username already exists.");
                        } else {
                            Log.v(TAG, "onDataChange: Username doesn't exist.");
                            registerNewEmailAndPassword();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    /**
     * Create a new user account associated with the specified email address and password.
     * <p>
     * On successful creation of the user account, this user will also be signed in to the
     * application, and this method calls methods updateUsername(FirebaseUser),
     * writeUserInfoToFirebaseDatabase(FirebaseUser), and updateUi(FirebaseUser). <br>
     * If the account already exists or the password is invalid, set an error message.
     *
     * @see #updateUsername(FirebaseUser)
     * @see #writeUserInfoToFirebaseDatabase(FirebaseUser)
     * @see #updateUi(FirebaseUser)
     */
    private void registerNewEmailAndPassword() {
        auth.createUserWithEmailAndPassword(editTextEmail.getText().toString(),
                editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmailAndPassword: success.");

                            FirebaseUser user = auth.getCurrentUser();

                            updateUsername(user);
                            writeUserInfoToFirebaseDatabase(user);
                            updateUi(user);
                        } else {
                            Log.w(TAG, "createUserWithEmailAndPassword: failure.");

                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            try {
                                throw task.getException();
                            }
                            // If user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword) {
                                Log.d(TAG, "onComplete: weak_password");

                                textInputLayoutPassword.setErrorEnabled(true);
                                textInputLayoutPassword.setError("Must be between 6 and 128 in "
                                        + "length.");
                            }
                            // If user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Log.d(TAG, "onComplete: malformed_email");

                                textInputLayoutEmail.setErrorEnabled(true);
                                textInputLayoutEmail.setError("Not a well formed email address.");
                            }
                            // If the email address already exists.
                            catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d(TAG, "onComplete: exist_email");

                                textInputLayoutEmail.setErrorEnabled(true);
                                textInputLayoutEmail.setError("A user with this email address "
                                        + "already exists.");
                            }
                            catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        }
                    }
                });
    }

    /**
     * Write the currently signed-in user information - the user's display name and email - to the
     * Firebase realtime database.
     *
     * @param user the currently signed-in user.
     */
    private void writeUserInfoToFirebaseDatabase(FirebaseUser user) {
        usersRef.child(user.getUid()).setValue(new User(userName, user.getEmail()));
    }

    /**
     * Update the currently signed-in user's display name on the user's Auth object.
     *
     * @param user the currently signed-in user.
     */
    private void updateUsername(FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User profile updated");
                }
            }
        });
    }
}
