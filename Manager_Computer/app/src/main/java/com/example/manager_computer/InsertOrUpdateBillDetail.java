package com.example.manager_computer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InsertOrUpdateBillDetail extends AppCompatActivity implements View.OnClickListener {
    ArrayAdapter<Service> serviceArrayAdapter;
    private Spinner billDetailService;
    private Button saveBillDetail;
    private EditText billDetailQuantity;
    private EditText billDetailServiceName;
    private String billCode;
    private String billDetailCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_or_update_bill_detail);

        saveBillDetail = (Button)findViewById(R.id.saveBillDetail);

        saveBillDetail.setOnClickListener(this);

        billDetailQuantity = (EditText)findViewById(R.id.billDetailQuantity);

        billDetailServiceName = (EditText)findViewById(R.id.billDetailServiceName);

        billDetailService = (Spinner)findViewById(R.id.billDetailService);

        List<Service> services = Service.listAll(Service.class);

        serviceArrayAdapter = new ArrayAdapter<Service>(this, android.R.layout.simple_spinner_dropdown_item, services);

        billDetailService.setAdapter(serviceArrayAdapter);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        billCode = bundle.getString(Constants.BILL_CODE_KEY);

        if (bundle != null) {
            billDetailCode = bundle.getString(Constants.BILLDETAIL_CODE_KEY);

            if (billDetailCode != null && billDetailCode != "") {
                billDetailQuantity.setText(String.valueOf(bundle.getInt(Constants.BILLDETAIL_QUANTITY_KEY)));

                String service = bundle.getString(Constants.BILLDETAIL_SERVICE_KEY);

                List<Service> services2 = Service.findWithQuery(Service.class, "SELECT * FROM SERVICE WHERE code = '" + service + "'");

                Service serviceName = null;

                if (services2 != null) {
                    if (services2.size() == 1) {
                        serviceName = services2.get(0);

                        billDetailServiceName.setVisibility(View.VISIBLE);

                        billDetailServiceName.setText(String.valueOf(serviceName));
                    }
                }

                billDetailService.setVisibility(View.GONE);

                setTitle("Sửa chi tiết phát sinh: " + billDetailCode);
            } else {
                billDetailCode = "";

                setTitle("Thêm chi tiết phát sinh");
            }
        } else {
            setTitle("Thêm chi tiết phát sinh");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == saveBillDetail) {
            insertOrUpdate();
        }
    }

    private void insertOrUpdate() {
        String quantity = billDetailQuantity.getText().toString();
        Object service = billDetailService.getSelectedItem();

        if (service == null) {
            Toast.makeText(this, "Chưa chọn dịch vụ", Toast.LENGTH_SHORT).show();

            return;
        }

        Service parseService = (Service)service;

        if (parseService == null) {
            Toast.makeText(this, "Chưa chọn dịch vụ", Toast.LENGTH_SHORT).show();

            return;
        }

        if (quantity == null || quantity == "") {
            Toast.makeText(this, "Số lượng dịch vụ không được bỏ trống", Toast.LENGTH_SHORT).show();

            if (billDetailQuantity.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(billDetailQuantity, InputMethodManager.SHOW_IMPLICIT);
            }

            return;
        }

        int parseQuantity = Integer.parseInt(quantity);

        if (parseQuantity <= 0) {
            Toast.makeText(this, "Số lượng dịch vụ không được phải lớn hơn 0", Toast.LENGTH_SHORT).show();

            if (billDetailQuantity.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(billDetailQuantity, InputMethodManager.SHOW_IMPLICIT);
            }

            return;
        }

        boolean exist = false;

        List<Detail> billDetails = new ArrayList<Detail>();

        if (billDetailCode == null || billDetailCode == "") {
            billDetails = Detail.findWithQuery(Detail.class, "SELECT * FROM DETAIL WHERE bill_code = '" + billCode + "' AND service_code = '" + parseService.getCode() + "'");

            if (billDetails != null) {
                if (billDetails.size() == 1) {
                    exist = true;
                }
            }
        }

        if (billDetailCode == null || billDetailCode == "" && !exist) {
            // Insert
            Detail billDetail = new Detail(billCode, parseService.getCode(), parseQuantity);

            billDetail.save();

            Toast.makeText(this, "Thêm chi tiết phát sinh thành công", Toast.LENGTH_SHORT).show();

            finish();
        } else if (exist && billDetailCode == null || billDetailCode == "") {
            // Update increase
            Detail billDetail = billDetails.get(0);

            billDetail.setQuantity(billDetail.getQuantity() + parseQuantity);

            billDetail.save();

            Toast.makeText(this, "Cập nhật và gộp chi tiết phát sinh thành công", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            // Update
            List<Detail> details = Detail.findWithQuery(Detail.class, "SELECT * FROM DETAIL WHERE code = '" + billDetailCode + "'");

            if (details != null) {
                if (details.size() == 1) {
                    Detail billDetail = details.get(0);

                    billDetail.setQuantity(parseQuantity);

                    billDetail.save();
                }
            }

            Toast.makeText(this, "Cập nhật chi tiết phát sinh thành công", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();

        intent.putExtra(Constants.TYPE_KEY, Constants.TYPE_INSERT_OR_UPDATE);

        setResult(RESULT_OK, intent);

        super.finish();
    }
}
