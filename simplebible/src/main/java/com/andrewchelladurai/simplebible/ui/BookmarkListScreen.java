package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleScreenOps;

public class BookmarkListScreen
    extends Fragment {

  private View rootView;
  private SimpleBibleScreenOps activityOps;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    if (!(context instanceof SimpleBibleScreenOps)) {
      throw new RuntimeException(context.toString() + " must implement SimpleBibleScreenOps");
    }

    activityOps = (SimpleBibleScreenOps) context;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedState) {
    rootView = inflater.inflate(R.layout.bookmarklist_screen, container, false);

    final TextView textView = rootView.findViewById(R.id.bookmark_list_scr_help_text);
    textView.setText(HtmlCompat.fromHtml(
        getString(R.string.bookmark_list_scr_help_text), HtmlCompat.FROM_HTML_MODE_LEGACY));

    return rootView;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityOps = null;
  }

}
