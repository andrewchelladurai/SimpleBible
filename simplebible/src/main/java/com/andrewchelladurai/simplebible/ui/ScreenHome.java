package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupJob;
import com.andrewchelladurai.simplebible.model.ScreenHomeModel;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

public class ScreenHome
    extends Fragment {

  private static final String TAG = "ScreenHome";
  private static int PROGRESS_VALUE;
  private ScreenHomeModel model;
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private TextView progressTextView;
  private int maxProgressValue;
  private String progressTextTemplate;

  public ScreenHome() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    model = ViewModelProviders.of(this).get(ScreenHomeModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_home, container, false);

    // setup listener for the share fab
    rootView.findViewById(R.id.scr_home_fab_share)
            .setOnClickListener(v -> handleActionShare());

    // We will be referencing items in the next block more than 30K times
    // let's avoid the extra calls and save some time.
    progressTextView = rootView.findViewById(R.id.scr_home_progress_text);
    maxProgressValue = VerseUtils.EXPECTED_COUNT + BookUtils.EXPECTED_COUNT;
    progressTextTemplate = getString(R.string.scrHomeProgressTextTemplate);

    // start observing the DbSetupJobState in the model even before we start it
    // this is so that we do nto miss any changes in the state
    model.getDbSetupJobState().observe(this, newJobState -> {
      if (newJobState == DbSetupJob.STARTED
          || newJobState == DbSetupJob.RUNNING) {
        showLoadingVerse();
      } else if (newJobState == DbSetupJob.FAILED) {
        Log.d(TAG, "onCreateView: DbSetupJob state = FAILED");
        mainOps.showErrorScreen(getString(R.string.dbSetupFailureMessage), true, true);
      } else if (newJobState == DbSetupJob.FINISHED) {
        Log.d(TAG, "onCreateView: DbSetupJob state = FINISHED");
        updateContent();
      }

    });

    // Activity / Fragment launched for the first time
    if (savedState == null) {
      Log.d(TAG, "onCreateView: first run since [savedState == null]");
      startDbSetupService();
    }

    mainOps.hideKeyboard();
    mainOps.showNavigationView();

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  private void startDbSetupService() {
    Log.d(TAG, "startDbSetupService() called");

    final Context context = requireContext();
    final Intent intent = new Intent(context, DbSetupJob.class);

    // start the database setup service & tie it with a ResultReceiver
    // to update the model with the job's status when it changes
    DbSetupJob.startWork(context, intent, new ResultReceiver(new Handler()) {

      @Override
      protected void onReceiveResult(final int newJobState, final Bundle resultData) {
        if (newJobState == DbSetupJob.RUNNING) {
          PROGRESS_VALUE = resultData.getInt(DbSetupJob.LINE_PROGRESS);
        }
        model.setDbSetupJobState(newJobState);
      }
    });
  }

  private void showVerseText(@StringRes int stringResId) {
    ((TextView) rootView.findViewById(R.id.scr_home_verse))
        .setText(HtmlCompat.fromHtml(getString(stringResId), HtmlCompat.FROM_HTML_MODE_LEGACY));
  }

  private String getVerseText() {
    return ((TextView) rootView.findViewById(R.id.scr_home_verse)).getText().toString();
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
    final String verseText = getVerseText();
    mainOps.shareText(verseText);
  }

  private void showLoadingVerse() {
    mainOps.hideNavigationView();

    // show the verse text for a loading progress
    showVerseText(R.string.scr_home_verse_content_loading);

    // show the progress bar
    View view = rootView.findViewById(R.id.scr_home_progress_bar);
    if (view.getVisibility() != View.VISIBLE) {
      view.setVisibility(View.VISIBLE);
    }

    // show the progress text
    if (progressTextView.getVisibility() != View.VISIBLE) {
      progressTextView.setVisibility(View.VISIBLE);
    }
    progressTextView.setText(String.format(progressTextTemplate,
                                           (PROGRESS_VALUE * 100) / maxProgressValue));

    // hide the share fab
    view = rootView.findViewById(R.id.scr_home_fab_share);
    if (view.getVisibility() != View.GONE) {
      view.setVisibility(View.GONE);
    }
  }

  private void updateContent() {
    Log.d(TAG, "updateContent: ");

    model.getVerseCount().observe(this, verseCount -> {
      if (verseCount == VerseUtils.EXPECTED_COUNT) {

        mainOps.showNavigationView();

        // show the verse text for a loading progress
        showVerseText(R.string.scrHomeVerseDefault);

        // hide the progress bar
        View view = rootView.findViewById(R.id.scr_home_progress_bar);
        if (view.getVisibility() != View.GONE) {
          view.setVisibility(View.GONE);
        }

        // hide the progress text
        if (progressTextView.getVisibility() != View.GONE) {
          progressTextView.setVisibility(View.GONE);
        }

        // show the share fab
        view = rootView.findViewById(R.id.scr_home_fab_share);
        if (view.getVisibility() != View.VISIBLE) {
          view.setVisibility(View.VISIBLE);
        }

        showDailyVerse();
      }
    });

  }

  private void showDailyVerse() {
    Log.d(TAG, "showDailyVerse:");
    final int dayNumber = model.getDayNumber();
    final String[] array = getResources().getStringArray(R.array.array_daily_verse);
    final String reference = (array.length >= dayNumber)
                             ? array[dayNumber]
                             : getString(R.string.defaultVerseReference);
    if (!VerseUtils.getInstance().validateReference(reference)) {
      Log.e(TAG, "showDailyVerse: invalid reference [" + reference + "]");
      showVerseText(R.string.scrHomeVerseDefault);
      return;
    }
    model.getVerse(reference).observe(this, verse -> {
      if (verse == null) {
        Log.e(TAG, "showDailyVerse: no verse found for reference [" + reference + "]");
        showVerseText(R.string.scrHomeVerseDefault);
        return;
      }
      model.getBook(verse.getBook()).observe(this, book -> {
        if (book == null) {
          Log.e(TAG, "showDailyVerse: no book found for position [" + verse.getBook() + "]");
          showVerseText(R.string.scrHomeVerseDefault);
          return;
        }

        Log.d(TAG, "showDailyVerse: dayNumber[" + dayNumber + "] has reference[" + reference + "]");
        final String template = getString(R.string.scr_home_verse_content_template);
        final String bookName = book.getName();
        final int chapterNum = verse.getChapter();
        final int verseNum = verse.getVerse();
        final String verseText = verse.getText();
        final String formattedText = String.format(
            template, bookName, chapterNum, verseNum, verseText);

        final TextView textView = rootView.findViewById(R.id.scr_home_verse);
        textView.setText(HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_LEGACY));

      });
    });
  }

}
