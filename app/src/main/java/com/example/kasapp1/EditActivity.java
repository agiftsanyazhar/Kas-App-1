package com.example.kasapp1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.kasapp1.helper.CurrentDate;
import com.example.kasapp1.helper.SqliteHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class EditActivity extends AppCompatActivity {

    RadioGroup radio_status;
    RadioButton masuk, keluar;
    EditText jumlah, keterangan, edit_tanggal;
    Button simpan;

    String notifStatus, tangal;

    SqliteHelper sqliteHelper;
    Cursor cursor;

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        sqliteHelper = new SqliteHelper(this);

        radio_status = findViewById(R.id.radio_status);
        masuk = findViewById(R.id.masuk);
        keluar = findViewById(R.id.keluar);
        jumlah = findViewById(R.id.jumlah);
        keterangan = findViewById(R.id.keterangan);
        edit_tanggal = findViewById(R.id.edit_tanggal);
        simpan = findViewById(R.id.simpan);

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT *, strftime('%d/%m/%Y %H:%M:%S', tanggal) FROM transaksi WHERE id='" + MainActivity.transaksi_id + "'", null);
        cursor.moveToFirst();

        notifStatus = cursor.getString(1);
        switch (notifStatus) {
            case "Masuk":
                masuk.setChecked(true);
                break;
            case "Keluar":
                keluar.setChecked(true);
                break;
        }

        jumlah.setText(cursor.getString(2));
        keterangan.setText(cursor.getString(3));

        tangal = cursor.getString(4);
        edit_tanggal.setText(cursor.getString(5));
        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        NumberFormat numberFormat = new DecimalFormat("00");

                        tangal = year + "-" + numberFormat.format(month + 1) + "-" + numberFormat.format(dayOfMonth);
                        Log.e("_tanggal", tangal);

                        edit_tanggal.setText(numberFormat.format(dayOfMonth) + "/" + numberFormat.format(month) + "/" + numberFormat.format(year));
                    }
                }, CurrentDate.year, CurrentDate.month, CurrentDate.day);
                datePickerDialog.show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Edit");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}