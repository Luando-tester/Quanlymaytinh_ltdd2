package com.example.quanlysuachuamaytinh;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PhieuThuMainActivity extends AppCompatActivity {
    ListView lvphieuthu;
    ArrayList<PhieuThu> arrayPhieutThu;
    EditText edmaphieu,edmakhachhang,edngaytaophieu,edsotien;
    Button btnTao,btnSuaPhieu,btnXoatatcaphieu;
    Spinner spnLoaiPhieu;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phieuthu);
        lvphieuthu = (ListView) findViewById(R.id.lvphieuthu);
        arrayPhieutThu = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter(PhieuThuMainActivity.this, android.R.layout.simple_list_item_1, arrayPhieutThu);
        lvphieuthu.setAdapter(adapter);
        btnTao = (Button) findViewById(R.id.btnTao);
        btnSuaPhieu =(Button) findViewById(R.id.btnSua);
        btnXoatatcaphieu =(Button)findViewById(R.id.btnXoatatcaphieu);
        btnTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Taodulieu();
            }
        });
        btnSuaPhieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Suadulieu();
            }
        });
        btnXoatatcaphieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Xoatatcadulieu();
            }
        });
        setControl();
    }

    private void setControl() {
        arrayPhieutThu = new ArrayList<>();

        edmaphieu = (EditText) findViewById(R.id.edmaphieu);
        edmakhachhang = (EditText) findViewById(R.id.edmakhachhang);
        edngaytaophieu = (EditText) findViewById(R.id.edngaytaophieu);
        spnLoaiPhieu = (Spinner) findViewById(R.id.spnloaiphieu);
        edsotien = (EditText) findViewById(R.id.edsotien);

        //2. setAdapter listview
        lvphieuthu = (ListView) findViewById(R.id.lvphieuthu);
        adapter = new MyAdapter(this, R.layout.layout_list_item, arrayPhieutThu);
        lvphieuthu.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
       {
//            case R.id.mn_Them:
//                Taodulieu();
//                Toast.makeText(MainActivity.this, "Tạo Thành Công", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.mn_Xoa:
//                Xoatatcadulieu();
//                Toast.makeText(MainActivity.this, "Sửa Thành Công", Toast.LENGTH_LONG).show();;
//                break;
//            case R.id.mn_Sửa:
//                Suadulieu();
//                Toast.makeText(MainActivity.this, "Xóa dữ liệu Thành Công", Toast.LENGTH_LONG).show();
//                break;
        case R.id.mn_Thoat:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void Xoatatcadulieu() {
        for (int i = 0; i < arrayPhieutThu.size(); i++) {
            if (arrayPhieutThu.get(i).isChon()) {
                arrayPhieutThu.remove(i);
                i--;
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void Suadulieu() {
        final Dialog dialog = new Dialog(PhieuThuMainActivity.this);
        dialog.setTitle("Update");
        dialog.setContentView(R.layout.dialog_layout);

        final EditText edtDialogMaPhieu = (EditText) dialog.findViewById(R.id.edmaphieu);
        final EditText edtDialogMaKH = (EditText) dialog.findViewById(R.id.edmakhachhang);
        final EditText edtDialogNgayTaoPhieu = (EditText) dialog.findViewById(R.id.edngaytaophieu);
        final EditText edtDialogSoTien = (EditText) dialog.findViewById(R.id.edsotien);
        final Spinner spnDialogLoaiPhieu = (Spinner) dialog.findViewById(R.id.spnloaiphieu);

        Button btnDialogThem = (Button) dialog.findViewById(R.id.btnAdds);
        Button btnDialogExit = (Button) dialog.findViewById(R.id.btnExits);
        btnDialogThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maPT = edtDialogMaPhieu.getText().toString();
                String maKH = edtDialogMaKH.getText().toString();
                String ngay = edtDialogNgayTaoPhieu.getText().toString();
                String loaphieu = spnDialogLoaiPhieu.getSelectedItem().toString();
                int sotien = Integer.parseInt(edtDialogSoTien.getText().toString());

                PhieuThu tt = new PhieuThu(maPT,maKH,ngay,loaphieu,sotien);
                if (maPT.equals("")) {
                    Toast.makeText(PhieuThuMainActivity.this, "Vui lòng nhập mã phiếu", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < arrayPhieutThu.size(); i++) {
                        if (arrayPhieutThu.get(i).isChon()) {
                            arrayPhieutThu.get(i).setSoPT(tt.getSoPT());
                            arrayPhieutThu.get(i).setMaKh(tt.getMaKh());
                            arrayPhieutThu.get(i).setNgay(tt.getNgay());
                            arrayPhieutThu.get(i).setLoaphieu(tt.getLoaphieu());
                            arrayPhieutThu.get(i).setSotien(tt.getSotien());
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                }
            }
        });
        btnDialogExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void Taodulieu(){
        // Lấy dữ liệu
        String maPT = edmaphieu.getText().toString();
        String maKH = edmakhachhang.getText().toString();
        String ngay = edngaytaophieu.getText().toString();
        String loaiphieu = spnLoaiPhieu.getSelectedItem().toString();
        int sotien = Integer.parseInt(edsotien.getText().toString());

        //3. xu ly thong tin
        PhieuThu phieuThu = new PhieuThu();
        phieuThu.setSoPT(maPT);
        phieuThu.setMaKh(maKH);
        phieuThu.setNgay(ngay);
        phieuThu.setLoaphieu(loaiphieu);
        phieuThu.setSotien(sotien);
        //4. đưa danh sách
        arrayPhieutThu.add(phieuThu);
        adapter.notifyDataSetChanged();
    }
}

