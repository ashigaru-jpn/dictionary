package jp.mobs.gogo.android.dictionary;

import java.util.ArrayList;
import java.util.List;

import jp.mobs.gogo.android.common.AdMobUtility;
import jp.mobs.gogo.android.common.MyClipBoardManager;
import jp.mobs.gogo.android.common.Utility;
import jp.mobs.gogo.android.common.VoiceInputUtil;
import jp.mobs.gogo.android.common.WebViewFinder;
import jp.mobs.gogo.android.dictionary.model.HistoryModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int SELECTTEXT_MENU_ID = Menu.FIRST;
    private DictWebView dictWebView;
    // private DictChromeView dictWebView;
    private EditText searchEdit;
    private LinearLayout buttomBtnLayout;
    private WebViewFinder webViewFinder;
    // private ChromeViewFinder webViewFinder;
    private HistoryDbAdapter historyDbAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        historyDbAdapter = new HistoryDbAdapter(this);

        // adView
        final LinearLayout layout = (LinearLayout) findViewById(R.id.l_ad);
        layout.addView(AdMobUtility.getView(this));

        searchEdit = (EditText) findViewById(R.id.l_retrieval_word);
        searchEdit.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(final View v, final int keyCode,
                    final KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Utility.closeKeyBoard(MainActivity.this, v);
                    search();
                    return true;
                }
                return false;
            }
        });

        final ImageButton clearButton = (ImageButton) findViewById(R.id.l_btn_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                searchEdit.setText("");
                searchEdit.requestFocus();
            }
        });

        new VoiceInputUtil().createImageButton(this, R.id.l_btn_voice);

        /**
         * Spinner
         */
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final List<String> spinnerItems = DictSite.getNameList(this);
        for (final String item : spinnerItems) {
            spinnerAdapter.add(item);
        }
        final Spinner spinner = (Spinner) findViewById(R.id.l_dict);
        spinner.setAdapter(spinnerAdapter);
        final DictSite startUpSite = new PrefHolder(this).getSelectedDict();
        final int dictSitePosition = startUpSite.getPosition();
        spinner.setSelection(dictSitePosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                final DictSite site = DictSite.getByPosition(position);
                // dictWebView.setSite(site);
                dictWebView.setSite(site);
                new PrefHolder(MainActivity.this).setDictSite(site);
                search();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> arg0) {
            }
        });

        /*
         * WebView
         */
        dictWebView = new DictWebViewCreator().create(this, R.id.l_web);
        dictWebView.setSite(startUpSite);
        // dictWebView = new DictChromeViewCreator().create(this, R.id.l_web);
        // dictWebView.setSite(startUpSite);

        final ImageButton historyButton = (ImageButton) findViewById(R.id.l_btn_history);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final List<HistoryModel> hModels = historyDbAdapter
                        .getAllHistory();
                if (hModels == null || hModels.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.no_history,
                            Toast.LENGTH_LONG).show();
                    return;
                }
                final List<String> keyWords = new ArrayList<String>();
                for (int i = 0; i < hModels.size(); i++) {
                    keyWords.add(hModels.get(hModels.size() - i - 1).getWord());
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_menu_recent_history48)
                        .setTitle(R.string.please_select)
                        .setItems(keyWords.toArray(new String[0]),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog,
                                            final int which) {
                                        final String keyWord = keyWords
                                                .get(which);
                                        searchEdit.setText(keyWord);
                                        Utility.closeKeyBoard(
                                                MainActivity.this, v);
                                        search();
                                    }
                                })
                        .setPositiveButton(
                                getResources()
                                        .getString(R.string.clear_history),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog,
                                            final int whichButton) {
                                        Toast.makeText(MainActivity.this,
                                                R.string.cleared_history,
                                                Toast.LENGTH_LONG).show();
                                        historyDbAdapter.clearAllHistory();
                                    }
                                }).show();
            }
        });

        /**
         * find
         */
        webViewFinder = new WebViewFinder(this, R.id.l_find_text_base,
                dictWebView.getWebView());
        // webViewFinder = new ChromeViewFinder(this, R.id.l_find_text_base,
        // dictWebView.getWebView());

        /**
         * Buttom Buton
         */
        buttomBtnLayout = (LinearLayout) findViewById(R.id.l_btn_area);
        buttomBtnLayout.setVisibility(View.INVISIBLE);

        final Button closeBtn = (Button) findViewById(R.id.l_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PrefHolder hlr = new PrefHolder(MainActivity.this);
                if (hlr.clearHistoryByCloseBtn()) {
                    new HistoryDbAdapter(MainActivity.this).clearAllHistory();
                    Toast.makeText(MainActivity.this, R.string.cleared_history,
                            Toast.LENGTH_SHORT).show();
                }
                if (hlr.exitByCloseBtn()) {
                    finish();
                }
                if (dictWebView.close()) {
                    buttomBtnLayout.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, R.string.closed,
                            Toast.LENGTH_SHORT).show();
                }
                if (hlr.clearBrowsHistoryByCloseBtn()) {
                    dictWebView.getWebView().clearHistory();
                    Toast.makeText(MainActivity.this,
                            R.string.cleared_brows_history, Toast.LENGTH_SHORT)
                            .show();
                }
                if (hlr.clearBrowsCashByCloseBtn()) {
                    dictWebView.getWebView().clearCache(true);
                    Toast.makeText(MainActivity.this,
                            R.string.cleared_brows_cach, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        // Button tagBtn = (Button) findViewById(R.id.l_tag);
        // tagBtn.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // String[] tagNames = new
        // TagDefDbAdapter(MainActivity.this).getTagNames();
        // if (tagNames.length == 0) {
        // new AlertDialog.Builder(MainActivity.this)
        // .setIcon(R.drawable.ic_menu_tag48)
        // .setTitle(R.string.add_tags)
        // .setMessage(R.string.not_found_tag)
        // .setNegativeButton(R.string.edit_tags, new
        // DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // doTagEdit();
        // }
        // })
        // .show();
        // } else {
        // final String url = dictWebView.getWebView().getUrl();
        // final String title = dictWebView.getWebView().getTitle();
        // final List<TagDefModel> models = new
        // TagDefDbAdapter(MainActivity.this).getAllTagDefs();
        // final boolean[] tagFlags = new
        // PageTagDbAdapter(MainActivity.this).getFlags(models, url);
        // new AlertDialog.Builder(MainActivity.this)
        // .setIcon(R.drawable.ic_menu_tag48)
        // .setTitle(R.string.add_tags_msg)
        // .setMultiChoiceItems(tagNames, tagFlags,
        // new DialogInterface.OnMultiChoiceClickListener(){
        // public void onClick(DialogInterface dialog, int which, boolean
        // isChecked) {
        // tagFlags[which] = isChecked;
        // TagDefModel model = models.get(which);
        // new PageTagDbAdapter(MainActivity.this).editTag(url, model,
        // isChecked, title);
        // }
        // })
        // .setNeutralButton(R.string.edit_tags, new
        // DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // doTagEdit();
        // }
        // })
        // .setNegativeButton(R.string.apply_tags, new
        // DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // doTagAppli();
        // }
        // })
        // .show();
        // }
        // }
        // });

        /**
         * Intent
         */
        if (Intent.ACTION_SEND.equals(getIntent().getAction())) {
            final String word = getIntent().getExtras()
                    .getCharSequence(Intent.EXTRA_TEXT).toString();
            searchEdit.setText(word);
        }
    }

    // private void doTagEdit() {
    // Intent intent = new Intent();
    // intent.setClassName(
    // "jp.mobs.gogo.android.dictionary",
    // "jp.mobs.gogo.android.dictionary.TagEditorActivity");
    // startActivity(intent);
    // }
    // private void doTagAppli() {
    // Intent intent = new Intent();
    // intent.setClassName(
    // "jp.mobs.gogo.android.dictionary",
    // "jp.mobs.gogo.android.dictionary.TagAppliActivity");
    // startActivity(intent);
    // }

    @Override
    protected void onActivityResult(final int requestCode,
            final int resultCode, final Intent data) {
        if (requestCode == VoiceInputUtil.VOICE_REQUEST_CODE
                && resultCode == RESULT_OK) {
            final ArrayList<String> results = new VoiceInputUtil()
                    .getResult(data);
            searchEdit.setText(results.get(0));
        } else if (requestCode == R.id.menu_settings) {
            new PrefHolder(MainActivity.this)
                    .setDictSite(dictWebView.getSite());
            new HistoryDbAdapter(this).clearOutOfRange();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void search() {
        final String word = searchEdit.getText().toString();
        if (word != null && !word.equals("")) {
            dictWebView.searchWord(word);
            buttomBtnLayout.setVisibility(View.VISIBLE);
            historyDbAdapter.addHistory(word);
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dictWebView.getWebView().canGoBack()) {
                dictWebView.getWebView().goBack();
                Toast.makeText(MainActivity.this, R.string.goback,
                        Toast.LENGTH_SHORT).show();
                final String word = dictWebView.getWord();
                searchEdit.setText(word);
                if (!dictWebView.isBlank()) {
                    buttomBtnLayout.setVisibility(View.VISIBLE);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case SELECTTEXT_MENU_ID:
            if (!dictWebView.isBlank()) {
                selectText();
            }
            break;
        case R.id.menu_search:
            if (!dictWebView.isBlank()) {
                webViewFinder.show();
            }
            break;
        case R.id.menu_page_info:
            if (!dictWebView.isBlank()) {
                final Intent intent = new Intent();
                intent.setClassName(this.getClass().getPackage().getName(),
                        PageInfoActivity.class.getName());
                intent.putExtra(PageInfoActivity.TITLE, dictWebView
                        .getWebView().getTitle());
                intent.putExtra(PageInfoActivity.URL, dictWebView.getWebView()
                        .getUrl());
                startActivity(intent);
            }
            break;
        case R.id.menu_share:
            break;
        case R.id.menu_share_url_open:
            if (!dictWebView.isBlank()) {
                final Intent bi = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(dictWebView.getWebView().getUrl()));
                startActivity(bi);
            }
            break;
        case R.id.menu_share_url_share:
            if (!dictWebView.isBlank()) {
                String text = null;
                final String clipText = MyClipBoardManager.get(this);
                if (clipText == null || clipText.equals("")) {
                    text = clipText;
                } else {
                    text = clipText + "\n--\n"
                            + dictWebView.getWebView().getUrl();
                }

                final Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.putExtra(Intent.EXTRA_TITLE, dictWebView.getWord());
                sendIntent
                        .putExtra(Intent.EXTRA_SUBJECT, dictWebView.getWord());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
            break;
        // case R.id.menu_share_evernote:
        // if (!dictWebView.isBlank()) {
        // }
        // break;
        // case R.id.menu_tags:
        // String[] str_items = {
        // getResources().getString(R.string.edit_tags),
        // getResources().getString(R.string.apply_tags)};
        // new AlertDialog.Builder(this)
        // .setTitle(R.string.please_select)
        // .setItems(str_items, new DialogInterface.OnClickListener(){
        // public void onClick(DialogInterface dialog, int which) {
        // if (which == 0) {
        // doTagEdit();
        // } else if (which == 1) {
        // doTagAppli();
        // }
        // }
        // })
        // .show();
        //
        // break;
        case R.id.menu_settings:
            final Intent intent = new Intent(this, PrefActivity.class);
            startActivityForResult(intent, R.id.menu_settings);
            break;
        case R.id.menu_exit:
            finish();
            break;
        default:
            break;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public void selectText() {
        try {
            final KeyEvent shiftPressEvent = new KeyEvent(0, 0,
                    KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
            shiftPressEvent.dispatch(dictWebView.getWebView());
        } catch (final Exception e) {
            throw new AssertionError(e);
        }
    }
}
