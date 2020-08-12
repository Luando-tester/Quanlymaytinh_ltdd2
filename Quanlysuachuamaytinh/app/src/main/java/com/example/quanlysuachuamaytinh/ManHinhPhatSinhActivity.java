package com.example.quanlysuachuamaytinh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManHinhPhatSinhActivity extends AppCompatActivity {
    Button btnchuyenmanhinhphatsinh1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.manhinhphatsinh);
        btnchuyenmanhinhphatsinh1 = (Button) findViewById((R.id.btnchuyenmanhinhphatsinh1));
        btnchuyenmanhinhphatsinh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenmanhinhphatsinh1();
            }
        });
        super.onCreate(savedInstanceState);
    }

    private void chuyenmanhinhphatsinh1() {
        startActivity(new Intent(ManHinhPhatSinhActivity.this,ThongTinKhachHang.class));
    }

}
