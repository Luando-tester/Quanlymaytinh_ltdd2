package com.example.manager_computer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class ServiceDataAdapter extends RecyclerView.Adapter<ServiceDataAdapter.DataViewHolder> {
    private List<Service> services;
    private ServiceFragment serviceFragment;

    public ServiceDataAdapter(ServiceFragment serviceFragment, List<Service> services) {
        this.serviceFragment = serviceFragment;
        this.services = services;
    }

    @Override
    public int getItemCount() {
        return services == null ? 0 : services.size();
    }

    @Override
    public ServiceDataAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.itemService);

        if (viewType == 0) {
            layout.setBackgroundResource(R.color.colorAqua);
        } else {
            layout.setBackgroundResource(R.color.colorAqua2);
        }

        return new DataViewHolder(view, serviceFragment);
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
    public void onBindViewHolder(ServiceDataAdapter.DataViewHolder holder, int position) {
        if (services != null) {
            Service service = services.get(position);

            if (service != null) {
                System.out.println(service.getUrlImage());
                holder.itemServiceCode.setText(service.getCode());
                holder.itemServiceName.setText(service.getName());
                holder.itemServiceUnit.setText(service.getUnit());
                holder.itemServicePrice.setText(service.getPrice().toString());

                String filePathImage = service.getUrlImage();

                if (filePathImage != null && filePathImage != "") {
                    File imageFile = new  File(filePathImage);
                    if (imageFile != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                        if (bitmap != null) {
                            holder.itemServiceImage.setTag(filePathImage);
                            holder.itemServiceImage.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView itemServiceCode;
        private TextView itemServiceName;
        private TextView itemServiceUnit;
        private TextView itemServicePrice;
        private ServiceFragment _serviceFragment;
        private Button buttonDelete;
        private ImageView itemServiceImage;

        public DataViewHolder(View view, ServiceFragment serviceFragment) {
            super(view);

            buttonDelete = (Button)view.findViewById(R.id.buttonDelete);

            itemServiceCode = (TextView)view.findViewById(R.id.itemServiceCode);
            itemServiceName = (TextView)view.findViewById(R.id.itemServiceName);
            itemServiceUnit = (TextView)view.findViewById(R.id.itemServiceUnit);
            itemServicePrice = (TextView)view.findViewById(R.id.itemServicePrice);
            itemServiceImage = (ImageView)view.findViewById(R.id.itemServiceImage);

            this._serviceFragment = serviceFragment;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (_serviceFragment != null) {
                    String code = itemServiceCode.getText().toString();
                    String name = itemServiceName.getText().toString();
                    String unit = itemServiceUnit.getText().toString();
                    String price = itemServicePrice.getText().toString();
                    String urlImage = itemServiceImage.getTag() != null && itemServiceImage.getTag() != "" ? itemServiceImage.getTag().toString() : "";
                    Double priceFormat = Double.parseDouble(price == null || price == "" ? "0" : price);

                    Service service = new Service();
                    service.setCode(code);
                    service.setName(name);
                    service.setUnit(unit);
                    service.setUrlImage(urlImage);
                    service.setPrice(priceFormat);

                    _serviceFragment.navigationUpdate(service);
                }
                }
            });

            buttonDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String id = itemServiceCode.getText().toString();
                    new AlertDialog.Builder(_serviceFragment.getContext()).setTitle("Xóa dịch vụ")
                            .setMessage("Bạn có chắc chắn muốn xóa dịch vụ mã: " + id)
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                String id = itemServiceCode.getText().toString();
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (_serviceFragment != null) {
                                        _serviceFragment.deleteService(id);
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
