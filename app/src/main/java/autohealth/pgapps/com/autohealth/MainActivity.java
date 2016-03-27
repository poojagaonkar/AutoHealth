package autohealth.pgapps.com.autohealth;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import autohealth.pgapps.com.autohealth.Adapters.ReadingsAdapter;
import autohealth.pgapps.com.autohealth.Helpers.DatabaseHandler;
import autohealth.pgapps.com.autohealth.Helpers.DialogHelper;
import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;


public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    private  RecyclerView mRecyclerView;
    private ImageButton btnAdd;
    private LinearLayoutManager mLinearManager;
    private AlertDialog dialog;
    private EditText ETKM, ETFuelQty, ETFuelCost, ETTotalCost, ETMileage;
    private TextView TVPreviousKm;
    private CheckBox chkfullTank;
    private boolean isFullTank;
    private double Kms, FuelQty, FuelCost, TotalCost, Mileage;
    private SQLiteDatabase mDatabase;
    private DatabaseHandler dbHandler;
    private ReadingsAdapter mAdapter;
    private RelativeLayout noItemLayout;
    private List<ChildInfoModel> readingList;
    private ImageButton btnReport;
    private  DateFormat dateFormat;
    private String currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        btnAdd = (ImageButton)findViewById(R.id.btnAdd);
        btnReport = (ImageButton)findViewById(R.id.btnReport);
        noItemLayout = (RelativeLayout)findViewById(R.id.relLayoutDefault);

        mLinearManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearManager);

        dbHandler = new DatabaseHandler(this);

        readingList = dbHandler.getAllReadings();

        if(readingList.size() >0)
        {
            noItemLayout.setVisibility(View.GONE);
            mAdapter = new ReadingsAdapter(MainActivity.this, readingList);
            mRecyclerView.setAdapter(mAdapter);
        }
        else
        {
            noItemLayout.setVisibility(View.VISIBLE);
        }



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.form_layout, null);

                ETKM = (EditText) layout.findViewById(R.id.ETKms);
                ETFuelQty = (EditText) layout.findViewById(R.id.ETFuelQty);
                ETFuelCost = (EditText) layout.findViewById(R.id.ETFuelCost);
                ETMileage = (EditText) layout.findViewById(R.id.ETMileage);
                ETTotalCost = (EditText) layout.findViewById(R.id.ETTotalCost);
                final Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
                final Button btnSave = (Button) layout.findViewById(R.id.btnSave);
                chkfullTank = (CheckBox) layout.findViewById(R.id.CBFullTank);
                TVPreviousKm = (TextView) layout.findViewById(R.id.txtPreviousKm);

                String lastKm = "Previous Km reading: " + String.valueOf(readingList.get(0).getKilometers());
                TVPreviousKm.setText(lastKm);
                TVPreviousKm.setTextColor(Color.RED);
                //Building dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(layout);


                dialog = builder.create();
                dialog.show();

                ETKM.setOnFocusChangeListener(MainActivity.this);
                ETFuelCost.setOnFocusChangeListener(MainActivity.this);
                ETFuelQty.setOnFocusChangeListener(MainActivity.this);
                ETMileage.setOnFocusChangeListener(MainActivity.this);
                ETTotalCost.setOnFocusChangeListener(MainActivity.this);

                dateFormat= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                currentDate = dateFormat.format(cal.getTime());

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });
                btnSave.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Kms = Double.parseDouble(ETKM.getText().toString());
                        FuelQty = Double.parseDouble(ETFuelQty.getText().toString());
                        FuelCost = Double.parseDouble(ETFuelCost.getText().toString());
                        TotalCost = Double.parseDouble(ETTotalCost.getText().toString());
                        Mileage = Double.parseDouble(ETMileage.getText().toString());

                        if (chkfullTank.isChecked()) {
                            isFullTank = true;
                        } else {
                            isFullTank = false;
                        }

                        if (Kms <= 0) {
                            AlertDialog.Builder mAlert = new AlertDialog.Builder(MainActivity.this);
                            mAlert.setTitle("Message");
                            mAlert.setMessage("Enter Kms before proceeding");
                            mAlert.create().show();
                            return;
                        } else {



                            dbHandler.addReading(new ChildInfoModel(Kms, Kms, FuelQty, FuelCost, Mileage, TotalCost, isFullTank , currentDate));

                            // Reading all contacts
                            Log.d("Reading: ", "Reading all contacts..");
                            readingList = dbHandler.getAllReadings();


                            if (readingList.size() > 0) {


                                noItemLayout.setVisibility(View.GONE);
                                mAdapter = new ReadingsAdapter(MainActivity.this, readingList);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.setAdapter(mAdapter);
                                dialog.dismiss();
                            }


                        }
                    }
                });




            }
        });


        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(MainActivity.this,ReportActivty.class);
                startActivity(in);
            }
        });
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        double kms = 0;
        DialogHelper dHelper = new DialogHelper();
        switch (v.getId())
        {
            case R.id.ETKms:

                if (!hasFocus) {
                    try {
                        if(ETKM.getText().toString() != "")

                            Kms = Double.parseDouble(ETKM.getText().toString());
                        if (Kms <= 0) {
                            dHelper.CreateErrorDialog(MainActivity.this,"Error","Kilometers cannot be blank or less than 0");
                        }
                    } catch (Exception e1) {
                        dHelper.CreateErrorDialog(MainActivity.this,"Error","Kilometers cannot be blank or less than 0");

                    }
                }

                break;
            case R.id.ETFuelQty:
                if (hasFocus) {

                    if (FuelQty ==0 &&(FuelCost > 0 && TotalCost > 0)) {

                        FuelQty = TotalCost / FuelCost;
                        ETFuelQty.setText(String.valueOf(FuelQty));

                    }
                }
                else {
                        try {
                            if(ETFuelQty.getText().toString() != "") {
                                FuelQty = Double.parseDouble(ETFuelQty.getText().toString());
                            }

                        } catch (Exception e1) {
                            dHelper.CreateErrorDialog(MainActivity.this,"Error","Kilometers cannot be blank or less than 0");

                        }
                    }


                break;
            case R.id.ETFuelCost:
                if (hasFocus) {

                    if (FuelCost ==0 &&(FuelQty > 0 && TotalCost > 0)) {

                        FuelCost = TotalCost / FuelQty;
                        ETFuelCost.setText(String.valueOf(FuelCost));

                    }
                }
                else {
                    try {
                        if(ETFuelCost.getText().toString() != "") {
                            FuelCost = Double.parseDouble(ETFuelCost.getText().toString());
                        }

                    } catch (Exception e1) {
                        dHelper.CreateErrorDialog(MainActivity.this,"Error","Kilometers cannot be blank or less than 0");

                    }
                }

                break;
            case R.id.ETMileage:

                break;
            case R.id.ETTotalCost:

                if (hasFocus) {

                    if (TotalCost ==0 &&(FuelQty > 0 && FuelCost > 0)) {

                        TotalCost = FuelCost  * FuelQty;
                        ETTotalCost.setText(String.valueOf(TotalCost));

                    }
                }
                else {
                    try {
                        if(ETTotalCost.getText().toString() != "") {
                            TotalCost = Double.parseDouble(ETTotalCost.getText().toString());
                        }

                    } catch (Exception e1) {
                        dHelper.CreateErrorDialog(MainActivity.this,"Error","Kilometers cannot be blank or less than 0");

                    }
                }

                break;
        }
    }
}
