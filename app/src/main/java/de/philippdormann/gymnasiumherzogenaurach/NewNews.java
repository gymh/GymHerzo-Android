package de.philippdormann.gymnasiumherzogenaurach;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class NewNews extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview, container, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://gymh.philippdormann.de/news/").get();
                    Elements newsArticles = doc.select(".materialcard");
                    for (Element article : newsArticles) {
                        String articleTitle = article.select("h3").text();
                        String articleContent = article.select("p").text();
                        String articleImage = article.select("img").attr("src");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }
}