package fr.aureliejosephine.go4lunch.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;

import fr.aureliejosephine.go4lunch.models.details_places.DetailsResult;
import fr.aureliejosephine.go4lunch.models.places.Result;

import fr.aureliejosephine.go4lunch.ui.adapters.ListAdapter;
import fr.aureliejosephine.go4lunch.viewmodel.ListViewModel;

public class ListFragment extends Fragment  {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ListAdapter adapter;
    private ArrayList<DetailsResult> restaurantsList = new ArrayList<>();
    ListViewModel listViewModel;


    public ListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootsView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = rootsView.findViewById(R.id.recycler_list);

        initialization();

        getRestaurants();


        Log.i("ListFragment", "onCreateView: ");
        return rootsView;

    }


    /**
     * initialization of views and others
     *
     * @param @null
     */
    private void initialization() {
        Log.i("ListFragment", "initialization: ");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // adapter
        adapter = new ListAdapter(getActivity(), restaurantsList);
        recyclerView.setAdapter(adapter);

        // View Model
        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
    }


    /**
     * get movies articles from news api
     *
     * @param @null
     */
    private void getRestaurants() {
        Log.i("ListFragment", "getRestaurants: ");
        listViewModel.getNearbyResponseLiveData().observe(this, restaurantsResponse -> {
            if (restaurantsResponse != null) {

                List<DetailsResult> restaurants = restaurantsResponse.getResults();
                restaurantsList.addAll(restaurants);
                adapter.notifyDataSetChanged();
            }
        });
    }



}
