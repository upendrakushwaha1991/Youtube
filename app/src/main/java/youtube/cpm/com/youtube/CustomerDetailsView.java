package youtube.cpm.com.youtube;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import youtube.cpm.com.youtube.GetterSetter.CustomerDetailsSettrGetter;
import youtube.cpm.com.youtube.database.Database;

public class CustomerDetailsView extends AppCompatActivity {
    private EditText cotmnameview, contectview, emailview;
    private Toolbar toolbar;
    Database database;
    CustomerDetailsSettrGetter edit;
    ArrayList<CustomerDetailsSettrGetter> reasondata = new ArrayList<CustomerDetailsSettrGetter>();

    CustomerDetailsSettrGetter customerDetailsSettrGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_view);
        getId();
    }

    private void getId() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cotmnameview = (EditText) findViewById(R.id.cotmnameview);
        contectview = (EditText) findViewById(R.id.contectview);
        emailview = (EditText) findViewById(R.id.emailview);

        database = new Database(this);
        database.open();

        reasondata = database.getYoutube();
        for (int i1 = 0; i1 < reasondata.size(); i1++) {
            edit = (CustomerDetailsSettrGetter) reasondata.get(i1);

            cotmnameview.setText(edit.getCustomername());
            contectview.setText(edit.getContectnumber());
            emailview.setText(edit.getEmailaddress());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Customer Detail View");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {


            // NavUtils.navigateUpFromSameTask(this);
            finish();


            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }

}
