package com.example.e_parking;

import static com.example.e_parking.R.id.home;
import static com.example.e_parking.R.id.jmlh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;

import org.w3c.dom.Text;

import java.sql.Date;
import java.text.SimpleDateFormat;

import Model.DetailModel;
import Model.SimpanKendaraanModel;
import retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Struk extends AppCompatActivity {
    Button var_bayar, var_pil_bayar;
    ImageButton var_back;
    TextView total;
    LoadingDialog loadingDialog = new LoadingDialog(Struk.this);

    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struk);
        if(getIntent().getExtras().getString("pilih") != null){
            loadingDialog.startLoadingDialog();
        }
        getBayar();

        total = findViewById(R.id.jmlh);
        var_bayar = findViewById(R.id.btn_simpan);
        var_pil_bayar = findViewById(R.id.btn_pil_bayar);
        var_back = findViewById(R.id.btn_back_struk);

        var_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Struk.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
            if(total != null){
                var_bayar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getIntent().getExtras().getString("pilih") != null) {
                            loadingDialog.startLoadingDialog();
                            SimpanKendaraan(simpanKendaraan());
                        }else {
                            Toast.makeText(Struk.this, "Silahkan Pilih Pembayaran terlebih dahulu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            var_pil_bayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Struk.this,Bayar.class);
                    i.putExtra("ID",getIntent().getExtras().getInt("ID"));
                    startActivity(i);
                }
            });
        }

    public SimpanKendaraanModel simpanKendaraan(){
        SimpanKendaraanModel simpan = new SimpanKendaraanModel();
        TextView var_jmlh = findViewById(R.id.jmlh);
        simpan.setTotal_price(Integer.parseInt(var_jmlh.getText().toString()));

        return simpan;
    }

    public void SimpanKendaraan(SimpanKendaraanModel simpan){
        ApiService.endpoint().SimpanKendaraan("Bearer " + Preferences.getKEY_Token(getBaseContext()),getIntent().getExtras().getInt("ID"),simpan).enqueue(new Callback<SimpanKendaraanModel>() {
            @Override
            public void onResponse(Call<SimpanKendaraanModel> call, Response<SimpanKendaraanModel> response) {
                if (response.isSuccessful()){
                    SimpanKendaraanModel simpan = response.body();
                    if(simpan.getStatus() != true){
                        Toast.makeText(Struk.this, simpan.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Struk.this, simpan.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Struk.this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        print(simpan);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpanKendaraanModel> call, Throwable t) {
                Toast.makeText(Struk.this,"Error "+t,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void print(SimpanKendaraanModel simpan){
        TextView license, masuk, keluar, jumlah, lokasi, kendaraan, tglmasuk;

        license =  findViewById(R.id.nop);
        masuk =  findViewById(R.id.JAMmasuk);
        keluar =  findViewById(R.id.JAMkeluar);
        jumlah =  findViewById(R.id.jmlh);
        lokasi = findViewById(R.id.lokasi);
        kendaraan = findViewById(R.id.jenis_kendaraan);
        tglmasuk = findViewById(R.id.TGLmasuk);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, Struk.PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, Struk.PERMISSION_BLUETOOTH_ADMIN);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, Struk.PERMISSION_BLUETOOTH_CONNECT);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, Struk.PERMISSION_BLUETOOTH_SCAN);
        } else {
            EscPosPrinter printer = null;
            try {
                printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
            } catch (EscPosConnectionException e) {
                e.printStackTrace();
            }
            try {
                printer.printFormattedText(
                        "[C]=====================\n"+
                                "[C]Struk E-Parkir\n"+
                                "[C]=====================\n\n\n"+
                                "[L]ID Transaksi"+ "[R]" + getIntent().getExtras().getInt("ID") + "\n" +
                                "[L]Lokasi " + "[R]" + lokasi.getText().toString() + "\n" +
                                "[L]Juru Parkir " + "[R]" + Preferences.getKEY_User(getBaseContext()) + "\n" +
                                "[L]=====================\n" +
                                "[L]Kendaraan" + "[R]"+ kendaraan.getText().toString() + "\n" +
                                "[L]No Polisi" + "[R]" + license.getText().toString() + "\n" +
                                "[L]Tanggal" + "[R]" + tglmasuk.getText().toString() + "\n" +
                                "[L]Jam Masuk" + "[R]" + masuk.getText().toString() + "\n" +
                                "[L]Jam Keluar" + "[R]" + keluar.getText().toString() + "\n" +
                                "[L]Tarif" + "[R]" + jumlah.getText().toString() + "\n" +
                                "[L]Pembayaran" + "[R]" + getIntent().getExtras().getString("pilih")
                );
                printer.disconnectPrinter();
            } catch (EscPosConnectionException e) {
                e.printStackTrace();
            } catch (EscPosParserException e) {
                e.printStackTrace();
            } catch (EscPosEncodingException e) {
                e.printStackTrace();
            } catch (EscPosBarcodeException e) {
                e.printStackTrace();
            }
        }
    }

    public void getBayar(){
        ApiService.endpoint().GetKendaraan("Bearer " + Preferences.getKEY_Token(getBaseContext()),getIntent().getExtras().getInt("ID")).enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(Call<DetailModel> call, Response<DetailModel> response) {
                DetailModel detailModel = response.body();

                TextView tglmasuk, license, masuk, keluar, jumlah, kendaraan, lokasi;

                license = findViewById(R.id.nop);
                tglmasuk = findViewById(R.id.TGLmasuk);
                masuk = findViewById(R.id.JAMmasuk);
                keluar = findViewById(R.id.JAMkeluar);
                jumlah = findViewById(R.id.jmlh);
                kendaraan = findViewById(R.id.jenis_kendaraan);
                lokasi = findViewById(R.id.lokasi);

                license.setText(detailModel.getLicense_plate());
                kendaraan.setText(detailModel.getVehicle_name());
                lokasi.setText(detailModel.getLocation());

                int TglMasuk = detailModel.getIn_time();
                SimpleDateFormat fmttgl = new SimpleDateFormat("yyyy-MM-dd");
                Date datetgl = new Date((long) TglMasuk * 1000);
                tglmasuk.setText(fmttgl.format(datetgl));

                int JamMasuk = detailModel.getIn_time();
                SimpleDateFormat fmt = new SimpleDateFormat("hh:mm:ss");
                Date date = new Date((long) JamMasuk * 1000);
                masuk.setText(fmt.format(date));

                int JamKeluar = detailModel.getOut_time();
                SimpleDateFormat fmtout = new SimpleDateFormat("hh:mm:ss");
                Date dateout = new Date((long) JamKeluar * 1000);
                keluar.setText(fmtout.format(dateout));

                int vehicle_price = detailModel.getVehicle_price();

                int jumlah_total = (((JamKeluar - JamMasuk)/3600)+1) * vehicle_price;

                jumlah.setText(String.valueOf(jumlah_total));
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {

            }
        });
    }

}