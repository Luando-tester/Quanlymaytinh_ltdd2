package com.example.manager_computer;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BillDetailDataAdapter extends RecyclerView.Adapter<BillDetailDataAdapter.DataViewHolder> {
    private List<Detail> billDetails;
    private InsertOrUpdateBill insertOrUpdateBill;

    public BillDetailDataAdapter(InsertOrUpdateBill insertOrUpdateBill, List<Detail> billDetails) {
        this.insertOrUpdateBill = insertOrUpdateBill;
        this.billDetails = billDetails;
    }

    @Override
    public int getItemCount() {
        return billDetails == null ? 0 : billDetails.size();
    }

    @Override
    public BillDetailDataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_detail, parent, false);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.itemBillDetail);

        if (viewType == 0) {
            layout.setBackgroundResource(R.color.colorAqua);
        } else {
            layout.setBackgroundResource(R.color.colorAqua2);
        }

        return new BillDetailDataAdapter.DataViewHolder(view, insertOrUpdateBill);
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
    public void onBindViewHolder(BillDetailDataAdapter.DataViewHolder holder, int position) {
        if (billDetails != null) {
            Detail billDetail = billDetails.get(position);

            if (billDetail != null) {
                holder.itemBillDetailCode.setText(billDetail.getCode());
                holder.itemBillCode.setText(billDetail.getBillCode());

                List<Service> services = Service.findWithQuery(Service.class, "SELECT * FROM SERVICE WHERE code = '" + billDetail.getServiceCode() + "'");

                if (services != null) {
                    if (services.size() == 1) {
                        holder.itemBillDetailServiceName.setText(services.get(0).getName());
                    } else {
                        holder.itemBillDetailServiceName.setText("Không xác định");
                    }
                } else {
                    holder.itemBillDetailServiceName.setText("Không xác định");
                }

                holder.itemBillDetailQuantity.setText(String.valueOf(billDetail.getQuantity()));
                holder.itemBillDetailServiceCode.setText(billDetail.getServiceCode());
            }
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView itemBillDetailCode;
        private TextView itemBillDetailServiceName;
        private TextView itemBillDetailQuantity;
        private TextView itemBillDetailServiceCode;
        private TextView itemBillCode;
        private InsertOrUpdateBill _insertOrUpdateBill;
        private Button buttonDelete;

        public DataViewHolder(View view, InsertOrUpdateBill insertOrUpdateBill) {
            super(view);

            buttonDelete = (Button)view.findViewById(R.id.buttonDelete);

            itemBillDetailCode = (TextView)view.findViewById(R.id.itemBillDetailCode);
            itemBillDetailServiceName = (TextView)view.findViewById(R.id.itemBillDetailServiceName);
            itemBillDetailQuantity = (TextView)view.findViewById(R.id.itemBillDetailQuantity);
            itemBillDetailServiceCode = (TextView)view.findViewById(R.id.itemBillDetailServiceCode);
            itemBillCode = (TextView)view.findViewById(R.id.itemBillCode);

            this._insertOrUpdateBill = insertOrUpdateBill;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_insertOrUpdateBill != null) {
                        String code = itemBillDetailCode.getText().toString();
                        String quantity = itemBillDetailQuantity.getText().toString();
                        String serviceCode = itemBillDetailServiceCode.getText().toString();
                        String billCode = itemBillCode.getText().toString();

                        int parseQuantity = quantity != null && quantity != null ? Integer.parseInt(quantity) : 0;

                        Detail billDetail = new Detail();
                        billDetail.setCode(code);
                        billDetail.setQuantity(parseQuantity);
                        billDetail.setServiceCode(serviceCode);
                        billDetail.setBillCode(billCode);

                        _insertOrUpdateBill.navigationUpdate(billDetail);
                    }
                }
            });

            buttonDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String id = itemBillDetailCode.getText().toString();
                    new AlertDialog.Builder(_insertOrUpdateBill).setTitle("Xóa chi tiết phát sinh")
                            .setMessage("Bạn có chắc chắn muốn xóa chi tiết phát sinh mã: " + id)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                String id = itemBillDetailCode.getText().toString();
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (_insertOrUpdateBill != null) {
                                        _insertOrUpdateBill.deleteBillDetail(id);
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
