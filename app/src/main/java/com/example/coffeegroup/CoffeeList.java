package com.example.coffeegroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.coffeegroup.Services.CoffeeAdapter;
import com.example.coffeegroup.Services.ConstData;
import com.example.coffeegroup.Services.UserFormat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.LinkedList;

public class CoffeeList extends AppCompatActivity {

    private TextView tvNoData;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    boolean doubleBackToExitPressedOnce = false;

    private LinkedList<UserFormat> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_list);

        ids();
        list = new LinkedList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading information..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstData.firebaseDBRef);
        list.clear();
        readInfo();

    }

    private void readInfo()
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot i: dataSnapshot.getChildren())
                    {
                        UserFormat temp = i.getValue(UserFormat.class);
                        list.add(temp);
                    }
                    if (!list.isEmpty())
                        recyclerView.setAdapter(new CoffeeAdapter(list,getApplicationContext()));
                    else
                    {
                        tvNoData.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
    }

    private void ids()
    {
        tvNoData = findViewById(R.id.tvNoData);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fabEdit = findViewById(R.id.fabEdit);
        fabEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog();
            }
        });
    }

    private void dialog()
    {
        final Dialog dialog = new Dialog(CoffeeList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String[] sugarLevel = { "0 sugar", "1 sugar", "2 sugar", "3 sugar", "4 sugar", "5 sugar", "6 sugar"};

        final int[] brewing = {0};
        final int[] sugar = {0};
        final String[] name = {""};
        final boolean[] yesNo = {false};

        final EditText etName = dialog.findViewById(R.id.etName);
        SeekBar seekBarBrewing = dialog.findViewById(R.id.seekBarBrewing);
        Spinner spinner = dialog.findViewById(R.id.spinnerSugar);
        final CheckBox checkBox = dialog.findViewById(R.id.checkBoxYesNo);

        seekBarBrewing.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brewing[0] = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sugarLevel);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sugar[0] =position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sugar[0]=0;
            }
        });

        Button buttonUpdate = dialog.findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etName.getText().toString().trim()))
                    etName.setError("Required");
                else
                {
                    name[0] =etName.getText().toString().trim();
                    yesNo[0] =checkBox.isChecked();
                    UserFormat user = new UserFormat(name[0],sugar[0],brewing[0],yesNo[0]?1:0);
                    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference(ConstData.firebaseDBRef);
                    dbReference.child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Successfully Registered.", Toast.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.clear();
                                        readInfo();
                                    }
                                },2000);
                            }
                        }
                    });
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}