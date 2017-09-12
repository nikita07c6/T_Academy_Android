package kr.co.citizoomproject.android.citizoom.Interest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

import kr.co.citizoomproject.android.citizoom.AsyncLawJSONList;
import kr.co.citizoomproject.android.citizoom.Law.LawEntityObject;
import kr.co.citizoomproject.android.citizoom.MyApplication;
import kr.co.citizoomproject.android.citizoom.R;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-08-01.
 */
public class InterestLawFragment  extends Fragment {
    static InterestLawMemberActivity owner;
    RecyclerView rv;
    ImageView nothing;
    private ArrayList<LawEntityObject> interestArr = new ArrayList<>();
    private InterestLawJSONReqListAdapter adapter;

    public InterestLawFragment() {
    }

    public static InterestLawFragment newInstance(int initValue) {
        InterestLawFragment interestLawFragment = new InterestLawFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        interestLawFragment.setArguments(bundle);
        return interestLawFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshInterestLawList();
    }

    public void refreshInterestLawList() {
        AsyncLawJSONList task = new AsyncLawJSONList();
        task.adapter = adapter;
        task.nothing = nothing;
        task.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.interest_law_fragment, container, false);
        owner = (InterestLawMemberActivity)getActivity();

        nothing = (ImageView) view.findViewById(R.id.nothing_interest_law);

        rv = (RecyclerView) view.findViewById(R.id.interest_law_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getMyContext());
        rv.setLayoutManager(layoutManager);
        adapter = new InterestLawJSONReqListAdapter(this.getActivity(), interestArr);
        adapter.fragment = this;
        rv.setAdapter(adapter);

        return view;
    }

}
