package com.wd.tech.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wd.tech.R;


import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> mList = new ArrayList<>();

    public void addAll(List<String> list) {
        mList.addAll(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.circle_image_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.image.setImageURI(Uri.parse(mList.get(i)));
//        viewHolder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(viewHolder.itemView.getContext(), ImageActivity.class);
//                intent.putExtra("image", mList.get(i));
//                viewHolder.itemView.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.circle_image);
        }
    }
}
