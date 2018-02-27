

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Set;

/**
 * Created by Anup on 11-Feb-18.
 * This is shared Preferences Manager class.
 * The purpose of this class is to access shared preferences through one gateway to avoid any duplicate key-value pair.
 * It is strongly advised to access shared preference through this class.
 *
 * Happy Coding :) -@D
 */

public class SharedPreferencesManager {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String preference_file_key = "customer_app_sp";
    public static final String Preference_LOGIN = "pref";
    private final int preference_file_mode = Context.MODE_PRIVATE;



    /*public SharedPreferencesManager(@NonNull Context context) {

        mSharedPreferences = context.getSharedPreferences(preference_file_key, preference_file_mode);
    }*/

    public SharedPreferencesManager(@NonNull Context context, @NonNull String preferenceName ){

        mSharedPreferences = context.getSharedPreferences(preferenceName,preference_file_mode);
    }

    public void setString(String key, String value){
        editor = mSharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key){
        return mSharedPreferences.getString(key, "");
    }

    public void setInt(String key, int value){
        editor = mSharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public int getInt(String key){
        return mSharedPreferences.getInt(key, 0);
    }

    public void setLong(String key, long value){
        editor = mSharedPreferences.edit();
        editor.putLong(key,value);
        editor.apply();
    }

    public long getLong(String key){
        return mSharedPreferences.getLong(key, 0);
    }

    public void setFloat(String key, float value){
        editor = mSharedPreferences.edit();
        editor.putFloat(key,value);
        editor.apply();
    }

    public float getFloat(String key){
        return mSharedPreferences.getFloat(key, 0);
    }

    public void setBoolean(String key, boolean value){
        editor = mSharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getBoolean(String key){
        return mSharedPreferences.getBoolean(key, false);
    }


    public void remove(String key){
        editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void removeArray(String[] keyList){
        editor = mSharedPreferences.edit();
        for (String s : keyList){
            editor.remove(s);
        }
        editor.apply();
    }

    public SharedPreferences getInstance(){
        return mSharedPreferences;
    }

    public SharedPreferences.Editor getEditor(){
        return mSharedPreferences.edit();
    }

    /*
    // String set
    private void setStringSet(String key, Set<String> value){
        editor = mSharedPreferences.edit();
        editor.putStringSet(key,value);
        editor.apply();
    }

    private Set<String> getStringSet(String key){
        return mSharedPreferences.getStringSet(key, );
    }
    */




}
