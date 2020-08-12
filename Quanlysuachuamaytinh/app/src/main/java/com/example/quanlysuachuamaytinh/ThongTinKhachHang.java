package com.example.quanlysuachuamaytinh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThongTinKhachHang extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongtinkhachhang);
        Button btnbtnchuyendenphieuthu=(Button)
                findViewById(R.id.btnchuyendenphieuthu);
        btnbtnchuyendenphieuthu.setOnClickListener(new
           View.OnClickListener() {
               public void onClick(View arg0) {
                   chuyendenphieuthu();
               }
           });
    }
    public void chuyendenphieuthu()
    {
        Intent myIntent=new Intent(this, PhieuThuMainActivity.class);
        startActivity(myIntent);
    }
}
