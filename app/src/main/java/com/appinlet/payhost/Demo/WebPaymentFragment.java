package com.appinlet.payhost.Demo;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.appinlet.payhost.Api.PayGateResponseCallback;
import com.appinlet.payhost.Api.SinglePaymentApi;
import com.appinlet.payhost.Api.WebPaymentApi;
import com.appinlet.payhost.Model.Address;
import com.appinlet.payhost.Model.BillingDetails;
import com.appinlet.payhost.Model.Card;
import com.appinlet.payhost.Model.Customer;
import com.appinlet.payhost.Model.Language;
import com.appinlet.payhost.Model.Order;
import com.appinlet.payhost.Model.OrderItems;
import com.appinlet.payhost.Model.Redirect;
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
public class WebPaymentFragment extends BaseFragment {

    public static WebPaymentFragment newInstance() {
        Bundle args = new Bundle();

        WebPaymentFragment fragment = new WebPaymentFragment();
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

    public WebPaymentFragment() {
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
            Address address = Address.builder()
                    .AddressLine1("240 Main Road")
                    .City("Cape Town")
                    .Country("ZAF")
                    .State("WC")
                    .build();
            Customer customer = Customer.builder()
                    .address(address)
                    .Email("itsupport@paygate.co.za")
                    .Fax("0878202020")
                    .FirstName("PayGate")
                    .MiddleName("")
                    .DateOfBirth("1991-10-13")
                    .LastName("Test")
                    .Mobile("0878202020")
                    .Telephone("0878202020")
                    .Title("Mr")
                    .build();

            WebPaymentApi.newBuilder()
                    .withContext(getActivity())
                    .withCard(Card.builder().cardNumber("4000000000000002").exp("122019").build())
                    .withCustomer(customer)
                    .withPayGateId("10011072130")
                    .withPassword("test")
                    .withOrder(Order.builder()
                            .MerchantOrderId("order_123456369")
                            .TransactionDate("2018-07-01T18:30:00+02:00")
                            .Currency("ZAR")
                            .Amount("100")
                            .Locale(Language.builder()
                                    .language("en-us").build())
                            .address(address)
                            .customer(customer)
                            .BillingDetailsObject(BillingDetails.builder()
                                    .AddressObject(address)
                                    .CustomerObject(customer)
                                    .build())
                            .build())
                    .withRedirect(Redirect.builder()
                            .NotifyUrl("http://NOTIFY")
                            .ReturnUrl("http://redirect/now")
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
