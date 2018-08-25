package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Book;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.BookRepository;
import com.andrewchelladurai.simplebible.data.repository.SearchRepository;
import com.andrewchelladurai.simplebible.presenter.SearchScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.SearchListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class SearchScreen
    extends AppCompatActivity
    implements SearchScreenOps {

    private static final String TAG = "SearchScreen";

    private static SearchScreenPresenter mPresenter;
    private static SearchListAdapter     mAdapter;

    private TextInputEditText mInputField;
    private SearchRepository  mSearchRepository;
    private BookRepository    mBookRepository;
    private ScrollView        mHelpView;
    private RecyclerView      mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchRepository = ViewModelProviders.of(this).get(SearchRepository.class);
        mBookRepository = ViewModelProviders.of(this).get(BookRepository.class);

        final int bookCount = 66;
        final String firstBook = getString(R.string.first_book);
        final String lastBook = getString(R.string.last_book);

        mBookRepository.queryDatabase(bookCount, firstBook, lastBook)
                       .observe(this, new Observer<List<Book>>() {
                           @Override
                           public void onChanged(final List<Book> books) {
                               mBookRepository.populateCache(books, bookCount, firstBook, lastBook);
                           }
                       });

        if (mPresenter == null || mAdapter == null) {
            mPresenter = new SearchScreenPresenter(this);
            mAdapter = new SearchListAdapter(this);
        }

        mInputField = findViewById(R.id.act_srch_input);
        mInputField.addTextChangedListener(this);

        mListView = findViewById(R.id.act_srch_list);
        mListView.setAdapter(mAdapter);

        BottomAppBar mBar = findViewById(R.id.act_srch_appbar);
        mBar.replaceMenu(R.menu.search_screen_appbar);
        mBar.setOnMenuItemClickListener(this);

        findViewById(R.id.act_srch_fab).setOnClickListener(this);

        TextView infoView = findViewById(R.id.act_srch_help);
        infoView.setText(Html.fromHtml(getString(R.string.act_srch_help)));

        mHelpView = findViewById(R.id.act_srch_container_help);

        updateContent();
    }

    @Override
    public boolean onMenuItemClick(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_srch_menu_share:
                handleInteractionShare();
                break;
            case R.id.act_srch_menu_bookmark:
                handleInteractionBookmark();
                break;
            case R.id.act_srch_menu_clear:
                handleInteractionClear();
                break;
            case R.id.act_srch_menu_settings:
                handleInteractionSettings();
                break;
            default:
                Log.e(TAG, "onMenuItemClick: Unhandled event" + getString(R.string.msg_unexpected));
        }
        return true;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.act_srch_fab:
                handleActionSearch();
                break;
            default:
                Log.e(TAG, "onClick: Unhandled event" + getString(R.string.msg_unexpected));
        }
    }

    @Override
    public void onChanged(final List<Verse> list) {
        if (list.isEmpty()) {
            showMessageNoResults();
        } else {
            updateScreen(list);
        }
    }

    private void updateScreen(@NonNull final List<Verse> list) {
        if (mPresenter.populateCache(list, getSearchText())) {
            mAdapter.updateList(list, getSearchText());
            mAdapter.notifyDataSetChanged();
            updateContent();
        } else {
            showErrorPopulatingCache();
        }
    }

    private void updateContent() {
        if (mAdapter.getItemCount() > 0) {
            mHelpView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mHelpView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }

    @NonNull
    public String getSearchText() {
        return mInputField.getText().toString().trim();
    }

    private void handleActionSearch() {
        final String searchText = getSearchText();
        boolean isValid = mPresenter.validateSearchText(searchText);
        if (isValid) {
            mSearchRepository.queryDatabase(searchText).observe(this, this);
        }
    }

    private void handleInteractionSettings() {
        Log.d(TAG, "handleInteractionSettings() called");
    }

    private void handleInteractionClear() {
        Log.d(TAG, "handleInteractionClear() called");
    }

    private void handleInteractionBookmark() {
        Log.d(TAG, "handleInteractionBookmark() called");
    }

    private void handleInteractionShare() {
        Log.d(TAG, "handleInteractionShare() called");
    }

    @Override
    @NonNull
    public String getSearchVerseTemplateString() {
        return getString(R.string.act_srch_verse_template);
    }

    @Override
    public void actionVerseClicked(@NonNull final Verse verse) {
        verse.setSelected(!verse.isSelected());
    }

    @Override
    public void showErrorEmptySearchText() {
        mInputField.setError(getString(R.string.act_srch_err_empty));
    }

    @Override
    public void showErrorMinLimit() {
        mInputField.setError(getString(R.string.act_srch_err_length_min));
    }

    @Override
    public void showErrorMaxLimit() {
        mInputField.setError(getString(R.string.act_srch_err_length_max));
    }

    private void showMessageNoResults() {
        mInputField.setError(getString(R.string.act_srch_msg_no_results));
        Utilities.getInstance()
                 .createSnackBar(
                     mInputField,
                     R.string.act_srch_msg_no_results,
                     Snackbar.LENGTH_LONG,
                     getResources().getColor(R.color.act_srch_snackbar),
                     getResources().getColor(R.color.act_srch_snackbar_text))
                 .show();
    }

    @Override
    public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1,
                                  final int i2) {
    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int i, final int i1,
                              final int i2) {
    }

    @Override
    public void afterTextChanged(final Editable editable) {
        mInputField.setError(null);
    }

    private void showErrorPopulatingCache() {
        Utilities.getInstance()
                 .createSnackBar(
                     mInputField,
                     R.string.act_srch_err_populate_cache_failed,
                     Snackbar.LENGTH_LONG,
                     getResources().getColor(R.color.act_chap_snackbar_text),
                     getResources().getColor(R.color.act_chap_snackbar_text))
                 .show();
    }
}
