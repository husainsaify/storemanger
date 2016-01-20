package com.hackerkernel.storemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackerkernel.storemanager.activity.ProductActivity;
import com.hackerkernel.storemanager.R;
import com.hackerkernel.storemanager.extras.Keys;
import com.hackerkernel.storemanager.pojo.SimpleListPojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to adapt simpleList item like category & salesman list
 */
public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListAdapter.ViewHolderSimpleList>{

    private LayoutInflater mInflater;
    private List<SimpleListPojo> mList = new ArrayList<>();
    private Context mContext;
    private String mActivityName;

    //name of Activity using SimpleListAdapter
    public static String CATEGORY = "category";
    public static String SALESMAN = "salesman";

    public SimpleListAdapter(Context context,String activityName){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mActivityName = activityName;
    }

    public void setList(List<SimpleListPojo> list){
        mList = list;
        //update the adapter to display new items
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderSimpleList onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.simple_list_layout,parent,false);
        return new ViewHolderSimpleList(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderSimpleList holder, int position) {
        SimpleListPojo current = mList.get(position);
        //set item to  views
        holder.name.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolderSimpleList extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        public ViewHolderSimpleList(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.SimpleListText);

            /*Item click listener*/
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*
            * Check if SimpleListAdapter is used for CATEGORY
            * */
            if(mActivityName.equals(CATEGORY)){
                int position = getAdapterPosition();
                SimpleListPojo current = mList.get(position);
                //go to product activity
                Intent productIntent = new Intent(mContext, ProductActivity.class);
                //set categoryId and CategoryName in intenet
                productIntent.putExtra(Keys.PRAM_PL_CATEGORYID, current.getId());
                productIntent.putExtra(Keys.PRAM_PL_CATEGORYNAME, current.getName());
                mContext.startActivity(productIntent);
            }
        }
    }
}
