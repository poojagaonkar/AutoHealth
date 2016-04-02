package autohealth.pgapps.com.autohealth;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import autohealth.pgapps.com.autohealth.Custom.MyMarkerView;
import autohealth.pgapps.com.autohealth.Helpers.DatabaseHandler;
import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;

public class ReportActivty extends AppCompatActivity{

    private LineChart mChart;
    private Typeface mTf;
    private DatabaseHandler dbHandler;
    private List<ChildInfoModel> readingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_report_activty);

        dbHandler = new DatabaseHandler(this);
        readingList = dbHandler.getAllReadings();
        mChart = (LineChart) findViewById(R.id.chart1);



        LineData data = getData(readingList.size(), 100);
        data.setValueTypeface(mTf);

        setupChart(mChart, data, Color.WHITE);


    }



    private void setupChart(LineChart chart, LineData data, int color) {

        chart.setData(data);
        // - X Axis
        XAxis xAxis = chart.getXAxis();

        xAxis.setTextSize(12f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());
        xAxis.setEnabled(true);
        xAxis.disableGridDashedLine();
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);

        // - Y Axis
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());

        leftAxis.setAxisMinValue(0f); // to set minimum yAxis
        leftAxis.setStartAtZero(false);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setDrawGridLines(true);
        chart.getAxisRight().setEnabled(false);

        chart.setDescription("");
        chart.setNoDataTextDescription("Auto Health");
        // enable touch gestures
        chart.setTouchEnabled(true);
         // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.animateX(2500);

    }

    private LineData getData(int count, float range) {

    /* String[] mMonths = new String[]{"January","February","March","April","May","June","July","August","Spetember"
     ,"October","November","December"};
*/
        List<Date> mDates = new ArrayList<Date>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for(ChildInfoModel data: readingList)
        {
            try {
                Date dateObj = sdf.parse(data.getCreatedDate());
                mDates.add(dateObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(mDates.get(i));
            int mYear = cal.get(Calendar.YEAR);
            int mMonth = cal.get(Calendar.MONTH) + 1;
            int mDate = cal.get(Calendar.DAY_OF_MONTH);
            String dateString = String.valueOf(mDate) +"/" + String.valueOf(mMonth)+"/" + String.valueOf(mYear);
            xVals.add(dateString);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> yMileage = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
           ChildInfoModel data = readingList.get(i);
            float val = (float)data.getFuelCost();
            yVals.add(new Entry(val, i));
        }

        for (int i = 0; i < count; i++) {
            ChildInfoModel data = readingList.get(i);
            float val = (float)data.getMileage();
            yMileage.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Fuel Cost");
        set1.setLineWidth(1.75f);
        set1.setCircleRadius(3f);
        set1.setColor(Color.GREEN);
        set1.setCircleColor(Color.GREEN);
        set1.setHighLightColor(Color.GREEN);
        set1.setDrawValues(true);

        LineDataSet set2 = new LineDataSet(yMileage, "Mileage");
        set1.setLineWidth(1.75f);
        set1.setCircleRadius(3f);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setHighLightColor(Color.RED);
        set1.setDrawValues(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2);

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        return data;
    }
}
