package com.example.manager_computer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment implements View.OnClickListener {
    private Button addCustomer;
    private View parent;
    private EditText searchCustomerText;
    private RecyclerView listCustomer;
    private CustomerDataAdapter customerDataAdapter;
    private Button searchCustomer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_customer, container, false);

        listCustomer = (RecyclerView)parent.findViewById(R.id.listCustomer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.VERTICAL, false);

        listCustomer.setLayoutManager(layoutManager);
        listCustomer.setHasFixedSize(true);

        searchCustomerText = (EditText)parent.findViewById(R.id.searchCustomerText);

        addCustomer = (Button)parent.findViewById(R.id.addCustomer);

        addCustomer.setOnClickListener(this);

        searchCustomer = (Button)parent.findViewById(R.id.searchCustomer);

        searchCustomer.setOnClickListener(this);

        loadData();

        return parent;
    }

    private void loadData() {
        String search = searchCustomerText.getText().toString();

        List<Customer> customers = new ArrayList<Customer>();

        if (!search.isEmpty() && search != null && search != "") {
            customers = Customer.findWithQuery(Customer.class, "SELECT * FROM CUSTOMER WHERE Name LIKE '%" + search + "%' ORDER BY Name ASC");
        } else {
            customers = Customer.findWithQuery(Customer.class, "SELECT * FROM CUSTOMER ORDER BY Name ASC");
        }

        customerDataAdapter = new CustomerDataAdapter(this, customers);

        listCustomer.setAdapter(customerDataAdapter);
    }

    public void navigationUpdate(Customer customer) {
        if (customer != null) {
            Intent intent = new Intent(parent.getContext(), InsertOrUpdateCustomer.class);

            intent.putExtra(Constants.CUSTOMER_CODE_KEY, customer.getCode());
            intent.putExtra(Constants.CUSTOMER_NAME_KEY, customer.getName());
            intent.putExtra(Constants.CUSTOMER_ADDRESS_KEY, customer.getAddress());
            intent.putExtra(Constants.CUSTOMER_DATEOFBIRTH_KEY, customer.getDateOfBirth());

            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }

    private void navigation(View view) {
        if (view == addCustomer) {
            Intent intent = new Intent(parent.getContext(), InsertOrUpdateCustomer.class);

            startActivityForResult(intent, Constants.REQUEST_CODE);
        } else if (view == searchCustomer) {
            loadData();
        }
    }

    public void deleteCustomer(String id) {
        List<Customer> customers = Customer.findWithQuery(Customer.class, "SELECT * FROM CUSTOMER WHERE code = ?", id);

        if (customers != null) {
            if (customers.size() == 1) {
                Customer customer = customers.get(0);

                customer.delete();

                loadData();
            }
        }
    }

    @Override
    public void onClick(View v) {
        navigation(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == Constants.REQUEST_CODE) {
            Bundle bundle = data.getExtras();

            if (bundle != null) {
                String type = bundle.getString(Constants.TYPE_KEY);

                if (type != null && type != "") {
                    loadData();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
