package youtube.cpm.com.youtube;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import youtube.cpm.com.youtube.GetterSetter.CustomerDetailsSettrGetter;
import youtube.cpm.com.youtube.constant.CommonString;
import youtube.cpm.com.youtube.database.Database;
import youtube.cpm.com.youtube.util.Base64;
import youtube.cpm.com.youtube.util.FragmentDialogFragmentToShowMessages;
import youtube.cpm.com.youtube.util.Utilities;
import youtube.cpm.com.youtube.xmlGetterSetter.FailureGetterSetter;
import youtube.cpm.com.youtube.xmlHandlers.FailureXMLHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String errormsg = "", Path;
    private ProgressBar pb;
    private TextView percentage, message;
    private FailureGetterSetter failureGetterSetter = null;
    Dialog dialog1;
    private FloatingActionButton fab;
    private Button btnview;
    private EditText location, customername, contectname, emailaddress, mobilebrand, mobilemodelno, installno;
    private TextView datetime, choosefile, textname;
    private Spinner sppiner;
    private ProgressDialog loading;
    private TextView reportingdate, icon_date;
    private TextView camera_payment_pic, gallery_payment_expense_pic;
    private Toolbar toolbar;
    private FragmentDialogFragmentToShowMessages fragmentDialogFragmentToShowMessages;
    String datePaid;
    String _pathforcheck = "", _path, str, str1;
    final Context context = this;
    Dialog dialog;
    private Typeface font;
    boolean gallery_flag = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    File finalFile;
    int a = 0, b = 0;
    String reportdate;
    private String imagepath = "1";
    String[] userNames = {"Select", "Upendra", "Satyendra", "Gaggan"};
    boolean validation = true;
    private String strlocation, strcustomername, strcontectnumber, stremailaddress, strmobilebrand, strmobilemodelno, strinstallno;
    CustomerDetailsSettrGetter customerDetails;
    Database db;
    Object result = "";
    Uri outputFileUri;
    String gallery_package = "";

    private SharedPreferences preferences = null;
    String currentdate, currentuserid, image_name = "";
    ArrayList<CustomerDetailsSettrGetter> inserted_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentdate = preferences.getString(CommonString.KEY_DATE, null);
        currentuserid = preferences.getString(CommonString.KEY_USERNAME, null);

        System.out.println("currentdate" + currentdate);
        str1 = CommonString.FILE_PATH;


        db = new Database(this);
        db.open();

        getId();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("         Customer Detail");
        try {
            db.open();
            inserted_list = db.getYoutubeByDate(currentdate);
            if (inserted_list.size() > 0) {
                for (int i = 0; i < inserted_list.size(); i++) {
                    if (currentdate.equalsIgnoreCase(inserted_list.get(i).getReportingdate())) {

                        System.out.println("hi<<<");
                    }
                    System.out.println("hi");
                    break;
                }

            } else {
                db.deletedRecord();
            }

        } catch (Exception ved) {
            ved.printStackTrace();
        }

    }

    private void getId() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //  getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        textname = (TextView) findViewById(R.id.textname);
        datetime = (TextView) findViewById(R.id.datetime);
        btnview = (Button) findViewById(R.id.btnview);
        choosefile = (TextView) findViewById(R.id.choosefile);
        reportingdate = (TextView) findViewById(R.id.reportingdate);
        icon_date = (TextView) findViewById(R.id.icon_date);
        sppiner = (Spinner) findViewById(R.id.sppiner);
        location = (EditText) findViewById(R.id.location);
        customername = (EditText) findViewById(R.id.customername);
        contectname = (EditText) findViewById(R.id.contectname);
        emailaddress = (EditText) findViewById(R.id.emailaddress);
        mobilebrand = (EditText) findViewById(R.id.mobilebrand);
        mobilemodelno = (EditText) findViewById(R.id.mobilemodelno);
        installno = (EditText) findViewById(R.id.installno);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(c.getTime());
        datePaid = formattedDate;

        System.out.println("Current=> " + datePaid);

        // reportingdate.setText(Utilities.setDateFormat(formattedDate));
        reportingdate.setText(currentdate);

        choosefile.setOnClickListener(this);
        icon_date.setOnClickListener(this);
        fab.setOnClickListener(this);
        btnview.setOnClickListener(this);
        customerDetails = new CustomerDetailsSettrGetter();
        customerDetails.setReportingdate("");
        customerDetails.setSupervisorname("");
        customerDetails.setLocation("");
        customerDetails.setCustomername("");
        customerDetails.setContectnumber("");
        customerDetails.setEmailaddress("");
        customerDetails.setMobilebrand("");
        customerDetails.setMobilemodelno("");
        customerDetails.setInstallid("");
        customerDetails.setPicture("");
        customerDetails.setN("");

        // customerDetails.setSupervisorname(str);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sppiner.setAdapter(aa);

        sppiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str = parent.getItemAtPosition(position).toString();

                customerDetails.setSupervisorname(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        icon_date.setTypeface(font);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choosefile:

                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choosephoto);

                camera_payment_pic = (TextView) dialog.findViewById(R.id.camera_payment_pic);
                gallery_payment_expense_pic = (TextView) dialog.findViewById(R.id.gallery_payment_expense_pic);
                final TextView court_order_child_close_button = (TextView) dialog.findViewById(R.id.payment_cross);
                court_order_child_close_button.setTypeface(font);

                camera_payment_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _pathforcheck = "YoutubeStore"
                                + "Image" + currentdate.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                        _path = CommonString.FILE_PATH + _pathforcheck;
                        startCameraActivity();
                        dialog.dismiss();
                    }
                });

                gallery_payment_expense_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        galleryIntent();
                        dialog.dismiss();

                    }
                });

                court_order_child_close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.icon_date:
                a++;
                b = 0;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                break;

            case R.id.fab:
                if (signUpValidation()) {

                    signup();

                }
                break;
            case R.id.btnview:
                Intent i = new Intent(MainActivity.this, CustomerDetailsView.class);
                startActivity(i);
                break;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        gallery_flag = true;
        image_name = "";
        _pathforcheck = "";
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    protected void startCameraActivity() {
        try {

            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            outputFileUri = Uri.fromFile(file);

            String defaultCameraPackage = "";
            final PackageManager packageManager = getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (int n = 0; n < list.size(); n++) {
                if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    Log.e("TAG", "Installed Applications  : " + list.get(n).loadLabel(packageManager).toString());
                    Log.e("TAG", "package name  : " + list.get(n).packageName);

                    //temp value in case camera is gallery app above jellybean
                    String packag = list.get(n).loadLabel(packageManager).toString();
                    if (packag.equalsIgnoreCase("Gallery") || packag.equalsIgnoreCase("Galeri") || packag.equalsIgnoreCase("Ø§Ù„Ø§Ø³ØªÙˆØ¯ÙŠÙˆ")) {
                        gallery_package = list.get(n).packageName;
                    }

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera") || packag.equalsIgnoreCase("Ø§Ù„ÙƒØ§Ù…ÙŠØ±Ø§")) {
                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    } else {

                        if (packag.equalsIgnoreCase("Camera") || packag.equalsIgnoreCase("Kamera") || packag.equalsIgnoreCase("Ø§Ù„ÙƒØ§Ù…ÙŠØ±Ø§")) {

                            defaultCameraPackage = list.get(n).packageName;
                            break;
                        }
                    }
                }
            }

            //com.android.gallery3d

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(defaultCameraPackage);
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {

            e.printStackTrace();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.setPackage(gallery_package);
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            // Log( e, "Error creating temp file for HTC Desire HD" );
            System.out.println("Error creating temp file for HTC Desire HD"+e);
            e.printStackTrace();
        }
    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());

       /* String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":"
                + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);*/

        return cdate;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str1 + _pathforcheck).exists()) {
                        //img_cam.setBackgroundResource(R.drawable.camera_list_tick);
                        // Decode the filepath with BitmapFactory followed by the position
                        Bitmap bmp = BitmapFactory.decodeFile(str + _pathforcheck);
                        image_name = _pathforcheck;
                        System.out.println("image_name" + image_name);
                        choosefile.setBackgroundColor(Color.BLUE);
                        textname.setText("Camera Capture");
                    }
                }
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == SELECT_FILE || _pathforcheck.equals("")) {
                        if (gallery_flag) {
                            onSelectFromGalleryResult(data);
                        }
                    }
                }

                break;
        }


        //  super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                if (bm != null) {
                    bm.recycle();
                    bm = null;
                    System.gc();
                }
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                finalFile = new File(getRealPathFromURI(tempUri));
                image_name = finalFile.getName();
                //  Toast.makeText(ChildEditProfileActivity.this,"image path"+finalFile,Toast.LENGTH_SHORT).show();
                System.out.println("image path" + finalFile);
                imagepath = tempUri.toString();
                System.out.println("image imagepath" + imagepath);
                //  Toast.makeText(MainActivity.this, "image path" + finalFile, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        choosefile.setBackgroundColor(Color.BLUE);
        textname.setText("Gallery Capture");
        //  image.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), thumbnail);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        finalFile = new File(getRealPathFromURI(tempUri));
        System.out.println("image path<<<<<<<<<<<<" + finalFile);
        imagepath = tempUri.toString();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, mm, dd);
            DatePicker dp = datePickerDialog.getDatePicker();
            dp.setMaxDate(calendar.getTimeInMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            datePaid = year + "/" + month + "/" + day;
            if ((day < 10 && month < 10)) {

                reportingdate.setText("0" + month + "/" + "0" + day + "/" + year);

            } else if (day > 10 && month < 10) {
                reportingdate.setText("0" + month + "/" + day + "/" + year);
            } else if (day < 10 && month > 10) {
                reportingdate.setText(month + "/" + "0" + day + "/" + year);
            } else {
                reportingdate.setText(month + "/" + day + "/" + year);
                System.out.println("<<<<<<<<<" + reportingdate);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.option_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.android) {

            Intent i = new Intent(MainActivity.this, CustomerViewActivity.class);
            startActivity(i);
        }

        if (id == android.R.id.home) {

            try {

                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    long date = System.currentTimeMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yy");
                    String dateString = sdf.format(date);
                    String currentDBPath = "//data//youtube.cpm.com.youtube//databases//" + Database.DATABASE_NAME;
                    String backupDBPath = "Youtube_backup" + dateString.replace('/', '-');
                    String path = Environment.getExternalStorageDirectory().getPath();
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(path, backupDBPath);

                    Snackbar.make(btnview, "Database Exported Successfully", Snackbar.LENGTH_SHORT).show();

                    if (currentDB.exists()) {
                        @SuppressWarnings("resource")
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            finish();

            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean signUpValidation() {

        strlocation = location.getText().toString().trim();
        strcustomername = customername.getText().toString().trim();
        strcontectnumber = contectname.getText().toString().trim();
        stremailaddress = emailaddress.getText().toString().trim();
        strmobilebrand = mobilebrand.getText().toString().trim();
        strmobilemodelno = mobilemodelno.getText().toString().trim();
        strinstallno = installno.getText().toString().trim();
        reportdate = reportingdate.getText().toString().trim();

        if (sppiner.getSelectedItem().toString().trim().equals("Select")) {
            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM",
                    "Please Selected Supervisor name");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");
        }
       else if (strlocation.length() < 1 && strlocation.length() < 1) {
            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM", "Enter  User Location");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");

        } else if (strcustomername.length() < 1 && strcustomername.length() < 1) {
            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM", "Enter  User Customer name");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");

        } else if (strcontectnumber.length() < 10) {
            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM", "Enter valid Mobile Number");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");
            // !ValidationUtil.validateEmail(stremailaddress)
//stremailaddress.length() < 1
        } else if (!Utilities.isValid(stremailaddress)) {

            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM", "Enter valid Email ID");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");
        } else if (strmobilebrand.length() < 1) {
            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM", "Enter User Mobile Brand");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");
        } else if (strmobilemodelno.length() < 1) {
            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM", "Enter User Mobile Model Number");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");
        } else if (strinstallno.length() < 1) {
            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM", "Enter User Install Id");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");

        }

        else if (image_name.isEmpty()) {

            validation = false;
            fragmentDialogFragmentToShowMessages = new FragmentDialogFragmentToShowMessages(MainActivity.this, "PARINAAM",
                    "Please add photo");
            fragmentDialogFragmentToShowMessages.show(getSupportFragmentManager(), "");
        } else {
            validation = true;
        }
        return validation;
    }

    public void signup() {

        //  loading = ProgressDialog.show(MainActivity.this, "Processing", "Please wait...", false, false);
        customerDetails.setCustomername(strcustomername);
        customerDetails.setReportingdate(datePaid);
        customerDetails.setLocation(strlocation);
        customerDetails.setContectnumber(strcontectnumber);
        customerDetails.setEmailaddress(stremailaddress);
        customerDetails.setMobilebrand(strmobilebrand);
        customerDetails.setMobilemodelno(strmobilemodelno);
        customerDetails.setInstallid(strinstallno);

        customerDetails.setPicture(image_name);
        customerDetails.setN("N");

        long id = db.insert_Youtube(customerDetails);
        System.out.println("id" + id);
        if (id > 0) {

            if (Utilities.isConnectingToInternet(MainActivity.this)) {

                new UploadTask().execute();
            } else {
                Snackbar.make(btnview, "Internet Not Connected", Snackbar.LENGTH_SHORT).show();
            }

            location.setText("");
            customername.setText("");
            contectname.setText("");
            emailaddress.setText("");
            mobilebrand.setText("");
            mobilemodelno.setText("");
            installno.setText("");
            choosefile.setBackgroundResource(R.drawable.rouded_bacground);
            textname.setText("No file chosen");




        }
        //   loading.dismiss();
/*
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        loading.dismiss();
                    }
                }, 3000);
*/
    }

    private class UploadTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this,"Processing","Please wait...",false,false);
            /*dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("Uploading Data");
            dialog.setCancelable(false);
            dialog.show();*/
            loading.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
            percentage = (TextView) dialog.findViewById(R.id.percentage);
            message = (TextView) dialog.findViewById(R.id.message);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // loading.dismiss();
            //  dialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                String stock_facing_xml = "";
                String onXML = "";
                //customergetset = db.getYoutube();


                if (customerDetails != null) {
//                    for (int i1 = 0; i1 < customergetset.size(); i1++) {
                    onXML = "[YOUTUBE_FACING_DATA_NEW]"
                            + "[REPORTING_DATE]" + customerDetails.getReportingdate() + "[/REPORTING_DATE]"
                            + "[SUPERVISOR_NAME]" + str + "[/SUPERVISOR_NAME]"
                            + "[USER_ID]" + currentuserid + "[/USER_ID]"
                            + "[LOCATION]" + customerDetails.getLocation() + "[/LOCATION]"
                            + "[CUSTOMER_NAME]" + customerDetails.getCustomername() + "[/CUSTOMER_NAME]"
                            + "[CONTECT_NUMBER]" + customerDetails.getContectnumber() + "[/CONTECT_NUMBER]"
                            + "[EMAIL_ADDRESS]" + customerDetails.getEmailaddress() + "[/EMAIL_ADDRESS]"
                            + "[MOBILE_BRAND]" + customerDetails.getMobilebrand() + "[/MOBILE_BRAND]"
                            + "[MOBILE_MODELNO]" + customerDetails.getMobilemodelno() + "[/MOBILE_MODELNO]"
                            + "[INSTAAL_ID]" + customerDetails.getInstallid() + "[/INSTAAL_ID]"

                            + "[UPLODE_PICTURE]" + customerDetails.getPicture() + "[/UPLODE_PICTURE]"
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
                    System.out.println("result" + result);


                    if (customerDetails.getPicture() != null && !customerDetails.getPicture().equals("")) {
                        if (gallery_flag) {
                            try {
                                result = UploadImageFromGallery(customerDetails.getPicture(), "StoreImages");
                                if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                    return "StoreImages";
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (new File(CommonString.FILE_PATH + customerDetails.getPicture()).exists()) {
                                try {
                                    result = UploadImage(customerDetails.getPicture(), "StoreImages");
                                    if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                                        return "StoreImages";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
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
            // dialog.dismiss();
            if (s.equalsIgnoreCase("success")) {
                db.updateYouTube("Y", customerDetails.getInstallid());
                customerDetails = new CustomerDetailsSettrGetter();
                location.setText("");
                customername.setText("");
                contectname.setText("");
                emailaddress.setText("");
                mobilebrand.setText("");
                mobilemodelno.setText("");
                installno.setText("");
                choosefile.setBackgroundResource(R.drawable.rouded_bacground);
                textname.setText("No file chosen");

            }
            loading.dismiss();
            Toast.makeText(getApplicationContext(), "Customer Details Successfully Upload ", Toast.LENGTH_LONG).show();
            System.out.println("<<<<<<<<<<<<<<<<<<<<<,,.." + s);
        }
    }


    public void popup() {

        dialog1 = new Dialog(MainActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_request);
        final TextView ok = (TextView) dialog1.findViewById(R.id.add);
        final TextView cancle = (TextView) dialog1.findViewById(R.id.requst);


        final LinearLayout addevent = (LinearLayout) dialog1.findViewById(R.id.add_vactions);
        final LinearLayout request_trade = (LinearLayout) dialog1.findViewById(R.id.request_trade);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        wlp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();


            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog1.cancel();
            }
        });

    }

    private class UploadPreviusDate extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle("Uploading Data");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // loading.dismiss();
            dialog.dismiss();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                String stock_facing_xml = "";
                String onXML = "";
                //customergetset = db.getYoutube();


                if (customerDetails != null) {
//                    for (int i1 = 0; i1 < customergetset.size(); i1++) {
                    onXML = "[YOUTUBE_FACING_DATA_NEW]"
                            + "[REPORTING_DATE]" + customerDetails.getReportingdate() + "[/REPORTING_DATE]"
                            + "[SUPERVISOR_NAME]" + str + "[/SUPERVISOR_NAME]"
                            + "[USER_ID]" + currentuserid + "[/USER_ID]"
                            + "[LOCATION]" + customerDetails.getLocation() + "[/LOCATION]"
                            + "[CUSTOMER_NAME]" + customerDetails.getCustomername() + "[/CUSTOMER_NAME]"
                            + "[CONTECT_NUMBER]" + customerDetails.getContectnumber() + "[/CONTECT_NUMBER]"
                            + "[EMAIL_ADDRESS]" + customerDetails.getEmailaddress() + "[/EMAIL_ADDRESS]"
                            + "[MOBILE_BRAND]" + customerDetails.getMobilebrand() + "[/MOBILE_BRAND]"
                            + "[MOBILE_MODELNO]" + customerDetails.getMobilemodelno() + "[/MOBILE_MODELNO]"
                            + "[INSTAAL_ID]" + customerDetails.getInstallid() + "[/INSTAAL_ID]"

                            + "[UPLODE_PICTURE]" + customerDetails.getPicture() + "[/UPLODE_PICTURE]"
                            + "[/YOUTUBE_FACING_DATA_NEW]";

                    stock_facing_xml = stock_facing_xml + onXML;
                    //}

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

                    System.out.println("result" + result);
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
            Toast.makeText(getApplicationContext(), "Customer Details Successfully Upload ", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            if (s.equalsIgnoreCase("success")) {
                db.updateYouTube("Y", customerDetails.getInstallid());
                //customerDetails = new CustomerDetailsSettrGetter();
                // db.open();
                //  db.deletedRecord();
            }

            System.out.println("<<<<<<<<<<<<<<<<<<<<<,,.." + s);
        }
    }

    public String UploadImage(String path, String folder_name) throws Exception {
        errormsg = "";
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str1 + path, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1639;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(str1 + path, o2);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);

        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_IMAGE);

        String[] split = path.split("/");
        String path1 = split[split.length - 1];

        request.addProperty("img", ba1);
        request.addProperty("name", path1);
        request.addProperty("FolderName", folder_name);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
        androidHttpTransport.call(CommonString.SOAP_ACTION_UPLOAD_IMAGE, envelope);

        result = envelope.getResponse();

        if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
            if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                return CommonString.KEY_FALSE;
            }

            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();

            // for failure
            FailureXMLHandler failureXMLHandler = new FailureXMLHandler();
            xmlR.setContentHandler(failureXMLHandler);

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result.toString()));
            xmlR.parse(is);

            failureGetterSetter = failureXMLHandler.getFailureGetterSetter();

            if (failureGetterSetter.getStatus().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                errormsg = failureGetterSetter.getErrorMsg();
                return CommonString.KEY_FAILURE;
            }
        } else {
            new File(str1 + path).delete();
        }

        return result.toString();
    }



    public String UploadImageFromGallery(String path, String folder_name) throws Exception {
        errormsg = "";
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        String pat= finalFile.getParent();//it contain your path of image..im using a temp string..
        // String filename=path.substring(pat.lastIndexOf("/")+1);
        pat=pat+"/";

        BitmapFactory.decodeFile( pat+ path, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1639;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(pat + path, o2);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);

        SoapObject request = new SoapObject(CommonString.NAMESPACE, CommonString.METHOD_UPLOAD_IMAGE);

        String[] split = path.split("/");
        String path1 = split[split.length - 1];

        request.addProperty("img", ba1);
        request.addProperty("name", path1);
        request.addProperty("FolderName", folder_name);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(CommonString.URL);
        androidHttpTransport.call(CommonString.SOAP_ACTION_UPLOAD_IMAGE, envelope);

        result = envelope.getResponse();

        if (!result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
            if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
                return CommonString.KEY_FALSE;
            }

            SAXParserFactory saxPF = SAXParserFactory.newInstance();
            SAXParser saxP = saxPF.newSAXParser();
            XMLReader xmlR = saxP.getXMLReader();

            // for failure
            FailureXMLHandler failureXMLHandler = new FailureXMLHandler();
            xmlR.setContentHandler(failureXMLHandler);

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(result.toString()));
            xmlR.parse(is);

            failureGetterSetter = failureXMLHandler.getFailureGetterSetter();

            if (failureGetterSetter.getStatus().equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                errormsg = failureGetterSetter.getErrorMsg();
                return CommonString.KEY_FAILURE;
            }
        } else {
            // new File(str1 + path).delete();
        }

        return result.toString();
    }


    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
