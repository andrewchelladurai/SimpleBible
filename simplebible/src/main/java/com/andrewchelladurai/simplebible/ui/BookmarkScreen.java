package com.andrewchelladurai.simplebible.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.andrewchelladurai.simplebible.R;
import com.andrewchelladurai.simplebible.model.BookmarkViewModel;
import com.andrewchelladurai.simplebible.ui.ops.BookmarkScreenOps;
import com.andrewchelladurai.simplebible.ui.ops.SimpleBibleOps;
import com.google.android.material.bottomappbar.BottomAppBar;

public class BookmarkScreen
    extends Fragment
    implements BookmarkScreenOps {

  private static final String TAG = "BookmarkScreen";

  static final String ARG_STR_REFERENCE = "ARG_STR_REFERENCE";

  private BookmarkViewModel model;

  private SimpleBibleOps ops;

  private View rootView;

  @Override
  public void onAttach(@NonNull final Context context) {
    Log.d(TAG, "onAttach:");
    super.onAttach(context);

    if (context instanceof SimpleBibleOps) {
      ops = (SimpleBibleOps) context;
    } else {
      throw new ClassCastException(TAG + " onAttach: [Context] must implement [SimpleBibleOps]");
    }

    model = ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().getApplication())
                .create(BookmarkViewModel.class);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    Log.d(TAG, "onCreateView:");
    ops.hideKeyboard();
    ops.hideNavigationView();

    rootView = inflater.inflate(R.layout.bookmark_detail_screen, container, false);

    ((BottomAppBar) rootView.findViewById(R.id.scr_bookmark_details_app_bar))
        .setOnMenuItemClickListener(item -> {
          switch (item.getItemId()) {
            case R.id.menu_action_save_scr_bookmark_detail:
              handleActionSave();
              return true;
            case R.id.menu_action_delete_scr_bookmark_detail:
              handleActionDelete();
              return true;
            case R.id.menu_action_edit_scr_bookmark_detail:
              handleActionEdit();
              return true;
            case R.id.menu_action_share_scr_bookmark_detail:
              handleActionShare();
              return true;
            default:
              Log.e(TAG, "onCreateView: unknown Menu item [" + item.getTitle() + "] captured");
              return false;
          }
        });

    return rootView;
  }

  private void handleActionSave() {
    Log.d(TAG, "handleActionSave:");
  }

  private void handleActionDelete() {
    Log.d(TAG, "handleActionDelete:");
  }

  private void handleActionEdit() {
    Log.d(TAG, "handleActionEdit:");
  }

  private void handleActionShare() {
    Log.d(TAG, "handleActionShare:");
  }

}
