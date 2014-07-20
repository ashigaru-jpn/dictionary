package jp.mobs.gogo.android.dictionary;

import java.util.List;

import jp.mobs.gogo.android.dictionary.model.TagDefModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TagEditorActivity extends Activity {
    private ArrayAdapter<TagDefModel> adapter;
    private TagDefDbAdapter tagDefDbAdapter;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_editor);

        tagDefDbAdapter = new TagDefDbAdapter(this);

        final ListView listView = (ListView) findViewById(R.id.l_tag_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                final String rename = view.getResources().getString(
                        R.string.rename);
                final String delete = view.getResources().getString(
                        R.string.delete);
                final String[] str_items = { rename, delete };
                new AlertDialog.Builder(TagEditorActivity.this)
                        .setTitle(
                                view.getResources().getString(
                                        R.string.please_select))
                        .setItems(str_items,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog,
                                            final int which) {
                                        final String whichStr = str_items[which];
                                        if (rename.equals(whichStr)) {
                                            renameTagDef(position);
                                        } else if (delete.equals(whichStr)) {
                                            removeTagDef(position);
                                        }
                                    }

                                    private void renameTagDef(final int position) {
                                        final TagDefModel model = adapter
                                                .getItem(position);
                                        final EditText edtInput = new EditText(
                                                TagEditorActivity.this);
                                        edtInput.setText(model.getName());
                                        renameDialog(view, edtInput, model);
                                    }

                                    private void removeTagDef(final int position) {
                                        new AlertDialog.Builder(
                                                TagEditorActivity.this)
                                                .setIcon(
                                                        android.R.drawable.ic_menu_close_clear_cancel)
                                                .setMessage(
                                                        getResources()
                                                                .getString(
                                                                        R.string.is_it_ok))
                                                .setPositiveButton(
                                                        getResources()
                                                                .getString(
                                                                        R.string.ok),
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(
                                                                    final DialogInterface dialog,
                                                                    final int whichButton) {
                                                                final TagDefModel model = adapter
                                                                        .getItem(position);
                                                                // Delete Page
                                                                // Tags
                                                                new PageTagDbAdapter(
                                                                        TagEditorActivity.this)
                                                                        .deleteByTagID(model
                                                                                .getId());

                                                                // Delete Tag
                                                                // Def
                                                                final boolean result = tagDefDbAdapter
                                                                        .remove(model);
                                                                if (result) {
                                                                    Toast.makeText(
                                                                            TagEditorActivity.this,
                                                                            getResources()
                                                                                    .getString(
                                                                                            R.string.success),
                                                                            Toast.LENGTH_SHORT)
                                                                            .show();
                                                                    adapter.remove(model);
                                                                }
                                                            }
                                                        })
                                                .setNegativeButton(
                                                        getResources()
                                                                .getString(
                                                                        R.string.cancel),
                                                        new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(
                                                                    final DialogInterface dialog,
                                                                    final int whichButton) {
                                                            }
                                                        }).show();
                                    }
                                }).show();
            }
        });
        adapter = new ArrayAdapter<TagDefModel>(this,
                android.R.layout.simple_list_item_1);
        final List<TagDefModel> tagDefModels = tagDefDbAdapter.getAllTagDefs();
        for (final TagDefModel m : tagDefModels) {
            adapter.add(m);
        }
        listView.setAdapter(adapter);

        final Button addBtn = (Button) findViewById(R.id.l_new_tag_add_btn);
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                addTag();
            }
        });
    }

    private void renameDialog(final View view, final EditText edtInput,
            final TagDefModel model) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(
                TagEditorActivity.this);
        adb.setTitle(view.getResources().getString(R.string.please_input));
        adb.setView(edtInput);
        adb.setPositiveButton(view.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                        final String oldName = model.getName();
                        model.setName(edtInput.getText().toString());
                        final boolean result = tagDefDbAdapter.rename(model);
                        if (result) {
                            Toast.makeText(TagEditorActivity.this,
                                    getResources().getString(R.string.success),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            model.setName(oldName);
                        }
                    }
                });
        adb.setNegativeButton(view.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int whichButton) {
                    }
                });
        final AlertDialog ad = adb.show();

        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                final Button okBtn = ad
                        .getButton(DialogInterface.BUTTON_POSITIVE);
                okBtn.setEnabled(s.length() > 0);
            }

            @Override
            public void beforeTextChanged(final CharSequence s,
                    final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start,
                    final int before, final int count) {
            }
        });
    }

    private void addTag() {
        final EditText edit = (EditText) findViewById(R.id.l_new_tag_input);
        if (edit.getText().toString().length() > 0) {
            final TagDefModel model = tagDefDbAdapter.add(edit.getText()
                    .toString());
            adapter.add(model);
        }
    }
}
