package com.example.manager_computer;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerDataAdapter extends RecyclerView.Adapter<CustomerDataAdapter.DataViewHolder> {
    private List<Customer> customers;
    private CustomerFragment customerFragment;

    public CustomerDataAdapter(CustomerFragment customerFragment, List<Customer> customers) {
        this.customerFragment = customerFragment;
        this.customers = customers;
    }

    @Override
    public int getItemCount() {
        return customers == null ? 0 : customers.size();
    }

    @Override
    public CustomerDataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.itemCustomer);

        if (viewType == 0) {
            layout.setBackgroundResource(R.color.colorAqua);
        } else {
            layout.setBackgroundResource(R.color.colorAqua2);
        }

        return new DataViewHolder(view, customerFragment);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(CustomerDataAdapter.DataViewHolder holder, int position) {
        if (customers != null) {
            Customer customer = customers.get(position);

            if (customer != null) {
                holder.itemCustomerCode.setText(customer.getCode());
                holder.itemCustomerName.setText(customer.getName());
                holder.itemCustomerAddress.setText(customer.getAddress());
                holder.itemCustomerDateOfBirth.setText(customer.getDateOfBirth() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(customer.getDateOfBirth()));
            }
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCustomerCode;
        private TextView itemCustomerName;
        private TextView itemCustomerAddress;
        private TextView itemCustomerDateOfBirth;
        private CustomerFragment _customerFragment;
        private Button buttonDelete;

        public DataViewHolder(View view, CustomerFragment customerFragment) {
            super(view);

            itemCustomerCode = (TextView)view.findViewById(R.id.itemCustomerCode);
            itemCustomerName = (TextView)view.findViewById(R.id.itemCustomerName);
            itemCustomerAddress = (TextView)view.findViewById(R.id.itemCustomerAddress);
            itemCustomerDateOfBirth = (TextView)view.findViewById(R.id.itemCustomerDateOfBirth);

            buttonDelete = (Button)view.findViewById(R.id.buttonDelete);

            this._customerFragment = customerFragment;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_customerFragment != null) {
                        String code = itemCustomerCode.getText().toString();
                        String name = itemCustomerName.getText().toString();
                        String address = itemCustomerAddress.getText().toString();
                        String dateOfBirth = itemCustomerDateOfBirth.getText().toString();
                        Date dateOfBirthFormat = null;
                        try {
                            dateOfBirthFormat = dateOfBirth != null && dateOfBirth != "" ? new SimpleDateFormat("dd/MM/yyyy").parse(dateOfBirth) : null;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Customer customer = new Customer();
                        customer.setCode(code);
                        customer.setName(name);
                        customer.setAddress(address);
                        customer.setDateOfBirth(dateOfBirthFormat);

                        _customerFragment.navigationUpdate(customer);
                    }
                }
            });

            buttonDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String id = itemCustomerCode.getText().toString();
                    new AlertDialog.Builder(_customerFragment.getContext()).setTitle("Xóa khách hàng")
                            .setMessage("Bạn có chắc chắn muốn xóa khách hàng mã: " + id)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                String id = itemCustomerCode.getText().toString();
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (_customerFragment != null) {
                                        _customerFragment.deleteCustomer(id);
                                    }
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();

                    return true;
                }
            });
        }
    }
}
