package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.andrewchelladurai.simplebible.R;

public class BookmarkListScreen extends Fragment {

  private OnFragmentInteractionListener mListener;

  @Override public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement FragmentInteractionListener");
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.bookmarklist_screen, container, false);
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  interface OnFragmentInteractionListener {

  }

}