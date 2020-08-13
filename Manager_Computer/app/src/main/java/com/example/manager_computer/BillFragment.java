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

public class BillFragment extends Fragment implements View.OnClickListener {
    private Button addBill;
    private View parent;
    private EditText searchBillText;
    private RecyclerView listBill;
    private BillDataAdapter billDataAdapter;
    private Button searchBill;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_bill, container, false);

        listBill = (RecyclerView)parent.findViewById(R.id.listBill);

        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.VERTICAL, false);

        listBill.setLayoutManager(layoutManager);
        listBill.setHasFixedSize(true);

        searchBillText = (EditText)parent.findViewById(R.id.searchBillText);

        addBill = (Button)parent.findViewById(R.id.addBill);

        addBill.setOnClickListener(this);

        searchBill = (Button)parent.findViewById(R.id.searchBill);

        searchBill.setOnClickListener(this);

        loadData();

        return parent;
    }

    private void loadData() {
        String search = searchBillText.getText().toString();

        List<Bill> bills = new ArrayList<Bill>();

        if (!search.isEmpty() && search != null && search != "") {
            bills = Bill.findWithQuery(Bill.class, "SELECT * FROM BILL WHERE Code LIKE '%" + search + "%' ORDER BY Code ASC");
        } else {
            bills = Bill.findWithQuery(Bill.class, "SELECT * FROM BILL ORDER BY Code ASC");
        }

        billDataAdapter = new BillDataAdapter(this, bills);

        listBill.setAdapter(billDataAdapter);
    }

    public void navigationUpdate(Bill bill) {
        if (bill != null) {
            Intent intent = new Intent(parent.getContext(), InsertOrUpdateBill.class);

            intent.putExtra(Constants.BILL_CODE_KEY, bill.getCode());
            intent.putExtra(Constants.BILL_CREATEDDATE_KEY, bill.getCreateDate());
            intent.putExtra(Constants.CUSTOMER_CODE_KEY, bill.getCustomerCode());

            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }

    private void navigation(View view) {
        if (view == addBill) {
            Intent intent = new Intent(parent.getContext(), InsertOrUpdateBill.class);

            startActivityForResult(intent, Constants.REQUEST_CODE);
        } else if (view == searchBill) {
            loadData();
        }
    }

    public void deleteBill(String id) {
        List<Bill> bills = Bill.findWithQuery(Bill.class, "SELECT * FROM BILL WHERE code = ?", id);

        if (bills != null) {
            if (bills.size() == 1) {
                Bill bill = bills.get(0);

                bill.delete();

                Detail.deleteAll(Detail.class, "bill_code = ?", id);

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
