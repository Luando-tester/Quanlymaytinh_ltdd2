package com.example.manager_computer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InsertOrUpdateBill extends AppCompatActivity implements View.OnClickListener {
    private Spinner billCustomer;
    ArrayAdapter<Customer> customerArrayAdapter;
    private Button addBillDetail;
    private RecyclerView listBillDetail;
    private BillDetailDataAdapter billDetailDataAdapter;
    private String billCode;
    private Button saveBill;
    private EditText billCustomerName;
    private TextView billTotal;
    private Double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_or_update_bill);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        saveBill = (Button)findViewById(R.id.saveBill);

        saveBill.setOnClickListener(this);

        billCustomerName = (EditText)findViewById(R.id.billCustomerName);

        billTotal = (TextView)findViewById(R.id.billTotal);

        addBillDetail = (Button)findViewById(R.id.addBillDetail);

        addBillDetail.setOnClickListener(this);

        billCustomer = (Spinner)findViewById(R.id.billCustomer);

        List<Customer> customers = Customer.listAll(Customer.class);

        customerArrayAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_spinner_dropdown_item, customers);

        billCustomer.setAdapter(customerArrayAdapter);

        listBillDetail = (RecyclerView)findViewById(R.id.listBillDetail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        listBillDetail.setLayoutManager(layoutManager);
        listBillDetail.setHasFixedSize(true);

        if (bundle != null) {
            billCode = bundle.getString(Constants.BILL_CODE_KEY);
            String customerCode = bundle.getString(Constants.CUSTOMER_CODE_KEY);

            List<Customer> customers2 = Customer.findWithQuery(Customer.class, "SELECT * FROM CUSTOMER WHERE code = '" + customerCode + "'");

            Customer customerName = null;

            if (customers2 != null) {
                if (customers2.size() == 1) {
                    customerName = customers2.get(0);

                    billCustomerName.setVisibility(View.VISIBLE);

                    billCustomerName.setText(String.valueOf(customerName));
                }
            }

            billCustomer.setVisibility(View.GONE);

            saveBill.setVisibility(View.GONE);

            setTitle("Sửa phiếu phát sinh " + billCode);
        } else {
            billCode = Bill.generateRandomCode();

            setTitle("Tạo phiếu phát sinh");
        }

        loadDataBillDetail();
    }

    @Override
    public void onClick(View view) {
        if (view == addBillDetail) {
            Intent intent = new Intent(this, InsertOrUpdateBillDetail.class);

            intent.putExtra(Constants.BILL_CODE_KEY, billCode);

            startActivityForResult(intent, Constants.REQUEST_CODE);
        } else if (view == saveBill) {
            insertOrUpdate();
        }
    }

    private void insertOrUpdate() {
        Object customer = billCustomer.getSelectedItem();

        if (customer == null) {
            Toast.makeText(this, "Chưa chọn khách hàng", Toast.LENGTH_SHORT).show();

            return;
        }

        Customer parseService = (Customer)customer;

        if (parseService == null) {
            Toast.makeText(this, "Chưa chọn khách hàng", Toast.LENGTH_SHORT).show();

            return;
        }

        List<Detail> billDetails = Detail.findWithQuery(Detail.class, "SELECT * FROM DETAIL WHERE bill_code = '" + billCode + "'");

        if (billDetails == null) {
            Toast.makeText(this, "Chưa chọn chi tiết phát sinh", Toast.LENGTH_SHORT).show();

            return;
        }

        if (billDetails.size() <= 0) {
            Toast.makeText(this, "Chưa chọn chi tiết phát sinh", Toast.LENGTH_SHORT).show();

            return;
        }

        List<Bill> bills = Bill.findWithQuery(Bill.class, "SELECT * FROM BILL WHERE code = '" + billCode + "'");

        if (bills != null) {
            if (bills.size() == 1) {
                // Update
            } else {
                // Insert
                Bill bill = new Bill(false, ((Customer) customer).getCode());
                bill.setCode(billCode);
                bill.setCreatedDate(new Date());

                bill.save();

                Toast.makeText(this, "Tạo phiếu phát sinh thành công", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
    }

    private void loadDataBillDetail() {
        List<Detail> billDetails = Detail.findWithQuery(Detail.class, "SELECT * FROM DETAIL WHERE bill_code = '" + billCode + "'");

        billDetailDataAdapter = new BillDetailDataAdapter(this, billDetails);

        total = 0.0;

        for (int i = 0; i < billDetails.size(); i++) {
            Detail detail = billDetails.get(i);

            String serviceCode = detail.getServiceCode();

            List<Service> services = Service.findWithQuery(Service.class, "SELECT * FROM SERVICE WHERE code = '" + serviceCode + "'");

            if (services != null) {
                if (services.size() == 1) {
                    Service service = services.get(0);

                    total += service.getPrice() * detail.getQuantity();
                }
            }
        }

        listBillDetail.setAdapter(billDetailDataAdapter);
        billTotal.setText(total.toString());
    }

    public void navigationUpdate(Detail billDetail) {
        Intent intent = new Intent(this, InsertOrUpdateBillDetail.class);

        System.out.println("1 " + billDetail.getCode());
        System.out.println("1 " + billDetail.getQuantity());

        intent.putExtra(Constants.BILLDETAIL_SERVICE_KEY, billDetail.getServiceCode());
        intent.putExtra(Constants.BILLDETAIL_QUANTITY_KEY, billDetail.getQuantity());
        intent.putExtra(Constants.BILLDETAIL_CODE_KEY, billDetail.getCode());
        intent.putExtra(Constants.BILL_CODE_KEY, billDetail.getBillCode());

        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    public void deleteBillDetail(String id) {
        List<Detail> billDetails = Detail.findWithQuery(Detail.class, "SELECT * FROM DETAIL WHERE code = '" + id + "'");

        if (billDetails != null) {
            if (billDetails.size() == 1) {
                Detail billDetail = billDetails.get(0);

                billDetail.delete();

                loadDataBillDetail();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == Constants.REQUEST_CODE) {
            Bundle bundle = data.getExtras();

            if (bundle != null) {
                String type = bundle.getString(Constants.TYPE_KEY);

                if (type != null && type != "") {
                    loadDataBillDetail();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();

        intent.putExtra(Constants.TYPE_KEY, Constants.TYPE_INSERT_OR_UPDATE);

        setResult(RESULT_OK, intent);

        super.finish();
    }
}
