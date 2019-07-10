package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.entity.Book;
import com.andrewchelladurai.simplebible.model.ScreenChapterModel;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

public class ScreenChapter
    extends Fragment {

  public static final String ARG_BOOK = "ARG_BOOK";
  public static final String ARG_CHAPTER = "ARG_CHAPTER";

  private static final String TAG = "ScreenChapter";
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private ScreenChapterModel model;

  public ScreenChapter() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    model = ViewModelProviders.of(this).get(ScreenChapterModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_chapter_fragment, container, false);
    mainOps.hideNavigationView();
    mainOps.hideKeyboard();

    if (savedState == null) {

      final Bundle arguments = getArguments();

      if (arguments == null) {
        final String message = getString(R.string.scrChapterNoArguments);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showErrorScreen(message, true, true);
        return rootView;
      }

      final Book bookArg = arguments.getParcelable(ARG_BOOK);

      if (bookArg == null) {
        final String message = getString(R.string.scrChapterErrNoBook);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showErrorScreen(message, true, true);
        return rootView;
      }

      model.setBook(bookArg);
      final int chapter = arguments.getInt(ARG_CHAPTER);

      if (chapter < 1 || chapter > bookArg.getChapters()) {
        final String message = getString(R.string.scrChapterErrChapterInvalid);
        Log.e(TAG, "onCreateView: " + message);
        mainOps.showMessage(message);
        model.setBookChapter(1);
      } else {
        model.setBookChapter(chapter);
      }
    }

    final Book book = model.getBook();
    final String htmlText = getString(R.string.scrChapterTitleTemplate,
                                      model.getChapter(),
                                      book.getName(),
                                      book.getDescription());
    final TextView title = rootView.findViewById(R.id.scrChapterTitle);
    title.setText(HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT));

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

}
