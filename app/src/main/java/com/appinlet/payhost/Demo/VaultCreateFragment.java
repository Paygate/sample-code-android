package com.appinlet.payhost.Demo;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appinlet.payhost.Api.PayGateResponseCallback;
import com.appinlet.payhost.Api.VaultCreateApi;
import com.appinlet.payhost.Model.Card;
import com.appinlet.payhost.Model.UserDefined;
import com.appinlet.payhost.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class VaultCreateFragment extends BaseFragment {

    public static VaultCreateFragment newInstance() {

        Bundle args = new Bundle();

        VaultCreateFragment fragment = new VaultCreateFragment();
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

    public VaultCreateFragment() {
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
        try {
            show();
            List<UserDefined> userDefinedList = new ArrayList<>();
            userDefinedList.add(UserDefined.builder().key("name").value("AndroidVaulr").build());
            VaultCreateApi.newBuilder()
                    .withContext(getActivity())
                    .withPayGateId(paygateid.getText().toString())
                    .withPassword(password.getText().toString())
                    .withCard(Card.builder()
                            .cardNumber(card.getText().toString())
                            .exp(exp.getText().toString())
                            .build())
                    .withUserDefinedList(userDefinedList)
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
