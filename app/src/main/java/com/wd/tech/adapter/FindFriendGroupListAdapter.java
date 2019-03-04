package com.wd.tech.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;
import com.wd.tech.bean.FriendGroup;

import java.util.ArrayList;
import java.util.List;

public class FindFriendGroupListAdapter extends RecyclerView.Adapter<FindFriendGroupListAdapter.VH> {
    private List<FriendGroup> list = new ArrayList<>();
    private ClickListener clickListener;

    public void setList(List<FriendGroup> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LinearLayout.inflate(viewGroup.getContext(), R.layout.find_friend_groups_adapter, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {

        vh.textView.setText(list.get(i).getGroupName());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.click(list.get(i).getGroupId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VH extends RecyclerView.ViewHolder{
        private final TextView textView;
        public VH(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.find_friend_groups_name);
        }
    }
    public interface ClickListener{
        void click(int id);
    }
    public void setOnItemClickListener(ClickListener clickListener){
      this.clickListener=clickListener;
    }
}
