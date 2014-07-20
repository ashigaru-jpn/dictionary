package jp.mobs.gogo.android.common;

import jp.mobs.gogo.android.dictionary.HistoryDbAdapter;
import jp.mobs.gogo.android.dictionary.R;
import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class HistoryUtil {

    public HistoryUtil() {
    }

    public ImageButton getHistoryImageButton(final Activity activity,
            final int layout, final HistoryDbAdapter historyDbAdapter) {
        final ImageButton historyButton = (ImageButton) activity
                .findViewById(layout);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(activity,
                        activity.getResources().getString(R.string.not_yet),
                        Toast.LENGTH_LONG).show();
            }
        });
        return historyButton;
    }
}
