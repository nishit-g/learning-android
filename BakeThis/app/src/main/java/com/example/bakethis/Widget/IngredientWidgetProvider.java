package com.example.bakethis.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.bakethis.Activity.MainActivity;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.IngredientObject;
import com.example.bakethis.R;
import com.example.bakethis.databinding.IngredientWidgetProviderBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidgetProvider extends AppWidgetProvider {
//    private static final String TAG = "IngredientWidgetProvide";

    private ArrayList<IngredientObject> ingredientList;
    private String recipeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        views.setOnClickPendingIntent(R.id.ll_widget,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        Log.d(TAG, "Updating the widget(s).....");
        getSharedValues(context);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            updateWidgetTitle(context, appWidgetManager, appWidgetId);
            updateIngredientList(context,appWidgetManager,appWidgetId);
        }
    }

    private void updateIngredientList(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
//        Log.d(TAG, "Updating Ingredients.....");
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);

        if(ingredientList!=null){
            Intent intent = new Intent(context, WidgetListService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            view.setRemoteAdapter(R.id.lv_ingredients, intent);

            view.setViewVisibility(R.id.widget_no_ingredients, View.GONE);
            view.setViewVisibility(R.id.lv_ingredients,View.VISIBLE);
            view.setViewVisibility(R.id.widget_ingredient,View.VISIBLE);
            view.setViewVisibility(R.id.iv_no_list,View.GONE);

        }
        else{
            view.setViewVisibility(R.id.widget_no_ingredients, View.VISIBLE);
            view.setViewVisibility(R.id.lv_ingredients,View.GONE);
            view.setViewVisibility(R.id.widget_ingredient,View.GONE);
            view.setViewVisibility(R.id.iv_no_list,View.VISIBLE);
            view.setTextViewText(R.id.widget_no_ingredients, context.getResources().getString(R.string.please_select_recipe));
        }

        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    private void updateWidgetTitle(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget_provider);

        if(recipeName!=null){
            view.setTextViewText(R.id.widget_ingredient,recipeName);
        }
        
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    private void getSharedValues(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        recipeName = sharedPreferences.getString(Constants.PREF_RECIPE_NAME,"default_recipe");

        Gson gson = new Gson();
        String recipeListJson = sharedPreferences.getString(Constants.PREF_RECIPE_LIST,null);
        Type type = new TypeToken<ArrayList<IngredientObject>>() {}.getType();

        ingredientList = gson.fromJson(recipeListJson,type);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

