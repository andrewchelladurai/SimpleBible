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
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Verse;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import java.util.ArrayList;

public class ScreenBookmark
    extends Fragment {

  public static final String ARG_VERSE_LIST = "ARG_VERSE_LIST";
  private static final String TAG = "ScreenBookmark";
  private ScreenSimpleBibleOps mainOps;
  private View rootView;

  public ScreenBookmark() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement InteractionListener");
    }
    mainOps = (ScreenSimpleBibleOps) context;
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

      Log.d(TAG, "onCreateView: [" + list.size() + "] verses passed");
    }

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

}
