package com.example.ucla.notebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ucla on 2017/11/5.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<User> mUserList;
    private AdapterView.OnItemClickListener mListener; // Item点击事件
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public MyAdapter(List<User> userList) {
        mUserList = userList;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        final ViewHolder viewHolder = new ViewHolder(view);
        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(viewHolder.itemView,position); // 2
                }
            });
        }
        if(mOnItemLongClickListener != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView,position);
                    //返回true 表示消耗了事件 事件不会继续传递
                    return true;
                }
            });
        }
        return viewHolder;
    }

    public MyAdapter() {
        super();
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        User user = mUserList.get(position);
        holder.title.setText(user.getTitle());
        holder.content.setText(user.getContent());
//        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mUserList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.atitle);
            content = itemView.findViewById(R.id.content);


//
//            User user = new User();
//            String s = user.getTitle();
//            title.setText(s);

//            User first = DataSupport.findFirst(User.class);

        }


//        void bind(int listIndex){
//            title.setText(String.valueOf(listIndex));
//        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

}

