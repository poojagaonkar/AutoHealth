package autohealth.pgapps.com.autohealth;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import autohealth.pgapps.com.autohealth.Adapters.ReadingsAdapter;
import autohealth.pgapps.com.autohealth.Helpers.Constants;
import autohealth.pgapps.com.autohealth.Helpers.DatabaseHandler;
import autohealth.pgapps.com.autohealth.Helpers.DialogHelper;
import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;

public class NewRecordActivity extends AppCompatActivity implements View.OnFocusChangeListener , CompoundButton.OnCheckedChangeListener{

    private EditText ETKM, ETFuelQty, ETFuelCost, ETTotalCost, ETMileage;
    private TextView TVPreviousKm;
    private CheckBox chkfullTank;
    private boolean isFullTank;
    private double Kms, FuelQty, FuelCost, TotalCost, Mileage;
    private double lastKmReading;
    private  DateFormat dateFormat;
    private String currentDate;
    private DatabaseHandler dbHandler;
    private List<ChildInfoModel> readingList;
    private Constants mConstants;
    private List<ChildInfoModel> tempFullList;
    private  List<ChildInfoModel> nonFullTankList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_layout);

        ETKM = (EditText) findViewById(R.id.ETKms);
        ETFuelQty = (EditText) findViewById(R.id.ETFuelQty);
        ETFuelCost = (EditText) findViewById(R.id.ETFuelCost);
        ETMileage = (EditText) findViewById(R.id.ETMileage);
        ETTotalCost = (EditText) findViewById(R.id.ETTotalCost);
        final Button btnCancel = (Button) findViewById(R.id.btnCancel);
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        chkfullTank = (CheckBox) findViewById(R.id.CBFullTank);
        TVPreviousKm = (TextView) findViewById(R.id.txtPreviousKm);

        dbHandler = new DatabaseHandler(this);
        readingList = dbHandler.getAllReadings();

        lastKmReading = readingList.get(readingList.size()-1).getKilometers();
        String lastKm = "Previous Km reading: " + String.valueOf(lastKmReading);
        TVPreviousKm.setText(lastKm);
        TVPreviousKm.setTextColor(Color.RED);
       

        ETMileage.setEnabled(false);
        ETMileage.setBackgroundColor(Color.LTGRAY);

        ETKM.setOnFocusChangeListener(this);
        ETFuelCost.setOnFocusChangeListener(this);
        ETFuelQty.setOnFocusChangeListener(this);
      //  ETMileage.setOnFocusChangeListener(this);
        ETTotalCost.setOnFocusChangeListener(this);
        chkfullTank.setOnCheckedChangeListener(NewRecordActivity.this);

        dateFormat= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        currentDate = dateFormat.format(cal.getTime());
        tempFullList = new ArrayList<ChildInfoModel>();
        nonFullTankList = new ArrayList<ChildInfoModel>();
        for(ChildInfoModel mModel : readingList)
        {
            if(mModel.isFullTank() ==true)
            {
                tempFullList.add(mModel);
                mConstants.setFullTankList(tempFullList);
            }
            else
            {
                nonFullTankList.add(mModel);
                mConstants.setNonFullTankList(nonFullTankList);
            }

        }



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               NewRecordActivity.this.finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Kms = Double.parseDouble(ETKM.getText().toString());
                FuelQty = Double.parseDouble(ETFuelQty.getText().toString());
                FuelCost = Double.parseDouble(ETFuelCost.getText().toString());
                TotalCost = Double.parseDouble(ETTotalCost.getText().toString());

                if(isFullTank) {
                    Mileage = Double.parseDouble(ETMileage.getText().toString());
                }
                else
                {
                    Mileage = 0;
                }

                if (Kms <= 0) {
                    AlertDialog.Builder mAlert = new AlertDialog.Builder(NewRecordActivity.this);
                    mAlert.setTitle("Message");
                    mAlert.setMessage("Enter Kms before proceeding");
                    mAlert.create().show();
                    return;
                } else {



                    dbHandler.addReading(new ChildInfoModel(Kms, Kms, FuelQty, FuelCost, Mileage, TotalCost, isFullTank , currentDate));

                    // Reading all contacts
                    NewRecordActivity.this.finish();


                }
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
                            dHelper.CreateErrorDialog(this,"Error","Kilometers cannot be blank or less than 0");
                        }
                        if( Kms <= lastKmReading)
                        {
                            dHelper.CreateErrorDialog(this,"Error","Kilometers cannot be less than or equal to previous km reading");
                        }
                    } catch (Exception e1) {
                        dHelper.CreateErrorDialog(this,"Error","Kilometers cannot be blank or less than 0");

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
                        dHelper.CreateErrorDialog(this,"Error","Kilometers cannot be blank or less than 0");

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
                        dHelper.CreateErrorDialog(this,"Error","Kilometers cannot be blank or less than 0");

                    }
                }

                break;
           /* case R.id.ETMileage:
                if (hasFocus) {


                    double mTotalfuelQty = 0;
                    if(mConstants.fullTankList.size()>0) {
                        for (ChildInfoModel mModel2 : mConstants.fullTankList) {
                            mTotalfuelQty = mTotalfuelQty + mModel2.getFuelqty();
                        }
                    }


                    double prevKm = tempFullList.get(0).getKilometers();
                    if(prevKm <=0)
                    {
                        ETMileage.setText(String.valueOf(0));
                    }
                    else if (FuelQty > 0 && Kms > 0 && prevKm > 0 && isFullTank) {
                        try {

                            if(mTotalfuelQty >0)
                            {
                                FuelQty = FuelQty + mTotalfuelQty;
                            }
                            Mileage = (Kms - prevKm) / FuelQty;
                            ETMileage.setText(String.format("%.2f",Mileage));
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                    else
                    {
                        dHelper.CreateErrorDialog(this,"Error","Have you filled the tank completely?");
                    }
                }



                break;*/
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
                        dHelper.CreateErrorDialog(this,"Error","Kilometers cannot be blank or less than 0");

                    }
                }

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        double mTotalfuelQty = 0;
        double prevKm = 0;
        if(isChecked)
        {
            isFullTank = true;
            ETMileage.setEnabled(true);
            ETMileage.setBackgroundColor(Color.WHITE);

            if(mConstants.fullTankList.size()>0) {

                int readFullId = mConstants.fullTankList.get(mConstants.fullTankList.size()-1).getID();
                int readNonFullId = mConstants.nonFullTankList.get(mConstants.nonFullTankList.size()-1).getID();
                mTotalfuelQty = mConstants.fullTankList.get(mConstants.fullTankList.size()-1).getFuelqty();
                for (ChildInfoModel mModel2 : mConstants.nonFullTankList) {

                    if(mModel2.getID() > readFullId) {
                        mTotalfuelQty = mTotalfuelQty + mModel2.getFuelqty();
                    }
                }


                /*for (ChildInfoModel mModel2 : mConstants.nonFullTankList) {
                    mTotalfuelQty = mTotalfuelQty + mModel2.getFuelqty();
                }*/
            }


             prevKm = tempFullList.get(tempFullList.size()-1).getKilometers();

            if(prevKm <=0)
            {
                ETMileage.setText(String.valueOf(0));
            }
            else if (FuelQty > 0 && Kms > 0 && prevKm > 0 && isFullTank) {
                try {

                    if (mTotalfuelQty > 0) {
                        if(readingList.get(readingList.size()-1).isFullTank() == true)
                        {
                            FuelQty = mTotalfuelQty;
                        }
                        else
                        FuelQty = FuelQty + mTotalfuelQty;
                    }
                    Mileage = (Kms - prevKm) / FuelQty;
                    ETMileage.setText(String.format("%.2f", Mileage));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        else
        {
            isFullTank = false;
            ETMileage.setEnabled(false);
            ETMileage.setBackgroundColor(Color.LTGRAY);
            Mileage =0;
            FuelQty = Double.parseDouble(ETFuelQty.getText().toString());
            prevKm = 0;
            mTotalfuelQty = 0;
            ETMileage.setText("");
        }
    }
}
