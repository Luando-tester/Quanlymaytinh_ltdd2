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

public class ServiceFragment extends Fragment implements View.OnClickListener {
    private Button addService;
    private View parent;
    private EditText searchServiceText;
    private RecyclerView listService;
    private ServiceDataAdapter serviceDataAdapter;
    private Button searchService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_service, container, false);

        listService = (RecyclerView)parent.findViewById(R.id.listService);

        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.VERTICAL, false);

        listService.setLayoutManager(layoutManager);
        listService.setHasFixedSize(true);

        searchServiceText = (EditText)parent.findViewById(R.id.searchServiceText);

        addService = (Button)parent.findViewById(R.id.addService);

        addService.setOnClickListener(this);

        searchService = (Button)parent.findViewById(R.id.searchService);

        searchService.setOnClickListener(this);

        loadData();

        return parent;
    }

    private void loadData() {
        String search = searchServiceText.getText().toString();

        List<Service> services = new ArrayList<Service>();

        if (!search.isEmpty() && search != null && search != "") {
            services = Service.findWithQuery(Service.class, "SELECT * FROM SERVICE WHERE Name LIKE '%" + search + "%' ORDER BY Name ASC");
        } else {
            services = Service.findWithQuery(Service.class, "SELECT * FROM SERVICE ORDER BY Name ASC");
        }

        serviceDataAdapter = new ServiceDataAdapter(this, services);

        listService.setAdapter(serviceDataAdapter);
    }

    public void deleteService(String id) {
        List<Service> services = Service.findWithQuery(Service.class, "SELECT * FROM SERVICE WHERE code = ?", id);

        if (services != null) {
            if (services.size() == 1) {
                Service service = services.get(0);

                service.delete();

                loadData();
            }
        }
    }

    public void navigationUpdate(Service service) {
        if (service != null) {
            Intent intent = new Intent(parent.getContext(), InsertOrUpdateService.class);

            intent.putExtra(Constants.SERVICE_CODE_KEY, service.getCode());
            intent.putExtra(Constants.SERVICE_NAME_KEY, service.getName());
            intent.putExtra(Constants.SERVICE_PRICE_KEY, service.getPrice());
            intent.putExtra(Constants.SERVICE_UNIT_KEY, service.getUnit());
            intent.putExtra(Constants.SERVICE_URL_IMAGE_KEY, service.getUrlImage());

            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }

    private void navigation(View view) {
        if (view == addService) {
            Intent intent = new Intent(parent.getContext(), InsertOrUpdateService.class);

            startActivityForResult(intent, Constants.REQUEST_CODE);
        } else if (view == searchService) {
            loadData();
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
