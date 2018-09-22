package de.philippdormann.gymnasiumherzogenaurach;

import android.content.SharedPreferences;
import android.util.Log;

public class ThemeHelper {

    public int setTheme(int themeRes) {
        Log.d("LOGGER", String.valueOf(themeRes));
        switch (themeRes) {
            case R.id.theme_red:
                return R.style.Red;
            case R.id.theme_pink:
                return R.style.Pink;
        }
        return R.style.AppTheme;
    }

    public int setThemeFromStorage(SharedPreferences sharedPreferences) {
        int themeRes = sharedPreferences.getInt("THEME", R.id.theme_blue);
        switch (themeRes) {
            case R.id.theme_red:
                return R.style.Red;
            case R.id.theme_pink:
                return R.style.Pink;
        }
        return R.style.AppTheme;
    }
}