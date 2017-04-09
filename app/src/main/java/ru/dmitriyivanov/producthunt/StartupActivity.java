package ru.dmitriyivanov.producthunt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.dmitriyivanov.producthunt.Fragments.FragmentManager;
import ru.dmitriyivanov.producthunt.Fragments.ProductListFgm;
import ru.dmitriyivanov.producthunt.ProductHuntApi.ApiFactory;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.Category;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.CategoryList;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.Post;
import ru.dmitriyivanov.producthunt.ProductHuntApi.Models.PostList;
import ru.dmitriyivanov.producthunt.ProductHuntApi.ProductHuntService;

public class StartupActivity extends AppCompatActivity implements UI {
    public static final String TAG = "StartupActivity";
    private Toolbar mToolbar;
    private Spinner mCategoryListSpinner;
    private ArrayAdapter mCategoriesListAdapter;
    private List<Category> mCategoryList;
    private ProductHuntService mApiService;
    private Handler workHandler = new Handler();
    private Category currentCategory;
    private List<Post> mActualPosts;

    // Delay в секундах для повторной проверки на наличие новых постов
    private final int REFRESH_TASK_DELAY = 60;

    @Override
    public void initialize() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCategoryListSpinner = (Spinner) findViewById(R.id.categoryListSpinner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);
        initialize();
        // ToolBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Category list spinner
        mApiService = ApiFactory.getProductHuntService();
        Call<CategoryList> call = mApiService.getCategories();
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                if(response.isSuccessful()) {
                    mCategoryList = response.body().categories;
                    mCategoriesListAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.category_item_spinner, Utils.getTitleCategories(mCategoryList));
                    mCategoryListSpinner.setAdapter(mCategoriesListAdapter);
                    mCategoryListSpinner.setOnItemSelectedListener(onChangeCategoryListener);
                }
            }
            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                t.printStackTrace();
            }
        });

        // Показываем список продуктов
        FragmentManager.show(StartupActivity.this, ProductListFgm.newInstance(), ProductListFgm.TAG, false);

        // Запускаем Task для проверки обновлений у выбранной категории
        startTask();
    }

    private Runnable refreshTask = new Runnable() {
        public void run() {
            if(mActualPosts != null && mActualPosts.size() > 0) {
                final Post lastVisible = mActualPosts.get(2);
                // Скачиваем новый посты
                Call<PostList> call = mApiService.getPostsByCategoryName(currentCategory.slug);
                call.enqueue(new Callback<PostList>() {
                    @Override
                    public void onResponse(Call<PostList> call, Response<PostList> response) {
                        if(response.isSuccessful()) {
                            List<Post> newPostList = response.body().posts;
                            // Считаем кол-во новых постов
                            int count = 0;
                            for (int i = 0; i < newPostList.size(); i++) {
                                if(lastVisible.id != newPostList.get(i).id)
                                    ++count;
                                else
                                    break;
                            }
                            if(count > 0) {
                                mActualPosts = newPostList;
                                // Создаем уведомление о новых постах
                                Context context = getApplicationContext();
                                Intent notificationIntent = new Intent(getApplicationContext(), StartupActivity.class);
                                notificationIntent.putExtra("notification_message", String.valueOf(count));
                                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                Notification.Builder builder = new Notification.Builder(context);
                                if(count == 1) {
                                    builder.setContentTitle(newPostList.get(0).name);
                                    builder.setContentText(newPostList.get(0).description);
                                } else {
                                    // Если более чем один
                                    builder.setContentTitle(getString(R.string.new_posts));
                                    builder.setContentText(String.format(getString(R.string.new_posts_more_one), currentCategory.name, String.valueOf(count)));
                                }
                                builder.setContentIntent(pendingIntent)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.mipmap.ic_launcher);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0, builder.build());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostList> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            startTask();
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle extras = intent.getExtras();
        if(extras != null) {
            if(extras.containsKey("notification_message")) {
                EventBus.getDefault().post(currentCategory);
                FragmentManager.reshowProductList(StartupActivity.this);
            }
        }
    }

    public void startTask()
    {
        workHandler.postDelayed(refreshTask, REFRESH_TASK_DELAY * 1000);
    }


    // Callback for changing category
    private AdapterView.OnItemSelectedListener onChangeCategoryListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            currentCategory = mCategoryList.get(i);
            EventBus.getDefault().post(currentCategory);
            FragmentManager.reshowProductList(StartupActivity.this);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @Subscribe
    public void onEvent(List<Post> posts) {
        mActualPosts = posts;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }
}
