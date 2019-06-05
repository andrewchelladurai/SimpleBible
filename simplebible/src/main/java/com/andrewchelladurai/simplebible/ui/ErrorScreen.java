package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.andrewchelladurai.simplebible.R;

public class ErrorScreen
    extends Fragment {

  public static final String TAG = "ErrorScreen";

  public static final String ARG_MESSAGE = "MESSAGE";
  public static final String ARG_EMAIL_DEV = "EMAIL_DEV";

  private FragmentInteractionListener mListener;
  private View rootView;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof FragmentInteractionListener) {
      mListener = (FragmentInteractionListener) context;
    } else {
      throw new RuntimeException(
          context.toString() + " must implement FragmentInteractionListener");
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.error_screen, container, false);

    rootView.findViewById(R.id.err_scr_button).setOnClickListener(
        v -> handleClickButtonEmailDeveloper());

    if (savedState != null) {
      return rootView;
    }

    final Bundle args = getArguments();
    if (args == null) {
      throw new UnsupportedOperationException(TAG + " onCreateView: Null Arguments bundle passed");
    }

    final TextView messageView = rootView.findViewById(R.id.err_scr_msg);
    if (args.containsKey(ARG_MESSAGE)
        && args.getString(ARG_MESSAGE, null) != null
        && !args.getString(ARG_MESSAGE, "").isEmpty()) {
      messageView.setText(args.getString(ARG_MESSAGE, ""));
    } else {
      messageView.setVisibility(View.INVISIBLE);
    }

    if (args.containsKey(ARG_EMAIL_DEV) && args.getBoolean(ARG_EMAIL_DEV, false)) {
      rootView.findViewById(R.id.err_scr_button).setVisibility(View.VISIBLE);
      rootView.findViewById(R.id.err_scr_disclaimer).setVisibility(View.VISIBLE);
    } else {
      rootView.findViewById(R.id.err_scr_button).setVisibility(View.INVISIBLE);
      rootView.findViewById(R.id.err_scr_disclaimer).setVisibility(View.INVISIBLE);
    }

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  private void handleClickButtonEmailDeveloper() {
    final TextView messageView = rootView.findViewById(R.id.err_scr_msg);
    Log.d(TAG, "handleClickButtonEmailDeveloper: email [" + messageView.getText() + "]");
    // TODO: 20/5/19 Pass the message to the MainActivity to prepare a composed email message
  }

  interface FragmentInteractionListener {

  }

}
