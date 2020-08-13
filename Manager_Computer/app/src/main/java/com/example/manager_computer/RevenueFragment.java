package com.example.manager_computer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class RevenueFragment extends Fragment {
    private View parent;
    private Button revenueFilter;
    private DatePicker revenueMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_revenue, container, false);

        revenueFilter = (Button)parent.findViewById(R.id.revenueFilter);
        revenueMonth = (DatePicker)parent.findViewById(R.id.revenueMonth);

        revenueFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = revenueMonth.getYear();
                int month = revenueMonth.getMonth() + 1;
                String monthString = String.valueOf(month);

                monthString = monthString.length() == 1 ? "0" + monthString : monthString;

                List<Bill> bills = Bill.findWithQuery(Bill.class, "SELECT * FROM BILL WHERE strftime('%m', date(created_date)) = '" + monthString + "'");

                //List<Bill> bills = Bill.findWithQuery(Bill.class, "SELECT * FROM BILL");

                for (int i = 0; i < bills.size(); i++) {
                    System.out.println(bills.get(i).getCreateDate());
                }
            }
        });

        return parent;
    }
}