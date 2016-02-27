package com.andrewchelladurai.simplebible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InteractionListener} interface
 * to handle interaction events.
 * Use the {@link V2AboutFragment#getInstance} factory method to
 * create an instance of this fragment.
 */
public class V2AboutFragment
        extends Fragment {

    private static final String TAG = "V2AboutFragment";
    private static V2AboutFragment     staticInstance;
    private        InteractionListener mListener;

    public V2AboutFragment() {
        // Required empty public constructor
    }

    public static V2AboutFragment getInstance() {
        if (staticInstance == null) {
            staticInstance = new V2AboutFragment();
            Bundle args = new Bundle();
            staticInstance.setArguments(args);
        }
        return staticInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_v2_about, container, false);
        WebView webView = (WebView) view.findViewById(R.id.fragment_v2_about_webView);

        webView.loadUrl("file:///android_asset/about_me.html");
//        AssetManager amanager = getActivity().getAssets();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InteractionListener) {
            mListener = (InteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement InteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface InteractionListener {

        void onAboutFragmentInteraction(View view);
    }
}
