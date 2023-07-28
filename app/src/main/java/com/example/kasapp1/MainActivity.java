package com.example.kasapp1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.kasapp1.helper.SqliteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.kasapp1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listKas;
    ArrayList<HashMap<String, String>> arusKas;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    SqliteHelper sqliteHelper;
    Cursor cursor;

    TextView pemasukan, pengeluaran, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
            }
        });

        listKas = findViewById(R.id.list_kas);
        pemasukan = findViewById(R.id.pemasukan);
        pengeluaran = findViewById(R.id.pengeluaran);
        total = findViewById(R.id.total);

        arusKas = new ArrayList<>();

        sqliteHelper = new SqliteHelper(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        kasAdapter();
    }

    private void kasAdapter() {
        arusKas.clear();
        listKas.setAdapter(null);

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM transaksi", null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            HashMap<String, String> map = new HashMap<>();
            map.put("id", cursor.getString(0));
            map.put("status", cursor.getString(1));
            map.put("jumlah", cursor.getString(2));
            map.put("keterangan", cursor.getString(3));
            map.put("tanggal", cursor.getString(4));

            arusKas.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, arusKas, R.layout.list_kas,
                new String[]{"id", "status", "jumlah", "keterangan", "tanggal"},
                new int[]{R.id.id, R.id.text_status, R.id.jumlah, R.id.keterangan, R.id.tanggal});

        listKas.setAdapter(simpleAdapter);

        kasTotal();
    }

    private void kasTotal() {
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(Locale.GERMANY);

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        cursor = db.rawQuery("SELECT SUM(jumlah) AS 'MASUK' FROM transaksi WHERE status='Masuk'", null);
        cursor.moveToFirst();
        double masukSum = cursor.getDouble(cursor.getColumnIndex("MASUK"));
        pemasukan.setText(rupiah.format(masukSum));

        cursor = db.rawQuery("SELECT SUM(jumlah) AS 'KELUAR' FROM transaksi WHERE status='Keluar'", null);
        cursor.moveToFirst();
        double keluarSum = cursor.getDouble(cursor.getColumnIndex("KELUAR"));
        pengeluaran.setText(rupiah.format(keluarSum));

        cursor = db.rawQuery("SELECT SUM(jumlah) AS 'TOTAL' FROM transaksi", null);
        cursor.moveToFirst();
        double totalSum = cursor.getDouble(cursor.getColumnIndex("TOTAL"));
        total.setText(rupiah.format(totalSum));

        double difference = masukSum - keluarSum;
        total.setText(rupiah.format(difference));

//        cursor = db.rawQuery("SELECT SUM(jumlah) AS 'TOTAL'," +
//                "(SELECT SUM(jumlah) AS 'MASUK' FROM transaksi WHERE status='Masuk')," +
//                "(SELECT SUM(jumlah) AS 'KELUAR' FROM transaksi WHERE status='Keluar')", null);
//        cursor.moveToFirst();
//
//        pemasukan.setText(rupiah.format(cursor.getDouble(1)));
//        pengeluaran.setText(rupiah.format(cursor.getDouble(2)));
//        total.setText(rupiah.format(cursor.getDouble(1) - cursor.getDouble(2)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}