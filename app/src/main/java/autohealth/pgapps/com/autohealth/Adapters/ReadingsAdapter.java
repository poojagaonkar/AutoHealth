package autohealth.pgapps.com.autohealth.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import autohealth.pgapps.com.autohealth.MainActivity;
import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;
import autohealth.pgapps.com.autohealth.R;

/**
 * Created by HomePC on 13-03-16.
 */
public class ReadingsAdapter extends RecyclerView.Adapter <ReadingsAdapter.ViewHolder> {

    private Activity mContext;
    private List<ChildInfoModel> readingList;


    public ReadingsAdapter(Activity mainActivity, List<ChildInfoModel> readingList) {

        this.mContext = mainActivity;
        this.readingList = readingList;
        Collections.reverse(this.readingList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_layout, parent, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ChildInfoModel data = readingList.get(position);
        holder.TVKms.setText("Kilometers: "+String.valueOf(data.getKilometers()));
        holder.TVFuelQty.setText("Fuel quantity: "+String.valueOf(data.getFuelqty()));
        holder.TVFuelCost.setText("Fuel cost: "+ String.valueOf(data.getFuelCost()));
        holder.TVTotalCost.setText("Total cost: "+String.valueOf(data.getTotalCost()));
        holder.TVMileage.setText("Mileage: "+ String.valueOf(data.getMileage()));

        Boolean isFull = data.isFullTank();
        if(isFull)
        {
            holder.TVFullTank.setText("Full Tank: "+"Yes");
            holder.TVFullTank.setTextColor(Color.GREEN);
        }
        else
        {
            holder.TVFullTank.setText("Full Tank: "+"No");
            holder.TVFullTank.setTextColor(Color.RED);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date creationDate = sdf.parse(data.getCreatedDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(creationDate);
            int mYear = cal.get(Calendar.YEAR);
            int mMonth = cal.get(Calendar.MONTH) + 1;
            int mDate = cal.get(Calendar.DAY_OF_MONTH);

            holder.TVDate.setText("Date: "+ String.valueOf(mDate) +"/" + String.valueOf(mMonth)+"/" + String.valueOf(mYear));
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return readingList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        
        public TextView TVKms;
        public TextView TVFuelQty;
        public TextView TVFuelCost;
        public TextView TVTotalCost;
        public TextView TVMileage;
        public TextView TVFullTank;
        public TextView TVDate;
        public ViewHolder(View itemView) {
            super(itemView);

            TVKms = (TextView)itemView.findViewById(R.id.TVKms);
            TVFuelQty = (TextView)itemView.findViewById(R.id.TVFuelQty);
            TVFuelCost =(TextView)itemView.findViewById(R.id.TVFuelCost);
            TVTotalCost = (TextView)itemView.findViewById(R.id.TVTotalCost);
            TVMileage =(TextView)itemView.findViewById(R.id.TVMileage);
            TVFullTank =(TextView)itemView.findViewById(R.id.TVFullTank);
            TVDate = (TextView)itemView.findViewById(R.id.TVDate);
        }
    }

}
