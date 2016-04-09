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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import autohealth.pgapps.com.autohealth.Adapters.ReadingsAdapter;
import autohealth.pgapps.com.autohealth.Helpers.Constants;
import autohealth.pgapps.com.autohealth.Helpers.DatabaseHandler;
import autohealth.pgapps.com.autohealth.Helpers.DialogHelper;
import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;


public class MainActivity extends AppCompatActivity {

    private  RecyclerView mRecyclerView;
    private ImageButton btnAdd;
    private LinearLayoutManager mLinearManager;
    private AlertDialog dialog;

    private boolean isFullTank;
    private double Kms, FuelQty, FuelCost, TotalCost, Mileage;
    private SQLiteDatabase mDatabase;
    private DatabaseHandler dbHandler;
    private ReadingsAdapter mAdapter;
    private RelativeLayout noItemLayout;
    private List<ChildInfoModel> readingList;
    private ImageButton btnReport;

    private double lastKmReading;
    private Constants mConstants;
    private List<ChildInfoModel> tempFullList;
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
        mConstants = new Constants();

        if(readingList.size() >0)
        {
            noItemLayout.setVisibility(View.GONE);
            mAdapter = new ReadingsAdapter(MainActivity.this, readingList);
            mRecyclerView.setAdapter(mAdapter);
            lastKmReading = readingList.get(0).getKilometers();
            tempFullList = new ArrayList<ChildInfoModel>();
           // mConstants.fullTankList = new ArrayList<ChildInfoModel>();
            for(ChildInfoModel mModel : readingList)
            {
                if(mModel.isFullTank() ==true)
                {
                    tempFullList.add(mModel);
                    mConstants.setFullTankList(tempFullList);
                }
                /*else
                {
                    List<ChildInfoModel> mList = new ArrayList<ChildInfoModel>();
                    mList.add(mModel);
                    mConstants.setFullTankList(mList);
                }*/
            }
        }
        else
        {
            noItemLayout.setVisibility(View.VISIBLE);
        }



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, NewRecordActivity.class);
                MainActivity.this.startActivityForResult(in,200, null);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200) {
            Log.d("Reading: ", "Reading all contacts..");
            readingList = dbHandler.getAllReadings();


            if (readingList.size() > 0) {


                noItemLayout.setVisibility(View.GONE);
                mAdapter = new ReadingsAdapter(MainActivity.this, readingList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);

            }
        }
        else
        {

        }
    }
}
