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
import androidx.navigation.fragment.NavHostFragment;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupJob;
import com.andrewchelladurai.simplebible.data.entity.VerseEntity;
import com.andrewchelladurai.simplebible.model.ScreenHomeModel;
import com.andrewchelladurai.simplebible.object.Book;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;

public class ScreenHome
    extends Fragment {

  private static final String TAG = "ScreenHome";
  private ScreenHomeModel model;
  private ScreenSimpleBibleOps mainOps;
  private View rootView;
  private TextView progressTextView;
  private String progressTextTemplate;
  private int maxProgressValue;

  public ScreenHome() {
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof ScreenSimpleBibleOps)) {
      throw new RuntimeException(context.toString() + " must implement ScreenSimpleBibleOps");
    }
    mainOps = (ScreenSimpleBibleOps) context;
    if (model == null) {
      model = new ScreenHomeModel(requireActivity().getApplication());
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.screen_home, container, false);
    mainOps.hideKeyboard();

    // setup listener for the share fab
    rootView.findViewById(R.id.screen_home_fab_share)
            .setOnClickListener(v -> handleActionShare());
    rootView.findViewById(R.id.screen_home_fab_bookmark)
            .setOnClickListener(v -> handleActionBookmark());
    rootView.findViewById(R.id.screen_home_fab_chapter)
            .setOnClickListener(v -> handleActionChapter());

    // We will be referencing items in the next block more than 30K times
    // let's avoid the extra calls and save some time.
    progressTextView = rootView.findViewById(R.id.screen_home_progress_text);
    maxProgressValue = VerseUtils.EXPECTED_COUNT + BookUtils.EXPECTED_COUNT;
    progressTextTemplate = getString(R.string.screen_home_template_progress);

    startDbSetupJobMonitoring();

    mainOps.showNavigationView();
    return rootView;
  }

  private void startDbSetupJobMonitoring() {
    Log.d(TAG, "startDbSetupJobMonitoring:");

    // start observing the DbSetupJobState in the model even before we start it
    // this is so that we do nto miss any changes in the state
    model.getDbSetupJobState()
         .observe(getViewLifecycleOwner(), jobState -> {
           switch (jobState) {
             case DbSetupJob.NOT_STARTED:
               Log.d(TAG, "startDbSetupJobMonitoring: DbSetupJob.NOT_STARTED");
               startDbSetupJob();
               break;
             case DbSetupJob.STARTED:
               showLoadingVerse();
               break;
             case DbSetupJob.FAILED:
               Log.d(TAG, "startDbSetupJobMonitoring: DbSetupJob.FAILED");
               mainOps.showErrorScreen(getString(R.string.db_setup_msg_failure), true, true);
               break;
             case DbSetupJob.FINISHED:
               Log.d(TAG, "startDbSetupJobMonitoring: DbSetupJob.FINISHED");
               validateSetupJobCompletion();
               break;
             default:

           }
         });
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
    mainOps.shareText(getVerseText());
  }

  private void handleActionBookmark() {
    Log.d(TAG, "handleActionBookmark:");

    final Bundle bundle = new Bundle();
    bundle.putParcelableArray(ScreenBookmarkDetail.ARG_VERSE_LIST,
                              new VerseEntity[]{model.getCachedVerse()});

    NavHostFragment.findNavController(this)
                   .navigate(R.id.screen_home_to_screen_bookmark_detail, bundle);
  }

  private void handleActionChapter() {
    Log.d(TAG, "handleActionChapter:");
    final VerseEntity cachedVerse = model.getCachedVerse();

    final Bundle bundle = new Bundle();
    bundle.putInt(ScreenChapter.ARG_BOOK, cachedVerse.getBook());
    bundle.putInt(ScreenChapter.ARG_CHAPTER, cachedVerse.getChapter());
    bundle.putInt(ScreenChapter.ARG_VERSE, cachedVerse.getVerse());

    NavHostFragment.findNavController(this)
                   .navigate(R.id.screen_home_to_screen_chapter, bundle);
  }

  private void showLoadingVerse() {
    mainOps.hideNavigationView();

    // show the verse text for a loading progress
    showVerseText(R.string.screen_home_template_progress_verse);

    // show the progress bar
    rootView.findViewById(R.id.screen_home_progress_bar)
            .setVisibility(View.VISIBLE);

    // show the progress text
    progressTextView.setVisibility(View.VISIBLE);

    // hide the share fab
    rootView.findViewById(R.id.screen_home_fab_share)
            .setVisibility(View.GONE);
    rootView.findViewById(R.id.screen_home_fab_bookmark)
            .setVisibility(View.GONE);
    rootView.findViewById(R.id.screen_home_fab_chapter)
            .setVisibility(View.GONE);
  }

  private void startDbSetupJob() {
    Log.d(TAG, "startDbSetupJob:");

    final Context context = requireContext();
    final Intent intent = new Intent(context, DbSetupJob.class);

    // start the database setup service & tie it with a ResultReceiver
    // to update the model with the job's status when it changes
    DbSetupJob.startWork(context, intent, new ResultReceiver(new Handler()) {

      @Override
      protected void onReceiveResult(final int newJobState, final Bundle resultData) {
        if (newJobState == DbSetupJob.STARTED) {
          final int currentProgressValue = resultData.getInt(DbSetupJob.LINE_PROGRESS);
          final String formattedString = String.format(
              progressTextTemplate, (currentProgressValue * 100) / maxProgressValue);
          progressTextView.setText(formattedString);
        }
        model.setDbSetupJobState(newJobState);
      }
    });

  }

  private void showVerseText(@StringRes int stringResId) {
    ((TextView) rootView.findViewById(R.id.screen_home_verse))
        .setText(HtmlCompat.fromHtml(getString(stringResId),
                                     HtmlCompat.FROM_HTML_MODE_LEGACY));
  }

  private void validateSetupJobCompletion() {
    Log.d(TAG, "validateSetupJobCompletion: ");

    model.getVerseCount()
         .observe(getViewLifecycleOwner(), verseCount -> {
           if (verseCount == VerseUtils.EXPECTED_COUNT) {
             // show the verse text for a loading progress
             showVerseText(R.string.screen_home_template_default_verse);

             // hide the progress bar
             rootView.findViewById(R.id.screen_home_progress_bar)
                     .setVisibility(View.GONE);

             // hide the progress text
             progressTextView.setVisibility(View.GONE);

             // show the share & bookmark fab
             rootView.findViewById(R.id.screen_home_fab_share)
                     .setVisibility(View.VISIBLE);
             rootView.findViewById(R.id.screen_home_fab_bookmark)
                     .setVisibility(View.VISIBLE);
             rootView.findViewById(R.id.screen_home_fab_chapter)
                     .setVisibility(View.VISIBLE);

             showDailyVerse();
           }
         });

  }

  private void showDailyVerse() {
    Log.d(TAG, "showDailyVerse:");

    final String defaultReference = getString(R.string.default_verse_reference);
    final String cachedReference = model.getCachedReference();
    final int dayNumber = model.getDayNumber();
    final String[] array = getResources().getStringArray(R.array.daily_verse_references);
    final String reference = (array.length >= dayNumber)
                             ? array[dayNumber]
                             : defaultReference;

    if (dayNumber == model.getCachedDayOfYear()
        && reference.equalsIgnoreCase(cachedReference)) {
      Log.d(TAG, "showDailyVerse: Same day & reference, using cached data");
      formatAndDisplayDailyVerse(model.getCachedVerse(), model.getCachedBook());
      return;
    } else if (dayNumber == model.getCachedDayOfYear()
               && defaultReference.equalsIgnoreCase(cachedReference)) {
      Log.d(TAG, "showDailyVerse: Same day & default reference, using default text");

      showDefaultDailyVerse(defaultReference);

      return;
    }

    if (!VerseUtils.validateReference(reference)) {
      Log.e(TAG, "showDailyVerse: invalid reference [" + reference + "]");
      showDefaultDailyVerse(defaultReference);
      return;
    }

    model.getVerse(reference)
         .observe(getViewLifecycleOwner(), verse -> {

           if (verse == null) {
             Log.e(TAG, "showDailyVerse: no verse found for reference [" + reference + "]");
             showDefaultDailyVerse(defaultReference);
             return;
           }

           final int bookNum = verse.getBook();
           model.getBook(bookNum).observe(getViewLifecycleOwner(), bookEn -> {

             if (bookEn == null) {
               Log.e(TAG, "showDailyVerse: no book found for position [" + bookNum + "]");
               showDefaultDailyVerse(defaultReference);
               return;
             }

             Log.d(TAG, "showDailyVerse: using reference[" + reference
                        + "] for day[" + dayNumber + "]");

             final Book book = new Book(bookEn);

             formatAndDisplayDailyVerse(verse, book);

             model.setCachedDayOfYear(dayNumber);
             model.setCachedReference(reference);
             model.setCachedVerse(verse);
             model.setCachedBook(book);

           });
         });

    mainOps.showNavigationView();

  }

  private void formatAndDisplayDailyVerse(@NonNull final VerseEntity verse, @NonNull Book book) {
    Log.d(TAG, "formatAndDisplayDailyVerse:");
    final String template = getString(R.string.screen_home_template_verse);
    final String bookName = book.getName();
    final int chapterNum = verse.getChapter();
    final int verseNum = verse.getVerse();
    final String verseText = verse.getText();
    final String rawText = String.format(
        template, bookName, chapterNum, verseNum, verseText);

    final TextView textView = rootView.findViewById(R.id.screen_home_verse);
    textView.setText(HtmlCompat.fromHtml(rawText, HtmlCompat.FROM_HTML_MODE_LEGACY));
  }

  private void showDefaultDailyVerse(@NonNull final String reference) {
    Log.d(TAG, "showDefaultDailyVerse: reference = [" + reference + "]");

    final int defaultVerseRawText = R.string.screen_home_template_default_verse;

    if (!VerseUtils.validateReference(reference)) {
      Log.e(TAG, "showDefaultDailyVerse: invalid reference [" + reference + "]");
      showVerseText(defaultVerseRawText);
      return;
    }

    model.getVerse(reference).observe(getViewLifecycleOwner(), verse -> {

      if (verse == null) {
        Log.e(TAG, "showDefaultDailyVerse: no verse found for reference [" + reference + "]");
        showVerseText(defaultVerseRawText);
        return;
      }

      final int bookNum = verse.getBook();
      model.getBook(bookNum).observe(getViewLifecycleOwner(), bookEn -> {

        if (bookEn == null) {
          Log.e(TAG, "showDefaultDailyVerse: no book found for position [" + bookNum + "]");
          showVerseText(defaultVerseRawText);
          return;
        }

        final int dayNumber = model.getDayNumber();

        Log.d(TAG, "showDefaultDailyVerse: using reference["
                   + reference + "] for day [" + dayNumber + "]");

        final Book book = new Book(bookEn);
        showVerseText(defaultVerseRawText);

        model.setCachedDayOfYear(dayNumber);
        model.setCachedReference(reference);
        model.setCachedVerse(verse);
        model.setCachedBook(book);

      });
    });

    mainOps.showNavigationView();

  }

  private String getVerseText() {
    return ((TextView) rootView.findViewById(R.id.screen_home_verse)).getText()
                                                                     .toString();
  }

}
