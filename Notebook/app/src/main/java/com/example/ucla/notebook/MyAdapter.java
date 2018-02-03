package com.example.ucla.notebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ucla on 2017/11/5.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

    private List<Note> mNoteList;
    private AdapterView.OnItemClickListener mListener; // Item点击事件
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public MyAdapter(List<Note> noteList) {  mNoteList = noteList;  }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        final ViewHolder viewHolder = new ViewHolder(view);
        if(mOnItemClickListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.itemView,position);
                }
            });
        }
        if(mOnItemLongClickListener != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = viewHolder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(viewHolder.itemView,position);
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
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position){
        Note note = mNoteList.get(position);
        CharSequence formattedTitle = ColorPhrase.from(note.getTitle())
                .withSeparator("{}")
                .innerColor(0xFFE6454A)
                .outerColor(0xFF666666)
                .format();
        CharSequence formattedContent = ColorPhrase.from(note.getContent())
                .withSeparator("{}")
                .innerColor(0xFFE6454A)
                .outerColor(0xFF666666)
                .format();
        holder.title.setText(formattedTitle);
        holder.content.setText(formattedContent);
    }


    @Override
    public int getItemCount() {
        return mNoteList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.atitle);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

}

