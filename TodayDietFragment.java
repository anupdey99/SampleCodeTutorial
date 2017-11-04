


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.adlabs.apps.icare.R;
import com.adlabs.apps.icare.adapter.DietAdapter;
import com.adlabs.apps.icare.util.Listener;
import com.adlabs.apps.icare.util.RecyclerClick_Listener;
import com.adlabs.apps.icare.util.RecyclerTouchListener;

import com.adlabs.apps.icare.activity.CareActivity;
import com.adlabs.apps.icare.database.DBDietManager;
import com.adlabs.apps.icare.model.DietModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayDietFragment extends Fragment {


    private RecyclerView    recyclerView;
    private LinearLayout    emptyView;

    private DBDietManager dbManager;
    private ArrayList<DietModel> dietList ;
    private DietAdapter adapter;
    private Activity thisActivity;
    private Listener listener;

    private String today = null;
    private String personID;
    private boolean hasCheckedItems = false;
    private final int REQUEST_CODE_UPDATE = 500;
    private int listPosition = -1;



    public TodayDietFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dietList.isEmpty()){showEmptyView();
        }else {hideEmptyView();}
        //Toast.makeText(thisActivity, "onResume", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        thisActivity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diet_today, container, false);

        listener = getListener(this,Listener.class);

        recyclerView = (RecyclerView) view.findViewById(R.id.dietListRV);
        emptyView = (LinearLayout) view.findViewById(R.id.emptyState);

        dbManager = new DBDietManager(thisActivity);
        personID = CareActivity.STRING_PROFILE_ID;
        today =  new SimpleDateFormat("yyyy-MM-dd" , Locale.US).format(Calendar.getInstance().getTime());

        dietList = dbManager.getDietModelByPersonIdAndDietDate(personID, today, "=");

        adapter = new DietAdapter(thisActivity,dietList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(thisActivity,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        implementRecyclerViewClickListeners();

        return view;
    }

    private void showEmptyView(){
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
    private void hideEmptyView(){
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    private void implementRecyclerViewClickListeners() {


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                if (hasCheckedItems){
                    onListItemSelect(position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                if (!hasCheckedItems){
                    onListItemSelect(position);
                }
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        //Toggle the selection
        adapter.toggleSelection(position);
        //Check if any items are already selected or not
        hasCheckedItems = adapter.getSelectedCount() > 0;
        listener.onListItemSelect();
        if (!hasCheckedItems){
            closeSelection();
        }

    }

    public void closeSelection(){
        hasCheckedItems = false;
        adapter.removeSelection();
    }

    public DietAdapter getAdapter(){
        return adapter;
    }

    //Delete selected rows
    public boolean deleteRows() {
        boolean flag = false;
        SparseBooleanArray selected = adapter.getSelectedIds(); //Get selected ids
        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key
                flag =  dbManager.deleteDiet(dietList.get(selected.keyAt(i)).getDietId());
                dietList.remove(selected.keyAt(i));
                adapter.notifyDataSetChanged();//notify adapter

            }
        }
        closeSelection();
        if (dietList.isEmpty()){
            showEmptyView();
        }
        return flag;
    }

    public void updateRow(){
        SparseBooleanArray selected = adapter.getSelectedIds();
        listPosition = selected.keyAt(0);
        DietModel model = dietList.get(listPosition);

        Intent intent = new Intent(thisActivity,AddDietActivity.class);
        intent.putExtra(AddDietActivity.STRING_NAME_DIET_UPDATE, model.getDietId());
        intent.putExtra(AddDietActivity.STRING_NAME_DIET_LIST_POSITION, listPosition);

        closeSelection();
        startActivityForResult(intent,REQUEST_CODE_UPDATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_UPDATE:
                if (resultCode == Activity.RESULT_OK){

                    int dateWhen =  data.getIntExtra("dateWhen",-3);
                    int dietID = data.getIntExtra("updateID",-1);
                    //int position = data.getIntExtra("position", -1);

                    // pre 1 now 0 up -1
                    if (dateWhen == 0){

                        DietModel model = dbManager.getDietModelByDietID(dietID);
                        dietList.remove(listPosition);
                        dietList.add(listPosition,model);
                        adapter.notifyDataSetChanged();

                        Snackbar.make(emptyView,"Plan updated successfully",Snackbar.LENGTH_SHORT).show();
                    }

                }
            break;
        }

    }



    /**
     * @param fragment The fragment to get the listener for.
     * @param listenerClass The class of the listener to get.
     * @param <T> Type of the listener to get.
     * @return A listener object for the given fragment, cast from the owning parent fragment or
     * Activity, or null if neither is a listener.
     */
    @Nullable
    public static <T> T getListener(@NonNull Fragment fragment, @NonNull Class<T> listenerClass) {
        T listener = null;
        if (listenerClass.isInstance(fragment.getParentFragment())) {
            listener = listenerClass.cast(fragment.getParentFragment());
        }
        else if (listenerClass.isInstance(fragment.getActivity())) {
            listener = listenerClass.cast(fragment.getActivity());
        }

        return listener;
    }



}
