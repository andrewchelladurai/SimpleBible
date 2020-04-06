package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterNumberAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterNumberDialogOps;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChapterNumberDialog
    extends BottomSheetDialogFragment
    implements ChapterNumberDialogOps {

  private static final String TAG = "ChapterNumberDialog";

  @NonNull
  private ChapterNumberAdapter adapter;

  public ChapterNumberDialog() {
  }

  public void updateDialog(@NonNull final ChapterScreenOps ops,
                           @IntRange(from = 1) final int chapters) {
    adapter = new ChapterNumberAdapter(ops, chapters, this);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull final LayoutInflater inflater,
                           @Nullable final ViewGroup container,
                           @Nullable final Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.chapter_screen_list_chapter, container, false);
    ((RecyclerView) view.findViewById(R.id.scr_chapter_chapter_list)).setAdapter(adapter);
    return view;
  }

  @Override
  public void dismissSelectionView() {
    dismiss();
  }

}
