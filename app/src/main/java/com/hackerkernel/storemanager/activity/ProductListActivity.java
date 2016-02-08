package com.hackerkernel.storemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.storemanager.R;
import com.hackerkernel.storemanager.adapter.ProductListAdapter;
import com.hackerkernel.storemanager.extras.ApiUrl;
import com.hackerkernel.storemanager.extras.Keys;
import com.hackerkernel.storemanager.network.VolleySingleton;
import com.hackerkernel.storemanager.parser.JsonParser;
import com.hackerkernel.storemanager.pojo.ProductListPojo;
import com.hackerkernel.storemanager.storage.Database;
import com.hackerkernel.storemanager.storage.MySharedPreferences;
import com.hackerkernel.storemanager.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ProductListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //Global variable
    private static final String TAG = ProductListActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.layout) CoordinatorLayout mLayout;
    @Bind(R.id.swipeRefresh) SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.productRecyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.emptyRecyclerView) TextView mEmptyRecyclerView;

    private String mCategoryId;
    private String mCategoryName;
    private String mUserId;
    private Database db;
    private RequestQueue mRequestQueue;

    private EditText mEditCategoryNameEditText;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);

        //get the categoryId & categoryName
        if(getIntent().hasExtra(Keys.PRAM_PL_CATEGORYID) && getIntent().hasExtra(Keys.PRAM_PL_CATEGORYNAME)){
            mCategoryId = getIntent().getExtras().getString(Keys.PRAM_PL_CATEGORYID);
            mCategoryName = getIntent().getExtras().getString(Keys.PRAM_PL_CATEGORYNAME);
        }else{
            Toast.makeText(getApplication(), R.string.internal_error_restart_app,Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //set the Toolbar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(mCategoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Loggedin userId
        mUserId = MySharedPreferences.getInstance(getApplication()).getUserId();

        //Set Up Volley
        mRequestQueue = VolleySingleton.getInstance().getRequestQueue();

        //Set LayoutManger for recyclerView
        LinearLayoutManager manger = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(manger);

        //Instantiate Database
        db = new Database(this);

        //Instanciate edit Category name EditText
        mEditCategoryNameEditText = new EditText(this);
        mEditCategoryNameEditText.setText(mCategoryName);

        //Create Edit Category name Dialog
        createEditCategoryDialog();

        //Instantiate SwipeToRefreshLayout
        mSwipeRefresh.setOnRefreshListener(this);
        checkInternetAndDisplayList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //when back button is clicked
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_edit_category_name:
                showEditCategoryDialog();
                break;
            case R.id.action_delete_category:
                Toast.makeText(getApplication(),"delete cat",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    /********** MENU *****/


    /*
    * Check user has a Internet connected
    * if yes Fetch fresh product list from api and store it in sqlite database
    * if no Go to Sqlitedatabase and get the product list
    * if no data in SqliteDatabase show a message
    * */
    private void checkInternetAndDisplayList() {
        if(Util.isConnectedToInternet(getApplication())){ //connected
            fetchProductListInBackground(); //fetch data
        }else{ //not connected
            showListFromSqliteDatabase(); //method to display Data in list from Sqlite database
            Util.noInternetSnackbar(getApplication(), mLayout);
            //method to stop swipeRefreshlayout refresh icon
            stopRefreshing();
        }
    }

    /*
    * Method To Get Product list from API
    * */
    public void fetchProductListInBackground(){
        startRefreshing(); //show refresh
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrl.PRODUCT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stopRefreshing(); //Stop refresh
                //Parse response send by the server
                List<ProductListPojo> list  = parseProductListResponse(response);
                if(list != null){
                    setupRecyclerView(list);

                    //Store in PRODUCT LIST Table
                    db.deleteProductList(mUserId,mCategoryId);
                    db.insertProductList(list);
                }else{
                    //Show list from Local sqlite database
                    showListFromSqliteDatabase();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopRefreshing(); //Stop refresh
                //handle Volley error
                Log.d(TAG,"HUS: error "+error.getMessage());
                String errorMessage = VolleySingleton.handleVolleyError(error);
                if(errorMessage != null){
                    Util.redSnackbar(getApplication(), mLayout, errorMessage);
                }

                //Show list from Sqlite Database
                showListFromSqliteDatabase();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put(Keys.KEY_COM_USERID,mUserId);
                param.put(Keys.PRAM_PL_CATEGORYID,mCategoryId);
                return param;
            }
        };

        mRequestQueue.add(request);
    }

    private void showListFromSqliteDatabase() {
        //Display List From SQlite database
        List<ProductListPojo> list = db.getProductList(mUserId,mCategoryId);
        if(list != null){
            setupRecyclerView(list);
        }
    }

    public List<ProductListPojo> parseProductListResponse(String response){
        List<ProductListPojo> list = JsonParser.productListParser(response);
        if(list != null){
            //if response return false
            if(!list.get(0).isReturned()){
                Log.d(TAG,"HUS: parse: false");
                Util.redSnackbar(getApplication(), mLayout, list.get(0).getMessage());
                return null;
            }else if(list.get(0).getCount() == 0){
                Log.d(TAG, "HUS: count zero");
                /*
                * If count return 0 means no product added
                * Hide recyclerView and show TextView
                * */
                mRecyclerView.setVisibility(View.GONE);
                mEmptyRecyclerView.setVisibility(View.VISIBLE);
                return null;
            }else{
                Log.d(TAG,"HUS: item found");
                /*
                * If result found
                * Make recyclerview visible and TextView invisible
                * */
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyRecyclerView.setVisibility(View.GONE);
                return list;
            }
        }else{
            Toast.makeText(getApplication(), R.string.unable_to_parse_response, Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /*
    * Method to take a ProductListPojo list and set RecyclerView
    * */
    private void setupRecyclerView(List<ProductListPojo> list){
            ProductListAdapter adapter = new ProductListAdapter(getApplication());
            adapter.setList(list);
            mRecyclerView.setAdapter(adapter);
    }

    //Open insertProduct Activity when FAB is clicked
    public void openAddProduct(View view){
        Intent addProductIntent = new Intent(getApplication(),AddProductActivity.class);
        addProductIntent.putExtra("categoryId",mCategoryId);
        addProductIntent.putExtra("categoryName",mCategoryName);
        startActivity(addProductIntent);
    }

    /*
    * Swipe to refresh
    * */

    @Override
    public void onRefresh() {
        checkInternetAndDisplayList();
    }

    //method to stop swipeRefreshlayout refresh icon
    private void stopRefreshing() {
        if(mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.setRefreshing(false);
        }
    }

    private void startRefreshing(){
        if(!mSwipeRefresh.isRefreshing()){
            mSwipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefresh.setRefreshing(true);
                }
            });
        }
    }

    /******************** Edit Category name *****************/
    private void createEditCategoryDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.edit_category_name)
                .setView(mEditCategoryNameEditText)
                .setPositiveButton(R.string.edit,null)
                .setNegativeButton(R.string.cancel,null);
        dialog = builder.create();
    }
    private void showEditCategoryDialog(){
        dialog.show();
    }
}
