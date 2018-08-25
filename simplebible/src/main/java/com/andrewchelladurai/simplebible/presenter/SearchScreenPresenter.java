package com.andrewchelladurai.simplebible.presenter;

import com.andrewchelladurai.simplebible.data.entities.Verse;
import com.andrewchelladurai.simplebible.data.repository.SearchRepository;
import com.andrewchelladurai.simplebible.ui.ops.SearchScreenOps;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 24-Aug-2018 @ 11:08 PM.
 */
public class SearchScreenPresenter {

    private final SearchScreenOps mOps;

    public SearchScreenPresenter(final SearchScreenOps ops) {
        mOps = ops;
    }

    public boolean validateSearchText(final String searchText) {
        if (searchText.isEmpty()) {
            mOps.showErrorEmptySearchText();
            return false;
        }

        if (searchText.length() < 3) {
            mOps.showErrorMinLimit();
            return false;
        }

        if (searchText.length() > 50) {
            mOps.showErrorMaxLimit();
            return false;
        }

        return true;
    }

    public boolean populateCache(@NonNull final List<Verse> list,
                                 @NonNull final String searchText) {
        return SearchRepository.getInstance().populateCache(list, searchText);
    }
}
