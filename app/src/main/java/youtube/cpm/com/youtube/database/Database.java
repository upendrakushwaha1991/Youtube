package youtube.cpm.com.youtube.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import youtube.cpm.com.youtube.GetterSetter.CustomerDetailsSettrGetter;
import youtube.cpm.com.youtube.constant.CommonString;

/**
 * Created by upendra on 15/3/16.
 */
public class Database {

    private static class DatabaseHelper extends SQLiteOpenHelper {


        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_YOUTUBE_NAME);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        }
    }

    private static final int DATABASE_VERSION = 1;

    public static String DATABASE_NAME = "database.db";

    private DatabaseHelper db;
    public SQLiteDatabase sqLiteDb;
    private Context HCtx = null;


    Date mydate = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    String yestr = dateFormat.format(mydate);


    // Column name GPSMENU TABLE
    public static final String TABLE_YOUTUBE = "cage_game";


    public static final String TABLE_YOUTUBE_NAME = "CREATE TABLE  IF NOT EXISTS " + TABLE_YOUTUBE
            + " ("
            + CommonString.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + CommonString.KEYDATE + " VARCHAR,"
            + CommonString.KEYSUPERVIOSERNAME + " VARCHAR,"
            + CommonString.KEYLOCATION + " VARCHAR,"
            + CommonString.KEYCUSTOMERNAME + " VARCHAR,"
            + CommonString.KEYCONTECTNUMBER + " VARCHAR,"
            + CommonString.KEYEMAILADDRESS + " VARCHAR,"
            + CommonString.KEYMOBILEBRAND + " VARCHAR,"
            + CommonString.KEYIMAGE + " VARCHAR,"
            + CommonString.KEYMODELNO + " VARCHAR,"
            + CommonString.KEYINSTALLID + " VARCHAR,"
            + CommonString.KEYUPLODESTATUS + " VARCHAR" + ")";


    public Database(Context ctx) {
        HCtx = ctx;
    }

    public Database open() throws SQLException {

        if (sqLiteDb != null && db != null && sqLiteDb.isOpen())
            return this;

        db = new DatabaseHelper(HCtx);
        sqLiteDb = db.getWritableDatabase();
        return this;
    }


    public void cleanTable(String tableName) {
        sqLiteDb.delete(tableName, null, null);
    }

    public void deletedRecord() {
        try {
            sqLiteDb.delete(TABLE_YOUTUBE, null, null);
        } catch (Exception e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public long insert_Youtube(CustomerDetailsSettrGetter data) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(CommonString.KEYIMAGE, data.getPicture());
        initialValues.put(CommonString.KEYDATE, data.getReportingdate());
        initialValues.put(CommonString.KEYSUPERVIOSERNAME, data.getSupervisorname());
        initialValues.put(CommonString.KEYLOCATION, data.getLocation());
        initialValues.put(CommonString.KEYCUSTOMERNAME, data.getCustomername());
        initialValues.put(CommonString.KEYCONTECTNUMBER, data.getContectnumber());
        initialValues.put(CommonString.KEYEMAILADDRESS, data.getEmailaddress());
        initialValues.put(CommonString.KEYMOBILEBRAND, data.getMobilebrand());
        initialValues.put(CommonString.KEYMODELNO, data.getMobilemodelno());
        initialValues.put(CommonString.KEYINSTALLID, data.getInstallid());
        initialValues.put(CommonString.KEYUPLODESTATUS, data.getN());

        long id = sqLiteDb.insert(TABLE_YOUTUBE, null, initialValues);
        System.out.println("id" + id);
        return id;
    }

    public ArrayList<CustomerDetailsSettrGetter> getYoutube() {
        Log.d("FetchWidowdata->Start<-", "-");
        ArrayList<CustomerDetailsSettrGetter> youTubeInfo = new ArrayList<>();

        try {

            String SelectQuery = " Select * From " + TABLE_YOUTUBE;
            Cursor dbcursor = sqLiteDb.rawQuery(SelectQuery, null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CustomerDetailsSettrGetter sb = new CustomerDetailsSettrGetter();

                    sb.setReportingdate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYDATE)));
                    sb.setSupervisorname(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYSUPERVIOSERNAME)));
                    sb.setLocation(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYLOCATION)));
                    sb.setCustomername(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYCUSTOMERNAME)));
                    sb.setContectnumber(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYCONTECTNUMBER)));
                    sb.setEmailaddress(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYEMAILADDRESS)));
                    sb.setMobilebrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYMOBILEBRAND)));
                    sb.setMobilemodelno(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYMODELNO)));
                    sb.setInstallid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYINSTALLID)));
                    sb.setPicture(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYIMAGE)));
                    sb.setN(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYUPLODESTATUS)));

                    youTubeInfo.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return youTubeInfo;
            }
        } catch (Exception e) {
            Log.d("Exc get windows list!", e.toString());
            return youTubeInfo;
        }

        Log.d("Fetching windows->Stop<", "-");
        return youTubeInfo;
    }


    public ArrayList<CustomerDetailsSettrGetter> getYoutubeByDate(String visit_date) {
        ArrayList<CustomerDetailsSettrGetter> youTubeInfo = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = sqLiteDb.rawQuery("SELECT  * from " + TABLE_YOUTUBE + " where " + CommonString.KEYDATE + "='" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CustomerDetailsSettrGetter sb = new CustomerDetailsSettrGetter();
                    sb.setReportingdate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYDATE)));
                    sb.setSupervisorname(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYSUPERVIOSERNAME)));
                    sb.setLocation(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEYLOCATION)));
                    youTubeInfo.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return youTubeInfo;
            }
        } catch (Exception e) {
            Log.d("Exc get windows list!", e.toString());
            return youTubeInfo;
        }

        Log.d("Fetching windows->Stop<", "-");
        return youTubeInfo;
    }

    public boolean isCoverageDataFilled(String visit_date) {
        boolean filled = false;
        Cursor dbcursor = null;
        String SelectQueryFild = " Select * From " + TABLE_YOUTUBE;

        try {
            dbcursor = sqLiteDb.rawQuery(SelectQueryFild
                    + "where " + CommonString.KEY_VISIT_DATE + "<>'" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }
            }
        } catch (Exception e) {
            Log.d("Exception ", " when fetching Records!!!!!!!!!!!!!!!!!!!!! " + e.toString());
            return filled;
        }
        return filled;
    }

    public void updateYouTube(String status, String installId) {

        try {

            ContentValues updatedValues = new ContentValues();
            updatedValues.put(CommonString.KEYUPLODESTATUS, status);

            int l = sqLiteDb.update(TABLE_YOUTUBE, updatedValues, CommonString.KEYINSTALLID + "='" + installId + "'", null);
            System.out.println("update : " + l);
        } catch (Exception e) {
            Log.d("Database Data ", e.toString());

        }
    }


    public void updateEntry(String fisrtname, String lastName, String dob, String email) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put("fname", fisrtname);
        updatedValues.put("lname", lastName);
        updatedValues.put("dob", dob);
        updatedValues.put("email", email);
        String where = "email = ?";
        sqLiteDb.update(TABLE_YOUTUBE, updatedValues, where, new String[]{email});
    }


}
