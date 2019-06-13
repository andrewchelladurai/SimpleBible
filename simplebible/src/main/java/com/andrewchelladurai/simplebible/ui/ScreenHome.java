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
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.data.DbSetupJob;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class ScreenHome
    extends Fragment {

  private static final String TAG = "ScreenHome";

  private ScreenSimpleBibleOps mainOps;
  private View rootView;

  public ScreenHome() {
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
    rootView = inflater.inflate(R.layout.screen_home_fragment, container, false);

    // setup listener for the share fab
    rootView.findViewById(R.id.scrHomeFabShare)
            .setOnClickListener(v -> handleActionShare());

    // Activity / Fragment launched for the first time
    if (savedState == null) {
      // show the verse text for a loading progress
      showVerseText(R.string.scrHomeVerseLoading);

      // show the progress bar
      rootView.findViewById(R.id.scrHomeProgress).setVisibility(View.VISIBLE);

      // hide the share fab
      rootView.findViewById(R.id.scrHomeFabShare).setVisibility(View.GONE);

      startDbSetupService();
    }

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

    // start the database setup service
    DbSetupJob.startWork(context, intent, new DbSetupJobResultReceiver(new Handler()));
  }

  private void showVerseText(@StringRes int stringResId) {
    ((TextView) rootView.findViewById(R.id.scrHomeVerse))
        .setText(HtmlCompat.fromHtml(getString(stringResId), FROM_HTML_MODE_LEGACY));
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
    // TODO: 10/6/19 share the text in the verse view
  }

  public class DbSetupJobResultReceiver
      extends ResultReceiver {

    DbSetupJobResultReceiver(Handler handler) {
      super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      switch (resultCode) {
        case DbSetupJob.FINISHED:
          mainOps.showNavigationView();
          break;
        case DbSetupJob.STARTED:
        case DbSetupJob.RUNNING:
          mainOps.hideNavigationView();
          break;
        default:
          mainOps.hideNavigationView();
          Log.d(TAG, "onReceiveResult: unknown resultCode[" + resultCode + "]");
      }
    }

  }

}
