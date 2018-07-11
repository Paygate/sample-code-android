package com.appinlet.payhost.Api;

import android.content.Context;
import android.util.Log;

import com.appinlet.payhost.Model.Order;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenPaymentApi {
    private Context context;
    private String payGateId, password, Token, TokenDetail, Vault;
    private Order order;
    private PayGateResponseCallback payGateResponseCallback;

    private TokenPaymentApi(Builder builder) {
        context = builder.context;
        payGateId = builder.payGateId;
        password = builder.password;
        Token = builder.Token;
        TokenDetail = builder.TokenDetail;
        Vault = builder.Vault;
        order = builder.order;
        payGateResponseCallback = builder.payGateResponseCallback;
    }

    public void execute() throws Exception {
        if (this.context == null) {
            throw new Exception("Context is null");
        } else if (this.payGateId == null) {
            throw new Exception("PayGateId is required");
        } else if (this.password == null) {
            throw new Exception("Password is required");
        } else if (this.Token == null) {
            throw new Exception("Please prvide token");
        } else if (this.TokenDetail == null) {
            throw new Exception("Please provide TokenDetails.");
        } else {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/xml");
            final String data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body>  " +
                    "<ns1:SinglePaymentRequest>" +
                    "<ns1:TokenPaymentRequest>" +
                    "<ns1:Account>" +
                    "<ns1:PayGateId>" + payGateId + "</ns1:PayGateId>" +
                    "<ns1:Password>" + password + "</ns1:Password>" +
                    "</ns1:Account>" +
                    order.getCustomer().toxml() +
                    "<ns1:Token>" + Token + "</ns1:Token>" +
                    "<ns1:TokenDetail>" + TokenDetail + "</ns1:TokenDetail>" +
                    order.toXml() +
                    "</ns1:TokenPaymentRequest>" +
                    "</ns1:SinglePaymentRequest>" +
                    "</SOAP-ENV:Body></SOAP-ENV:Envelope>";

            RequestBody body = RequestBody.create(mediaType, data);
            Request request = new Request.Builder()
                    .url("https://secure.paygate.co.za/PayHost/process.trans")
                    .post(body)
                    .addHeader("content-type", "application/xml")
                    .addHeader("cache-control", "no-cache")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    payGateResponseCallback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    Log.e("res", res);
                    payGateResponseCallback.onSuccess(data, res);
                }
            });
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * {@code TokenPaymentApi} builder static inner class.
     */
    public static final class Builder {
        private Context context;
        private String payGateId;
        private String password;
        private String Token;
        private String TokenDetail;
        private String Vault;
        private Order order;
        private PayGateResponseCallback payGateResponseCallback;

        private Builder() {
        }

        /**
         * Sets the {@code context} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code context} to set
         * @return a reference to this Builder
         */
        public Builder withContext(Context val) {
            context = val;
            return this;
        }

        /**
         * Sets the {@code payGateId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code payGateId} to set
         * @return a reference to this Builder
         */
        public Builder withPayGateId(String val) {
            payGateId = val;
            return this;
        }

        /**
         * Sets the {@code password} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code password} to set
         * @return a reference to this Builder
         */
        public Builder withPassword(String val) {
            password = val;
            return this;
        }

        /**
         * Sets the {@code Token} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code Token} to set
         * @return a reference to this Builder
         */
        public Builder withToken(String val) {
            Token = val;
            return this;
        }

        /**
         * Sets the {@code TokenDetail} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code TokenDetail} to set
         * @return a reference to this Builder
         */
        public Builder withTokenDetail(String val) {
            TokenDetail = val;
            return this;
        }

        /**
         * Sets the {@code Vault} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code Vault} to set
         * @return a reference to this Builder
         */
        public Builder withVault(String val) {
            Vault = val;
            return this;
        }

        /**
         * Sets the {@code order} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code order} to set
         * @return a reference to this Builder
         */
        public Builder withOrder(Order val) {
            order = val;
            return this;
        }

        /**
         * Sets the {@code payGateResponseCallback} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code payGateResponseCallback} to set
         * @return a reference to this Builder
         */
        public Builder withPayGateResponseCallback(PayGateResponseCallback val) {
            payGateResponseCallback = val;
            return this;
        }

        /**
         * Returns a {@code TokenPaymentApi} built from the parameters previously set.
         *
         * @return a {@code TokenPaymentApi} built with parameters of this {@code TokenPaymentApi.Builder}
         */
        public TokenPaymentApi build() {
            return new TokenPaymentApi(this);
        }
    }
}
