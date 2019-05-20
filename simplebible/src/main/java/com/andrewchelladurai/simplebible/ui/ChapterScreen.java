package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.andrewchelladurai.simplebible.R;

public class ChapterScreen extends Fragment {

  private FragmentInteractionListener mListener;

  @Override public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof FragmentInteractionListener) {
      mListener = (FragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement FragmentInteractionListener");
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.chapter_screen, container, false);
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  interface FragmentInteractionListener {

  }

}
