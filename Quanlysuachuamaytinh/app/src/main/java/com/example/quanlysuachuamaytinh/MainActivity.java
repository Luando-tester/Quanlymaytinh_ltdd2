package com.example.quanlysuachuamaytinh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnchuyenmanhinhphatsinh,btnchuyenmanhinhdichvu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.phatsinhchitiet);
         btnchuyenmanhinhphatsinh=(Button)findViewById(R.id.btnchuyenmanhinhphatsinh);
         btnchuyenmanhinhdichvu=(Button)findViewById((R.id.btnchuyenmanhinhdichvu));
        btnchuyenmanhinhphatsinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenmanhinhphatsinh();
            }
        });
        btnchuyenmanhinhdichvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chuyenmanhinhdichvu();
            }
        });
        super.onCreate(savedInstanceState);
    }
    private void chuyenmanhinhphatsinh() {
        startActivity(new Intent(MainActivity.this,ManHinhPhatSinhActivity.class));
    }

    private void chuyenmanhinhdichvu() {
        startActivity(new Intent(MainActivity.this,ManhinhDichVuActivity.class));
    }
}
