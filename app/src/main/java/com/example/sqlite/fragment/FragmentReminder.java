package com.example.sqlite.fragment;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sqlite.PickTimeActivity;
import com.example.sqlite.R;
import com.example.sqlite.dal.SQLiteHelper;


public class FragmentReminder extends Fragment {
    private Button btSave, btOpenPicker;
    private EditText reminderET;
    private TextView reminderTV, timeTV;
    private SQLiteHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        db = new SQLiteHelper(getContext());
        reminderTV.setText(db.getReminder());
        timeTV.setText(db.getReminderTime());

        btSave.setOnClickListener(View -> {
            String reminderText = reminderET.getText().toString();
            db.setReminder(reminderText);
            reminderTV.setText(reminderText);
            Toast.makeText(getContext(), "Đã lưu nhắc nhở", Toast.LENGTH_SHORT).show();
        });

        reminderTV.setOnClickListener(View -> reminderET.setText(reminderTV.getText()));

        btOpenPicker.setOnClickListener(View -> {
            startActivity(new Intent(getActivity(), PickTimeActivity.class));
        });
    }

    private void initView(View view) {
        btSave = view.findViewById(R.id.btSave);
        btOpenPicker = view.findViewById(R.id.btOpenPicker);
        reminderET = view.findViewById(R.id.reminderET);
        reminderTV = view.findViewById(R.id.reminderTV);
        timeTV = view.findViewById(R.id.timeTV);
    }

    @Override
    public void onResume() {
        super.onResume();
        timeTV.setText(db.getReminderTime());
    }
}
