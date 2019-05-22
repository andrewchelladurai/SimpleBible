package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;

public class HomeScreen
    extends Fragment {

  private static final String TAG = "HomeScreen";

  private SimpleBibleScreenOps actvityOps;

  private FragmentInteractionListener fragListener;

  private ProgressBar pBar;

  private TextView tvVerse;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof FragmentInteractionListener)) {
      throw new RuntimeException(
          context.toString() + " must implement FragmentInteractionListener");
    }
    fragListener = (FragmentInteractionListener) context;
    actvityOps = (SimpleBibleScreenOps) context;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    final View view = inflater.inflate(R.layout.home_screen, container, false);

    pBar = view.findViewById(R.id.home_src_pbar);
    tvVerse = view.findViewById(R.id.home_src_txt_verse);
    tvVerse.setText(HtmlCompat.fromHtml(getString(R.string.home_src_txt_verse_default),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY));

    return view;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    fragListener = null;
    actvityOps = null;
  }

  interface FragmentInteractionListener {

  }

}
