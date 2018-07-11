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

import com.appinlet.payhost.Api.PayGateResponseCallback;
import com.appinlet.payhost.Api.TokenPaymentApi;
import com.appinlet.payhost.Model.Address;
import com.appinlet.payhost.Model.Customer;
import com.appinlet.payhost.Model.Order;
import com.appinlet.payhost.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TokenPaymentFragment extends BaseFragment {

    @BindView(R.id.h1)
    TextInputLayout h1;
    @BindView(R.id.h2)
    TextInputLayout h2;

    public static TokenPaymentFragment newInstance() {
        Bundle args = new Bundle();

        TokenPaymentFragment fragment = new TokenPaymentFragment();
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

    public TokenPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vault_create, container, false);
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
        exp.setText("VCO");
    }

    @OnClick(R.id.create)
    public void onViewClicked() {
        show();
        try {
            TokenPaymentApi.newBuilder()
                    .withContext(getActivity())
                    .withPayGateId(paygateid.getText().toString())
                    .withPassword(password.getText().toString())
                    .withToken(card.getText().toString())
                    .withTokenDetail(exp.getText().toString())
                    .withOrder(Order.builder()
                            .MerchantOrderId("token_order_123")
                            .Amount("6589")
                            .Currency("ZAR")
                            .TransactionDate("2018-07-01T18:30:00+02:00")
                            .customer(Customer.builder()
                                    .FirstName("PayGate")
                                    .LastName("Test")
                                    .Mobile("0878202020")
                                    .DateOfBirth("1991-10-13")
                                    .Email("itsupport@paygate.co.za")
                                    .address(Address.builder()
                                            .AddressLine1("240 Main Road")
                                            .City("Cape Town")
                                            .State("WC")
                                            .Country("ZAF")
                                            .build())
                                    .build())
                            .build())
                    .withPayGateResponseCallback(new PayGateResponseCallback() {
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
                    }).build().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
