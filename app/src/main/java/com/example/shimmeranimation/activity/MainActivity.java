package com.example.shimmeranimation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.shimmeranimation.R;
import com.example.shimmeranimation.adapters.RecyclerAdapter;
import com.example.shimmeranimation.client.ApiClient;
import com.example.shimmeranimation.client.ApiInterface;
import com.example.shimmeranimation.model.Album;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<Album> mAlbumList;
    RecyclerView mRecyclerView;
    RecyclerAdapter mRecyclerAdapter;
    ShimmerFrameLayout mShimmerFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init ui elements
        init();
        getAlbumList();
    }

    public void init() {
        mAlbumList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), mAlbumList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mShimmerFrame = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerFrame.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerFrame.stopShimmerAnimation();
        super.onPause();
    }

    public void getAlbumList() {
        //----TO consume the REST web service. In Our MainActivity.Java, First,initialize the ApiClient.---//
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //-----call the getAlbum() interface and implement the CallBacks.-----//
        Call<List<Album>> call = apiService.getAlbum();

        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                mAlbumList = response.body();
                Log.d("TAG", "Response = " + mAlbumList);
                //setting response to adapter
                mRecyclerAdapter.setAlbumList(mAlbumList);
                //Stop Shimmer Animation
                mShimmerFrame.stopShimmerAnimation();
                //hide shimmer frame visibility
                mShimmerFrame.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
}
