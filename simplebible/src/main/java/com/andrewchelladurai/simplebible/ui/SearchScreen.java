package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.repository.VerseRepository;
import com.andrewchelladurai.simplebible.presenter.SearchScreenPresenter;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

public class SearchScreen
    extends AppCompatActivity
    implements SearchScreenOps {

    private static final String TAG = "SearchScreen";

    private static SearchScreenPresenter mPresenter;
    private        TextInputLayout       mSearchContainer;
    private        TextInputEditText     mSearch;
    private        RecyclerView          mList;
    private        VerseRepository       mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchContainer = findViewById(R.id.act_srch_container_input);
        mSearch = findViewById(R.id.act_srch_input);
        mList = findViewById(R.id.act_srch_list);

        BottomAppBar mBar = findViewById(R.id.act_srch_appbar);
        mBar.replaceMenu(R.menu.search_screen_appbar);
        mBar.setOnMenuItemClickListener(this);

        findViewById(R.id.act_srch_fab).setOnClickListener(this);

        mRepository = ViewModelProviders.of(this).get(VerseRepository.class);

        mPresenter = new SearchScreenPresenter(this);
    }

    private void handleActionSearch() {
        Log.d(TAG, "handleActionSearch() called");
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
}
