package ie.ienquire.fff.musicapp.util;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ie.ienquire.fff.musicapp.R;

public class CitiesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String city;
    private List<String> list, sorted;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        String country = getIntent().getStringExtra("country");
        city = getIntent().getStringExtra("city");
        sorted = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(this);

        try {
            JSONArray objects = new JSONObject(loadJSONFromAsset()).getJSONArray(country);
            list = new ArrayList<>();
            for (int i = 0; i < objects.length(); i++) {
                String city = objects.getString(i);
                if (!list.contains(city)) {
                    if (!city.contains("Laoghaire")) list.add(city);
                }
            }
            Collections.sort(list);
            listView.setAdapter(new ArrayAdapter<>(this, R.layout.simple_list_item, list));

        } catch (JSONException e) {
            MySingleton.getInstance().showToast("Sorry, we were not able to show these cities");
            finish();
        }


        if (getSupportActionBar() != null) {
            setTitle("City/Town");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setSubtitle(country);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.cities, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!query.isEmpty()) {
                    sorted.clear();
                    for (String item : list) {
                        if (item.toLowerCase().contains(query.toLowerCase()))
                            sorted.add(item);
                    }

                    Collections.sort(sorted);
                    listView.setAdapter(new ArrayAdapter<>(CitiesActivity.this, R.layout.simple_list_item, sorted));
                } else {
                    listView.setAdapter(new ArrayAdapter<>(CitiesActivity.this, R.layout.simple_list_item, list));
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("app", "onQueryTextChange: query: " + newText);
                if (!newText.isEmpty()) {
                    sorted.clear();
                    for (String item : list) {
                        if (item.toLowerCase().contains(newText.toLowerCase()))
                            sorted.add(item);
                    }

                    Collections.sort(sorted);
                    listView.setAdapter(new ArrayAdapter<>(CitiesActivity.this, R.layout.simple_list_item, sorted));
                }

                return false;
            }

        });

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        city = (String) adapterView.getItemAtPosition(i);
        Intent it = new Intent();
        it.putExtra("city", city);
        setResult(Activity.RESULT_OK, it);
        finish();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("countriesToCities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
