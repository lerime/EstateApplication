package com.smtgroup.estateapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smtgroup.estateapplication.enums.UserEnum;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    EditText name, surname, email, phone, password;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.nav_txtName);
        surname = findViewById(R.id.txtSurname);
        email = findViewById(R.id.txtEmail);
        phone = findViewById(R.id.txtPhone);
        password = findViewById(R.id.txtPassword);

        register = findViewById(R.id.btnKayit);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("ref", "3d264cacec20af4f9b237a655f49bc60");
                hm.put("" + UserEnum.userName, name.getText().toString());
                hm.put("" + UserEnum.userSurname, surname.getText().toString());
                hm.put("" + UserEnum.userPhone, phone.getText().toString());
                hm.put("userMail", email.getText().toString());
                hm.put("" + UserEnum.userPass, password.getText().toString());
                String url = "http://jsonbulut.com/json/userRegister.php ";
                new reg(url, hm, SignUp.this).execute();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    class reg extends AsyncTask<Void, Void, Void> {
        String url = "";
        HashMap<String, String> hm = null;
        ProgressDialog pro = null;
        String data = "";
        Context cnx;

        public reg(String url, HashMap<String, String> hm, Context cnx) {
            this.url = url;
            this.hm = hm;
            this.cnx = cnx;
            pro = ProgressDialog.show(cnx, "Yükleniyor", "İşlem Yapılırken Bekleyiniz", true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();// ilk çalışan method
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                data = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).execute().body();
            } catch (Exception ex) {
                System.err.println("Data Getirme Hatasi : " + ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pro.dismiss();
            try {
                JSONObject obj = new JSONObject(data);
                JSONObject kobj = obj.getJSONArray("" + UserEnum.user).getJSONObject(0);

                boolean durum = kobj.getBoolean("" + UserEnum.durum);
                String mesaj = kobj.getString("" + UserEnum.mesaj);
                if (durum) {
                    String kid = kobj.getString("" + UserEnum.kullaniciId);
                    Toast.makeText(SignUp.this, mesaj + " " + kid, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp.this, mesaj, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}