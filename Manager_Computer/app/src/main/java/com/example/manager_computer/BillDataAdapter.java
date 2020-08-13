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

public class BillDataAdapter extends RecyclerView.Adapter<BillDataAdapter.DataViewHolder> {
    private List<Bill> bills;
    private BillFragment billFragment;

    public BillDataAdapter(BillFragment billFragment, List<Bill> bills) {
        this.billFragment = billFragment;
        this.bills = bills;
    }

    @Override
    public int getItemCount() {
        return bills == null ? 0 : bills.size();
    }

    @Override
    public BillDataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.itemBill);

        if (viewType == 0) {
            layout.setBackgroundResource(R.color.colorAqua);
        } else {
            layout.setBackgroundResource(R.color.colorAqua2);
        }

        return new DataViewHolder(view, billFragment);
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
    public void onBindViewHolder(BillDataAdapter.DataViewHolder holder, int position) {
        if (bills != null) {
            Bill bill = bills.get(position);

            if (bill != null) {
                holder.itemBillCode.setText(bill.getCode());
                holder.itemBillCreatedDate.setText(bill.getCreateDate() == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(bill.getCreateDate()));
                holder.itemBillCustomerCode.setText(bill.getCustomerCode());

                List<Customer> customers = Customer.findWithQuery(Customer.class, "SELECT * FROM CUSTOMER WHERE code = '" + bill.getCustomerCode() + "'");

                if (customers != null) {
                    if (customers.size() == 1) {
                        holder.itemBillCustomerName.setText(customers.get(0).getName());
                    } else {
                        holder.itemBillCustomerName.setText("Không xác định");
                    }
                } else {
                    holder.itemBillCustomerName.setText("Không xác định");
                }

                List<Detail> billDetails = Detail.findWithQuery(Detail.class, "SELECT * FROM DETAIL WHERE bill_code = '" + bill.getCode() + "'");

                Double total = 0.0;

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

                holder.itemBillTotal.setText(total.toString());
            }
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView itemBillCode;
        private TextView itemBillCreatedDate;
        private TextView itemBillCustomerCode;
        private BillFragment _billFragment;
        private TextView itemBillCustomerName;
        private Button buttonDelete;
        private TextView itemBillTotal;

        public DataViewHolder(View view, BillFragment billFragment) {
            super(view);

            buttonDelete = (Button)view.findViewById(R.id.buttonDelete);

            itemBillCode = (TextView)view.findViewById(R.id.itemBillCode);
            itemBillCreatedDate = (TextView)view.findViewById(R.id.itemBillCreateDate);
            itemBillCustomerName = (TextView)view.findViewById(R.id.itemBillCustomerName);
            itemBillCustomerCode = (TextView)view.findViewById(R.id.itemBillCustomerCode);
            itemBillTotal = (TextView)view.findViewById(R.id.itemBillTotal);

            this._billFragment = billFragment;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_billFragment != null) {
                        String code = itemBillCode.getText().toString();
                        String createdDate = itemBillCreatedDate.getText().toString();
                        String customerCode = itemBillCustomerCode.getText().toString();
                        Date createdDateFormat = null;
                        try {
                            createdDateFormat = createdDate != null && createdDate != "" ? new SimpleDateFormat("dd/MM/yyyy").parse(createdDate) : null;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Bill bill = new Bill();
                        bill.setCode(code);
                        bill.setCreatedDate(createdDateFormat);
                        bill.setCustomerCode(customerCode);

                        _billFragment.navigationUpdate(bill);
                    }
                }
            });

            buttonDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String id = itemBillCode.getText().toString();
                    new AlertDialog.Builder(_billFragment.getContext()).setTitle("Xóa phiếu phát sinh")
                            .setMessage("Bạn có chắc chắn muốn xóa phiếu phát sinh mã: " + id)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                String id = itemBillCode.getText().toString();
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (_billFragment != null) {
                                        _billFragment.deleteBill(id);
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
