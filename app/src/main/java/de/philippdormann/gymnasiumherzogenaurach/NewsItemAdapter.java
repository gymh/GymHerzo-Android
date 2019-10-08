package de.philippdormann.gymnasiumherzogenaurach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ViewHolder> {

    List<NewsItem> NewsItemList;
    Context context;

    public NewsItemAdapter(List<NewsItem> TvShowList) {
        this.NewsItemList = TvShowList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_news_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NewsItem newsItem = NewsItemList.get(position);

        holder.articleTitle.setText(newsItem.title);
        holder.articleContent.setText(newsItem.contentArticle);

        Picasso.get().load(newsItem.imageMain).into(holder.articleImage);
    }

    @Override
    public int getItemCount() {
        return NewsItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle;
        TextView articleContent;
        ImageView articleImage;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.news_article_title);
            articleContent = itemView.findViewById(R.id.news_article_content);
            articleImage = itemView.findViewById(R.id.news_article_image);
            cardView = itemView.findViewById(R.id.news_article_card_view);
        }

    }
}