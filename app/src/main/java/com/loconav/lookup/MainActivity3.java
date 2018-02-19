package com.loconav.lookup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;

import static com.loconav.lookup.Constants.DEVICE_ID;

public class MainActivity3 extends AppCompatActivity {
	ListView l1;
	Long time1;
	String hola="";
	String message="";
	Lookwrap d;
	cbc c=new cbc();
	CustomActionBar customActionBar;
	static ArrayList<Lookwrap> str = new ArrayList<Lookwrap>();
	Customlook adapter ;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity3);
		customActionBar = new CustomActionBar();
		customActionBar.getActionBar(this, R.drawable.leftarrow, R.string.details, true);
		dialog = new ProgressDialog(MainActivity3.this);
		dialog.setMessage("Please Wait");
		dialog.setCancelable(false);
		Button button = (Button)findViewById(R.id.refresh);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				c = new cbc();
				c.execute();
			}
		});

		Button share = (Button) findViewById(R.id.share);
		l1=(ListView)findViewById(R.id.listView1);
		adapter= new Customlook(this, str, share);
		l1.setAdapter(adapter);
		Bundle bundle = getIntent().getExtras();
		message = bundle.getString(DEVICE_ID);
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		final ConnectivityManager conMgr1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr1.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected())
			c.execute();
		else
			setContentView(R.layout.nointernet);
	}
	class cbc extends AsyncTask<Void,Void,String> {
		public String json_url;

		@Override
		protected void onPreExecute() {
			time1= System.currentTimeMillis();
			str.clear();
			json_url="http://babatrucks.com/api/v1/trucks/check_status";
			dialog.show();
		}
		@Override
		protected String doInBackground(Void[] voids) {
			try {
				URL obj = new URL("http://babatrucks.com/api/v1/trucks/check_status?truck[device_id]="+message);
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				// optional default is GET
				con.setRequestMethod("GET");
				//add request header
				con.setRequestProperty("X-Auth-Token", "b90190cf1616edd3270667c216696156");
				int responseCode = con.getResponseCode();
				System.out.println("\nSending 'GET' request to URL : " + "hola");
				System.out.println("Response Code : " + responseCode);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(con.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							//				if(isCancelled()) {
							//							break;
							//						}
							response.append(inputLine);
						}
				in.close();
				//print result
				System.out.println(response.toString());
				Log.e("Response",response.toString());
				return 	response.toString();
			} catch(Exception e) {
				System.out.print(""+ e.getMessage());
				}
		  return "raju";
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(String avoid) {
			dialog.cancel();
			if(!(avoid.equals("null")|| avoid == null || avoid.equals(""))) {
				hola=avoid;
				try {
					str.clear();
					d=new Lookwrap();

					JSONObject j=new JSONObject(hola);
					String k=j.getString("long");
					d.longi=k;

					k=j.getString("lat");
					d.lati=k;

					k=j.getString("created_at");
					d.locatedAt = k;
					Log.e("Value", k+"");

					k=j.getString("io_state");
					d.ioState = k;

					k=j.getString("distance");
					d.distance = k;

					k=j.getString("orientation");
					d.orientation = k;

					k = j.getString("device_id");
					d.device_id = k;
					str.add(d);

					adapter.notifyDataSetChanged();
		//				Thread.sleep(500000);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		//			} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				finish();
				Toast.makeText(getApplicationContext(), "Something Went Wrong !!", Toast.LENGTH_LONG).show();
			}

			dialog.dismiss();
		}

	}
}
