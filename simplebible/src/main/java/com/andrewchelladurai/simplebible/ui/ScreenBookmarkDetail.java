package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.model.BookmarkDetailModel;
import com.andrewchelladurai.simplebible.ui.adapter.BookmarkDetailAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ScreenBookmarkDetailOps;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

import java.util.ArrayList;

public class ScreenBookmarkDetail
    extends Fragment
    implements ScreenBookmarkDetailOps {

  public static final String ARG_VERSE_LIST = "ARG_VERSE_LIST";
  private static final String TAG = "ScreenBookmarkDetail";
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private BookmarkDetailModel model;
  private BookmarkDetailAdapter adapter;

  public ScreenBookmarkDetail() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    model = ViewModelProviders.of(this).get(BookmarkDetailModel.class);
    adapter = new BookmarkDetailAdapter(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_bookmark_fragment, container, false);

    // on first load, get all the passed verses, show an error if it's empty
    if (savedState == null) {

      // have we got arguments
      final Bundle arguments = getArguments();
      if (arguments == null) {
        mainOps.showErrorScreen(getString(R.string.scrBookmarkErrNullVerseList), true, true);
        return rootView;
      }

      // does the arguments contain the key we need
      if (!arguments.containsKey(ARG_VERSE_LIST)) {
        mainOps.showErrorScreen(getString(R.string.scrBookmarkErrEmptyVerseList), true, true);
        return rootView;
      }

      // does the passed value actually hold data for our use
      final Parcelable[] parcelableArray = arguments.getParcelableArray(ARG_VERSE_LIST);
      if (parcelableArray == null || parcelableArray.length == 0) {
        mainOps.showErrorScreen(getString(R.string.scrBookmarkErrEmptyVerseList), true, true);
        return rootView;
      }

      // if yes, convert it into a type we can use
      final ArrayList<Verse> list = new ArrayList<>();
      for (final Parcelable parcelable : parcelableArray) {
        list.add((Verse) parcelable);
      }

      model.cacheList(list);

      rootView.findViewById(R.id.scrBookmarkActionDelete)
              .setOnClickListener(view -> handleClickActionDelete());
      rootView.findViewById(R.id.scrBookmarkActionSave)
              .setOnClickListener(view -> handleClickActionSave());
      rootView.findViewById(R.id.scrBookmarkActionEdit)
              .setOnClickListener(view -> handleClickActionEdit());

      updateContent();

    }

    return rootView;
  }

  private void handleClickActionEdit() {
    Log.d(TAG, "handleClickActionEdit() called");
  }

  private void handleClickActionSave() {
    Log.d(TAG, "handleClickActionSave() called");
  }

  private void handleClickActionDelete() {
    Log.d(TAG, "handleClickActionDelete() called");
  }

  private void updateContent() {
    adapter.updateList(model.getCachedList());
    final RecyclerView recyclerView = rootView.findViewById(R.id.scrBookmarkList);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

}
