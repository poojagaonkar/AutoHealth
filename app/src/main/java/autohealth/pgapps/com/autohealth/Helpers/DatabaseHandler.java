package autohealth.pgapps.com.autohealth.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import autohealth.pgapps.com.autohealth.Models.ChildInfoModel;

/**
 * Created by HomePC on 13-03-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "FuelDatabase";

    // Contacts table name
    private static final String TABLE_FUEL = "fuelTable";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PREVIOUSKM = "previousKm";
    private static final String KEY_KM = "kms";
    private static final String KEY_FUELQTY = "fuelQty";
    private static final String KEY_FUELCOST = "fuelCost";
    private static final String KEY_MILEAGE = "mileage";
    private static final String KEY_TOTALCOST = "totalCost";
    private static final String KEY_ISFULLTANK = "isfullTank";
    private static final String KEY_CREATIONDATE = "creationDate";
    private boolean isFullTank;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FUEL_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FUEL + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + KEY_PREVIOUSKM + " DOUBLE,"
                + KEY_KM + " DOUBLE,"
                + KEY_FUELQTY + " DOUBLE,"
                + KEY_FUELCOST + " DOUBLE,"
                + KEY_TOTALCOST + " DOUBLE,"
                + KEY_MILEAGE + " DOUBLE,"
                +KEY_ISFULLTANK + " INTEGER,"
                +KEY_CREATIONDATE + " TEXT"
                + ")";
        db.execSQL(CREATE_FUEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUEL);

        // Create tables again
        onCreate(db);
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new reading
    public void addReading(ChildInfoModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PREVIOUSKM, data.getPreviousKms());
        values.put(KEY_KM, data.getKilometers());
        values.put(KEY_FUELQTY, data.getFuelqty());
        values.put(KEY_FUELCOST, data.getFuelCost());
        values.put(KEY_TOTALCOST, data.getTotalCost());
        values.put(KEY_MILEAGE, data.getMileage());
        values.put(KEY_ISFULLTANK, data.isFullTank());
        values.put(KEY_CREATIONDATE, data.getCreatedDate());

        // Inserting Row
        db.insert(TABLE_FUEL, null, values);
        db.close(); // Closing database connection
    }

    // Getting single reading
    ChildInfoModel getReading(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FUEL, new String[]{KEY_ID,
                        KEY_KM, KEY_FUELQTY, KEY_FUELCOST, KEY_TOTALCOST, KEY_MILEAGE, KEY_ISFULLTANK, KEY_CREATIONDATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int fulltank = cursor.getInt(7);
        if(fulltank == 0 ) {
            isFullTank = false;
        } else
        {
            isFullTank = true;
        }
        ChildInfoModel data = new ChildInfoModel(Integer.parseInt(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)),
                Double.parseDouble(cursor.getString(1)),
                Double.parseDouble(cursor.getString(3)),
                Double.parseDouble(cursor.getString(4)),
                Double.parseDouble(cursor.getString(5)),
                Double.parseDouble(cursor.getString(6)),
                isFullTank,
                cursor.getString(8));

        return data;
    }

    // Getting All readings
    public List<ChildInfoModel> getAllReadings() {
        List<ChildInfoModel> readingList = new ArrayList<ChildInfoModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FUEL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChildInfoModel mModel = new ChildInfoModel();
                mModel.setID(cursor.getInt(0));
                mModel.setPreviousKms(cursor.getDouble(1));
                mModel.setKilometers(cursor.getDouble(1));
                mModel.setFuelqty(cursor.getDouble(3));
                mModel.setFuelCost(cursor.getDouble(4));
                 mModel.setTotalCost(cursor.getDouble(5));
                mModel.setMileage(cursor.getDouble(6));

                int fulltank = cursor.getInt(7);
                if(fulltank == 0 ) {
                    mModel.setIsFullTank(false);
                }
                else
                {
                    mModel.setIsFullTank(true);
                }
                mModel.setCreatedDate(cursor.getString(8));
                // Adding reading to list
                readingList.add(mModel);
            } while (cursor.moveToNext());
        }

        // return reading list
        return readingList;
    }

    // Updating single reading
    public int updateReading(ChildInfoModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PREVIOUSKM, contact.getPreviousKms());
        values.put(KEY_KM, contact.getKilometers());
        values.put(KEY_FUELQTY, contact.getFuelqty());
        values.put(KEY_FUELCOST, contact.getFuelCost());
        values.put(KEY_TOTALCOST, contact.getTotalCost());
        values.put(KEY_MILEAGE, contact.getMileage());
        values.put(KEY_ISFULLTANK, contact.isFullTank());
        values.put(KEY_CREATIONDATE, contact.getCreatedDate());

        // updating row
        return db.update(TABLE_FUEL, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single reading
    public void deleteReading(ChildInfoModel data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FUEL, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }


    // Getting readings Count
    public int getReadingsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FUEL;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
