package com.fenjuly.axren.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenjuly.axren.R;
import com.fenjuly.axren.data.ImageCacheManager;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by liurongchan on 16/3/22.
 */
public class PhotoFragment extends Fragment {

    private PhotoView photoView;
    private Context mContext;

    String url;

    public static PhotoFragment newInstance(String url) {
        PhotoFragment photoFragment;
            Bundle args = new Bundle();
            args.putString("url", url);
            photoFragment = new PhotoFragment();
            photoFragment.setArguments(args);
        return photoFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = savedInstanceState == null ? getArguments().getString("url")
                : savedInstanceState.getString("url");
        mContext = getActivity();
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.photo, container, false);
        photoView = (PhotoView) rootView.findViewById(R.id.photoview);
        loadImage();
        return rootView;
    }

    private void loadImage() {
       if (url != null) {
           ImageCacheManager.loadImage(url, ImageCacheManager
                   .getImageListener(photoView, null, null));
       }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url", url);
    }
}
