package com.appinlet.payhost.Demo;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.appinlet.payhost.Api.PayGateResponseCallback;
import com.appinlet.payhost.Api.SinglePaymentApi;
import com.appinlet.payhost.Model.Card;
import com.appinlet.payhost.Model.Customer;
import com.appinlet.payhost.Model.OrderItems;
import com.appinlet.payhost.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardPaymentFragment extends BaseFragment {

    public static CardPaymentFragment newInstance() {

        Bundle args = new Bundle();

        CardPaymentFragment fragment = new CardPaymentFragment();
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

    public CardPaymentFragment() {
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

    @OnClick(R.id.create)
    public void onViewClicked() {
        show();
        try {
            List<OrderItems> orderItemsList = new ArrayList<>();
            orderItemsList.add(OrderItems.builder()
                    .OrderQuantity("2")
                    .Currency("ZAR")
                    .ProductCategory("XYZ")
                    .ProductCode("1254987")
                    .ProductDescription("Any")
                    .ProductRisk("XX")
                    .UnitPrice("100").build());
            SinglePaymentApi.newBuilder()
                    .withContext(getActivity())
                    .withPayGateId("10011072130")
                    .withPassword("test")
                    .withCard(Card.builder().cardNumber("4000000000000002").exp("122019").build())
                    .withCustomer(Customer.builder()
                            .FirstName("PayGate")
                            .LastName("Test")
                            .Email("itsupport@paygate.co.za")
                            .Mobile("0878202020")
                            .Title("Mr")
                            .build())
                    .withAmount("100")
                    .withBudgetPeriod("0")
                    .withCurrency("ZAR")
                    .withMerchantOrderId("MY_CUSTOM_OD")
                    .withNotifyUrl("http://LOCALHOST/NOTIFY")
                    .withReturnUrl("http://LOCALHOST/RETURN")
                    .withOrderItemsList(orderItemsList)
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
                    }).build()
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
