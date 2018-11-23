package de.philippdormann.gymnasiumherzogenaurach;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("StaticFieldLeak")
    private static WebView webView;
    Context context;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("GYMH", MODE_PRIVATE);
        setTheme(sharedPref.getInt("THEME", R.style.Standard));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        context = this;

        int startseite = sharedPref.getInt("STARTSEITE", 0);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(startseite).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (startseite) {
            case 0:
                fragmentTransaction.replace(R.id.contentFrame, new News());
                break;
            case 1:
                fragmentTransaction.replace(R.id.contentFrame, new Vertretungsplan());
                break;
            case 2:
                fragmentTransaction.replace(R.id.contentFrame, new Notizen());
                break;
            case 3:
                fragmentTransaction.replace(R.id.contentFrame, new Todo());
                break;
            case 4:
                fragmentTransaction.replace(R.id.contentFrame, new Termine());
                break;
            case 5:
                fragmentTransaction.replace(R.id.contentFrame, new Speiseplan());
                break;
            default:
                fragmentTransaction.replace(R.id.contentFrame, new News());
                break;
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private static void showInFragmentWebView(final WebView webView, String url, final Context context) {
        webView.setWebViewClient(new WebViewClient());
        if (!offline(context)) {
            webView.clearCache(true);
        }
        if (offline(context)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/error.html");
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Wird geladen...");
        progressDialog.show();

        SharedPreferences sharedPref = context.getSharedPreferences("GYMH", MODE_PRIVATE);
        if (url.toLowerCase().contains("?")) {
            url += "&theme=";
        } else {
            url += "?theme=";
        }
        url += sharedPref.getString("THEME-NAME", "Standard");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if ((url.toLowerCase().contains(".pdf".toLowerCase())) || (url.toLowerCase().contains("play.google".toLowerCase()))) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(view.getContext(), Uri.parse(url));
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
        Log.d("URL", url);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private static boolean offline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragmentToPlace = new News();
        switch (id) {
            case R.id.nav_news:
                fragmentToPlace = new News();
                break;
            case R.id.nav_vertretungsplan:
                fragmentToPlace = new Vertretungsplan();
                break;
            case R.id.nav_notizen:
                fragmentToPlace = new Notizen();
                break;
            case R.id.nav_todo:
                fragmentToPlace = new Todo();
                break;
            case R.id.nav_termine:
                fragmentToPlace = new Termine();
                break;
            case R.id.nav_speiseplan:
                fragmentToPlace = new Speiseplan();
                break;
            case R.id.nav_stundenplan:
                fragmentToPlace = new Stundenplan();
                break;
            case R.id.nav_settings:
                fragmentToPlace = new Settings();
                break;
            case R.id.nav_about:
                fragmentToPlace = new About();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragmentToPlace);
        fragmentTransaction.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class About extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.about, container, false);
        }
    }

    public static class News extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "https://gymh.philippdormann.de/news/", getActivity());
            return view;
        }
    }

    public static class Todo extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "https://gymh.philippdormann.de/todo/", getActivity());
            return view;
        }
    }

    public static class Termine extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "https://gymh.philippdormann.de/termine/", getActivity());
            return view;
        }
    }

    public static class Vertretungsplan extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.vertretungsplan, container, false);
            Log.d("LOGGER", "loadVP");
            webView = view.findViewById(R.id.webView);
            SharedPreferences sharedPref = getActivity().getSharedPreferences("GYMH", MODE_PRIVATE);
            String filter = sharedPref.getString("FILTER", "");
            Boolean shouldShowLehrerFullname = sharedPref.getBoolean("LEHRER-FULLNAME", false);
            String vpURL = "https://gymh.philippdormann.de/vertretungsplan/" + "?f=" + filter;
            if (shouldShowLehrerFullname) {
                vpURL += "&display-lehrer-full";
            }

            Button montag = view.findViewById(R.id.montag);
            Button dienstag = view.findViewById(R.id.dienstag);
            Button mittwoch = view.findViewById(R.id.mittwoch);
            Button donnerstag = view.findViewById(R.id.donnerstag);
            Button freitag = view.findViewById(R.id.freitag);
            if (sharedPref.getBoolean("WOCHENANSICHT", false)) {
                montag.setVisibility(View.GONE);
                dienstag.setVisibility(View.GONE);
                mittwoch.setVisibility(View.GONE);
                donnerstag.setVisibility(View.GONE);
                freitag.setVisibility(View.GONE);
                String vpWeekURL = "https://gymh.philippdormann.de/vertretungsplan/" + "week.php?f=" + filter;
                if (shouldShowLehrerFullname) {
                    vpWeekURL += "&display-lehrer-full";
                }
                showInFragmentWebView(webView, vpWeekURL, getActivity());
            } else {
                final String finalVpURL = vpURL;
                montag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInFragmentWebView(webView, finalVpURL + "&d=mo", getActivity());
                    }
                });
                dienstag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInFragmentWebView(webView, finalVpURL + "&d=di", getActivity());
                    }
                });
                mittwoch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInFragmentWebView(webView, finalVpURL + "&d=mi", getActivity());
                    }
                });
                donnerstag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInFragmentWebView(webView, finalVpURL + "&d=do", getActivity());
                    }
                });
                freitag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showInFragmentWebView(webView, finalVpURL + "&d=fr", getActivity());
                    }
                });
                showInFragmentWebView(webView, vpURL, getActivity());
            }
            return view;
        }
    }

    public static class Speiseplan extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "https://gymh.philippdormann.de/mensaplan", getActivity());
            return view;
        }
    }

    public static class Stundenplan extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "https://gymh.philippdormann.de/stundenplan", getActivity());
            return view;
        }
    }

    public static class Notizen extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.notiz, container, false);

            SharedPreferences sharedPref = getActivity().getSharedPreferences("GYMH", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();
            final EditText editText_notiz = view.findViewById(R.id.editText_notiz);
            editText_notiz.setText(sharedPref.getString("NOTIZ", ""));
            editText_notiz.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("NOTIZ", editText_notiz.getText().toString());
                    editor.apply();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            return view;
        }
    }

    public static class Settings extends Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.settings, container, false);

            Button button = view.findViewById(R.id.button_spenden);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(getActivity(), Uri.parse("http://paypal.me/philippdormann"));
                }
            });

            SharedPreferences sharedPref = getActivity().getSharedPreferences("GYMH", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();
            final EditText editText_filter = view.findViewById(R.id.editText_filter);
            editText_filter.setText(sharedPref.getString("FILTER", "").toUpperCase());
            editText_filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("FILTER", editText_filter.getText().toString().toUpperCase());
                    editor.apply();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            Spinner spinner = view.findViewById(R.id.spinner);
            spinner.setSelection(sharedPref.getInt("STARTSEITE", 0));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    editor.putInt("STARTSEITE", position);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Switch switchWeek = view.findViewById(R.id.switchWeek);
            switchWeek.setChecked(sharedPref.getBoolean("WOCHENANSICHT", false));
            switchWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editor.putBoolean("WOCHENANSICHT", isChecked);
                    editor.apply();
                }
            });

            Switch switchLehrerDisplayFull = view.findViewById(R.id.switchLehrerDisplayFull);
            switchLehrerDisplayFull.setChecked(sharedPref.getBoolean("LEHRER-FULLNAME", false));
            switchLehrerDisplayFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editor.putBoolean("LEHRER-FULLNAME", isChecked);
                    editor.apply();
                }
            });

            Button theme_red = view.findViewById(R.id.theme_red);
            theme_red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Red, "Red");
                }
            });
            Button theme_pink = view.findViewById(R.id.theme_pink);
            theme_pink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Pink, "Pink");
                }
            });
            Button theme_night = view.findViewById(R.id.theme_night);
            theme_night.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Night, "Night");
                }
            });
            Button theme_blue = view.findViewById(R.id.theme_blue);
            theme_blue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Blue, "Blue");
                }
            });
            Button theme_green = view.findViewById(R.id.theme_green);
            theme_green.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Green, "Green");
                }
            });
            Button theme_bluegrey = view.findViewById(R.id.theme_bluegrey);
            theme_bluegrey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.BlueGrey, "BlueGrey");
                }
            });
            Button theme_lightgreen = view.findViewById(R.id.theme_lightgreen);
            theme_lightgreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.LightGreen, "LightGreen");
                }
            });
            Button theme_purple = view.findViewById(R.id.theme_purple);
            theme_purple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Purple, "Purple");
                }
            });
            Button theme_deeppurple = view.findViewById(R.id.theme_deeppurple);
            theme_deeppurple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.DeepPurple, "DeepPurple");
                }
            });
            Button theme_indigo = view.findViewById(R.id.theme_indigo);
            theme_indigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Indigo, "Indigo");
                }
            });
            Button theme_cyan = view.findViewById(R.id.theme_cyan);
            theme_cyan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Cyan, "Cyan");
                }
            });
            Button theme_standard = view.findViewById(R.id.theme_standard);
            theme_standard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Standard, "Standard");
                }
            });
            Button theme_rosegold = view.findViewById(R.id.theme_rosegold);
            theme_rosegold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Rosegold, "Rosegold");
                }
            });
            Button theme_orange = view.findViewById(R.id.theme_orange);
            theme_orange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Orange, "Orange");
                }
            });
            Button theme_amber = view.findViewById(R.id.theme_amber);
            theme_amber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Amber, "Amber");
                }
            });
            Button theme_teal = view.findViewById(R.id.theme_teal);
            theme_teal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Teal, "Teal");
                }
            });
            Button theme_sand = view.findViewById(R.id.theme_sand);
            theme_sand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Sand, "Sand");
                }
            });
            Button theme_rainbow = view.findViewById(R.id.theme_rainbow);
            theme_rainbow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTheme(R.style.Rainbow, "Rainbow");
                }
            });

            return view;
        }

        private void saveTheme(int theme, String themeName) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences("GYMH", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("THEME", theme);
            editor.putString("THEME-NAME", themeName);
            editor.apply();
            if (sharedPref.getInt("THEME", 1) == theme) {
                restart();
            }
        }

        void restart() {
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getActivity().finishAffinity();
            }
        }
    }
}