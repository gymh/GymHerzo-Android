package de.philippdormann.gymnasiumherzogenaurach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TerminItemAdapter extends RecyclerView.Adapter<TerminItemAdapter.ViewHolder> {

    List<TerminItem> TerminItemList;
    Context context;

    public TerminItemAdapter(List<TerminItem> TerminItemList) {
        this.TerminItemList = TerminItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_termine_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        TerminItem terminItem = TerminItemList.get(position);
        holder.title.setText(terminItem.title);
        holder.date.setText(terminItem.date);
        holder.description.setText(terminItem.description);
    }

    @Override
    public int getItemCount() {
        return TerminItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name_item);
            date = itemView.findViewById(R.id.date_item);
            description = itemView.findViewById(R.id.description_item);
        }

    }
}