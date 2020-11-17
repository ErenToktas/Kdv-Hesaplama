 package com.eren.kdvhesaplama;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DecimalFormat;

 public class MainActivity extends AppCompatActivity {

    private EditText editTutar,editKDV;
    private Button btnBaslik,btnyuzde18,btnyuzde8,btnyuzde1,btnHesapla;
    private TextView txtvwKdvDahilOrKdvHaric,txtvwislemtutar,txtvwkdvtutar,txtvwtoplamtutar;
    private RadioGroup RadioGroup1;
    private double tutar = 0.0;
    private double kdv = 0.0;
    private boolean kdvdahil = true;
    private Animation ay,ya;
    private AdView banner1;
    private TextWatcher editTutarDegisimler = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try{
            tutar=Double.parseDouble(s.toString());
            }catch (NumberFormatException e){
                tutar=0.0;
            }
            guncelle();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher editKDVDegisimler = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                kdv = Double.parseDouble(s.toString());

            }catch (NumberFormatException e){
                kdv = 0.0;

            }
            guncelle();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private RadioGroup.OnCheckedChangeListener radioGroupDegisimler = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            //dahil veya dahil değil
            if(checkedId == R.id.rdbtnKdvDahil) {
                kdvdahil = true;
            }
            else if (checkedId == R.id.rdbtnKdvHaric){
               kdvdahil = false;
            }
            guncelle();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        // reklam kısımı verileri
        banner1 = findViewById(R.id.banner1);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner1.loadAd(adRequest);

        // verileri eşiteleme

         editTutar = findViewById(R.id.editTutar);
         editKDV = findViewById(R.id.editKDV);
         btnBaslik = findViewById(R.id.btnBaslik);
         btnyuzde18 = findViewById(R.id.btnyuzde18);
         btnyuzde8 = findViewById(R.id.btnyuzde8);
         btnyuzde1 = findViewById(R.id.btnyuzde1);
         btnHesapla = findViewById(R.id.btnHesapla);
         txtvwKdvDahilOrKdvHaric = findViewById(R.id.txtvwKdvDahilOrKdvHaric);
         txtvwislemtutar = findViewById(R.id.txtvwislemtutar);
         txtvwkdvtutar = findViewById(R.id.txtvwkdvtutar);
         txtvwtoplamtutar = findViewById(R.id.txtvwtoplamtutar);
         RadioGroup1 = findViewById(R.id.RadioGroup1);

         // yukarıdan aşağı animasyon
        ay = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.asagidanyukarigelenbuton);
        ya = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.yukaridanasagibaslik);
        btnBaslik.setAnimation(ya);
        btnHesapla.setAnimation(ay);

         // Girilen verileri yakalama

        editTutar.addTextChangedListener(editTutarDegisimler);
        editKDV.addTextChangedListener(editKDVDegisimler);
        RadioGroup1.setOnCheckedChangeListener(radioGroupDegisimler);

        // basılan butonlarda (%1 %8 %18) geçiş yapılması
        btnyuzde1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editKDV.setText(String.valueOf(1));
            }
        });
        btnyuzde8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editKDV.setText(String.valueOf(8));
            }
        });
        btnyuzde18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editKDV.setText(String.valueOf(18));
            }
        });

        guncelle();

    }
    public void guncelle(){
        btnHesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hesapla butonuna basınca klavye aşağı insin
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0 );

                DecimalFormat formatter = new DecimalFormat("###,###.##");

                //Hesaplama
                double kdvdahilIslemTutari = tutar / (1 + kdv/100);
                double kdvdahilKdvTutari = tutar - kdvdahilIslemTutari ;

                double kdvharicKdvsi = tutar * (kdv/100);
                double kdvharicToplamTutar =  tutar + kdvharicKdvsi ;



                // kdv dahil veya hariç ise
                if(kdvdahil){
                    txtvwKdvDahilOrKdvHaric.setText(" ### KDV DAHİL ### ");
                    txtvwKdvDahilOrKdvHaric.setTextColor(Color.WHITE);
                    txtvwKdvDahilOrKdvHaric.setBackgroundResource(R.color.yesil);
                    txtvwKdvDahilOrKdvHaric.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

                    txtvwtoplamtutar.setText(formatter.format(tutar));
                    txtvwislemtutar.setText(formatter.format(kdvdahilKdvTutari));
                    txtvwkdvtutar.setText(formatter.format(kdvdahilKdvTutari));
                }
                else{
                    txtvwKdvDahilOrKdvHaric.setText(" ### KDV HARİÇ ### ");
                    txtvwKdvDahilOrKdvHaric.setTextColor(Color.WHITE);
                    txtvwKdvDahilOrKdvHaric.setBackgroundResource(R.color.kirmizi);
                    txtvwKdvDahilOrKdvHaric.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

                    txtvwislemtutar.setText(formatter.format(tutar));
                    txtvwkdvtutar.setText(formatter.format(kdvharicKdvsi));
                    txtvwtoplamtutar.setText(formatter.format(kdvharicToplamTutar));
                }

            }
        });
    }

}