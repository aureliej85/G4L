package fr.aureliejosephine.go4lunch.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;

import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.models.places.Result;

import fr.aureliejosephine.go4lunch.viewmodel.ListViewModel;

public class ListFragment extends Fragment  {

    List<Result> restaurantsList;

    ListAdapter listAdapter;
    RecyclerView recyclerView;
    ListViewModel listViewModel;
    NearByApiResponse nearByApiResponse;


    public ListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootsView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = rootsView.findViewById(R.id.recycler_list);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        listViewModel = ViewModelProviders.of(getActivity()).get(ListViewModel.class);
        listViewModel.init();

        listViewModel.getRestaurantsRepository().observe(this, restaurantsResponse -> {
            List<Result> restaurants = restaurantsResponse.getResults();
            restaurantsList.addAll(restaurants);
            listAdapter.notifyDataSetChanged();
        });

        setupRecyclerView();


        Log.i("ListFragment", "onCreateView: ");
        return rootsView;

    }


    private void setupRecyclerView() {
        if (listAdapter == null) {
            listAdapter = new ListAdapter(restaurantsList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(listAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setNestedScrollingEnabled(true);
        } else {
            listAdapter.notifyDataSetChanged();
        }
        Log.i("ListFragment", "setUpRecyclerView ");
    }




}
