package youtube.cpm.com.youtube.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import youtube.cpm.com.youtube.MainActivity;


public class FragmentDialogFragmentToShowMessages extends DialogFragment {
	private String title;
	private String message;
	private Context context;


	public FragmentDialogFragmentToShowMessages(Activity login,
												String title, String message) {
		this.context = login;
		this.title = title;
		this.message = message;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		if (title.equals("")) {
			alert.setMessage(message);
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
		}else if(title.equals("Reset")) {
			alert.setMessage(message);
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(context,MainActivity.class);
							startActivity(intent);



						}
					});

		}
		else {
			alert.setTitle(title);
			alert.setMessage(message);
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
		}

		Dialog d = alert.create();
		return d;
	}

}
