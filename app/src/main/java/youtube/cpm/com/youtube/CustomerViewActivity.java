package youtube.cpm.com.youtube;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import youtube.cpm.com.youtube.GetterSetter.CustomerDetailsSettrGetter;
import youtube.cpm.com.youtube.adapter.CustomerViewAdapter;
import youtube.cpm.com.youtube.constant.CommonString;
import youtube.cpm.com.youtube.database.Database;
import youtube.cpm.com.youtube.util.Utilities;

public class CustomerViewActivity extends AppCompatActivity {
    private RelativeLayout activity_customer_view;
    private FloatingActionButton fab;
    ListView listView;
    ArrayList<CustomerDetailsSettrGetter> reasondata = new ArrayList<CustomerDetailsSettrGetter>();
    Database database;
    private   CustomerViewAdapter adapter;
    private Toolbar toolbar;
    Object result = "";
    Dialog dialog;
    Database db;
    final Context context = this;
    private String currentdate, currentuserid;
    private ProgressDialog loading;
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentdate = preferences.getString(CommonString.KEY_DATE, null);
        currentuserid = preferences.getString(CommonString.KEY_USERNAME, null);

        db = new Database(this);
        db.open();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activity_customer_view=(RelativeLayout)findViewById(R.id.activity_customer_view) ;



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isConnectingToInternet(CustomerViewActivity.this)) {
                    signup();

                } else {

                    Snackbar.make(v, "Internet Not Connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reasondata.clear();
        database = new Database(this);
        database.open();
        reasondata = database.getYoutube();


        if (reasondata != null) {
            adapter = new CustomerViewAdapter(CustomerViewActivity.this, R.layout.list, reasondata);
            listView.setAdapter(adapter);
        }

        if (reasondata!=null){
            for (int i1 = 0; i1 < reasondata.size(); i1++){
                CustomerDetailsSettrGetter customerDetailsSettrGetter = reasondata.get(i1);
                if (customerDetailsSettrGetter.getN().equalsIgnoreCase("N")) {
                        fab.setVisibility(View.VISIBLE);
                        reasondata.clear();
                        reasondata.addAll(database.getYoutube());
                        adapter.notifyDataSetChanged();
                        listView.invalidateViews();
                        listView.refreshDrawableState();
                        break;
                }
                else

                    fab.setVisibility(View.INVISIBLE);
            }


        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Customer Detail View");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {

            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }
        return super.onOptionsItemSelected(item);
    }

    private class UploadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(CustomerViewActivity.this,"Processing","Please wait...",false,false);
            loading.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            dialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                String stock_facing_xml = "";
                String onXML = "";
                if (reasondata.size() > 0) {

                    for (int i1 = 0; i1 < reasondata.size(); i1++) {
                        onXML = "[YOUTUBE_FACING_DATA_NEW]"
                                + "[REPORTING_DATE]" + reasondata.get(i1).getReportingdate() + "[/REPORTING_DATE]"
                                // + "[SUPERVISOR_NAME]" + str + "[/SUPERVISOR_NAME]"
                                + "[USER_ID]" + currentuserid + "[/USER_ID]"
                                + "[LOCATION]" + reasondata.get(i1).getLocation() + "[/LOCATION]"
                                + "[CUSTOMER_NAME]" + reasondata.get(i1).getCustomername() + "[/CUSTOMER_NAME]"
                                + "[CONTECT_NUMBER]" + reasondata.get(i1).getContectnumber() + "[/CONTECT_NUMBER]"
                                + "[EMAIL_ADDRESS]" + reasondata.get(i1).getEmailaddress() + "[/EMAIL_ADDRESS]"
                                + "[MOBILE_BRAND]" + reasondata.get(i1).getMobilebrand() + "[/MOBILE_BRAND]"
                                + "[MOBILE_MODELNO]" + reasondata.get(i1).getMobilemodelno() + "[/MOBILE_MODELNO]"
                                + "[INSTAAL_ID]" + reasondata.get(i1).getInstallid() + "[/INSTAAL_ID]"
                                + "[UPLODE_PICTURE]" + reasondata.get(i1).getPicture() + "[/UPLODE_PICTURE]"
                                + "[/YOUTUBE_FACING_DATA_NEW]";

                        stock_facing_xml = stock_facing_xml + onXML;
                        final String sos_xml = "[DATA]" + stock_facing_xml + "[/DATA]";
                        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_STOCK_XML_DATA);
                        request.addProperty("XMLDATA", sos_xml);
                        request.addProperty("KEYS", "STOCK_FACING_DATA_NEW");
                        request.addProperty("USERNAME", "asdf");

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = true;
                        envelope.setOutputSoapObject(request);

                        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
                        //   androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_STOCK_XML_DATA, envelope);
                        androidHttpTransport.call(CommonString.SOAP_ACTION + CommonString.METHOD_UPLOAD_STOCK_XML_DATA, envelope);

                        result = envelope.getResponse().toString();

                        db.open();
                        db.updateYouTube("Y", reasondata.get(i1).getInstallid());
                        System.out.println("result" + result);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return (String) result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
          //  dialog.dismiss();
            if (s.equalsIgnoreCase("success")) {


                if (reasondata.size() > 0) {

                    reasondata.clear();
                    reasondata.addAll( database.getYoutube());
                    adapter.notifyDataSetChanged();
                    listView.invalidateViews();
                    listView.refreshDrawableState();
                    finish();
                    Toast.makeText(getApplicationContext(), "Customer Details Successfully Upload ", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(CustomerViewActivity.this, CustomerViewActivity.class);
                    startActivity(i);

                }


            }

        }
    }

    public void signup() {
        new UploadTask().execute();
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                    }
                }, 3000);
    }

}
