package com.android.rest_client_android;

import android.os.Bundle;

import com.android.rest_client_android.models.Track;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView responsesDisplay;
    private Button gettracksButton;
    private Button addtrackButton;
    private Button edittrackButton;
    private Button deletetrackButton;
    private EditText trackidtext;
    private EditText titletext;
    private EditText singertext;

    private SwaggerAPI apiserver; //Interface of the API methods


    private void getTracks(){
        //Method getTracks() of the Interface
        Call<List<Track>> call = apiserver.getTracks();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if(!response.isSuccessful()){
                    responsesDisplay.setText("Error Code: " + response.code());
                    return;
                }
                //Successful response
                //!!!!!!!!!!!!!!!!!!! RECYCLERVIEW !!!!!!!!!!!!!!!1
                List<Track> trackslist = response.body();
                responsesDisplay.setText("");
                for(Track t : trackslist){
                    responsesDisplay.append("Track ID: " + t.getId() + " Track Title: " + t.getTitle() + " Track Singer: " + t.getSinger() + " \n");
                }
            }
            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                responsesDisplay.setText(t.getMessage());
            }
        });
    }

    private void getTrack(String id){
        //Method getTrackbyID() of the Interface
        Call<Track> call = apiserver.getTrackbyID(id);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if(!response.isSuccessful()){
                    responsesDisplay.setText("Error Code: " + response.code());
                    return;
                }
                //Successful response
                //!!!!!!!!!!!!!!!!!!! RECYCLERVIEW !!!!!!!!!!!!!!!1
                Track t = response.body();
                responsesDisplay.setText("");
                responsesDisplay.append("Track ID: " + t.getId() + " Track Title: " + t.getTitle() + " Track Singer: " + t.getSinger() + " \n");
            }
            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                responsesDisplay.setText(t.getMessage());
            }
        });
    }

    private void addTrack(String title, String singer){
        //Method addTrack() of the Interface
        Track tr = new Track(title,singer);
        Call<Track> call = apiserver.addTrack(tr);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if(!response.isSuccessful()){
                    responsesDisplay.setText("Error Code: " + response.code());
                    return;
                }
                //Successful response
                //!!!!!!!!!!!!!!!!!!! RECYCLERVIEW !!!!!!!!!!!!!!!1
                Track tresp = response.body();
                responsesDisplay.setText("");
                responsesDisplay.append("Code: " + response.code());
                if(response.code() == 201)
                    responsesDisplay.append(" TRACK CREATED");

                responsesDisplay.append("\n");
                responsesDisplay.append("Track ID: " + tresp.getId() + " Track Title: " + tresp.getTitle() + " Track Singer: " + tresp.getSinger() + " \n");
            }
            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                responsesDisplay.setText(t.getMessage());
            }
        });
    }

    private void editTrack(Track t){
        //Method editTrack() of the Interface
        Call<Void> call = apiserver.editTrack(t);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()){
                    responsesDisplay.setText("Error Code: " + response.code());
                }
                //Successful response
                else{
                    //--------------------PROVISIONAL--------------------//
                    responsesDisplay.setText("");
                    responsesDisplay.append("Code: " + response.code());
                    if(response.code() == 201)
                        responsesDisplay.append(" TRACK UPDATED");
                    //-------------------------------------------------//
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                responsesDisplay.setText(t.getMessage());
            }
        });
    }

    private void deleteTrack (String id){
        //Method deleteTrack() of the Interface
        Call <Void> call = apiserver.deleteTrack(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                responsesDisplay.setText("");
                responsesDisplay.setText("Code: " + response.code());
                if(response.code() == 201)
                    responsesDisplay.append(" TRACK DELETED");
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                responsesDisplay.setText(t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LAYOUT OBJECTS
        responsesDisplay = findViewById(R.id.responsesTextView);
        gettracksButton = findViewById(R.id.getListTracksButton);
        addtrackButton = findViewById(R.id.postTrackButton);
        edittrackButton = findViewById(R.id.putTrackButton);
        deletetrackButton = findViewById(R.id.deleteTrackButton);
        trackidtext = findViewById(R.id.trackIDEditText);
        titletext = findViewById(R.id.titleEditText);
        singertext = findViewById(R.id.singerEditText);

        //Retrofit builder
        Retrofit retrofit = new Retrofit.Builder()
                //Aqui ira la direccion de red del servidor que aloje la API. En caso de localhost (loopback), en emulador de Android: 10.0.2.2
                .baseUrl("http://10.0.2.2:8080/dsaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //API Interface initialization
        apiserver = retrofit.create(SwaggerAPI.class);


        //LAYOUT EVENTS
        gettracksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                getTracks();
            }
        });

        addtrackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(titletext.getText().toString().equals("")|| singertext.getText().toString().equals("")){
                    Snackbar.make(v, "You must input Track Title and Singer", Snackbar.LENGTH_LONG)
                            .setAction("Warning", null).show();
                }
                else{
                    addTrack(titletext.getText().toString(), singertext.getText().toString());
                }
            }
        });

        edittrackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Provisional
                if(trackidtext.getText().toString().equals("")){
                    Snackbar.make(v, "You must input the Track ID to be modified", Snackbar.LENGTH_LONG)
                            .setAction("Warning", null).show();
                }
                else{
                    Track t; //Seleccionar la cancion que pulsas con el id
                    //Provisional
                    //Buscar que campos se han cambiado
                    t = new Track(trackidtext.getText().toString(),titletext.getText().toString(), singertext.getText().toString());
                    editTrack(t);
                }
            }
        });

        deletetrackButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Provisional
                if(trackidtext.getText().toString().equals("")){
                    Snackbar.make(v, "You must input the Track ID to be deleted", Snackbar.LENGTH_LONG)
                            .setAction("Warning", null).show();
                }
                else{
                    //Seleccionar el id de la cancion que pulsas
                    //Provisional
                    deleteTrack(trackidtext.getText().toString());
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
