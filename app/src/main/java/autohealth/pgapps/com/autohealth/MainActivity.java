package autohealth.pgapps.com.autohealth;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
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


import java.util.List;

import autohealth.pgapps.com.autohealth.Adapters.ReadingsAdapter;
import autohealth.pgapps.com.autohealth.Helpers.DatabaseHandler;
import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;


public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener{

    private  RecyclerView mRecyclerView;
    private ImageButton btnAdd;
    private LinearLayoutManager mLinearManager;
    private AlertDialog dialog;
    private EditText ETKM, ETFuelQty, ETFuelCost, ETTotalCost, ETMileage;
    private CheckBox chkfullTank;
    private boolean isFullTank;
    private double Kms, FuelQty, FuelCost, TotalCost, Mileage;
    private SQLiteDatabase mDatabase;
    private DatabaseHandler dbHandler;
    private ReadingsAdapter mAdapter;
    private RelativeLayout noItemLayout;
    private List<ChildInfoModel> readingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        btnAdd = (ImageButton)findViewById(R.id.btnAdd);
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
                //Building dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(layout);


                dialog = builder.create();
                dialog.show();

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
                            dbHandler.addReading(new ChildInfoModel(Kms, Kms, FuelQty, FuelCost, Mileage, TotalCost, isFullTank));

                            // Reading all contacts
                            Log.d("Reading: ", "Reading all contacts..");
                            readingList = dbHandler.getAllReadings();


                            if(readingList.size() >0) {

                                noItemLayout.setVisibility(View.GONE);
                                mAdapter = new ReadingsAdapter(MainActivity.this, readingList);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerView.setAdapter(mAdapter);
                                dialog.dismiss();
                            }


                            /*for (ChildInfoModel cn : contacts) {
                                String log = "Id: " + cn.getID() + " ,Previous Km: " + cn.getPreviousKms() + " ,Kms: " + cn.getKilometers()
                                        + " ,FuelQty: " + cn.getFuelqty()
                                        + " ,FuelCost: " + cn.getFuelCost()
                                        + " ,Mileage: " + cn.getMileage()
                                        + " ,TotalCost: " + cn.getTotalCost()
                                        + " ,FullTank: " + cn.isFullTank();
                                // Writing Contacts to log
                                Toast.makeText(MainActivity.this,"Name: " + log, Toast.LENGTH_LONG).show();

                            }*/
                        }
                    }
                });

               /* ETKM.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        Kms = 0;
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        try {
                            Kms = Double.parseDouble(ETKM.getText().toString());
                        } catch (NumberFormatException e) {
                            Kms = 0;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                        if (Kms <= 0) {
                            ETFuelQty.setEnabled(false);
                            ETTotalCost.setEnabled(false);
                            ETMileage.setEnabled(false);
                            ETFuelCost.setEnabled(false);
                            AlertDialog.Builder mAlert = new AlertDialog.Builder(MainActivity.this);
                            mAlert.setTitle("Message");
                            mAlert.setMessage("Enter Kms before proceeding");
                            mAlert.create().show();
                        } else {

                            ETFuelQty.setEnabled(true);
                            ETTotalCost.setEnabled(true);
                            ETMileage.setEnabled(true);
                            ETFuelCost.setEnabled(true);
                        }
                    }
                });*/

               /* ETKM.setOnFocusChangeListener(MainActivity.this);
                ETFuelCost.setOnFocusChangeListener(MainActivity.this);
                ETFuelQty.setOnFocusChangeListener(MainActivity.this);
                ETMileage.setOnFocusChangeListener(MainActivity.this);
                ETTotalCost.setOnFocusChangeListener(MainActivity.this);*/


            }
        });

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        double kms = 0;

        switch (v.getId())
        {
            case R.id.ETKms:

                try {
                    kms = Double.parseDouble(ETKM.getText().toString());
                } catch (NumberFormatException e) {
                    kms = 0;
                }

                if(kms <= 0)
                {
                    AlertDialog.Builder mAlert = new AlertDialog.Builder(MainActivity.this);
                    mAlert.setTitle("Message");
                    mAlert.setMessage("Enter Kms before proceeding");
                    mAlert.create().show();
                }

                break;
            case R.id.ETFuelQty:

                break;
            case R.id.ETFuelCost:

                break;
            case R.id.ETMileage:

                break;
            case R.id.ETTotalCost:

                break;
        }
    }
}
