package jp.mobs.gogo.android.dictionary;

import java.util.List;

import jp.mobs.gogo.android.dictionary.model.TagDefModel;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TagAppliActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_appli);

        /**
         * Spinner
         */
        final ArrayAdapter<TagDefModel> spinnerAdapter = new ArrayAdapter<TagDefModel>(
                this, android.R.layout.simple_spinner_item);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final List<TagDefModel> spinnerItems = new TagDefDbAdapter(this)
                .getAllTagDefs();
        for (final TagDefModel item : spinnerItems) {
            spinnerAdapter.add(item);
        }
        final Spinner spinner = (Spinner) findViewById(R.id.l_tag_spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                final TagDefModel selectedModel = spinnerAdapter
                        .getItem(position);
                // TODO
            }

            @Override
            public void onNothingSelected(final AdapterView<?> arg0) {
            }
        });
    }
}
