package com.example.multiselectapplicationtest;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiSelectActivity extends AppCompatActivity implements ActionMode.Callback, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView textView;
    private ScanLogRvAdapter mAdapter;
    private boolean isMultiSelect = false;
    private List<DataModel> mDataList = new ArrayList<DataModel>();
    private Context mContext;
    private List<String> mSelectedPositions = new ArrayList<>();
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_select);
        mContext = MultiSelectActivity.this;


        swipeRefreshLayout = findViewById(R.id.swipe_ref_lay);
        recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.empty_text);

        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ScanLogRvAdapter(mContext, mDataList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        addNewDataToList();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
                    //if multiple selection is enabled then select item on single click else perform normal click on item.
                    multiSelect(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    mSelectedPositions = new ArrayList<>();
                    isMultiSelect = true;

                    if (actionMode == null) {
                        actionMode = startActionMode(MultiSelectActivity.this); //show ActionMode.
                    }
                }

                multiSelect(position);
            }
        }));

    }


    private void multiSelect(int position) {
        if (position < mDataList.size()) {
            DataModel data = mDataList.get(position);
            if (data != null) {
                if (actionMode != null) {

                    data.toggle();


                    if (mSelectedPositions.contains(String.valueOf(position)))
                        mSelectedPositions.remove(String.valueOf(position));
                    else
                        mSelectedPositions.add(String.valueOf(position));

                    if (mSelectedPositions.size() > 0)
                        actionMode.setTitle(String.valueOf(mSelectedPositions.size())); //show selected item count on action mode.
                    else {
                        actionMode.setTitle(""); //remove item count from action mode.
                        actionMode.finish(); //hide action mode.
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    private void addNewDataToList() {
        mDataList.clear();
        mSelectedPositions.clear();

        mDataList.add(new DataModel("1", "text1"));
        mDataList.add(new DataModel("2", "text2"));
        mDataList.add(new DataModel("3", "text3"));
        mDataList.add(new DataModel("4", "text4"));
        mDataList.add(new DataModel("5", "text5"));
        mDataList.add(new DataModel("6", "text6"));
        mDataList.add(new DataModel("7", "text7"));
        mDataList.add(new DataModel("8", "text8"));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_delete:
                //just to show selected items.
                StringBuilder stringBuilder = new StringBuilder();



                /*for (DataModel data : mDataList) {
                    if (data.isSelected()) {
                        stringBuilder.append("\n").append(data.getTest1());
                        *//*if (mDataList.contains(data))
                            mDataList.remove(data);*//*
                    }
                }*/


                Iterator<DataModel> itr = mDataList.iterator();
                while (itr.hasNext()) {
                    DataModel dataModel = itr.next();
                    if (dataModel.isSelected()) {
                        stringBuilder.append("\n").append(dataModel.getTest1());
                        itr.remove();
                    }
                }


                mSelectedPositions.clear();

                if (actionMode != null) {
                    actionMode.setTitle(""); //remove item count from action mode.
                    actionMode.finish();
                }

                mAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Selected items are :" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        isMultiSelect = false;
        if (mDataList != null) {
            for (DataModel dataModel : mDataList) {
                dataModel.setSelected(false);
            }
            mAdapter.notifyDataSetChanged();
            mSelectedPositions.clear();
        }
        Log.d("myTag", "onDestroyActionMode: ");
    }

    @Override
    public void onRefresh() {
        addNewDataToList();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (actionMode != null) {
            actionMode.setTitle(""); //remove item count from action mode.
            actionMode.finish();
        }
    }
}
