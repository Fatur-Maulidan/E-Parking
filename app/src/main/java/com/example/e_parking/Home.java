package com.example.e_parking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import Model.LogoutModel;
import retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    Button btnmbl_masuk, btnmbl_keluar;
    Button btnmtr_masuk, btnmtr_keluar;
    Button btn_logout;
    String MNum, PNama, token;
    int id, angka = 1;

    private final static int EXIT_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null){
            Toast.makeText(Home.this, "Perangkat tidak memiliki akses Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if(!bluetoothAdapter.isEnabled()){
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1);
            }
        }

        Intent i = getIntent();
        PNama = Preferences.getKEY_User(getBaseContext());
        token = Preferences.getKEY_Token(getBaseContext());
        id = Preferences.getKey_Id(getBaseContext());

        if(angka==1){
            Toast.makeText(Home.this, "Selamat Datang "+PNama, Toast.LENGTH_SHORT).show();
            angka++;
        }

        btnmbl_masuk = (Button) findViewById(R.id.M_mobil);
        btnmbl_keluar = (Button) findViewById(R.id.K_mobil);

        btnmtr_masuk = (Button) findViewById(R.id.M_motor);
        btnmtr_keluar = (Button) findViewById(R.id.K_motor);

        btn_logout = findViewById(R.id.logout);

        btnmbl_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, MasukKendaraan.class);
                    i.putExtra("Kategori",1);
                startActivity(i);
            }
        });

        btnmbl_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Keluar.class);
                i.putExtra("Initialize", 1);
                startActivity(i);
            }
        });

        btnmtr_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, MasukKendaraan.class);
                    i.putExtra("Kategori",2);
                startActivity(i);
            }
        });

        btnmtr_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Keluar.class);
                i.putExtra("Initialize", 2);
                startActivity(i);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keluar();
            }
        });
    }

    private void keluar(){
        int id = Preferences.getKey_Id(getBaseContext());
        String token = Preferences.getKEY_Token(getBaseContext());
        Call<LogoutModel> logout = ApiService.endpoint().SendLogout("Bearer "+ token, id);

        logout.enqueue(new Callback<LogoutModel>() {
            @Override
            public void onResponse(Call<LogoutModel> call, Response<LogoutModel> response) {
                Preferences.clearLoggedInUser(getBaseContext());
                startActivity(new Intent(Home.this, Login_Activity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                finish();
            }
            @Override
            public void onFailure(Call<LogoutModel> call, Throwable t) {
                Toast.makeText(Home.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                }).create().show();
    }
}