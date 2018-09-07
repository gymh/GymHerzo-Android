package de.philippdormann.gymnasiumherzogenaurach;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("StaticFieldLeak")
    private static WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    private static void showInFragmentWebView(final WebView webView, String url, Context context) {
        webView.setWebViewClient(new WebViewClient());
        if (offline(context)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
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
        webView.loadUrl(url);
    }

    private static boolean offline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = getSharedPreferences("GYMH", MODE_PRIVATE);
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
                fragmentTransaction.replace(R.id.contentFrame, new Termine());
                break;
            case 4:
                fragmentTransaction.replace(R.id.contentFrame, new Speiseplan());
                break;
            case 5:
                fragmentTransaction.replace(R.id.contentFrame, new Noten());
                break;
            case 6:
                fragmentTransaction.replace(R.id.contentFrame, new Stundenplan());
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            case R.id.nav_termine:
                fragmentToPlace = new Termine();
                break;
            case R.id.nav_speiseplan:
                fragmentToPlace = new Speiseplan();
                break;
            case R.id.nav_noten:
                fragmentToPlace = new Noten();
                break;
            case R.id.nav_stundenplan:
                fragmentToPlace = new Stundenplan();
                break;
            case R.id.nav_settings:
                fragmentToPlace = new Settings();
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

    public static class News extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "https://philippdormann.de/gymh/news/app.php", getActivity());
            return view;
        }
    }

    public static class Termine extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "https://philippdormann.de/gymh/termine/", getActivity());
            return view;
        }
    }

    public static class Noten extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.noten, container, false);
            return view;
        }
    }

    public static class Stundenplan extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.stundenplan, container, false);
            return view;
        }
    }

    public static class Vertretungsplan extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.vertretungsplan, container, false);
            Log.d("LOGGER", "loadVP");
            webView = view.findViewById(R.id.webView);
            SharedPreferences sharedPref = getActivity().getSharedPreferences("GYMH", MODE_PRIVATE);
            String filter = sharedPref.getString("FILTER", "");
            String vpURL = "https://philippdormann.de/gymh/vertretungsplan/" + "?f=" + filter;

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
                showInFragmentWebView(webView, "https://philippdormann.de/gymh/vertretungsplan/" + "week.php?f=" + filter, getActivity());
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

    public static class Speiseplan extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.webview, container, false);
            webView = view.findViewById(R.id.webView);
            showInFragmentWebView(webView, "http://mensawelten.de/LOGINPLAN.ASPX?P=FO111&E=herz", getActivity());
            return view;
        }
    }

    public static class Notizen extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.notizen, container, false);

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

    public static class Settings extends android.support.v4.app.Fragment {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.settings, container, false);

            SharedPreferences sharedPref = getActivity().getSharedPreferences("GYMH", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();
            final EditText editText_filter = view.findViewById(R.id.editText_filter);
            editText_filter.setText(sharedPref.getString("FILTER", ""));
            editText_filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editor.putString("FILTER", editText_filter.getText().toString());
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
            return view;
        }
    }
}