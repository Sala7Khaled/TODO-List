package com.sala7khaled.todo_auth;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import adapters.MyAdapter;
import models.ListItem;

public class MainActivity extends AppCompatActivity {

    Button add_Btn, ok_Btn, cancel_Btn;
    EditText todo_EditText;
    EditText desc_EditText;
    Dialog dialog;
    ProgressBar progressBar;
    Toolbar toolbar;

    ArrayList<ListItem> listItemsArrayList;

    RecyclerView recyclerView;
    MyAdapter myAdapter;

    DatabaseReference myDatabase;
    FirebaseAuth myAuth;

    @Override
    public void onStart() {
        super.onStart();

        listItemsArrayList.clear();
        final String userUid = myAuth.getCurrentUser().getUid();
        myDatabase = FirebaseDatabase.getInstance().getReference();

        myDatabase.child("Tasks").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ListItem getDataListItem = ds.getValue(ListItem.class);
                    myAdapter.addItem(getDataListItem);
                }
                myAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error getting data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        setListeners();
    }

    private void initializeComponents() {

        listItemsArrayList = new ArrayList<>();
        myDatabase = FirebaseDatabase.getInstance().getReference();
        myAuth = FirebaseAuth.getInstance();

        add_Btn = findViewById(R.id.todo_add_Btn);
        recyclerView = findViewById(R.id.RecView);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Liner
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        // Gird
        // recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));


        myAdapter = new MyAdapter(MainActivity.this, listItemsArrayList, new MyAdapter.CallBack() {
            @Override
            public void onItemClick(int position) {

                Intent itemIntent = new Intent(MainActivity.this, ItemActivity.class);

                String todo = listItemsArrayList.get(position).getTodo();
                String desc = listItemsArrayList.get(position).getDesc();

                itemIntent.putExtra("title", todo);
                itemIntent.putExtra("desc", desc);
                startActivity(itemIntent);

            }

            @Override
            public void onChecked(int position, boolean checked) {

                listItemsArrayList.get(position).setCheckbox(checked);

                // Update Firebase
                FirebaseUser firebaseUser = myAuth.getCurrentUser();
                String userUid = firebaseUser.getUid();
                String userKey = listItemsArrayList.get(position).getId();

                myDatabase.child("Tasks").child(userUid).child(userKey).child("checkbox").setValue(checked);
                myAdapter.notifyItemChanged(position);
            }


        });
        recyclerView.setAdapter(myAdapter);

    }

    private void setListeners() {

        add_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(MainActivity.this);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                //dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                dialog.setContentView(R.layout.custom_dialog);

                todo_EditText = dialog.findViewById(R.id.todo_EditText);
                desc_EditText = dialog.findViewById(R.id.desc_EditText);
                ok_Btn = dialog.findViewById(R.id.ok_Btn);
                cancel_Btn = dialog.findViewById(R.id.cancel_Btn);

                dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);


                ok_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String todo_text = todo_EditText.getText().toString().trim();
                        String desc_text = desc_EditText.getText().toString().trim();

                        if (todo_text.length() != 0 && desc_text.length() != 0) {

                            FirebaseUser firebaseUser = myAuth.getCurrentUser();
                            String userUid = firebaseUser.getUid();
                            String key = myDatabase.child("Tasks/" + userUid).push().getKey();

                            // Update UI
                            ListItem newListItem = new ListItem(todo_text, desc_text, key, false);
                            myAdapter.addItem(newListItem);

                            // Update Firebase
                            myDatabase.child("Tasks/" + userUid).child(key).setValue(newListItem);

                            dialog.cancel();
                        } else if (todo_EditText.getText().toString().length() != 0) {
                            Toast.makeText(MainActivity.this, "Please add description!", Toast.LENGTH_SHORT).show();
                        } else if (desc_EditText.getText().toString().length() != 0) {
                            Toast.makeText(MainActivity.this, "Whoops you forgot the Title!", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.cancel();
                            Toast.makeText(MainActivity.this, "Nothing added!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                cancel_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_delete) {

            FirebaseUser firebaseUser = myAuth.getCurrentUser();
            String userUid = firebaseUser.getUid();
            for (int i = 0; i < listItemsArrayList.size(); i++)
            {
                if(listItemsArrayList.get(i).isCheckbox())
                {
                    String userKey = listItemsArrayList.get(i).getId();
                    myDatabase.child("Tasks").child(userUid).child(userKey).removeValue();
                    listItemsArrayList.remove(i);
                    myAdapter.notifyItemRemoved(i);

                }
            }
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.menu_logout) {

            FirebaseUser currentUser = myAuth.getCurrentUser();
            myAuth.signOut();

            Toast.makeText(MainActivity.this, "Goodbye " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}