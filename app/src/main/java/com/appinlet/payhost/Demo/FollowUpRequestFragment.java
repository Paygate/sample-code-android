package com.appinlet.payhost.Demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.appinlet.payhost.Api.FollowUpApi;
import com.appinlet.payhost.Api.PayGateResponseCallback;
import com.appinlet.payhost.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowUpRequestFragment extends BaseFragment {

    @BindView(R.id.h1)
    TextInputLayout h1;
    @BindView(R.id.h2)
    TextInputLayout h2;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;


    public static FollowUpRequestFragment newInstance() {

        Bundle args = new Bundle();

        FollowUpRequestFragment fragment = new FollowUpRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.card)
    TextInputEditText card;
    @BindView(R.id.exp)
    TextInputEditText exp;
    @BindView(R.id.create)
    Button create;
    @BindView(R.id.requesttext)
    EditText requestView;
    @BindView(R.id.responsetext)
    EditText responseView;
    Unbinder unbinder;
    @BindView(R.id.paygateid)
    TextInputEditText paygateid;
    @BindView(R.id.password)
    TextInputEditText password;

    public FollowUpRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followup_create, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        h1.setHint("Token");
        h2.setHint("Token Description");
        card.setText("2258098676320541501");
        exp.setText("Authorisation");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.q:
                        h1.setHint("PayGate Request ID");
                        exp.setVisibility(View.GONE);
                        break;
                    case R.id.v:
                        h1.setHint("Transaction ID");
                        h2.setHint("Transaction Type");
                        exp.setVisibility(View.VISIBLE);
                        break;
                    case R.id.s:
                        h1.setHint("Transaction ID");
                        exp.setVisibility(View.GONE);
                        break;
                    case R.id.r:
                        h1.setHint("Transaction ID");
                        h2.setHint("Amount");
                        exp.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.create)
    public void onViewClicked() {
        show();
        try {
            FollowUpApi followUpApi = FollowUpApi.getInstance()
                    .withContext(getActivity())
                    .account(paygateid.getText().toString(), password.getText().toString());
            if (radioGroup.getCheckedRadioButtonId() == R.id.q) {
                followUpApi.doQueryRequest(card.getText().toString());
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.v) {
                followUpApi.doVoidRequest(card.getText().toString(), exp.getText().toString());
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.s) {
                followUpApi.doSettlementRequest(card.getText().toString());
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.r) {
                followUpApi.doRefundRequest(card.getText().toString(), exp.getText().toString());
            }

            followUpApi.withCallback(new PayGateResponseCallback() {
                @Override
                public void onSuccess(final String request, final String response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestView.setText(request);
                            responseView.setText(response);
                            hide();
                        }
                    });

                }

                @Override
                public void onError(final String e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseView.setText(e);
                            hide();
                        }
                    });
                }
            })
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
