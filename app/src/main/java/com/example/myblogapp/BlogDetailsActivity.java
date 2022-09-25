package com.example.myblogapp;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myblogapp.http.Blog;

public class BlogDetailsActivity extends AppCompatActivity {

    //    public static final String IMAGE_URL =
//            "https://bitbucket.org/dmytrodanylyk/travel-blog-resources/raw/3436e16367c8ec2312a0644bebd2694d484eb047/images/sydney_image.jpg";
//    public static final String AVATAR_URL =
//            "https://bitbucket.org/dmytrodanylyk/travel-blog-resources/raw/3436e16367c8ec2312a0644bebd2694d484eb047/avatars/avatar1.jpg";

    private ImageView mainImg;
    private ImageView avatar;
    private TextView title;
    private TextView date;
    private TextView author;
    private TextView description;
    private TextView rating;
    private RatingBar ratingBar;
    private TextView textViews;
    private ProgressBar progressBar;
    private TextView loadingText;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //here setting data through java programming
        mainImg = findViewById(R.id.imageMain);
        //here to show image not to feel unnatural we will be using here 'transition' property
        //to show image with a fade animation
//        Glide.with(this).load(IMAGE_URL).transition(DrawableTransitionOptions.withCrossFade()).into(mainImg);

        avatar = findViewById(R.id.avatar);
//        avatar.setImageResource(R.drawable.avatar);
        //here used transform property of glide to circle the image

//        Glide.with(this).load(AVATAR_URL).transform(new CircleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(avatar);

        title = findViewById(R.id.titleText);
//        title.setText(R.string.title);

        date = findViewById(R.id.date);
//        date.setText(R.string.date);

        author = findViewById(R.id.author);
//        author.setText(R.string.author_name);

        rating = findViewById(R.id.textRating);
//        rating.setText(R.string.rating);

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setVisibility(View.GONE);
//        ratingBar.setRating(4.4f);

        description = findViewById(R.id.textDes);
//        description.setText(R.string.description);

        textViews = findViewById(R.id.textViews);

        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);
        loadingText = findViewById(R.id.loadingText);

        //functionality of back btn
        ImageView back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(view -> finish());

        //calling load method
//        loadData();

//        here getting data coming from MainActivity
        Blog blog = getIntent() // 1
                .getExtras() // 2
                .getParcelable(MainActivity.EXTRAS_BLOG); // 3

        Log.d("image", blog.getImage());
//        //calling show method
        showData(blog);


    }

//    //loading method
//    private void loadData() {
//        BlogHttpClient.INSTANCE.loadBlogArticles(new BlogArticlesCallBack() {
//            @Override
//            public void onSuccess(List<Blog> blogList) {
//                runOnUiThread(() -> showData(blogList.get(0)));
//            }
//
//            @Override
//            public void onError() {
//                //error handling
//                runOnUiThread(() -> showErrorSnackBar());
//            }
//        });
//    }


    //method to show snack bar to show error message if failed
//    private void showErrorSnackBar() {
//        View rootView = findViewById(android.R.id.content);
//        @SuppressLint("ShowToast") Snackbar snackbar = Snackbar.make(rootView, "Error while loading Blog", Snackbar.LENGTH_INDEFINITE);
//        snackbar.setActionTextColor(getResources().getColor(R.color.orange500));
//        snackbar.setAction("Retry", view -> {
//            Data();
//            snackbar.dismiss();
//        });
//        snackbar.show();
//
//    }

    //show BlogDetailsActivity
//    public static void startBlogDetailsActivity(Activity activity,Blog blog){
//        Intent intent = new Intent(activity,BlogDetailsActivity.class);
//        intent.putExtra(MainActivity.EXTRAS_BLOG, (Parcelable) blog);
//        activity.startActivity(intent);
//    }

    //method showData
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showData(Blog blog) {
        //first disabling the progress bar
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        title.setText(blog.getTitle());
        date.setText(blog.getDate());
        author.setText(blog.getAuthor().getName());
        rating.setText(String.valueOf(blog.getRating()));
        description.setText(Html.fromHtml(blog.getDescription())); // CHANGING HTML STYLE FROM NORMAL
        ratingBar.setRating(blog.getRating());
        ratingBar.setVisibility(View.VISIBLE);
        textViews.setText(String.format("(%d) views", blog.getViews()));

        Glide.with(this).load(blog.getImageURL()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        progressBar2.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar2.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade()).into(mainImg);

        Glide.with(this).load(blog.getAuthor().getAvatarURL())
                .transition(DrawableTransitionOptions.withCrossFade()).into(avatar);
    }
}
