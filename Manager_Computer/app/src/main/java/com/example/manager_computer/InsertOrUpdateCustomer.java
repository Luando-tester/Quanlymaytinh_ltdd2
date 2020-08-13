package com.example.manager_computer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

public class InsertOrUpdateCustomer extends AppCompatActivity {
    private Spinner serviceUnit;
    private String editID;
    private EditText customerName;
    private DatePicker customerDateOfBirth;
    private EditText customerAddress;
    private Button saveCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_or_update_customer);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        customerName = (EditText)findViewById(R.id.customerName);

        customerAddress = (EditText)findViewById(R.id.customerAddress);

        customerDateOfBirth = (DatePicker)findViewById(R.id.customerDateOfBirth);

        saveCustomer = (Button)findViewById(R.id.saveCustomer);

        saveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertOrUpdate();
            }
        });

        if (bundle != null) {
            editID = bundle.getString(Constants.CUSTOMER_CODE_KEY);
            customerName.setText(bundle.getString(Constants.CUSTOMER_NAME_KEY));
            customerAddress.setText(bundle.getString(Constants.CUSTOMER_ADDRESS_KEY));

            setTitle("Sửa khách hàng: " + editID);
        } else {
            setTitle("Thêm khách hàng");
        }
    }

    private void insertOrUpdate() {
        String name = customerName.getText().toString();
        String address = customerAddress.getText().toString();
        Date dateOfBirth = new Date(customerDateOfBirth.getYear() - 1900, customerDateOfBirth.getMonth(), customerDateOfBirth.getDayOfMonth());

        if (name.isEmpty()) {
            Toast.makeText(this, "Tên khách hàng không được bỏ trống", Toast.LENGTH_SHORT).show();

            if (customerName.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(customerName, InputMethodManager.SHOW_IMPLICIT);
            }

            return;
        }

        if (address.isEmpty()) {
            Toast.makeText(this, "Địa chỉ khách hàng không được bỏ trống", Toast.LENGTH_SHORT).show();

            if (customerAddress.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(customerAddress, InputMethodManager.SHOW_IMPLICIT);
            }

            return;
        }

        if (dateOfBirth == null) {
            Toast.makeText(this, "Chưa chọn ngày sinh", Toast.LENGTH_SHORT).show();

            return;
        }

        if (editID == null || editID == "") {
            // Insert
            Customer customer = new Customer(name, address, dateOfBirth);

            customer.save();

            Toast.makeText(this, "Thêm thông tin khách hàng thành công", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            // Update
            List<Customer> customers = Customer.findWithQuery(Customer.class, "SELECT * FROM CUSTOMER WHERE code = ?", editID);

            if (customers != null) {
                if (customers.size() == 1) {
                    Customer service = customers.get(0);

                    service.setName(name);
                    service.setAddress(address);
                    service.setDateOfBirth(dateOfBirth);
                    service.save();

                    Toast.makeText(this, "Sửa thông tin khách hàng thành công", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }

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
