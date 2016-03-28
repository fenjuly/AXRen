package com.fenjuly.axren.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenjuly.axren.R;
import com.fenjuly.axren.model.Comments;
import com.fenjuly.axren.network.RetrofitTool;
import com.fenjuly.axren.ui.adapter.CommentAndRepostAdapter;
import com.fenjuly.axren.utils.AccessTokenKeeper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liurongchan on 16/3/24.
 */
public class CommentAndRepostFragment extends Fragment {

    private String idstr;

    private RecyclerView recyclerView;
    private CommentAndRepostAdapter commentAndRepostAdapter;

    public static CommentAndRepostFragment newInstance(String idstr) {
        CommentAndRepostFragment commentAndRepostFragment;
        Bundle args = new Bundle();
        args.putString("idstr", idstr);
        commentAndRepostFragment = new CommentAndRepostFragment();
        commentAndRepostFragment.setArguments(args);
        return commentAndRepostFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        idstr = savedInstanceState == null ? getArguments().getString("idstr")
                : savedInstanceState.getString("idstr");
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.weibo_detail_comment_area, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();
        return rootView;
    }

    private void loadData() {
        RetrofitTool.getInstance().getWeiBoComments(AccessTokenKeeper.readAccessToken(getActivity()).getToken(), idstr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Comments>() {
                    @Override
                    public void onCompleted() {
                        Log.e("CommentAndReonCompleted", "invoked");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError", e.getMessage());
                    }

                    @Override
                    public void onNext(Comments comments) {
                        if (comments != null) {
                            Log.e("comments", comments.toString());
                            commentAndRepostAdapter = new CommentAndRepostAdapter(getActivity(), comments.getComments());
                            recyclerView.setAdapter(commentAndRepostAdapter);
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("idstr", idstr);
    }
}
