package com.example.kasapp1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class AddActivity extends AppCompatActivity {

    RadioGroup status;
    RadioButton masuk, keluar;
    EditText jumlah, keterangan;
    Button simpan;

    String notifStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        notifStatus = "";

        status = findViewById(R.id.status);
        masuk = findViewById(R.id.masuk);
        keluar = findViewById(R.id.keluar);
        jumlah = findViewById(R.id.jumlah);
        keterangan = findViewById(R.id.keterangan);
        simpan = findViewById(R.id.simpan);

        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if (i == R.id.masuk) {
                    notifStatus = "Masuk";
                } else if (i == R.id.keluar) {
                    notifStatus = "Keluar";
                }
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (notifStatus.equals("")) {
                    Toast.makeText(getApplicationContext(), "Status harus diisi", Toast.LENGTH_LONG).show();
                    status.requestFocus();
                } else if (jumlah.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Jumlah harus diisi", Toast.LENGTH_LONG).show();
                    jumlah.requestFocus();
                } else if (keterangan.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Keterangan harus diisi", Toast.LENGTH_LONG).show();
                    keterangan.requestFocus();
                } else {
                    simpanData();
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Tambah");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void simpanData() {
        Toast.makeText(getApplicationContext(), "Data keuangan berhasil disimpan", Toast.LENGTH_LONG).show();
    }
}