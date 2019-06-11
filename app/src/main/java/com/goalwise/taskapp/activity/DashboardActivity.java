package com.goalwise.taskapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.goalwise.taskapp.R;
import com.goalwise.taskapp.adapter.FundsAdapter;
import com.goalwise.taskapp.interfaces.NetworkCallBack;
import com.goalwise.taskapp.models.Fund;
import com.goalwise.taskapp.taskApp.GlobalApp;
import com.goalwise.taskapp.utils.NetworkManager;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView funds_search_view;
    private View dashboard_root_view;
    private View search_funds_empty_layout;
    private View search_funds_layout;
    private RecyclerView search_funds_recycler_view;
    private ProgressDialog mProgressDialog = null;
    private FundsAdapter fundsAdapter;
    private ArrayList<Fund> fundArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        intView();
    }

    private void intView() {
        funds_search_view = findViewById(R.id.funds_search_view);
        dashboard_root_view = findViewById(R.id.dashboard_root_view);
        search_funds_recycler_view = findViewById(R.id.search_funds_recycler_view);
        search_funds_empty_layout = findViewById(R.id.search_funds_empty_layout);
        search_funds_layout = findViewById(R.id.search_funds_layout);

        fundsAdapter = new FundsAdapter(this, fundArrayList);
        search_funds_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        search_funds_recycler_view.setHasFixedSize(true);
        search_funds_recycler_view.setAdapter(fundsAdapter);
        funds_search_view.setOnQueryTextListener(this);
        checkEmpty();
    }

    private void checkEmpty() {
        if (fundArrayList != null && fundArrayList.size() > 0) {
            search_funds_empty_layout.setVisibility(View.GONE);
            search_funds_layout.setVisibility(View.VISIBLE);
        } else {
            search_funds_empty_layout.setVisibility(View.VISIBLE);
            search_funds_layout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressDialog();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (newText.length() >= 3) { /*after length is three then only it will query for network*/
            GlobalApp.getInstance().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    handleFetchFund(newText);
                }
            });
        } else {
            if (fundArrayList != null && fundArrayList.size() > 0) {
                fundArrayList.clear();
            }
        }
        return false;
    }

    private void handleFetchFund(final String searchText) {
        if (GlobalApp.getInstance().isNetworkAvailable()) {
            showProgressDialog("Please Wait...");
            new NetworkManager(this).fetchFund(searchText, new NetworkCallBack() {
                @Override
                public void onSuccess(int type, Object... object) {
                    hideProgressDialog();
                    String status = (String) object[0];
                    fundArrayList = (ArrayList<Fund>) object[1];
                    if (fundsAdapter != null) {
                        fundsAdapter.update(fundArrayList);
                    }
                    checkEmpty();
                }

                @Override
                public void onError(Exception exception) {
                    hideProgressDialog();
                    GlobalApp.getInstance().globalToast("Something went wrong,Please try again", getApplicationContext());
                }
            });
        } else {
            GlobalApp.getInstance().globalSnackbar(dashboard_root_view, "No internet available.. Please check", Snackbar.LENGTH_INDEFINITE, "Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalApp.getInstance().dismissGlobalSnackbar();
                    handleFetchFund(searchText);
                }
            });
        }
    }

    public void showProgressDialog(final String message) {
        if (mProgressDialog != null) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }

}
