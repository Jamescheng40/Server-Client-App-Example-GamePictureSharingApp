package com.example.new_application;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;



public class SearchPageFragment extends Fragment implements SPgetsearchresult.Asynresponse {

    private EditText textview;
    List<String> listobj = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        textview = (EditText) textview.findViewById(R.id.editText7);
        SPgetsearchresult result = new SPgetsearchresult(textview.getText().toString(), this);
        result.execute();
      //  result.execute();


        //if(textview.getText().equals(""))



        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.sp_recyclerview,container,false);

//setting up linearmanager;a must step for initiating recycleview; refer to documentation for more details
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        while(listobj == null){}
       // SearchViewRecyclerViewAdapter adapter = new SearchViewRecyclerViewAdapter(this,);
        //rv.setAdapter(adapter);

        return rv;

    }


    @Override
    public void processFinish(List<String> output) {
        listobj = output;
    }
}

