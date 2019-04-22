/**
 * 
 * @author Faruk Hossain
 * FIXME
 *
 */
package com.rfit.card.holder.digitalholder.appfrw;

import android.app.Application;
import android.content.SharedPreferences;

import com.rfit.card.holder.digitalholder.db.DatabaseController;
import com.rfit.card.holder.digitalholder.model.ContactDbModel;

// Put this in the manifest file
// android:name="com.microasset.saiful.appfrw.BaseApplication">
// Here for example:  com.bjit.alquran.mvc is the name of application package.
public class BaseApplication extends Application {

	 public void onCreate () {
		 super.onCreate();
		 this.init();
	}
	
	public void onTerminate ()  {
	    super.onTerminate() ;
	}
	
	/**
	 * We need a folder, localization, or other data that should be available in whole application.
	 */
	public void init() {

		DatabaseController databaseConterller = DatabaseController.getInstance();
		databaseConterller.createDatabase(this, DatabaseController.DATABASE_NAME, getDataBaseCurrentVersion());

		ContactDbModel.getInstance(this).query();
	}

	public int getDataBaseCurrentVersion() {
		SharedPreferences preferences = getSharedPreferences("DB_SHARED_PREF", 0);
		return preferences.getInt("DB_VERSION", 1);
	}

}
