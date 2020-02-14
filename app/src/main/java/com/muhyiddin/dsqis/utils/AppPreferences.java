package com.muhyiddin.dsqis.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String PREFS_NAME = "app_pref";

    private static final String UID = "uid";
    private static final String ROLE = "role";
    private static final String NAMA = "nama";

    private final SharedPreferences prefs;

    public AppPreferences(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void resetPreference(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void setUid(String uid){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(UID, uid);
        editor.apply();
    }

    public String getUid(){
        return prefs.getString(UID, null);
    }

    public void setNama(String nama){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NAMA, nama);
        editor.apply();
    }

    public String getNama(){
        return prefs.getString(NAMA, null);
    }

    public void setRole(int role){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(ROLE, role);
        editor.apply();
    }

    public int getRole(){
        return prefs.getInt(ROLE, 0);
    }


}