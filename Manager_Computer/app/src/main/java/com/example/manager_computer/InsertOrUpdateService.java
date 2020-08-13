package com.example.manager_computer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.util.List;

public class InsertOrUpdateService extends AppCompatActivity {
    private Spinner serviceUnit;
    private String editID;
    private EditText serviceName;
    private EditText servicePrice;
    private Button saveService;
    private  ArrayAdapter<String> adapterServiceUnit;
    private Button serviceChooseImage;
    private String filePathImage;
    private ImageView serviceImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.insert_or_update_service);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        serviceName = (EditText)findViewById(R.id.serviceName);

        servicePrice = (EditText)findViewById(R.id.servicePrice);

        serviceUnit = (Spinner)findViewById(R.id.serviceUnit);

        saveService = (Button)findViewById(R.id.saveService);

        serviceChooseImage = (Button)findViewById(R.id.serviceChooseImage);

        serviceImage = (ImageView)findViewById(R.id.serviceImage);

        saveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertOrUpdate();
            }
        });
        serviceChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
                chooseImage.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent choosen = Intent.createChooser(chooseImage, "Select Picture");

                choosen.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { pickIntent });

                startActivityForResult(choosen, 2);
            }
        });

        adapterServiceUnit = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.SERVICE_UNITS);

        serviceUnit.setAdapter(adapterServiceUnit);

        if (bundle != null) {
            editID = bundle.getString(Constants.SERVICE_CODE_KEY);
            serviceName.setText(bundle.getString(Constants.SERVICE_NAME_KEY));

            String unit = bundle.getString(Constants.SERVICE_UNIT_KEY);

            if (unit != null && unit != "") {
                int spinnerPosition = adapterServiceUnit.getPosition(unit);

                if (spinnerPosition > -1) {
                    serviceUnit.setSelection(spinnerPosition);
                }
            }

            servicePrice.setText(String.valueOf(bundle.getDouble(Constants.SERVICE_PRICE_KEY)));

            filePathImage = bundle.getString(Constants.SERVICE_URL_IMAGE_KEY);

            if (filePathImage != null && filePathImage != "") {
                File imageFile = new File(filePathImage);
                if (imageFile != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                    if (bitmap != null) {
                        serviceImage.setImageBitmap(bitmap);
                    }
                }
            }

            setTitle("Sửa dịch vụ: " + editID);
        } else {
            setTitle("Thêm dịch vụ");
        }
    }

    private void insertOrUpdate() {
        String name = serviceName.getText().toString();
        String price = servicePrice.getText().toString();
        Object unit = serviceUnit.getSelectedItem();

        if (filePathImage == null || filePathImage == "") {
            Toast.makeText(this, "Chưa chọn hình ảnh dịch vụ", Toast.LENGTH_SHORT).show();

            return;
        }

        if (name.isEmpty()) {
            Toast.makeText(this, "Tên dịch vụ không được bỏ trống", Toast.LENGTH_SHORT).show();

            if (serviceName.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(serviceName, InputMethodManager.SHOW_IMPLICIT);
            }

            return;
        }

        if (price.isEmpty()) {
            Toast.makeText(this, "Giá dịch vụ không được bỏ trống", Toast.LENGTH_SHORT).show();

            if (servicePrice.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(servicePrice, InputMethodManager.SHOW_IMPLICIT);
            }

            return;
        }

        Double parsePrice = Double.parseDouble(price);

        if (parsePrice < 0) {
            Toast.makeText(this, "Giá dịch vụ không được nhỏ hơn 0", Toast.LENGTH_SHORT).show();

            if (servicePrice.requestFocus()) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(servicePrice, InputMethodManager.SHOW_IMPLICIT);
            }

            return;
        }

        if (unit == null || unit == "") {
            Toast.makeText(this, "Chưa chọn đơn vị tính", Toast.LENGTH_SHORT).show();

            return;
        }

        if (editID == null || editID == "") {
            // Insert
            Service service = new Service(name, unit.toString(), parsePrice, filePathImage);

            service.save();

            Toast.makeText(this, "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            // Update
            List<Service> services = Service.findWithQuery(Service.class, "SELECT * FROM SERVICE WHERE code = ?", editID);

            if (services != null) {
                if (services.size() == 1) {
                    Service service = services.get(0);

                    service.setName(name);
                    service.setUnit(unit.toString());
                    service.setPrice(parsePrice);
                    service.setUrlImage(filePathImage);
                    service.save();

                    Toast.makeText(this, "Sửa dịch vụ thành công", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            serviceImage.setImageURI(selectedImage);

            filePathImage = getRealPathFromURI(selectedImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
