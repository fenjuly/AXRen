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
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.fenjuly.axren.R;
import com.fenjuly.axren.model.Status;
import com.fenjuly.axren.model.Statuses;
import com.fenjuly.axren.network.RetrofitTool;
import com.fenjuly.axren.ui.adapter.TimeLineAdapter;
import com.fenjuly.axren.ui.listener.OnRcvScrollListener;
import com.fenjuly.axren.utils.AccessTokenKeeper;
import com.fenjuly.axren.utils.DensityUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

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

    private LinearLayout loading_part;
    private AVLoadingIndicatorView loading;

    private boolean isFirstLoad = true;

    private List<Status> statusList;

    int page = 1;

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
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlayout);
        mContext = getActivity();
        timeline = (RecyclerView) rootView.findViewById(R.id.timeline);
        timeline.setLayoutManager(new LinearLayoutManager(mContext));

        loading_part = (LinearLayout) rootView.findViewById(R.id.loading_part);
        loading = (AVLoadingIndicatorView) rootView.findViewById(R.id.loading);

        timeline.setOnScrollListener(new OnRcvScrollListener(){
            @Override
            public void onBottom() {
                super.onBottom();
                timeline.setTranslationY(0 - DensityUtils.dip2px(getActivity(), 60));
                loading_part.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
                loadData();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        loadFromTop();
        return rootView;
    }

    private void refresh() {
        page = 1;
        statusList.clear();
        mSwipeRefreshLayout.setRefreshing(true);
        loadFromTop();
    }

    private void loadFromTop() {
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
        loadData();
    }

    private void loadData() {
        RetrofitTool.getInstance().getTimeLine(AccessTokenKeeper.readAccessToken(mContext).getToken(), page)
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
                        if (page == 1) {
                            if (statuses != null) {
                                statusList = statuses.getStatuses();
                            }
                            timeLineAdapter = new TimeLineAdapter(statusList, mContext);
                            timeline.setAdapter(timeLineAdapter);
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            if (statuses != null) {
                                statusList.addAll(statuses.getStatuses());
                            }
                            timeLineAdapter.refresh(statusList);
                        }
                        page++;
                        timeline.setTranslationY(0);
                        loading_part.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                    }
                });
    }

}
