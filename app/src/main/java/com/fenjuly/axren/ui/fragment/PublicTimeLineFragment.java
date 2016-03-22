package com.fenjuly.axren.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenjuly.axren.R;
import com.fenjuly.axren.model.Statuses;
import com.fenjuly.axren.network.RetrofitTool;
import com.fenjuly.axren.ui.adapter.TimeLineAdapter;
import com.fenjuly.axren.utils.AccessTokenKeeper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liurongchan on 16/3/19.
 */
public class PublicTimeLineFragment extends Fragment {

    private static PublicTimeLineFragment publicTimeLineFragment;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView timeline;
    private TimeLineAdapter timeLineAdapter;
    private Context mContext;

    private boolean isFirstLoad = true;

    public static PublicTimeLineFragment newInstance() {
        if (publicTimeLineFragment == null) {
            Bundle args = new Bundle();
            publicTimeLineFragment = new PublicTimeLineFragment();
            publicTimeLineFragment.setArguments(args);
        }
        return publicTimeLineFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlayout);
        mContext = getActivity();
        timeline = (RecyclerView) rootView.findViewById(R.id.timeline);
        timeline.setLayoutManager(new LinearLayoutManager(mContext));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        loadData();
        return rootView;
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {
        if (isFirstLoad) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        RetrofitTool.getInstance().getTimeLine(AccessTokenKeeper.readAccessToken(mContext).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Statuses>() {
                    @Override
                    public void onCompleted() {
                        if (isFirstLoad) {
                            mSwipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    isFirstLoad = false;
                                }
                            });
                        }
                        Log.e("onCompleted", "invoked");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onError", e.getMessage());
                    }

                    @Override
                    public void onNext(Statuses statuses) {
                        timeLineAdapter = new TimeLineAdapter(statuses, mContext);
                        timeline.setAdapter(timeLineAdapter);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

}
