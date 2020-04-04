package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.EntityBook;
import com.andrewchelladurai.simplebible.data.EntityVerse;
import com.andrewchelladurai.simplebible.model.HomeViewModel;
import com.andrewchelladurai.simplebible.ui.ops.HomeScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.Utils;

import java.util.Calendar;

public class HomeScreen
    extends Fragment
    implements HomeScreenOps {

  private static final String TAG = "HomeScreen";

  private HomeViewModel model;

  private SimpleBibleOps ops;

  private View rootView;

  @Override
  public void onAttach(@NonNull final Context context) {
    Log.d(TAG, "onAttach:");
    super.onAttach(context);

    if (context instanceof SimpleBibleOps) {
      ops = (SimpleBibleOps) context;
    } else {
      throw new ClassCastException(TAG + " onAttach: [Context] must implement [SimpleBibleOps]");
    }

    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(HomeViewModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedState) {
    Log.d(TAG, "onCreateView:");
    rootView = inflater.inflate(R.layout.home_screen, container, false);
    ops.showNavigationView();

    updateContent();

    return rootView;
  }

  private void updateContent() {
    Log.d(TAG, "updateContent:");

    final EntityVerse cachedVerse = model.getCachedVerse();
    if (cachedVerse != null) {
      displayVerse(cachedVerse);
      return;
    }

    final int dayNo = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    final String[] array = requireContext().getResources()
                                           .getStringArray(R.array.daily_verse_references);

    final String reference = (array.length < dayNo)
                             ? getString(R.string.default_verse_reference)
                             : array[dayNo];

    final Utils utils = Utils.getInstance();
    final boolean validated = utils.validateVerseReference(reference);

    if (!validated) {
      Log.e(TAG, "updateContent: reference[" + reference + "] not valid");
      displayDefaultVerse();
      return;
    }

    final int[] parts = utils.splitVerseReference(reference);
    if (parts == null) {
      Log.e(TAG, "updateContent: invalid parts of reference[" + reference + "]");
      displayDefaultVerse();
      return;
    }

    model.getVerse(parts[0], parts[1], parts[2]).observe(getViewLifecycleOwner(), verse -> {
      if (verse == null) {
        Log.e(TAG, "updateContent: null verse returned for reference[" + reference + "]");
        return;
      }

      model.setCachedVerse(verse);
      displayVerse(verse);

    });
  }

  private void displayVerse(@NonNull final EntityVerse verse) {
    Log.d(TAG, "displayVerse:");
    EntityBook book = Utils.getInstance().getCachedBook(verse.getBook());

    if (book == null) {
      displayDefaultVerse();
      Log.e(TAG, "displayVerse: No book found for book number in verse[" + verse + "]");
      return;
    }

    final String formattedText = String.format(getString(R.string.scr_home_verse_template),
                                               book.getName(),
                                               verse.getChapter(),
                                               verse.getVerse(),
                                               verse.getText());
    final Spanned htmlText = HtmlCompat.fromHtml(formattedText,
                                                 HtmlCompat.FROM_HTML_MODE_COMPACT);
    final TextView textView = rootView.findViewById(R.id.scr_home_verse);
    textView.setText(htmlText);
  }

  private void displayDefaultVerse() {
    Log.d(TAG, "displayDefaultVerse:");
    final Spanned htmlText = HtmlCompat.fromHtml(getString(R.string.scr_home_verse_default),
                                                 HtmlCompat.FROM_HTML_MODE_COMPACT);
    final TextView textView = rootView.findViewById(R.id.scr_home_verse);
    textView.setText(htmlText);
  }

}
