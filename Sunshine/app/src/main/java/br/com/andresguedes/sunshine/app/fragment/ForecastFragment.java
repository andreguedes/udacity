package br.com.andresguedes.sunshine.app.fragment;

/**
 * Created by aguedes on 28/01/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

import br.com.andresguedes.sunshine.app.FetchWeatherTask;
import br.com.andresguedes.sunshine.app.R;
import br.com.andresguedes.sunshine.app.activity.DetailActivity;
import br.com.andresguedes.sunshine.app.test.WeatherDataParserTest;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateWeather();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        updateWeather();
    }

    private void updateWeather() {
        FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity(), adapter);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = preferences.getString("location", "Araraquara");
        fetchWeatherTask.execute(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        String[] forecastArray = {
//                "Hoje - Sol - 32/36",
//                "Amanha - Sol - 31/35",
//                "Depois de amanha - Sol - 32/36",
//                "Daqui 3 dias - Sol - 32/36",
//                "Daqui 4 dias - Sol - 32/36"
//        };
//
//        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, new ArrayList<String>());

        ListView listview_forecast = (ListView) rootView.findViewById(R.id.listview_forecast);
        listview_forecast.setAdapter(adapter);
        listview_forecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String forecastItem = adapter.getItem(i);
//                Toast.makeText(getActivity(), forecastItem, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecastItem);
                startActivity(intent);
            }
        });

        // Testing
        WeatherDataParserTest weatherDataParserTest = new WeatherDataParserTest();
        try {
            weatherDataParserTest.testFremontLastDay();
            weatherDataParserTest.testMountainViewThirdDay();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

}