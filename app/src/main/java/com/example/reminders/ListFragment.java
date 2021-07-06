package com.example.reminders;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Objects;

public class ListFragment extends Fragment {

    ListView listView;
    TextView NoRem;
    ArrayList<RemindersItems> dataList;
    MyAdapter listAdapter;

    /*
    * i need to store the data somewhere else coz every time the fragment changes the data resets
     *
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        listView = view.findViewById(R.id.view_rlist);
        NoRem = view.findViewById(R.id.view_Norem);



        view.findViewById(R.id.view_mainfabadd).setOnClickListener(view1 -> {
            AddFragment addFragment = new AddFragment();
            FragmentManager fragmentManager = getFragmentManager();
            assert fragmentManager != null;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_below, R.anim.fade_out);
            transaction.addToBackStack(null);
            transaction.replace(R.id.view_fl,addFragment).commit();
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (dataList.size() == 0) {
            NoRem.setVisibility(View.VISIBLE);
        }
        else {
            listAdapter = new MyAdapter(getContext(), dataList);
            listAdapter.notifyDataSetChanged();
            listView.setAdapter(listAdapter);
            NoRem.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            //position is irrespective if id and when the position of list changes the id does not
            //get total count of list items currently assign the id by count+1
             TextView tv_id = view1.findViewById(R.id.tv_id);
             String id_string = tv_id.getText().toString();
             showDialogOnList(id_string);
        });

    }

    public void showDialogOnList(final String id) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View v = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        Button btn_cancel = v.findViewById(R.id.btn_cancelDialog);
        Button btn_edit = v.findViewById(R.id.btn_editDialog);
        Button btn_del = v.findViewById(R.id.btn_deleteDialog);

        alert.setView(v);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        btn_cancel.setOnClickListener(v13 -> alertDialog.dismiss());

        btn_del.setOnClickListener(v12 -> {
            ((MainActivity) Objects.requireNonNull(getActivity())).deleteDataDBbyAct(String.valueOf(id));
            dataList = ((MainActivity) Objects.requireNonNull(getActivity())).getRemDataFromAct();
            alertDialog.dismiss();
            if (dataList.size() == 0) {
                NoRem.setVisibility(View.VISIBLE);
            }
            listAdapter = new MyAdapter(getContext(), dataList);
            listAdapter.notifyDataSetChanged();
            listView.setAdapter(listAdapter);
            ((MainActivity) Objects.requireNonNull(getActivity())).cancelAlarm(id);
        });

        btn_edit.setOnClickListener(v1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            AddFragment addFragment = new AddFragment();
            addFragment.setArguments(bundle);
            alertDialog.dismiss();
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.view_fl,addFragment).commit();
        });
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataList = ((MainActivity) Objects.requireNonNull(getActivity())).getRemDataFromAct();
    }

}