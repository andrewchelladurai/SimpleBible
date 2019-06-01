package com.andrewchelladurai.simplebible.ui;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class SimpleBibleScreen
    extends AppCompatActivity
    implements SimpleBibleScreenOps,
               BookmarkListScreen.OnFragmentInteractionListener,
               PreferencesScreen.FragmentInteractionListener {

  private static final String TAG = "SimpleBibleScreen";

  private NotificationChannel notificationChannel = null;

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setTheme(R.style.SbTheme);
    setContentView(R.layout.simple_bible_screen);

    // this we need to do all the times we start the application
    showMainActivityNavBar();

    if (savedState == null) {
      // Need this only once when the application launches
      createNotificationChannel();
    }
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notificationChannel = new NotificationChannel(getPackageName(),
                                                    getString(
                                                        R.string.app_notification_channel_name),
                                                    NotificationManager.IMPORTANCE_HIGH);
      notificationChannel.setDescription(getString(R.string.app_notification_channel_description));

      getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
      Log.d(TAG, "createNotificationChannel: created");
    }
    Log.d(TAG, "createNotificationChannel: skipped");
  }

  @Override
  public void showNavigationComponent() {
    findViewById(R.id.main_scr_bottom_nav_bar).setVisibility(View.VISIBLE);
  }

  @Override
  public void hideNavigationComponent() {
    findViewById(R.id.main_scr_bottom_nav_bar).setVisibility(View.GONE);
  }

  @Override
  public void showErrorScreen(@Nullable final String message, final boolean emailDev) {
    hideKeyboard();
    Log.d(TAG, "showErrorScreen(): message = [" + message + "], emailDev = [" + emailDev + "]");
    Bundle args = new Bundle();
    args.putString(ErrorScreen.ARG_MESSAGE, message);
    args.putBoolean(ErrorScreen.ARG_EMAIL_DEV, emailDev);
    Navigation.findNavController(this, R.id.main_scr_nav_host_fragment)
              .navigate(R.id.action_global_errorScreen, args);
  }

  @Override
  public void showErrorMessage(@NonNull final String message) {
    hideKeyboard();
    Snackbar.make(findViewById(R.id.main_scr_root), message, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.main_scr_bottom_nav_bar)
            .show();
  }

  @Override
  public void shareText(@NonNull final String text) {
    hideKeyboard();
    if (text.isEmpty()) {
      Log.e(TAG, "shareText: why do you want to share empty text?");
      return;
    }
    final String appName = getString(R.string.app_name);
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, appName);
    intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
    startActivity(Intent.createChooser(intent, appName));
  }

  @Override
  public void hideKeyboard() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    //Find the currently focused view, so we can grab the correct window token from it.
    View view = getCurrentFocus();
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
      view = new View(this);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  @Override
  public void showChapterScreenNavBar(@NonNull final ChapterScreenOps chapterOps) {
    final BottomNavigationView navBar = findViewById(R.id.main_scr_bottom_nav_bar);
    navBar.getMenu().clear();
    navBar.inflateMenu(R.menu.chapter_scr_bottom_nav_bar);
    navBar.setOnNavigationItemSelectedListener(
        item -> {
          switch (item.getItemId()) {
            case R.id.chapter_scr_butt_share:
              chapterOps.handleClickActionShare();
              return true;
            case R.id.chapter_scr_butt_bmark:
              chapterOps.handleClickActionBookmark();
              return true;
            case R.id.chapter_scr_butt_clear:
              chapterOps.handleClickActionReset();
              return true;
            default:
              Log.e(TAG, "showChapterScreenNavBar: unknown chapter navBar item");
              return false;
          }
        });
  }

  @Override
  public void showMainActivityNavBar() {
    final BottomNavigationView navBar = findViewById(R.id.main_scr_bottom_nav_bar);
    navBar.getMenu().clear();
    navBar.inflateMenu(R.menu.main_scr_bottom_nav_bar);

    final NavController navController =
        Navigation.findNavController(this, R.id.main_scr_nav_host_fragment);
    NavigationUI.setupWithNavController(navBar, navController);
  }

}
