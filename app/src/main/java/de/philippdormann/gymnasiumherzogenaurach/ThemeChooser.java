package de.philippdormann.gymnasiumherzogenaurach;

import android.content.Context;
import android.view.View;
import android.widget.Button;

class ThemeChooser {

    View view;
    private Context context;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new ThemeHelper().setTheme(v.getId());
        }
    };

    ThemeChooser(final View view, final Context context) {
        this.view = view;
        this.context = context;
        Button theme_rot = view.findViewById(R.id.theme_red);
        theme_rot.setOnClickListener(onClickListener);
    }
}
