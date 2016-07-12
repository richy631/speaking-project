package com.yangyu.myslidingmenudemo02;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBManager {
	
	private final int BUFFER_SIZE = 4096;
	public static final String DB_NAME = "worddb.sqlite";
	public static final String PACKAGE_NAME = "com.yangyu.myslidingmenudemo02";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME;
	private SQLiteDatabase db;
	private Context ctx;
	
	
	public DBManager(Context ctx) {
		this.ctx = ctx;
	}
		 
	public SQLiteDatabase getDatabase() {
		return this.db;
	} 

	public void setDatabase(SQLiteDatabase db) {
		this.db = db;
	}
		 
	public void openDatabase() {
		System.out.println(DB_PATH + "/" + DB_NAME);
		this.db = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}
	
	private SQLiteDatabase openDatabase(String dbfile) { 
		try {
			//if (!(new File(dbfile).exists())) {
				InputStream is = this.ctx.getResources().openRawResource(R.raw.systemdb);
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			//}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
			Log.v("Database", "Load database success");
			return db;
		} catch (FileNotFoundException e) {
			Log.v("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.v("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}
	
	public void closeDatabase() {
        this.db.close();
    }
}
