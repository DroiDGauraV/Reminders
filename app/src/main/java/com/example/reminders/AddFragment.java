package com.example.reminders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;


public class AddFragment extends Fragment{

    FloatingActionButton btn_done;
    FloatingActionButton btn_cancel;
    EditText etMsg;
    EditText etHead;
    TextView date_view;
    TextView time_view;
    Button btn_setDate;
    Button btn_setTime;


    //id is stored here
    String id = "";
    //date is stored here
    String date = "";
    //time is stored here
    String time = "";
    //head is stored here
    String head = "";
    //msg is stored here
    String msg = "";

    int mDayOfMonth;
    int mMonth;
    int mYear;


    DatePickerDialog.OnDateSetListener setListener;

    Bundle bundle;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_add, container, false);

        etMsg = view.findViewById(R.id.view_etMsg);
        etHead = view.findViewById(R.id.view_etHead);
        date_view = view.findViewById(R.id.tv_date);
        time_view = view.findViewById(R.id.tv_time);
        btn_setDate = view.findViewById(R.id.btn_setDate);
        btn_setTime = view.findViewById(R.id.btn_setTime);

        btn_done = view.findViewById(R.id.view_fragAdd);
        btn_cancel = view.findViewById(R.id.view_fragCancel);


        bundle = getArguments();

        if (bundle != null) {
            String id = bundle.getString("id");
            etHead.setText( ((MainActivity) Objects.requireNonNull(getActivity())).getRemDataFromActById(id,"HEAD"));
            etMsg.setText( ((MainActivity) Objects.requireNonNull(getActivity())).getRemDataFromActById(id,"MSG"));
            date_view.setText( ((MainActivity) Objects.requireNonNull(getActivity())).getRemDataFromActById(id,"DATE"));
            time_view.setText( ((MainActivity) Objects.requireNonNull(getActivity())).getRemDataFromActById(id,"TIME"));
        }

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar calendar = Calendar.getInstance();

        btn_done.setOnClickListener(v -> {
            //to pass data from one fragment to another
            msg = etMsg.getText().toString();
            head = etHead.getText().toString();
            date = date_view.getText().toString();
            time = time_view.getText().toString();

            ListFragment listFragment = new ListFragment();

            if (!date.equals("") && !time.equals("") && !head.equals("")) {
                if (bundle != null) {
                    id = bundle.getString("id");
                    ((MainActivity) Objects.requireNonNull(getActivity())).updateBDbyAct(id, head, msg, date, time);
                    int idInt = Integer.parseInt(id);

                    startAlarm(calendar, idInt);
                } else {
                    ((MainActivity) Objects.requireNonNull(getActivity())).addDataInDB(head, msg, date, time);
                    int dataListSize = ((MainActivity) Objects.requireNonNull(getActivity())).DataListSize();
                    //remove this
                    Toast.makeText(getContext(), "New Reminder Added.", Toast.LENGTH_SHORT).show();

                    startAlarm(calendar, dataListSize);
                }


                //changing fragment here
                assert getFragmentManager() != null;
                //getFragmentManager().beginTransaction().replace(R.id.view_fl,listFragment).commit();
                changeFragment(listFragment);
            } else {
                Toast.makeText(getContext(), "Date, Time and Title must not be empty", Toast.LENGTH_SHORT).show();
            }
        });


        btn_cancel.setOnClickListener(v -> {
            ListFragment listFragment = new ListFragment();
            //assert getFragmentManager() != null;
            //getFragmentManager().beginTransaction().replace(R.id.view_fl,listFragment).commit();
            changeFragment(listFragment);
        });

        btn_setDate.setOnClickListener(v -> {
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            if (AddFragment.this.getActivity() == null) {
                Toast.makeText(AddFragment.this.getActivity(), "Context null", Toast.LENGTH_SHORT).show();
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddFragment.this.getActivity(),
                    setListener, mYear, mMonth, mDayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        setListener = (view1, year, month, dayOfMonth) -> {
            month++;
            date = dayOfMonth + "/" + month + "/" + year;
            date_view.setText(date);
        };

        btn_setTime.setOnClickListener(v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddFragment.this.getActivity(), (view12, hourOfDay, minute1) -> {
                calendar.set(mYear, mMonth, mDayOfMonth, hourOfDay, minute1);
                calendar.set(Calendar.SECOND, 0);

                time = (String) DateFormat.format("hh:mm aa", calendar);
                time_view.setText(time);
                //time_view.setText(String.valueOf(calendar.getTimeInMillis()));
            },hour, minute, DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startAlarm(Calendar c, int id) {
        ((MainActivity) Objects.requireNonNull(getActivity())).startAlarmByAct(c,head,msg,id);
    }

    public void changeFragment (Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.exit_to_below);
        transaction.replace(R.id.view_fl,fragment).commit();
    }

}