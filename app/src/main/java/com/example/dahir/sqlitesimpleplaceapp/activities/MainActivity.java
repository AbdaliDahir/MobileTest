package com.example.dahir.sqlitesimpleplaceapp.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dahir.sqlitesimpleplaceapp.R;
import com.example.dahir.sqlitesimpleplaceapp.adapters.PendingplaceAdapter;
import com.example.dahir.sqlitesimpleplaceapp.helpers.TagDBHelper;
import com.example.dahir.sqlitesimpleplaceapp.helpers.placeDBHelper;
import com.example.dahir.sqlitesimpleplaceapp.models.PendingplaceModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Start 23 : 05 : 2019
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener{
    private RecyclerView pendingplaces;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<PendingplaceModel> pendingplaceModels;
    private PendingplaceAdapter pendingplaceAdapter;
    private FloatingActionButton addNewplace;
    private TagDBHelper tagDBHelper;
    private String getTagTitleString;
    private placeDBHelper placeDBHelper;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setTitle(getString(R.string.app_title));
        showDrawerLayout();
        navigationMenuInit();
        loadPendingplaces();
    }

    //loading all the pending places
    private void loadPendingplaces(){
        pendingplaces=(RecyclerView)findViewById(R.id.pending_places_view);
        linearLayout=(LinearLayout)findViewById(R.id.no_pending_place_section);
        tagDBHelper=new TagDBHelper(this);
        placeDBHelper=new placeDBHelper(this);

        if(placeDBHelper.countplaces()==0){
            linearLayout.setVisibility(View.VISIBLE);
            pendingplaces.setVisibility(View.GONE);
        }else{
            pendingplaceModels=new ArrayList<>();
            pendingplaceModels=placeDBHelper.fetchAllplaces();
            pendingplaceAdapter=new PendingplaceAdapter(pendingplaceModels,this);
        }
        linearLayoutManager=new LinearLayoutManager(this);
        pendingplaces.setAdapter(pendingplaceAdapter);
        pendingplaces.setLayoutManager(linearLayoutManager);
        addNewplace=(FloatingActionButton)findViewById(R.id.fabAddplace);
        addNewplace.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabAddplace:
                if(tagDBHelper.countTags()==0){
                    showDialog();
                }else{
                    showNewplaceDialog();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //add the drawer layout
    private void showDrawerLayout(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,(Toolbar) findViewById(R.id.toolbar) , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //initialize navigation menu
    private void navigationMenuInit(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pending_task_options,menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<PendingplaceModel> newPendingplaceModels=new ArrayList<>();
                for(PendingplaceModel pendingplaceModel:pendingplaceModels){
                    String getplaceTitle=pendingplaceModel.getplaceTitle().toLowerCase();
                    String getplaceContent=pendingplaceModel.getplaceContent().toLowerCase();
                    String getplaceTag=pendingplaceModel.getplaceTag().toLowerCase();

                    if(getplaceTitle.contains(newText) || getplaceContent.contains(newText) || getplaceTag.contains(newText)){
                        newPendingplaceModels.add(pendingplaceModel);
                    }
                }
                pendingplaceAdapter.filterplaces(newPendingplaceModels);
                pendingplaceAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                return true;
            case R.id.all_tags:
                startActivity(new Intent(this,AllTags.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this,AppSettings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.pending_places) {
            startActivity(new Intent(this,MainActivity.class));
        } else if (id == R.id.tags) {
            startActivity(new Intent(this,AllTags.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(this,AppSettings.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //show dialog if there is no tag in the database
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.tag_create_dialog_title_text);
        builder.setMessage(R.string.no_tag_in_the_db_text);
        builder.setPositiveButton(R.string.create_new_tag, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MainActivity.this,AllTags.class));
            }
        }).setNegativeButton(R.string.tag_edit_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    //show add new places dialog and adding the places into the database
    private void showNewplaceDialog(){
        //getting current calendar credentials
        final Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.add_new_place_dialog,null);
        builder.setView(view);
        final TextInputEditText placeTitle=(TextInputEditText)view.findViewById(R.id.place_title);
        final TextInputEditText placeContent=(TextInputEditText)view.findViewById(R.id.place_content);
        Spinner placeTags=(Spinner)view.findViewById(R.id.place_tag);
        //stores all the tags title in string format
        ArrayAdapter<String> tagsModelArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,tagDBHelper.fetchTagStrings());
        //setting dropdown view resouce for spinner
        tagsModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //setting the spinner adapter
        placeTags.setAdapter(tagsModelArrayAdapter);
        placeTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTagTitleString=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final TextInputEditText placeDate=(TextInputEditText)view.findViewById(R.id.place_date);

        //getting the placedate
        placeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR,i);
                        calendar.set(Calendar.MONTH,i1);
                        calendar.set(Calendar.DAY_OF_MONTH,i2);
                        placeDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        TextView cancel=(TextView)view.findViewById(R.id.cancel);
        TextView addplace=(TextView)view.findViewById(R.id.add_new_place);
        addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting all the values from add new places dialog
                String getplaceTitle=placeTitle.getText().toString();
                String getplaceContent=placeContent.getText().toString();
                int placeTagID=tagDBHelper.fetchTagID(getTagTitleString);
                String getplaceDate=placeDate.getText().toString();

                //checking the data fiels
                boolean isTitleEmpty=placeTitle.getText().toString().isEmpty();
                boolean isContentEmpty=placeContent.getText().toString().isEmpty();
                boolean isDateEmpty=placeDate.getText().toString().isEmpty();

                //adding the places
                if(isTitleEmpty){
                    placeTitle.setError("place title required !");
                }else if(isContentEmpty){
                    placeContent.setError("place content required !");
                }else if(isDateEmpty){
                    placeDate.setError("place date required !");
                }else if(placeDBHelper.addNewplace(
                        new PendingplaceModel(getplaceTitle,getplaceContent,String.valueOf(placeTagID),getplaceDate)
                )){
                    Toast.makeText(MainActivity.this, R.string.place_title_add_success_msg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
        builder.create().show();
    }
}
