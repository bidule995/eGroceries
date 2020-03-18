package com.iut.mygrocerylist;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Theme {

    public static int getTheme(Activity activity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        String theme = sp.getString("theme", "-1");
        switch (theme) {
            default:
            case "default":
                return R.style.AppTheme;
            case "envy_green":
                return R.style.EnvyGreen;
            case "sun_dew_gold":
                return R.style.SunDewGold;
            case "panther_pink":
                return R.style.PantherPink;
            case "blizzard_blue":
                return R.style.BlizzardBlue;
            case "furious_red":
                return R.style.FuriousRed;
            case "emo_dark":
                return R.style.EmoDark;
        }
    }
}
