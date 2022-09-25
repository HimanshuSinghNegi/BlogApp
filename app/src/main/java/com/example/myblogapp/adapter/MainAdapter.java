package com.example.myblogapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.myblogapp.R;
import com.example.myblogapp.http.Blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements Filterable {
    final OnItemClickListener clickListener;
    Context context;
    List<Blog> blogList;
    List<Blog> currentList;
    List<Blog> backUp;
    ImageView imgNoDataFound;

    public MainAdapter(Context context, List<Blog> blogList, OnItemClickListener clickListener, ImageView imgNoDataFound) {
        this.context = context;
        this.blogList = blogList;
        this.clickListener = clickListener;
        this.imgNoDataFound = imgNoDataFound;
        this.backUp = new ArrayList<>(blogList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(view1 -> clickListener.onItemClicked(blogList.get(viewHolder.getAdapterPosition())));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.blogTitle.setText(blog.getTitle());
        holder.blogDate.setText(blog.getDate());
        Glide.with(context).load(blog.getAuthor().getAvatarURL()).circleCrop().transition(DrawableTransitionOptions.withCrossFade()).into(holder.blogImg);

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    //method to sort list on the basis of title
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    public void sortByTitle() {
        currentList = new ArrayList<>(blogList);
        //here sorting list on the basis of title by Comparator
        Collections.sort(currentList, Comparator.comparing(Blog::getTitle));
        blogList.clear();
        blogList.addAll(currentList);
        notifyDataSetChanged();

    }

    //method to sort on the basis of date
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    public void sortByDate() {
        currentList = new ArrayList<>(blogList);
        Collections.sort(currentList, Comparator.comparing(Blog::getDateMilliseconds));
        blogList.clear();
        blogList.addAll(currentList);
        notifyDataSetChanged();
    }

    // perform on background thread
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Blog> filterList = new ArrayList<>();
                if ((charSequence.toString().isEmpty())) {
                    filterList.addAll(backUp);
                } else {
                    for (Blog blog : backUp) {
                        if (blog.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterList.add(blog);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filterList;
                return results;
            }

            //performed at main thread
            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //here setting the imgNoFoundData Gone
                imgNoDataFound.setVisibility(View.GONE);
                blogList.clear();
                blogList.addAll((List<Blog>) filterResults.values);
                notifyDataSetChanged();

                //condition if search is not matched

                if (blogList.size() == 0) {
                    imgNoDataFound.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView blogTitle;
        TextView blogDate;
        ImageView blogImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            blogTitle = itemView.findViewById(R.id.blogTitle);
            blogDate = itemView.findViewById(R.id.blogDate);
            blogImg = itemView.findViewById(R.id.avatar);
        }
    }
}
