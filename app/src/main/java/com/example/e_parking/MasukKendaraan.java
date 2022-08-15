package com.example.e_parking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import Model.Data;
import retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasukKendaraan extends AppCompatActivity {
    EditText kota, angka, terakhir;
    int kategori;
    Button var_mulai;
    ImageButton var_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masuk_kendaraan);
        kota = findViewById(R.id.ET_kota);
        angka = findViewById(R.id.ET_Angka);
        terakhir = findViewById(R.id.ET_Aterakhir);
        var_mulai = findViewById(R.id.btn_mulai);
        var_back = findViewById(R.id.btn_back_masuk);

        kota.setText("D");
        Intent i = getIntent();
        kategori = i.getExtras().getInt("Kategori");

        var_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MasukKendaraan.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        var_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(angka.getText().toString()) || TextUtils.isEmpty(terakhir.getText().toString())){
                    Toast.makeText(MasukKendaraan.this, "Masukkan No Polisi secara benar", Toast.LENGTH_LONG).show();
                } else {
                    Ksend(kendaraan());
                }
            }
        });
        }

        public Data kendaraan(){
            Data data = new Data();
            data.setParker_id(Preferences.getKey_Id(getBaseContext()));
            data.setVehicle_id(kategori);
            data.setLicense_plate(kota.getText().toString()+""+angka.getText().toString()+""+terakhir.getText().toString());
            return data;
        }

        public void Ksend(Data data) {
            Call<Data> dataa = ApiService.endpoint().SendKendaraan("Bearer "+ Preferences.getKEY_Token(getBaseContext()),data);
            dataa.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if(response.isSuccessful()){
                        startActivity(new Intent(MasukKendaraan.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else {
                        Toast.makeText(MasukKendaraan.this, "Data sudah ada",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Toast.makeText(MasukKendaraan.this, "Error "+t,Toast.LENGTH_SHORT).show();
                }
            });
    }
}
