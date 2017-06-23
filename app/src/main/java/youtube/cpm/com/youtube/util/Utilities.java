package youtube.cpm.com.youtube.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by upendra on 20/4/17.
 */

public final class Utilities {


	public EditText editText;
	public static String a;


	public static int keyDel;

	/**
	 * Checks if is connecting to Internet.
	 *
	 * @param context the context
	 * @return true, if is connecting to Internet
	 */
	public static int j1=0,j2=0;
	public static String fullImage;
	public static String custodyColor,custodyColor2;

	public static String pos;
	public static String chID, actual_chId, parent_Id;
	public static List<Integer> custodyDate = new ArrayList<Integer>();
	public static List<String> events = new ArrayList<String>();
	public static List<String> trade = new ArrayList<String>();
	public static List<String> vacation = new ArrayList<String>();
	public static List<String> childImgEvent = new ArrayList<String>();
	public static List<String> childNameEvent = new ArrayList<String>();
	public static Map<String, String> tradeMap = new HashMap<String, String>();
	public static Map<String, String> vacationMap = new HashMap<String, String>();
	public static Map<String, String> holidayMap = new HashMap<String, String>();

	public static String getChID() {
		return chID;
	}

	public static void setChID(String chID) {
		Utilities.chID = chID;
	}

	public static String getActual_chId() {
		return actual_chId;
	}

	public static void setActual_chId(String actual_chId) {
		Utilities.actual_chId = actual_chId;
	}

	public static String getParent_Id() {
		return parent_Id;
	}

	public static void setParent_Id(String parent_Id) {
		Utilities.parent_Id = parent_Id;
	}

	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (NetworkInfo anInfo : info) {
					if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//This Method is used to compress the bitmap Format for Adapter
	public static Bitmap compressBitmap(String imageuri) {
		String filePath = imageuri;
		Bitmap bmp = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		bmp = BitmapFactory.decodeFile(filePath, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;
		String imageType = options.outMimeType;
		//      max Height and width values of the compressed image is taken as 816x612

		float maxHeight = 816.0f;
		float maxWidth = 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		//setting inSampleSize value allows to load a scaled down version of the original image

		options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inDither = true;
		options.inMutable = true;
		options.inTempStorage = new byte[32 * 1024];

		try {
//          load the bitmap from its path
			bmp = BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			//scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_4444);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}
		try {
			ExifInterface ei = new ExifInterface(imageuri);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					bmp = rotateImage(bmp, 90);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					bmp = rotateImage(bmp, 180);
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;

	}

	public static Bitmap rotateImage(Bitmap source, float angle) {
		Bitmap retVal;

		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

		return retVal;
	}

	//This Method is used to calculate the size of Image
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static String rotateImage(String imageUri) {
		Bitmap bmp = null;
		bmp = BitmapFactory.decodeFile(imageUri);
		try {
			ExifInterface ei = new ExifInterface(imageUri);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					bmp = rotateImage(bmp, 90);
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					bmp = rotateImage(bmp, 180);
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream out = null;
		String filename = getFilename();
		try {
			out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
			bmp.compress(Bitmap.CompressFormat.JPEG, 82, out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return filename;


	}
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------

	//This Method is used to store the image after compress
	public static String getFilename() {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder");
		if (!file.exists()) {
			file.mkdirs();
		}
		String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
		return uriSting;

	}


	// validating email id
	public static boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	public static boolean isValid(String email)
	{
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		//String expression = "^[\\w\\.-]+@(\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())
		{
			return true;
		}
		else{
			return false;
		}
	}

	public static boolean isValidMobile(String phone) {
		return android.util.Patterns.PHONE.matcher(phone).matches();
	}

	public static boolean isValidMail(String email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public static String h;

	public static Boolean exists(final String URLName) {
		new Thread() {

			public void run() {
				//your "file checking code" goes here like this
				//write your results to log cat, since you cant do Toast from threads without handlers also...

				try {
					HttpURLConnection.setFollowRedirects(false);
					// note : you may also need
					//HttpURLConnection.setInstanceFollowRedirects(false)

					HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
					con.setRequestMethod("HEAD");
					if ((con.getResponseCode() == HttpURLConnection.HTTP_OK))
						h = "exist";
					else
						h = "not exist";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		/*if(h.equalsIgnoreCase("exist"))
		return true;
		else*/
			return false;
	}

	public static List<Date> getDates(String dateString1, String dateString2)
	{
		ArrayList<Date> dates = new ArrayList<Date>();
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

		Date date1 = null;
		Date date2 = null;

		try {
			date1 = df1 .parse(dateString1);
			date2 = df1 .parse(dateString2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);


		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		while(!cal1.after(cal2))
		{
			dates.add(cal1.getTime());
			cal1.add(Calendar.DATE, 1);
		}
		return dates;
	}

public static String setDateFormat(String start_dt){
	System.out.println("xxxd"+start_dt);
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	try {
		Date date = (Date)formatter.parse(start_dt);
		SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
		String finalString = newFormat.format(date);
		return finalString;
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return null;
}
	public static String setTimeFormat(String start_dt) {
		System.out.println("xxxd" + start_dt);
		String newvalue="";
		if(!start_dt.isEmpty())
		{
			String s[]=start_dt.split(":");
			System.out.println("time-----------------"+s[0]+"parseint"+ Integer.parseInt(s[0]));
			int t= Integer.parseInt(s[0]);
			System.out.println("vv....."+t);
			if(t<12)
			{
				s[0]= Integer.valueOf(s[0]).toString();
				newvalue=s[0]+":"+s[1]+" AM";
				System.out.println("value..."+newvalue);
			}
			else
			{
				int time= Integer.parseInt(s[0])-12;
				newvalue=time+":"+s[1]+" PM";
				System.out.println("value..."+newvalue);
			}

		}

		return newvalue;
	}


	public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);
			System.out.println("change_date<<<<<<<<<<<<<"+outputDate);

		} catch (ParseException e) {
			System.out.println("ParseException - dateFormat");
		}

		return outputDate;

	}


	public static void usFormatNo  (final EditText text)
	{


	//	editText=text;
		text.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {

				boolean flag = true;
				String eachBlock[] = text.getText().toString().split("-");
				for (int i = 0; i < eachBlock.length; i++)
				{
					if (eachBlock[i].length() > 3)
					{
						Log.v("11111111111111111111","cc"+flag + eachBlock[i].length());
					}
				}
				if (flag) {
					text.setOnKeyListener(new View.OnKeyListener() {

						public boolean onKey(View v, int keyCode, KeyEvent event) {

							if (keyCode == KeyEvent.KEYCODE_DEL)
								keyDel = 1;
							return false;
						}
					});

					if (keyDel == 0) {

						if (((text.getText().length() + 1) % 4) == 0)
						{
							if (text.getText().toString().split("-").length <= 2)
							{
								text.setText(text.getText() + "-");
								text.setSelection(text.getText().length());
							}
						}
						a = text.getText().toString();
					}
					else
					{
						a = text.getText().toString();
						keyDel = 0;
					}

				} else {
					text.setText(a);
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{


			}

			public void afterTextChanged(Editable s) {


			}


		});

	}
	public static String formatPhoneNumber(String number){
		//number = "(" + number.substring(0,3) + ") " + number.substring(3,6)+" "+number.substring(6);
		number  =   number.substring(0, number.length()-4) + "-" + number.substring(number.length()-4, number.length());

		number  =   number.substring(0,number.length()-8)+")"+" "+number.substring(number.length()-8,number.length());

		number  =   number.substring(0, number.length()-13)+"("+number.substring(number.length()-13, number.length());

		return number;
	}


}