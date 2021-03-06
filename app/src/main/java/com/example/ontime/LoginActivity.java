
package com.example.ontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * The type Login activity.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * The Tag.
     */
    String TAG = "Sample";
    /**
     * The Rider choice.
     */
    RadioButton rider_choice;
    /**
     * The Driver choice.
     */
    RadioButton driver_choice;
    /**
     * The Rider choice tag.
     */
    int rider_choice_tag=0;
    /**
     * The Driver choice tag.
     */
    int driver_choice_tag=0;

    private Button LoginButton;
    private EditText oldUserName,oldUserPassword;
    private String userName,userPassword, password;
    /**
     * The Db.
     */
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        LoginButton = findViewById(R.id.login_button);
        oldUserName= findViewById(R.id.login_name_text);
        oldUserPassword = findViewById(R.id.login_password_text);

        db = FirebaseFirestore.getInstance();
        rider_choice = findViewById(R.id.rider);
        driver_choice = findViewById(R.id.driver);


        if(rider_choice.isChecked()){
            rider_choice_tag=1;
        }else {
            rider_choice_tag=2;
        }

        rider_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rider_choice_tag==1){
                    rider_choice.setChecked(false);
                    rider_choice_tag=2;
                }else if(rider_choice_tag==2){
                    rider_choice.setChecked(true);
                    rider_choice_tag=1;
                }
            }
        });


        if(driver_choice.isChecked()){
            driver_choice_tag=1;
        }else {
            driver_choice_tag=2;
        }
        driver_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(driver_choice_tag==1){
                    driver_choice.setChecked(false);
                    driver_choice_tag=2;
                }else if(driver_choice_tag==2){
                    driver_choice.setChecked(true);
                    driver_choice_tag=1;
                }
            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // for riders
                if (rider_choice.isChecked() && !driver_choice.isChecked()) {
                    final CollectionReference collectionReference = db.collection("Riders");
                    userName = oldUserName.getText().toString();
                    oldUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    userPassword = oldUserPassword.getText().toString();

                    if (TextUtils.isEmpty(userName)) {
                        Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(userPassword)) {
                        Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    } else {
                        Query query = collectionReference.whereEqualTo("userName", userName);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String password = documentSnapshot.getString("password");

                                        if (password.equals(userPassword)) {
                                            Log.d(TAG, "User Exists");
                                            Toast.makeText(LoginActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getBaseContext(), RiderMapActivity.class);
                                            intent.putExtra("username", userName);
                                            startActivity(intent);
                                        }
                                    }
                                }

                                if (task.getResult().size() == 0) {
                                    Log.d(TAG, "User does not exist or wrong password");
                                    Toast.makeText(LoginActivity.this, "User does not exist or wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                else if(driver_choice.isChecked() && !rider_choice.isChecked()){
                    final CollectionReference collectionReference = db.collection("Drivers");
                    userName = oldUserName.getText().toString();
                    oldUserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    userPassword = oldUserPassword.getText().toString();

                    if (TextUtils.isEmpty(userName)) {
                        Toast.makeText(LoginActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(userPassword)) {
                        Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    } else {
                        Query query = collectionReference.whereEqualTo("userName", userName);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String password = documentSnapshot.getString("password");

                                        if (password.equals(userPassword)) {
                                            Log.d(TAG, "User Exists");
                                            Toast.makeText(LoginActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getBaseContext(), DriverMapActivity.class);

                                            intent.putExtra("username", userName);
                                            startActivity(intent);
                                        }
                                    }
                                }

                                if (task.getResult().size() == 0) {
                                    Log.d(TAG, "User does not exist or wrong password");
                                    Toast.makeText(LoginActivity.this, "User does not exist or wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "You can only choice rider or driver", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

