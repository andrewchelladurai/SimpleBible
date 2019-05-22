package com.andrewchelladurai.simplebible.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;
import com.google.android.material.bottomappbar.BottomAppBar;

public class SimpleBibleScreen
    extends AppCompatActivity
    implements SimpleBibleScreenOps,
               HomeScreen.FragmentInteractionListener,
               BookListScreen.FragmentInteractionListener,
               SearchScreen.FragmentInteractionListener,
               BookmarkListScreen.OnFragmentInteractionListener,
               PreferencesScreen.FragmentInteractionListener {

  private static final String TAG = "SimpleBibleScreen";

  private NotificationChannel notifChannel = null;

  @Override
  protected void onCreate(Bundle savedState) {
    super.onCreate(savedState);
    setTheme(R.style.SbTheme);
    setContentView(R.layout.simple_bible_screen);

    createNotificationChannel();

    setupNavigationComponent();
  }

  private void setupNavigationComponent() {
    Log.d(TAG, "setupNavigationComponent:");

    final NavController navController =
        Navigation.findNavController(this, R.id.main_scr_nav_host_fragment);
    final NavDestination currentDestination = navController.getCurrentDestination();
    final BottomAppBar appBar = findViewById(R.id.main_scr_bottom_app_bar);

    appBar.setOnMenuItemClickListener(item -> {
      switch (item.getItemId()) {
        case R.id.homeScreen:
          if (currentDestination != null
              && currentDestination.getId() != R.id.homeScreen) {
            navController.navigate(R.id.action_global_homeScreen);
          }
          return true;
        case R.id.bookListScreen:
          if (currentDestination != null
              && currentDestination.getId() != R.id.bookListScreen) {
            navController.navigate(R.id.action_global_bookListScreen);
          }
          return true;
        case R.id.searchScreen:
          if (currentDestination != null
              && currentDestination.getId() != R.id.searchScreen) {
            navController.navigate(R.id.action_global_searchScreen);
          }
          return true;
        case R.id.bookmarkListScreen:
          if (currentDestination != null
              && currentDestination.getId() != R.id.bookmarkListScreen) {
            navController.navigate(R.id.action_global_bookmarkListScreen);
          }
          return true;
        case R.id.preferencesScreen:
          if (currentDestination != null
              && currentDestination.getId() != R.id.preferencesScreen) {
            navController.navigate(R.id.action_global_preferencesScreen);
          }
          return true;
        default:
          Log.e(TAG, "setupNavigationComponent: unknown MenuItem["
                     + item.getTitle() + "-" + item.getItemId() + "]");
          return false;
      }
    });
  }

  private void createNotificationChannel() {
    Log.d(TAG, "createNotificationChannel:");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notifChannel = new NotificationChannel(getPackageName(),
                                             getString(R.string.app_notification_channel_name),
                                             NotificationManager.IMPORTANCE_HIGH);
      notifChannel.setDescription(getString(R.string.app_notification_channel_description));

      getSystemService(NotificationManager.class).createNotificationChannel(notifChannel);
    }
  }

}
