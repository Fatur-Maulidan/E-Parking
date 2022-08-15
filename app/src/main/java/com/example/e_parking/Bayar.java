package com.example.e_parking;

import static com.example.e_parking.R.id.jmlh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;

import java.sql.Date;
import java.text.SimpleDateFormat;

import Model.DetailModel;
import Model.SimpanKendaraanModel;
import retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bayar extends AppCompatActivity {
    Button cash, etoll, ovo, gopay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);

        gopay = findViewById(R.id.BTN_Gopay);
        ovo = findViewById(R.id.BTN_OVO);
        etoll = findViewById(R.id.BTN_EToll);
        cash = findViewById(R.id.BTN_cash);


//        gopay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Bayar.this, Struk.class);
//                i.putExtra("pilih", "GOPAY");
//                i.putExtra("ID",getIntent().getExtras().getInt("ID"));
//                startActivity(i);
//            }
//        });
//
//        ovo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Bayar.this, Struk.class);
//                i.putExtra("pilih", "OVO");
//                i.putExtra("ID",getIntent().getExtras().getInt("ID"));
//                startActivity(i);
//            }
//        });
//
//        etoll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Bayar.this, Struk.class);
//                i.putExtra("pilih", "E-Toll");
//                i.putExtra("ID",getIntent().getExtras().getInt("ID"));
//                startActivity(i);
//            }
//        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Bayar.this, Struk.class);
                i.putExtra("pilih", "TUNAI");
                i.putExtra("ID",getIntent().getExtras().getInt("ID"));
                startActivity(i);
            }
        });

    }
}