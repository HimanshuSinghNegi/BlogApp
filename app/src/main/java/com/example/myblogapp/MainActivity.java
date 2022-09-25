package com.example.myblogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myblogapp.adapter.MainAdapter;
import com.example.myblogapp.adapter.OnItemClickListener;
import com.example.myblogapp.database.BlogRepository;
import com.example.myblogapp.database.DataFromNetworkCallback;
import com.example.myblogapp.databinding.ActivityMainBinding;
import com.example.myblogapp.http.Blog;
import com.example.myblogapp.http.BlogArticlesCallBack;
import com.example.myblogapp.http.BlogHttpClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    static final String EXTRAS_BLOG = "EXTRAS_BLOG";
    private static final int SORT_DATE = 1;
    private static final int SORT_TITLE = 0;
    private BlogRepository repository;
    ActivityMainBinding binding;
    MainAdapter adapter;
    ImageView imgNoDataFound;
    @SuppressLint("StaticFieldLeak")

    private int currentSort = SORT_DATE;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //here setting screen only portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //BlogRepository
        repository = new BlogRepository(getApplicationContext()); // 1

        //binding refresh listener
        binding.refresh.setOnRefreshListener(this::loadDataFromNetwork);
//        //calling load method
//        loadData();
        loadDataFromDatabase(); // 3
        loadDataFromNetwork(); // 4

        //toast
        Toast toast = Toast.makeText(this, "onCreate()", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
        imgNoDataFound = findViewById(R.id.imgNoFound);
        imgNoDataFound.setVisibility(View.GONE);


        //adding sort functionality
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort) {
                onSortClicked(); // implemented later in this lesson
            }
            return true;
        });

        //here search functionality
        MenuItem searchItem = binding.toolbar.getMenu().findItem(R.id.search); // 1
        SearchView searchView = (SearchView) searchItem.getActionView(); // 2
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

    }

    private void loadDataFromDatabase() {
        repository.loadDataFromDatabase(blogList -> runOnUiThread(() -> {
            binding.refresh.setRefreshing(false);
                    adapter = new MainAdapter(getApplication(), blogList, MainActivity.this, imgNoDataFound);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    binding.recyclerView.setAdapter(adapter);
        }));
    }
    private void loadDataFromNetwork() {
        binding.refresh.setRefreshing(true); // 1

        repository.loadDataFromNetwork(new DataFromNetworkCallback() { // 2
            @Override
            public void onSuccess(List<Blog> blogList) {
                runOnUiThread(() -> { // 3
                    adapter = new MainAdapter(getApplication(), blogList, MainActivity.this, imgNoDataFound);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    binding.recyclerView.setAdapter(adapter);
                    binding.refresh.setRefreshing(false);
                });
            }

            @Override
            public void onError() {
                runOnUiThread(() -> {
                    binding.refresh.setRefreshing(false);
                    showErrorSnackBar();
                });
            }
        });
    }

    //Method to sort blogs on main screen
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onSortClicked() {
        String[] items = new String[]{"Title", "Date"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Sort Order")
                .setSingleChoiceItems(items, currentSort, (dialogInterface, selectedSort) -> {
                    dialogInterface.dismiss();
                    currentSort = selectedSort;
                    //calling sort
                    sortData();
                }).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortData() {
        if (currentSort == SORT_TITLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                adapter.sortByTitle();
            }
        } else if (currentSort == SORT_DATE) {
            adapter.sortByDate();
        }
    }

//
//    //loading method
//    private void loadData() {
//        //here setting setRefreshing true to show loading
//        binding.refresh.setRefreshing(true);
//        BlogHttpClient.INSTANCE.loadBlogArticles(new BlogArticlesCallBack() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onSuccess(List<Blog> blogList) {
//                runOnUiThread(() -> {
//                    //here disabling setRefreshing false to disable loading
//                    binding.refresh.setRefreshing(false);
//                    adapter = new MainAdapter(getApplication(), blogList, MainActivity.this, noData);
//
//                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//                    binding.recyclerView.setAdapter(adapter);
//
//
//                });
//
//            }
//
//            @Override
//            public void onError() {
//                //here disabling setRefreshing false to disable loading
//                binding.refresh.setRefreshing(false);
//                showErrorSnackBar();
//
//            }
//        });
//    }

    //method to show snack
    private void showErrorSnackBar() {
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view, "Error while loading profiles ", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", view1 -> {
            loadDataFromNetwork();
            snackbar.show();
        });
        snackbar.show();


    }


    @Override
    public void onItemClicked(Blog blog) {

        Toast.makeText(getApplication(), blog.getImage(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, BlogDetailsActivity.class);
        intent.putExtra(EXTRAS_BLOG, blog);
        startActivity(intent);


    }

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onStart() {
        super.onStart();
        Toast toast = Toast.makeText(this, "onStart()", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
        toast.show();

    }

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onResume() {
        super.onResume();
        Toast toast = Toast.makeText(this, "onResume()", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 0, 0);
        toast.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause()", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop()", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_LONG).show();
    }
}