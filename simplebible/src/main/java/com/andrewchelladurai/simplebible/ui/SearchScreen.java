package com.andrewchelladurai.simplebible.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.SearchRepository;
import com.andrewchelladurai.simplebible.presenter.SearchScreenPresenter;
import com.andrewchelladurai.simplebible.ui.adapter.SearchListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.andrewchelladurai.simplebible.util.Utilities;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class SearchScreen
    extends AppCompatActivity
    implements SearchScreenOps {

    private static final String TAG = "SearchScreen";

    private static SearchScreenPresenter mPresenter;
    private static SearchListAdapter     mAdapter;

    private SearchRepository mSearchRepository;

    private TextInputLayout   mInputFieldHolder;
    private TextInputEditText mInputField;
    private ScrollView        mHelpView;
    private RecyclerView      mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchRepository = ViewModelProviders.of(this).get(SearchRepository.class);

        if (mPresenter == null || mAdapter == null) {
            mPresenter = new SearchScreenPresenter(this);
            mAdapter = new SearchListAdapter(this);
        }

        mInputField = findViewById(R.id.act_srch_input);
        mInputField.addTextChangedListener(this);
        mInputFieldHolder = findViewById(R.id.act_srch_container_input);

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
        final int itemCount = mAdapter.getItemCount();
        if (itemCount > 0) {
            mHelpView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mInputFieldHolder.setHint(String.format(
                getString(R.string.act_srch_input_template), itemCount));
        } else {
            mHelpView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mInputFieldHolder.setHint(getString(R.string.act_srch_input));
        }
        mAdapter.notifyDataSetChanged();
    }

    @NonNull
    public String getSearchText() {
        return mInputField.getText().toString().trim();
    }

    private void handleActionSearch() {
        Utilities.getInstance().hideKeyboard(this, mInputField);
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
        boolean anyVerseSelected = mAdapter.isAnyVerseSelected();

        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }

        anyVerseSelected = mAdapter.discardSelectedVerses();
        if (!anyVerseSelected) {
            showMessageDiscardSelectedVerses();
            updateContent();
        }
    }

    private void handleInteractionBookmark() {
        if (!mAdapter.isAnyVerseSelected()) {
            showErrorEmptySelectedList();
            return;
        }

        final String references =
            mPresenter.getSelectedVerseReferences(mAdapter.getSelectedVerses());

        if (references == null || references.isEmpty()) {
            Log.e(TAG, "got Empty or Invalid bookmarkReference");
            showErrorInvalidReference();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(BookmarkScreen.REFERENCES, references);
        final Intent intent = new Intent(this, BookmarkScreen.class);
        intent.putExtras(bundle);

        mAdapter.discardSelectedVerses();
        mAdapter.notifyDataSetChanged();

        startActivity(intent);
    }

    private void showErrorInvalidReference() {
        Utilities.getInstance().createSnackBar(
            findViewById(R.id.act_srch_list),
            R.string.act_srch_err_invalid_bookmark_reference,
            Snackbar.LENGTH_SHORT,
            getResources().getColor(R.color.act_srch_snackbar_text),
            getResources().getColor(R.color.act_srch_snackbar)).show();
    }

    private void handleInteractionShare() {
        final boolean anyVerseSelected = mAdapter.isAnyVerseSelected();
        if (!anyVerseSelected) {
            showErrorEmptySelectedList();
            return;
        }

        final String selectedVerses = mPresenter.getSelectedVersesTextToShare(
            mAdapter.getSelectedVerses(), getSearchVerseTemplateString());

        final String textToShare = String.format(getString(R.string.act_srch_template_share),
                                                 selectedVerses);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");

        mAdapter.discardSelectedVerses();
        mAdapter.notifyDataSetChanged();

        startActivity(sendIntent);
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
        Utilities.getInstance()
                 .createSnackBar(
                     mListView,
                     R.string.act_srch_err_empty,
                     Snackbar.LENGTH_LONG,
                     getResources().getColor(R.color.act_srch_snackbar),
                     getResources().getColor(R.color.act_srch_snackbar_text))
                 .show();
        mAdapter.clearList();
        updateContent();
    }

    @Override
    public void showErrorMinLimit() {
        Utilities.getInstance()
                 .createSnackBar(
                     mListView,
                     R.string.act_srch_err_length_min,
                     Snackbar.LENGTH_LONG,
                     getResources().getColor(R.color.act_srch_snackbar),
                     getResources().getColor(R.color.act_srch_snackbar_text))
                 .show();
        mAdapter.clearList();
        updateContent();
    }

    @Override
    public void showErrorMaxLimit() {
        Utilities.getInstance()
                 .createSnackBar(
                     mListView,
                     R.string.act_srch_err_length_max,
                     Snackbar.LENGTH_LONG,
                     getResources().getColor(R.color.act_srch_snackbar),
                     getResources().getColor(R.color.act_srch_snackbar_text))
                 .show();
        mAdapter.clearList();
        updateContent();
    }

    private void showMessageNoResults() {
        Utilities.getInstance()
                 .createSnackBar(
                     mListView,
                     R.string.act_srch_msg_no_results,
                     Snackbar.LENGTH_LONG,
                     getResources().getColor(R.color.act_srch_snackbar),
                     getResources().getColor(R.color.act_srch_snackbar_text))
                 .show();
        mAdapter.clearList();
        updateContent();
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
        mInputFieldHolder.setHint(getString(R.string.act_srch_input));
    }

    private void showErrorPopulatingCache() {
        Utilities.getInstance()
                 .createSnackBar(
                     mListView,
                     R.string.act_srch_err_populate_cache_failed,
                     Snackbar.LENGTH_LONG,
                     getResources().getColor(R.color.act_srch_snackbar_text),
                     getResources().getColor(R.color.act_srch_snackbar_text))
                 .show();
        updateContent();
    }

    public void showErrorEmptySelectedList() {
        Utilities.getInstance()
                 .createSnackBar(
                     mListView,
                     R.string.act_srch_err_empty_selection_list,
                     Snackbar.LENGTH_SHORT,
                     getResources().getColor(R.color.act_srch_snackbar_text),
                     getResources().getColor(R.color.act_srch_snackbar))
                 .show();
    }

    private void showMessageDiscardSelectedVerses() {
        Utilities.getInstance()
                 .createSnackBar(
                     mListView,
                     R.string.act_srch_msg_discarded_selected_verses,
                     Snackbar.LENGTH_SHORT,
                     getResources().getColor(R.color.act_srch_snackbar_text),
                     getResources().getColor(R.color.act_srch_snackbar))
                 .show();
    }
}
