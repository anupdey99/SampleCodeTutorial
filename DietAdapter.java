import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adlabs.apps.icare.R;
import com.adlabs.apps.icare.activity.iCare;
import com.adlabs.apps.icare.model.DietModel;
import com.adlabs.apps.icare.util.Utility;

import java.util.List;



public class DietAdapter extends RecyclerView.Adapter<DietAdapter.MyViewHolder>{

    //private Context context;
    private List<DietModel> contentList;
    private SparseBooleanArray mSelectedItemsIds;

    public DietAdapter(Context context, List<DietModel> contentList) {
        //this.context = context;
        this.contentList = contentList;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView dietTypeTV;
        TextView dietMenuTV;
        TextView dietPlaceTV;
        TextView dietCaloriesTV;
        TextView dietTimeTV;
        TextView dietDateTV;
        CardView cardView ;


        MyViewHolder(View itemView) {
            super(itemView);

            dietTypeTV = (TextView) itemView.findViewById(R.id.dietRowDietType);
            dietMenuTV = (TextView) itemView.findViewById(R.id.dietRowMenu);
            dietPlaceTV = (TextView) itemView.findViewById(R.id.dietRowPlace);
            dietCaloriesTV = (TextView) itemView.findViewById(R.id.dietRowCalorie);
            dietTimeTV = (TextView) itemView.findViewById(R.id.dietRowTime);
            dietDateTV = (TextView) itemView.findViewById(R.id.dietRowDate);

            cardView = (CardView) itemView.findViewById(R.id.dietRowCV);

            TextView menuTV = itemView.findViewById(R.id.row_diet_menu);
            TextView placeTV = itemView.findViewById(R.id.row_diet_place);
            TextView caloriesTV = itemView.findViewById(R.id.row_diet_calorie);
            TextView dateTV = itemView.findViewById(R.id.row_diet_date);
            TextView timeTV = itemView.findViewById(R.id.row_diet_time);

            Drawable type = AppCompatResources.getDrawable(iCare.getAppContext(),R.drawable.ic_room_service);
            Drawable menu = AppCompatResources.getDrawable(iCare.getAppContext(),R.drawable.ic_food);
            Drawable place = AppCompatResources.getDrawable(iCare.getAppContext(),R.drawable.ic_place);
            Drawable calories = AppCompatResources.getDrawable(iCare.getAppContext(),R.drawable.ic_calories);
            Drawable date = AppCompatResources.getDrawable(iCare.getAppContext(),R.drawable.ic_calendar);
            Drawable time = AppCompatResources.getDrawable(iCare.getAppContext(),R.drawable.ic_calendar_clock);

            dietTypeTV.setCompoundDrawablesWithIntrinsicBounds(type,null,null,null);
            menuTV.setCompoundDrawablesWithIntrinsicBounds(menu,null,null,null);
            placeTV.setCompoundDrawablesWithIntrinsicBounds(place,null,null,null);
            caloriesTV.setCompoundDrawablesWithIntrinsicBounds(calories,null,null,null);
            dateTV.setCompoundDrawablesWithIntrinsicBounds(date,null,null,null);
            timeTV.setCompoundDrawablesWithIntrinsicBounds(time,null,null,null);




        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_diet, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DietModel model = contentList.get(position);
        String dietType = model.getDietType();
        String dietMenu = model.getDietMenu();
        String place = model.getDietPlace();
        String calorie = model.getDietCalories();
        String dietTime = model.getDietTime();
        String dietDate = model.getDietDate();
        //String remainderState = model.getReminderState();

        holder.dietTypeTV.setText(dietType);
        holder.dietMenuTV.setText(dietMenu);
        holder.dietPlaceTV.setText(returnDashWhenNull(place));
        holder.dietCaloriesTV.setText(returnDashWhenNull(calorie));
        holder.dietTimeTV.setText(returnDashWhenNull(Utility.convert12Hour(dietTime)));
        holder.dietDateTV.setText(dietDate);

        //Change background color of the selected items in list view
        //mSelectedItemsIds.get(position) ? 0x008975 : Color.TRANSPARENT
        holder.cardView.setCardBackgroundColor(mSelectedItemsIds.get(position) ? 0x59a8a8a8 : Color.TRANSPARENT);


    }


    @Override
    public int getItemCount() {
        return contentList.size();
    }

    private String returnDashWhenNull(String data){
        if (data != null && !data.isEmpty()){
            return data;
        }else {
            return "-";
        }
    }


    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}//
