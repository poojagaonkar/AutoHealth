package autohealth.pgapps.com.autohealth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;



import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private  RecyclerView mRecyclerView;
    private ImageButton btnAdd;
    private LinearLayoutManager mLinearManager;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.mRecyclerView);
        btnAdd = (ImageButton)findViewById(R.id.btnAdd);

        mLinearManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearManager);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.form_layout, null);

                final EditText ETKM = (EditText) layout.findViewById(R.id.ETKms);
                final EditText ETFuelQty = (EditText) layout.findViewById(R.id.ETFuelQty);
                final EditText ETFuelCost = (EditText) layout.findViewById(R.id.ETFuelCost);
                final EditText ETMileage = (EditText) layout.findViewById(R.id.ETMileage);
                final EditText ETTotalCost = (EditText) layout.findViewById(R.id.ETTotalCost);
                final Button btnCancel = (Button) layout.findViewById(R.id.btnCancel);
                final Button btnSave =(Button) layout.findViewById(R.id.btnCancel);
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
            }
        });

    }


}
