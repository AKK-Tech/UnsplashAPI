package com.example.unsplash.model.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.example.unsplash.model.models.Collection;
import com.example.unsplash.model.unsplash.Unsplash;
import com.example.unsplash.model.unsplash.UnsplashAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionDataSource extends PageKeyedDataSource<Integer, Collection> {

    private static final int FIRST_PAGE = 1;
    private final String CLIENT_ID = "32ae08ce9a09a12cec94bc4ed85e1a5a01a8c3d2e83c103160e9ac8c36c3081d";
    private UnsplashAPI unsplashAPI = Unsplash.getRetrofitInstance(CLIENT_ID).create(UnsplashAPI.class);

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Collection> callback) {
        unsplashAPI.getCollections(FIRST_PAGE).enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(@NonNull Call<List<Collection>> call, @NonNull Response<List<Collection>> response) {
                if (response.body() != null) {
                    callback.onResult(response.body(), null, FIRST_PAGE + 1);
                }
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Collection> callback) {
        unsplashAPI.getCollections(params.key).enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                if (response.body() != null) {
                    Integer key = (params.key > 1) ? params.key - 1 : null;
                    callback.onResult(response.body(), key);
                }
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Collection> callback) {
        unsplashAPI.getCollections(params.key).enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                if (response.body() != null) {
                    Integer key = params.key + 1;
                    callback.onResult(response.body(), key);
                }
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {

            }
        });
    }

//    @Override
//    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Collection> callback) {
//        unsplashAPI.getCollections(FIRST_PAGE).enqueue(new Callback<Collection>() {
//            @Override
//            public void onResponse(@NonNull Call<Collection> call, @NonNull Response<Collection> response) {
//                if (response.body() != null) {
//                    callback.onResult(response.body(), null, FIRST_PAGE + 1);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Collection> call, @NonNull Throwable t) {
//                Log.d("mLog", "onFailure: collection");
//            }
//        });
//    }
//
//    @Override
//    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Collection> callback) {
//        unsplashAPI.getCollections(params.key).enqueue(new Callback<Collection>() {
//            @Override
//            public void onResponse(@NonNull Call<Collection> call, @NonNull Response<Collection> response) {
//                if (response.body() != null) {
//                    Integer key = (params.key > 1) ? params.key - 1 : null;
//                    callback.onResult(response.body(), key);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Collection> call, @NonNull Throwable t) {
//
//            }
//        });
//    }
//
//    @Override
//    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Collection> callback) {
//        unsplashAPI.getCollections(params.key).enqueue(new Callback<List<Collection>>() {
//            @Override
//            public void onResponse(@NonNull Call<Collection> call, @NonNull Response<Collection> response) {
//                if (response.body() != null) {
//                    Integer key = params.key + 1;
//                    callback.onResult(response.body(), key);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Collection> call, @NonNull Throwable t) {
//
//            }
//        });
//    }
}