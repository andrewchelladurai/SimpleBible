package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

public class ScreenChapter
    extends Fragment {

  public static final String ARG_BOOK = "ARG_BOOK";
  private static final String TAG = "ScreenChapter";
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private Book bookArg;

  public ScreenChapter() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_chapter_fragment, container, false);
    mainOps.hideNavigationView();
    mainOps.hideKeyboard();
    final Bundle arguments = getArguments();
    if (arguments != null && arguments.containsKey(ARG_BOOK)) {
      bookArg = arguments.getParcelable(ARG_BOOK);
      Log.d(TAG, "onCreateView: passed book [" + bookArg + "]");
    }

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

}
