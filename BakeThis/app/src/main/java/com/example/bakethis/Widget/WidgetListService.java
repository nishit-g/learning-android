package com.example.bakethis.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.IngredientObject;
import com.example.bakethis.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WidgetListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    private static class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context;
        private ArrayList<IngredientObject> ingredientList;

        ListRemoteViewsFactory(Context applicationContext) {
            this.context = applicationContext;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            Gson gson = new Gson();
            String recipeListJson = sharedPreferences.getString(Constants.PREF_RECIPE_LIST,null);
            Type type = new TypeToken<ArrayList<IngredientObject>>() {}.getType();

            ingredientList = gson.fromJson(recipeListJson,type);
            Log.d("widget", "onDataSetChanged: ingredientList --> " + ingredientList.get(0).getIngredients());
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (ingredientList == null) return 0;
            return ingredientList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(ingredientList == null || ingredientList.size()==0) return null;

            IngredientObject currentIngredient = ingredientList.get(position);

            RemoteViews widgetListView = new RemoteViews(context.getPackageName(), R.layout.widget_list_ingredient);


            widgetListView.setTextViewText(R.id.tv_ingredients_widget, currentIngredient.getIngredients());

            String quantityAndMeasure = currentIngredient.getQuantity() + " " + currentIngredient.getMeasure();
            widgetListView.setTextViewText(R.id.tv_quantity_and_measure_widget,quantityAndMeasure);

            return widgetListView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
