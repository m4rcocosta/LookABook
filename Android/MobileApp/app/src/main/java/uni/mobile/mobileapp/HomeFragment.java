package uni.mobile.mobileapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class HomeFragment extends Fragment {
    private TextView tw;

    private String url = "https://www.diag.uniroma1.it/pannello/?q=export_json";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        tw = view.findViewById(R.id.hometw);
        tw.setMovementMethod(new ScrollingMovementMethod());

        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,  url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        ArrayList<JSONObject> rooms = new ArrayList<JSONObject>();
                        // Loop through the array elements
                        for(int i = 0; i < response.length(); i++) {
                            // Get current json object
                            try {
                                rooms.add(response.getJSONObject(i));
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        Collections.sort(rooms, new Comparator<JSONObject>() {

                            @Override
                            public int compare(JSONObject lhs, JSONObject rhs) {
                                // TODO Auto-generated method stub

                                try {
                                    return (lhs.getString("nome_aula").toLowerCase().compareTo(rhs.getString("nome_aula").toLowerCase()));
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });

                        Iterator it = rooms.iterator();
                        while (it.hasNext()) {
                            JSONObject room = (JSONObject) it.next();

                            try {
                                String name = room.getString("nome_aula");
                                String description = room.getString("Descrizione");
                                String start = room.getString("ora_inizio") + room.getString("minuti_inizio");
                                String end = room.getString("ora_fine") + room.getString("minuti_fine");
                                // Display the formatted json data in text view
                                tw.append("Aula: " + name +"\nDescription: " + description + "\nTime: " + start + "-" + end);
                                tw.append("\n\n");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


/*
                                // Get the current student (json object) data
                                String name = rooms.getString("nome_aula");
                                String description = rooms.getString("Descrizione");
                                String start = rooms.getString("ora_inizio") + ":" + rooms.getString("minuti_inizio");
                                String end = rooms.getString("ora_fine") + ":" + rooms.getString("minuti_fine");

                                // Display the formatted json data in text view
                                tw.append("Aula: " + name +"\nDescription: " + description + "\nTime: " + start + "-" + end);
                                tw.append("\n\n");*/
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        //Snackbar.make(mCLayout, "Error...", Snackbar.LENGTH_LONG).show();
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
}
