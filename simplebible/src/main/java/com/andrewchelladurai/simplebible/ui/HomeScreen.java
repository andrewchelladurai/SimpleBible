package com.andrewchelladurai.simplebible.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupService;
import com.andrewchelladurai.simplebible.model.HomeScreenModel;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.andrewchelladurai.simplebible.utils.BookUtils;
import com.andrewchelladurai.simplebible.utils.VerseUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class HomeScreen
    extends Fragment {

  private static final String TAG = "HomeScreen";

  private static boolean flagSetupFinished = false;

  private SimpleBibleScreenOps activityOps;

  private ProgressBar progressBar;

  private TextView tvVerse;

  private HomeScreenModel model;

  private FloatingActionButton fabShare;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof SimpleBibleScreenOps)) {
      throw new RuntimeException(context.toString() + " must implement SimpleBibleScreenOps");
    }
    activityOps = (SimpleBibleScreenOps) context;
    model = ViewModelProviders.of(this).get(HomeScreenModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    final View view = inflater.inflate(R.layout.home_screen, container, false);

    progressBar = view.findViewById(R.id.home_src_pbar);
    tvVerse = view.findViewById(R.id.home_src_txt_verse);
    fabShare = view.findViewById(R.id.home_src_fab_share);
    fabShare.setOnClickListener(v -> handleClickFabShare());

    showLoadingScreen();

    if (savedState == null && !flagSetupFinished) {
      // this is a new instance of the fragment and the DB is not yet setup. If yes then initiate
      // the database setup process.
      activityOps.hideNavigationComponent();
      startDbSetupService();
    } else if (savedState != null
               && flagSetupFinished
               && !model.getCachedVerseText().isEmpty()
               && model.getCachedVerseDay() == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
      // if there is a previously saved instance and the DB is setup, it means that the verses
      // are also present. Verify that this is so and then check if the day has changed, if no
      // then use the cached verse for the day
      progressBar.setVisibility(GONE);
      fabShare.setVisibility(VISIBLE);
      tvVerse.setText(HtmlCompat.fromHtml(model.getCachedVerseText(), FROM_HTML_MODE_LEGACY));
      Log.d(TAG, "onCreateView: using cached verse text");
    } else {
      // if we are here, it means we have a previous saved instance and a cached version of the
      // verse also exists BUT the day has changed. So we must update the day's verse.
      showDailyVerse();
    }
    return view;
  }

  @Override
  public void onResume() {
    setupDbServiceListeners();
    super.onResume();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
  }

  private void handleClickFabShare() {
    activityOps.shareText(
        HtmlCompat.fromHtml(model.getCachedVerseText(), FROM_HTML_MODE_LEGACY).toString());
  }

  private void showLoadingScreen() {
    progressBar.setVisibility(VISIBLE);
    fabShare.setVisibility(GONE);
    tvVerse.setText(HtmlCompat.fromHtml(getString(R.string.home_src_txt_verse_loading),
                                        FROM_HTML_MODE_LEGACY));
  }

  private void setupDbServiceListeners() {
    Log.d(TAG, "setupDbServiceListeners() : flagSetupFinished [" + flagSetupFinished + "]");
    if (flagSetupFinished && !model.getCachedVerseText().isEmpty()) {
      activityOps.showNavigationComponent();
      return;
    }

    activityOps.hideNavigationComponent();
    model.getBookCount().observe(this, bookCount -> {
      if (bookCount == BookUtils.EXPECTED_COUNT) {
        model.getVerseCount().observe(this, verseCount -> {
          if (verseCount == VerseUtils.EXPECTED_COUNT) {
            flagSetupFinished = true;
            activityOps.showNavigationComponent();
            showDailyVerse();
          }
        });
      }
    });
  }

  private void showDailyVerse() {
    Log.d(TAG, "showDailyVerse:");

    final String reference = model.getVerseReferenceForToday(
        getString(R.string.default_verse_reference),
        getResources().getStringArray(R.array.daily_verse));

    model.getVerseForToday(reference).observe(this, verse -> {
      if (verse == null) {
        showDefaultVerseText();
        final String message =
            String.format(getString(R.string.home_scr_err_nonexistent_verse),
                          reference);
        Log.e(TAG, "showDailyVerse: " + message);
        // activityOps.showErrorScreen(message, true);

        // hide the loading view and show the share fab
        progressBar.setVisibility(GONE);
        fabShare.setVisibility(VISIBLE);
        return;
      }

      model.getBook(verse.getBookNumber()).observe(this, book -> {
        if (book == null) {
          showDefaultVerseText();
          final String message =
              String.format(getString(R.string.home_scr_err_nonexistent_book),
                            verse.getBookNumber());
          Log.e(TAG, "showDailyVerse: " + message);
          // activityOps.showErrorScreen(message, true);

          // hide the loading view and show the share fab
          progressBar.setVisibility(GONE);
          fabShare.setVisibility(VISIBLE);
          return;
        }

        final String formattedText = String.format(getString(R.string.home_src_txt_verse_template),
                                                   book.getName(),
                                                   verse.getChapterNumber(),
                                                   verse.getVerseNumber(),
                                                   verse.getText());
        tvVerse.setText(HtmlCompat.fromHtml(formattedText, FROM_HTML_MODE_LEGACY));

        // saved a cached version so we can use it later and avoid redoing all this
        model.setCachedVerseText(formattedText);

        // hide the loading view and show the share fab
        progressBar.setVisibility(GONE);
        fabShare.setVisibility(VISIBLE);
      });
    });

  }

  private void showDefaultVerseText() {
    tvVerse.setText(HtmlCompat.fromHtml(getString(R.string.home_src_txt_verse_default),
                                        FROM_HTML_MODE_LEGACY));
  }

  private void startDbSetupService() {
    // create a listener to handle failed database setup
    LocalBroadcastManager.getInstance(requireContext()).registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(final Context context, final Intent intent) {
        String message = ": onReceive: DbSetupService setup failed";
        Log.d(TAG, message);
        activityOps.showErrorScreen(TAG + message, true);
      }
    }, new IntentFilter(DbSetupService.ACTION_SETUP_FAILURE));

    // start the database setup service
    final Context context = requireContext();
    ContextCompat.startForegroundService(context, new Intent(context, DbSetupService.class));
  }

}
