package com.andrewchelladurai.simplebible.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.adapter.ChapterListAdapter;
import com.andrewchelladurai.simplebible.ui.ops.ChapterScreenOps;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by : Andrew Chelladurai
 * Email : TheUnknownAndrew[at]GMail[dot]com
 * on : 15-Aug-2018 @ 1:53 PM.
 */
public class ChapterListDialog
    extends BottomSheetDialogFragment {

    private static final String TAG = "ChapterListDialog";
    private static Integer            sChapterCount;
    private static ChapterListAdapter sAdapter;

    public static ChapterListDialog newInstance(@NonNull final ChapterScreenOps ops,
                                                @NonNull Integer chapterCount) {
        sChapterCount = chapterCount;
        sAdapter = new ChapterListAdapter(ops);
        return new ChapterListDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_chapter_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList<Integer> list = new ArrayList<>(sChapterCount);
        for (int i = 0; i < sChapterCount; i++) {
            list.add((i + 1));
        }
        sAdapter.updateList(list, sChapterCount);

        final RecyclerView recyclerView = view.findViewById(R.id.chapter_dialog_list);
        recyclerView.setAdapter(sAdapter);
    }
}
