package com.example.e_parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.prefs.Preferences;

import Model.LoginModel;
import retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Activity extends AppCompatActivity {
    Button var_login;
    EditText var_number,var_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        var_login = (Button)findViewById(R.id.BTN_login);
        var_number = (EditText)findViewById(R.id.ET_mnumber);
        var_password = (EditText)findViewById(R.id.ET_password);

        var_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Cek apakah number dan password yang dimasukkan sudah diisi atau belum.
//              Check if number and password already fill
                if(TextUtils.isEmpty(var_number.getText().toString()) || TextUtils.isEmpty(var_password.getText().toString()))
                {
                    Toast.makeText(Login_Activity.this, "Member Number/Password Required", Toast.LENGTH_LONG).show();
                }
//              Jika sudah terisi number dan password akan masuk kedalam procedure (CLog) dengan parameter
//              Yang bertipe data (Class) LoginModel
//              If number and password required will enter into the procedure (CLog) with the parameter
//              The Datatype (Class) is LoginModel
                else
                {
                    CLog(loginreq());
                }
            }
        });
    }


//  Membuat fungsi untuk memasukkan variable kedalam class yang nantinya dikirim ke API
//  Create function to initialize variable inside class and send to the API

    public LoginModel loginreq(){

//      Membuat objek dari class LoginModel
//      Create Object from LoginModel Class

        LoginModel loginModel = new LoginModel();
        loginModel.setMember_number(var_number.getText().toString());
        loginModel.setPassword(var_password.getText().toString());

        return loginModel;
    }


    public void CLog(LoginModel loginModel){
        ApiService.endpoint().sendLogin(loginModel).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
               if(response.isSuccessful()){
                   LoginModel loginModel = response.body();
                   if (loginModel.getStatus() == false) {
                       Toast.makeText(Login_Activity.this, loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                   } else if (!loginModel.getStatus() == false){
                       Toast.makeText(Login_Activity.this, loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                       com.example.e_parking.Preferences.setKEY_Id(getBaseContext(),loginModel.getParker_id());
                       com.example.e_parking.Preferences.setKEY_User(getBaseContext(),loginModel.getParker_name());
                       com.example.e_parking.Preferences.setKEY_Token(getBaseContext(),loginModel.getToken());

                       Toast.makeText(Login_Activity.this, "Selamat Datang "+loginModel.getParker_name(),Toast.LENGTH_SHORT).show();
                       Intent i = new Intent(Login_Activity.this, Home.class);
                       startActivity(i);
                       finish();

                   }
               }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(Login_Activity.this, "Throwable : "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}