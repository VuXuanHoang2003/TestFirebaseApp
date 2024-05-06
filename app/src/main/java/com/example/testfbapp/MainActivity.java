package com.example.testfbapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testfbapp.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private DatabaseReference myRefRoot;
    private FirebaseFirestore firestore;
    // ...
    // Initialize Firebase Auth


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        //login("test@gmail.com", "123456");
        //createnewuser("newuser@gmail.com", "123456");
        //postDataToRealtimeDB();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        firestore = FirebaseFirestore.getInstance();
        //readDataFromRealtimeDB();
        //postDataToFireStore();
        addPostData(new Post("VXH1", "hehehe"));
        addPostData(new Post("VXH2", "hihihi"));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Login Successful");
                } else {
                    Log.d("DEBUG", "Login Failed");
                }
            }
        });
    }

    private void createnewuser(String newemail, String newpassword) {
        mAuth.createUserWithEmailAndPassword(newemail, newpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "User Created");
                } else {
                    Log.d("DEBUG", "User Creation Failed");
                }
            }
        });
    }

    private void resetpassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Password Reset Email Sent");
                } else {
                    Log.d("DEBUG", "Password Reset Email Failed");
                }
            }
        });
    }

    private void signout() {
        mAuth.signOut();
    }

    private void postDataToRealtimeDB(String data) {
        // Write a message to the database
        myRef.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Post Data" + data + " Successful");
                } else {
                    Log.d("DEBUG", "Post Data" + data + " Failed");
                }
            }
        });
    }

    private void readDataFromRealtimeDB() {
        // Read from the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("DEBUG", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DEBUG", "Failed to read value.", error.toException());
            }
        });
    }

    private void postDataToFireStore() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        firestore.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DEBUG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DEBUG", "Error adding document", e);
                    }
                });
    }

    public void addPostData(Post data) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefRoot = database.getReference();
        myRefRoot.child("posts").setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("DEBUG", "Post Data" + data + " Successful");
                } else {
                    Log.d("DEBUG", "Post Data" + data + " Failed");
                }
            }
        });
    }
}