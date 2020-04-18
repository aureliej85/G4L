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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.aureliejosephine.go4lunch.R;
import fr.aureliejosephine.go4lunch.injections.Injection;
import fr.aureliejosephine.go4lunch.injections.ViewModelFactory;
import fr.aureliejosephine.go4lunch.models.Restaurant;
import fr.aureliejosephine.go4lunch.models.places.NearByApiResponse;
import fr.aureliejosephine.go4lunch.models.places.Result;
import fr.aureliejosephine.go4lunch.viewmodel.ListViewModel;

public class ListFragment extends Fragment {

    ArrayList<NearByApiResponse> restaurantsList = new ArrayList<>();
    List<NearByApiResponse> restaurants = new ArrayList<>();
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

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        listViewModel = ViewModelProviders.of(this, viewModelFactory).get(ListViewModel.class);
        listViewModel.init();
        /*listViewModel.getRestaurantsRepository().observe(this, newsResponse -> {
            List<Result> restaurants = (List<Result>) listViewModel.getRestaurants("-33.870775, 151.199025", "AIzaSyASuNr6QZGHbqEtY1GEfoKlVdkaEMz1PBM");
            restaurantsList.addAll(restaurants);
            listAdapter.notifyDataSetChanged();
        });*/

        /*restaurants = (List<NearByApiResponse>) listViewModel.getRestaurants("-33.870775, 151.199025");
        restaurantsList.addAll(restaurants);*/

        //listAdapter.notifyDataSetChanged();
        nearByApiResponse = new NearByApiResponse();

        setupRecyclerView();

        Log.i("ListFragment", "onCreateView: ");
        return rootsView;

    }


    private void setupRecyclerView() {
        if (listAdapter == null) {
            listAdapter = new ListAdapter(nearByApiResponse.getResults());
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
