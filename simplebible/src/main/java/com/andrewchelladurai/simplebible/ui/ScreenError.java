package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.ScreenSimpleBibleOps;

public class ScreenError
    extends Fragment {

  private ScreenSimpleBibleOps mainOps;
  private View rootView;

  public ScreenError() {
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
    rootView = inflater.inflate(R.layout.screen_error_fragment, container, false);
    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mainOps = null;
  }

}
