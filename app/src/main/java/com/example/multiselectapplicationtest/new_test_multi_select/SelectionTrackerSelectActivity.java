package com.example.multiselectapplicationtest.new_test_multi_select;

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

import com.example.multiselectapplicationtest.DataModel;
import com.example.multiselectapplicationtest.MultiSelectActivity;
import com.example.multiselectapplicationtest.R;
import com.example.multiselectapplicationtest.RecyclerItemClickListener;
import com.example.multiselectapplicationtest.ScanLogRvAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectionTrackerSelectActivity extends AppCompatActivity implements  ActionMode.Callback, SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private ActionMode actionMode;
    private List<DataModel> mData = new ArrayList<DataModel>();
    private ScanLogRvAdapter adapter;
    private TextView textView;
    private RecyclerView recyclerView;
    private boolean isMultiSelect = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_tracker_select);
        mContext = SelectionTrackerSelectActivity.this;

        swipeRefreshLayout = findViewById(R.id.swipe_ref_lay);
        recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.empty_text);

        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new ScanLogRvAdapter(mContext, mData);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

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
                    isMultiSelect = true;

                    if (actionMode == null) {
                        actionMode = startActionMode(SelectionTrackerSelectActivity.this); //show ActionMode.
                    }
                }

                multiSelect(position);
            }
        }));


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
                for (DataModel data : mData) {
                    if (data.isSelected())
                        stringBuilder.append("\n").append(data.getTest1());
                }
                Toast.makeText(this, "Selected items are :" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        isMultiSelect = false;
        if (mData != null) {
            for (DataModel dataModel : mData) {
                dataModel.setSelected(false);
            }
            adapter.notifyDataSetChanged();
        }
        Log.d("myTag", "onDestroyActionMode: ");
    }

    @Override
    public void onRefresh() {
        mData.clear();
        addNewDataToList();
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (actionMode != null) {
            actionMode.setTitle(""); //remove item count from action mode.
            actionMode.finish();
        }
    }

    private void multiSelect(int position) {
        if (position < mData.size()) {
            DataModel data = mData.get(position);
            if (data != null) {
                if (actionMode != null) {

                    data.toggle();

                    int count = 0;

                    for (DataModel dataModel : mData) {
                        if (dataModel.isSelected()) {
                            count = count + 1;
                        }
                    }

                    if (count > 0)
                        actionMode.setTitle(String.valueOf(count)); //show selected item count on action mode.
                    else {
                        actionMode.setTitle(""); //remove item count from action mode.
                        actionMode.finish(); //hide action mode.
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        }
    }


    private void addNewDataToList() {
        mData.add(new DataModel("1", "text1"));
        mData.add(new DataModel("2", "text2"));
        mData.add(new DataModel("3", "text3"));
        mData.add(new DataModel("4", "text4"));
        mData.add(new DataModel("5", "text5"));
        mData.add(new DataModel("6", "text6"));
        mData.add(new DataModel("7", "text7"));
        mData.add(new DataModel("8", "text8"));
        adapter.notifyDataSetChanged();
    }
}
