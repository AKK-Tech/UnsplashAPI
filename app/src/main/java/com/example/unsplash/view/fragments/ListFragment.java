package com.example.unsplash.view.fragments;

import android.app.ActionBar;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.unsplash.R;
import com.example.unsplash.view.adapters.MyPagedListAdapter;
import com.example.unsplash.model.models.Photo;
import com.example.unsplash.view.adapters.PagedListOnClickListener;
import com.example.unsplash.viewmodel.PhotoViewModel;

import java.util.Objects;


public class ListFragment extends Fragment {
    MyPagedListAdapter mAdapter;
    RecyclerView rv;
    final int numberOfColumns = 2;
    PagedListOnClickListener listener;
    PhotoViewModel photoViewModel;
    BottomNavigationView bottomNav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        viewInit(view);
        photoViewModel.photoPagedList.observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(@Nullable PagedList<Photo> photos) {
                mAdapter.submitList(photos);
            }
        });
        rv.setLayoutManager(new GridLayoutManager(view.getContext(), numberOfColumns));
        listenerInit();
        mAdapter = new MyPagedListAdapter(getActivity(), listener);
        rv.setAdapter(mAdapter);
        return view;
    }

    private void viewInit(View view) {
        rv = view.findViewById(R.id.rView);
        bottomNav = view.findViewById(R.id.navigationView);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_collections:
                        break;
                    case R.id.navigation_search:
                        SearchFragment searchFragment = new SearchFragment();
                        Objects.requireNonNull(getFragmentManager()).beginTransaction().replace(R.id.container, searchFragment)
                                .addToBackStack(null).commit();
                }
                return false;

            }
        });
    }

    private void listenerInit() {
        listener = new PagedListOnClickListener() {
            @Override
            public void onClick(View view, Photo photo) {
                Bundle bundle = new Bundle();
                assert photo != null;
                bundle.putString("URI", photo.getUrls().getRegular());
                bundle.putString("SMTH", photo.getLikes().toString());
                bundle.putString("TRANS", view.getTransitionName());

                setSharedElementReturnTransition(TransitionInflater
                        .from(getActivity()).inflateTransition(android.R.transition.move));
                setReenterTransition(TransitionInflater
                        .from(getContext()).inflateTransition(android.R.transition.move).setDuration(100));
                ImageFragment imageFragment = new ImageFragment();
                imageFragment.setArguments(bundle);
                FragmentManager manager = (Objects.requireNonNull(getActivity()))
                        .getSupportFragmentManager();
                assert manager != null;
                manager.beginTransaction().setReorderingAllowed(true)
                        .addSharedElement(view, view.getTransitionName())
                        .replace(R.id.container, imageFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
    }

}
