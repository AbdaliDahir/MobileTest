package com.example.dahir.sqlitesimpleplaceapp.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dahir.sqlitesimpleplaceapp.R;
import com.example.dahir.sqlitesimpleplaceapp.activities.MainActivity;
import com.example.dahir.sqlitesimpleplaceapp.helpers.TagDBHelper;
import com.example.dahir.sqlitesimpleplaceapp.helpers.placeDBHelper;
import com.example.dahir.sqlitesimpleplaceapp.models.PendingplaceModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;



public class PendingplaceAdapter extends RecyclerView.Adapter<PendingplaceAdapter.PendingDataHolder>{
    private ArrayList<PendingplaceModel> pendingplaceModels;
    private Context context;
    private String getTagTitleString;
    private TagDBHelper tagDBHelper;
    private placeDBHelper placeDBHelper;

    public PendingplaceAdapter(ArrayList<PendingplaceModel> pendingplaceModels, Context context) {
        this.pendingplaceModels = pendingplaceModels;
        this.context = context;
    }

    @Override
    public PendingplaceAdapter.PendingDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pending_place_layout,parent,false);
        return new PendingDataHolder(view);
    }

    @Override
    public void onBindViewHolder(PendingplaceAdapter.PendingDataHolder holder, int position) {
        placeDBHelper=new placeDBHelper(context);
        final PendingplaceModel pendingplaceModel=pendingplaceModels.get(position);
        holder.placeTitle.setText(pendingplaceModel.getplaceTitle());
        holder.placeContent.setText(pendingplaceModel.getplaceContent());
        holder.placeDate.setText(pendingplaceModel.getplaceDate());
        holder.placeTag.setText(pendingplaceModel.getplaceTag());
        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.place_edit_del_options,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
                                showDialogEdit(pendingplaceModel.getplaceID());
                                return true;
                            case R.id.delete:
                                showDeleteDialog(pendingplaceModel.getplaceID());
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    //showing confirmation dialog for deleting the places
    private void showDeleteDialog(final int tagID){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("place delete confirmation");
        builder.setMessage("Do you really want to delete ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(placeDBHelper.removeplace(tagID)){
                    Toast.makeText(context, "place deleted successfully !", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "place not deleted !", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }).create().show();
    }

    @Override
    public int getItemCount() {
        return pendingplaceModels.size();
    }

    //showing edit dialog for editing places according to the placeid
    private void showDialogEdit(final int placeID){
        placeDBHelper=new placeDBHelper(context);
        tagDBHelper=new TagDBHelper(context);
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.edit_place_dialog,null);
        builder.setView(view);
        final TextInputEditText placeTitle=(TextInputEditText)view.findViewById(R.id.place_title);
        final TextInputEditText placeContent=(TextInputEditText)view.findViewById(R.id.place_content);
        Spinner placeTags=(Spinner)view.findViewById(R.id.place_tag);
        //stores all the tags title in string format
        ArrayAdapter<String> tagsModelArrayAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,tagDBHelper.fetchTagStrings());
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

        //setting the default values coming from the database
        placeTitle.setText(placeDBHelper.fetchplaceTitle(placeID));
        placeContent.setText(placeDBHelper.fetchplaceContent(placeID));
        placeDate.setText(placeDBHelper.fetchplaceDate(placeID));

        //getting current calendar credentials
        final Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final int hour=calendar.get(Calendar.HOUR);
        final int minute=calendar.get(Calendar.MINUTE);

        //getting the placedate
        placeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                }else if(placeDBHelper.updateplace(
                        new PendingplaceModel(placeID,getplaceTitle,getplaceContent,String.valueOf(placeTagID),getplaceDate)
                )){
                    Toast.makeText(context, R.string.place_title_add_success_msg, Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context,MainActivity.class));
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,MainActivity.class));
            }
        });
        builder.create().show();
    }


    public class PendingDataHolder extends RecyclerView.ViewHolder {
        TextView placeTitle,placeContent,placeTag,placeDate,placeTime;
        ImageView option;
        public PendingDataHolder(View itemView) {
            super(itemView);
            placeTitle=(TextView)itemView.findViewById(R.id.pending_place_title);
            placeContent=(TextView)itemView.findViewById(R.id.pending_place_content);
            placeTag=(TextView)itemView.findViewById(R.id.place_tag);
            placeDate=(TextView)itemView.findViewById(R.id.place_date);
            option=(ImageView)itemView.findViewById(R.id.option);
        }
    }

    //filter the search
    public void filterplaces(ArrayList<PendingplaceModel> newPendingplaceModels){
        pendingplaceModels=new ArrayList<>();
        pendingplaceModels.addAll(newPendingplaceModels);
        notifyDataSetChanged();
    }
}
