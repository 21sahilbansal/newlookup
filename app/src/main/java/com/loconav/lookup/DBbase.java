package com.loconav.lookup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DBbase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME="mydb.db";
	private static final String DATABASE_TABLE="Item";
	private static final String DATABASE_TABLE1="truckinfo";

	private static final String KEY_ID="id";
	private static final String NUMBER="Number";
	private static final String DEVICEID="Deviceid";
	private static final String STATUS="Status";
	private static final String MACHINE="Machine";
	private static final String RCOUNT="Rcount";
	private static final String TIMESTAMP="Timestamp";
	private static final int DATABASE_VERSION=1;

	private static final String ID="uuid";
	private static final String TRUCKNUMBER1="Trucknumber";
	private static final String MOBILENUMBER1="Mobilenumber";
	private static final String DEVICETYPE="Devicetype";
	private static final String MANUFACTURER="Manufacturer";
	private static final String VEHICLEMODEL="Vehiclemodel";
	private static final String DATE="Installationdate";
	private static final String DRIVERNAME="Drivername";
	private static final String DRIVERNO="Driverno";
	private static final String TRANSPORTERNAME="Tname";
	private static final String TRANSPORTERNO="Tno";
	private static final String OWNERNAME="Ownername";
	private static final String OWNERNO="Ownerno";
	private static final String INSTALLERNAME = "Installername";
	private static final String LOCATION="Location";
	private static final String EXTRANOTES="ExtraEXTRANOTES";


	private SQLiteDatabase db;
	public final static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	public final static Lock r = rwl.readLock();
	public final static Lock w = rwl.writeLock();

	public DBbase(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE
				+ "("
				+ KEY_ID
				+ " INTEGER PRIMARY KEY,"
				+ NUMBER
				+ " TEXT,"
				+ STATUS
				+ " INT,"
				+ MACHINE
				+ " INTEGER,"
				+ RCOUNT
				+ " INTEGER,"
				+ TIMESTAMP
				+ " LONG,"
				+ DEVICEID
				+ " TEXT"
				+ ")";



		String CREATE_TRUCK_TABLE = "CREATE TABLE " + DATABASE_TABLE1
				+ "("
				+ ID
				+ " TEXT NOT NULL DEFAULT '',"
				+ TRUCKNUMBER1
				+ " TEXT NOT NULL DEFAULT '',"
				+ MOBILENUMBER1
				+ " TEXT NOT NULL DEFAULT '',"
				+ DEVICEID
				+ " TEXT NOT NULL DEFAULT '',"
				+ DEVICETYPE
				+ " TEXT NOT NULL DEFAULT '',"
				+ MANUFACTURER
				+ " TEXT NOT NULL DEFAULT '',"
				+ VEHICLEMODEL
				+ " TEXT NOT NULL DEFAULT '',"
				+ DATE
				+ " LONG,"
				+ DRIVERNAME
				+ " TEXT NOT NULL DEFAULT '',"
				+ DRIVERNO
				+ " TEXT NOT NULL DEFAULT '',"
				+ TRANSPORTERNAME
				+ " TEXT NOT NULL DEFAULT '',"
				+ TRANSPORTERNO
				+ " TEXT NOT NULL DEFAULT '',"
				+ OWNERNAME
				+ " TEXT NOT NULL DEFAULT '',"
				+ OWNERNO
				+ " TEXT NOT NULL DEFAULT '',"
				+ INSTALLERNAME
				+ " TEXT NOT NULL DEFAULT '',"
				+ LOCATION
				+ " TEXT NOT NULL DEFAULT '',"
				+ EXTRANOTES
				+ " TEXT NOT NULL DEFAULT '',"
				+ "syncedat"
				+ " LONG,"
				+ "updatedat"
				+ " LONG,"
				+ "isverified"
				+ " TEXT NOT NULL DEFAULT ''"
				+ ")";


		db.execSQL(CREATE_CONTACTS_TABLE);
		db.execSQL(CREATE_TRUCK_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void open() {
		db=this.getWritableDatabase();
	}

	public void close() {
		db.close();
	}

	public void add(String number, int status, int machine, String deviceId) {
        if(db != null) {
            ContentValues cv = new ContentValues();
            cv.put(NUMBER, number);
            cv.put(STATUS, status);
            cv.put(RCOUNT, 0);
            cv.put(MACHINE, machine);
            cv.put(TIMESTAMP, System.currentTimeMillis());
            cv.put(DEVICEID, deviceId);
            db.insert(DATABASE_TABLE, null, cv);
        }else {
            Log.e("DB","NULL");
        }
	}

	public void deleteItem(String number){
        db.delete(DATABASE_TABLE, NUMBER+"= ?", new String[]{ number });
	}

	void updateRetryCount(String id, int count) {
		ContentValues c = new ContentValues();
		c.put(RCOUNT, count+1);
		c.put(TIMESTAMP, System.currentTimeMillis());
		String n[]={ id };
		db.update(DATABASE_TABLE, c, KEY_ID + " = ? ", n);
	}

	void updateRCount_Timestamp(String number, int rCount) {
		ContentValues c=new ContentValues();
		c.put(RCOUNT, rCount+1);
		c.put(TIMESTAMP, System.currentTimeMillis());
		String n[]={ number };
		db.update(DATABASE_TABLE, c, NUMBER + " = ? ", n);

	}

	public Cursor get() {
		Cursor c;
		c=db.rawQuery("SELECT * FROM Item ", null);
		return c;
	}

	public Cursor getRecordByNumber(String number) {
		Cursor c;
		String[] n = {number};
		c=db.rawQuery("SELECT * FROM Item WHERE Number = ? ", n);
		return c;
	}

	public void updateStatus(String number, int status) {
		ContentValues c = new ContentValues();
		c.put(RCOUNT, 0);
		Log.d("status", ""+(status+1));
		c.put(STATUS, (status+1));
		c.put(TIMESTAMP, System.currentTimeMillis());
		String[] n = {number};
		db.update(DATABASE_TABLE, c, NUMBER + " = ? ", n);

	}

	public void updateDeviceID(String number, String deviceid) {
		ContentValues c = new ContentValues();
		c.put(DEVICEID, deviceid);
		String[] n = {number};
		db.update(DATABASE_TABLE, c, NUMBER + " = ? ", n);

	}
	public Cursor getdevice(String number)	{
		Cursor c=null;
		String Columns[];
		Columns = new String[7];
		Columns[0]=KEY_ID;
		Columns[1]=NUMBER;
		Columns[2]=STATUS;
		Columns[3]=MACHINE;
		Columns[4]=RCOUNT;
		Columns[5]=TIMESTAMP;
		Columns[6]=DEVICEID;
		try {
			String[] n= {number};
			c=db.rawQuery("SELECT * FROM Item WHERE Number = ? ", n);
		} catch(SQLiteException e) {
			e.printStackTrace();
		}
		return c;
	}

	void createOrUpdateItem(String number, int status, int machine, String deviceId) {
		Cursor c1=null;
		try {
			w.lock();
			String[] n = {number};
			c1=db.rawQuery("SELECT * FROM item WHERE Number = ?", n);
			if(c1!=null && c1.moveToNext()) {
				ContentValues c = new ContentValues();
				c.put(STATUS, status);
				c.put(MACHINE, machine);
				c.put(DEVICEID, deviceId);
				c.put(TIMESTAMP, System.currentTimeMillis());
				c.put(RCOUNT, 0);
				db.update(DATABASE_TABLE, c, NUMBER + " = ? ", n);
			} else {
				add(number,status,machine,deviceId);
			}
		} finally {
			c1.close();
			w.unlock();
		}
	}


	Cursor getMaxTime() {
		Cursor c=null;
		try {
			r.lock();
			c=db.rawQuery("SELECT * FROM Item ORDER BY Timestamp DESC", null);
		} catch(SQLiteException e) {
			e.printStackTrace();
		}
		finally {
			r.unlock();
		}
		return c;
	}


	public long truckadd(String tNum1, String mNum1, String dId1, String manufac1,
                         String vModel1, String driverName1, String driverNo1,
                         String transName1, String transNo1, String ownerName1,
                         String ownerNo1, String installerName1, String location1,
                         String notes1, String deviceType) {
		try{
			w.lock();
			ContentValues cv = new ContentValues();
			cv.put(TRUCKNUMBER1, tNum1);
			cv.put(MOBILENUMBER1, mNum1);
			cv.put(DEVICEID, dId1);
			cv.put(DEVICETYPE, deviceType);
			cv.put(DATE, System.currentTimeMillis());
			cv.put(MANUFACTURER, manufac1);
			cv.put(VEHICLEMODEL, vModel1);
			cv.put(DRIVERNAME, driverName1);
			cv.put(DRIVERNO, driverNo1);
			cv.put(TRANSPORTERNAME, transName1);
			cv.put(TRANSPORTERNO, transNo1);
			cv.put(OWNERNAME, ownerName1);
			cv.put(OWNERNO, ownerNo1);
			cv.put(INSTALLERNAME, installerName1);
			cv.put(LOCATION, location1);
			cv.put(EXTRANOTES, notes1);
			cv.put(ID, UUID.randomUUID().toString());
			cv.put("syncedAt", "111");
			cv.put("updatedat", System.currentTimeMillis());
			cv.put("isverified", "false");
			return db.insert(DATABASE_TABLE1, null, cv);
		}finally {
			w.unlock();
		}
	}


	void createOrUpdateTruck(String tNum1, String mNum1, String dId1, String manufac1,
                             String vModel1, String driverName1, String driverNo1,
                             String transName1, String transNo1, String ownerName1,
                             String ownerNo1, String installerName1, String location1,
                             String notes1, String deviceType, String uuid) {
		Cursor c1=null;
		try {
			w.lock();
			String[] n = {uuid};
			c1=db.rawQuery("SELECT * FROM truckinfo WHERE uuid = ?", n);
			if(c1!=null && c1.moveToNext()) {
				ContentValues cv=new ContentValues();
				cv.put(TRUCKNUMBER1, tNum1);
				cv.put(MOBILENUMBER1, mNum1);
				cv.put(DEVICEID, dId1);
				cv.put(DEVICETYPE, deviceType);
				cv.put(MANUFACTURER, manufac1);
				cv.put(VEHICLEMODEL, vModel1);
				cv.put(EXTRANOTES, notes1);
				cv.put(LOCATION, location1);
				cv.put(INSTALLERNAME, installerName1);
				cv.put(OWNERNO, ownerNo1);
				cv.put(OWNERNAME, ownerName1);
				cv.put(TRANSPORTERNAME, transName1);
				cv.put(TRANSPORTERNO, transNo1);
				cv.put(DRIVERNAME, driverName1);
				cv.put(DRIVERNO, driverNo1);
				cv.put("syncedAt", "111");
				cv.put("updatedat", System.currentTimeMillis());
				cv.put("isverified", "false");
				db.update(DATABASE_TABLE1, cv, ID + " = ? ", n);

			} else {
				truckadd( tNum1, mNum1, dId1, manufac1,
						vModel1, driverName1, driverNo1,
						transName1, transNo1, ownerName1,
						ownerNo1, installerName1, location1,
						notes1, deviceType);
			}
		} finally {
			c1.close();
			w.unlock();
		}
	}
	public Cursor getTruck() {
		Cursor c;
		try{
			r.lock();
			c = db.rawQuery("SELECT * FROM truckinfo", null);
		}finally {
			r.unlock();
		}
		return c;
	}
	public Cursor getTruck1() {
		try{
			r.lock();
			Cursor c = null;
			c = db.rawQuery("SELECT * FROM truckinfo", null);
			return c;
		}finally {
			r.unlock();
		}
	}

	public Cursor getTruckByUuid(String uuid) {
		Cursor c;
		try{
			r.lock();
			String[] n = {uuid};
			c = db.rawQuery("SELECT * FROM truckinfo WHERE uuid = ? ", n);
		}finally {
			r.unlock();
		}
		return c;
	}

	public void updatesyncat(String tnumber , String syncedat) {
		try {
			w.lock();
			String n[] = {tnumber};
			ContentValues cv = new ContentValues();
			cv.put("syncedat", syncedat);
			db.update(DATABASE_TABLE1, cv, TRUCKNUMBER1 + " = ? ", n);
		}finally {
			w.unlock();
		}
	}


	public void updateIsVerified ( String truckno) {

		String n[] = {truckno};
		ContentValues cv=new ContentValues();
		cv.put("isverified", "true");

		Log.d("isverified", "set true for" + truckno);
		db.update(DATABASE_TABLE1, cv, TRUCKNUMBER1 + " = ? ", n);
	}

	public Boolean updateOk (String number) {

		Cursor c;
		String[] n = {"true",number};

		c = db.rawQuery("SELECT * FROM truckinfo WHERE isverified = ? AND Deviceid = ? ", n);
		Log.d("verified", "for "+ number);
		if(c!=null && c.moveToNext()) {
			return true;
		} return false;
	}


	public Cursor getUnverifiedDevices() {
		Cursor c;
		String k[] = {"false"};
		c = db.rawQuery("SELECT * FROM truckinfo WHERE isverified = ? ", k);
		c.moveToFirst();
		return c;
	}

	public Cursor getverifiedDevices() {
		Cursor c;
		String k[] = {"true"};
		c = db.rawQuery("SELECT * FROM truckinfo WHERE isverified = ? AND syncedat > updatedat", k);
		c.moveToFirst();
		return c;

	}


	public Cursor jsonPrepare() {
		Cursor c;
		String k[] = {"true"};
		c = db.rawQuery("SELECT * FROM truckinfo WHERE isverified = ? AND syncedat < updatedat ", k);
		c.moveToFirst();
		return c;
	}

}