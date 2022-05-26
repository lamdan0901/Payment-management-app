package com.example.sqlite.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite.ModifyItemActivity;
import com.example.sqlite.R;
import com.example.sqlite.adapter.RecyclerViewAdapter;
import com.example.sqlite.dal.SQLiteHelper;
import com.example.sqlite.model.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentHome extends Fragment implements RecyclerViewAdapter.ItemListener, RecyclerViewAdapter.ContextItemListener {
    private RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    private SQLiteHelper db;
    private TextView tvTong;
    private Spinner spViewType;
    private final String [] viewTypes = {"Chi tiêu hôm nay", "Toàn bộ chi tiêu"};

    Date d = new Date();
    SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycleView);
        tvTong = view.findViewById(R.id.tvTong);
        recyclerViewAdapter = new RecyclerViewAdapter();
        db = new SQLiteHelper(getContext());

        // Init view type spinner items
        spViewType = view.findViewById(R.id.spViewType);
        spViewType.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_spinner, viewTypes));

        List<Item> list = db.getByDate(f.format(d));
        tvTong.setText("Tổng tiền: " + sumPrice(list) + "K");
        recyclerViewAdapter.setList(list);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setItemListener(this);
        recyclerViewAdapter.setContextItemListener(this);

        spViewType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String category = spViewType.getItemAtPosition(position).toString();
                List<Item> list;

                if (category.equalsIgnoreCase("Toàn bộ chi tiêu")) {
                    list = db.getAll();
                } else {
                    list = db.getByDate(f.format(d));
                }
                recyclerViewAdapter.setList(list);
                tvTong.setText("Tổng tiền: " + sumPrice(list) + "K");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private int sumPrice(List<Item> list) {
        int sum = 0;
        for (Item i : list) {
            sum += Integer.parseInt(i.getPrice());
        }
        return sum;
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Item item=recyclerViewAdapter.getItem(position);
        Intent intent=new Intent(getActivity(), ModifyItemActivity.class);
        intent.putExtra(  "item", item);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Item> list;

        if(spViewType.getSelectedItem().toString().equals("Toàn bộ chi tiêu")){
            list = db.getAll();
        } else{
            list = db.getByDate(f.format(d));
        }

        recyclerViewAdapter.setList(list);
        tvTong.setText("Tổng tiền: " + sumPrice(list) + "K");
    }

    @Override
    public void onContextItemClickListener(int action, int position) {
        Item item = recyclerViewAdapter.getItem(position);

        if (action == 1) {
            Intent intent = new Intent(getActivity(), ModifyItemActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc muốn xóa " + item.getTitle() + " không?");
            builder.setIcon(R.drawable.ic_remove);

            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                SQLiteHelper db = new SQLiteHelper(getContext());
                db.deleteItem(item.getId());
                onResume();
            });

            builder.setNegativeButton("Không", (dialogInterface, i) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
