package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterNumberAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChapterNumberDialog
    extends BottomSheetDialogFragment {

  private static ChapterNumberAdapter adapter;

  public ChapterNumberDialog() {
  }

  public void updateAdapter(@NonNull ChapterNumberAdapter adapter) {
    ChapterNumberDialog.adapter = adapter;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.chapter_screen_list_chapter, container);
    ((RecyclerView) view.findViewById(R.id.scr_chapter_chapter_list)).setAdapter(adapter);

    return view;
  }

  @Override
  public void onSaveInstanceState(@NonNull final Bundle outState) {
    super.onSaveInstanceState(outState);
  }

}
