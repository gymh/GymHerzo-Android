package de.philippdormann.gymnasiumherzogenaurach;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Mensa extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview, container, false);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"__VIEWSTATE\"\r\n\r\n/wEPDwUJODQ5MDQ3NzI3D2QWAmYPZBYEAgEPDxYCHgRUZXh0BQVGTzExMWRkAgIPDxYCHwAFBGhlcnpkZGRtgVcVEuhU0pO3AddHgzwoCGHk72K/Ukl7pBU0ZbwTag==\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"__VIEWSTATEGENERATOR\"\r\n\r\n5B5CA0F\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"__SCROLLPOSITIONX\"\r\n\r\n0\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"__SCROLLPOSITIONY\"\r\n\r\n0\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"__EVENTTARGET\"\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"__EVENTARGUMENT\"\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"btnLogin\"\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("http://mensawelten.de/LOGINPLAN.ASPX?P=FO111&E=herz")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("__VIEWSTATE", "/wEPDwUJODQ5MDQ3NzI3D2QWAmYPZBYEAgEPDxYCHgRUZXh0BQVGTzExMWRkAgIPDxYCHwAFBGhlcnpkZGRtgVcVEuhU0pO3AddHgzwoCGHk72K/Ukl7pBU0ZbwTag==")
                .addHeader("__VIEWSTATEGENERATOR", "5B5CA0F")
                .addHeader("__SCROLLPOSITIONX", "0")
                .addHeader("__SCROLLPOSITIONY", "0")
                .addHeader("Cache-Control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.d("LOGGER", String.valueOf(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }
}
