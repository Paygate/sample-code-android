package com.appinlet.payhost.Api;

import android.content.Context;
import android.util.Log;

import com.appinlet.payhost.Model.Card;
import com.appinlet.payhost.Model.UserDefined;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VaultCreateApi {
    private Card card;
    private List<UserDefined> userDefinedList;

    private Context context;
    private String payGateId, password;
    private PayGateResponseCallback payGateResponseCallback;

    private VaultCreateApi(Builder builder) {
        card = builder.card;
        userDefinedList = builder.userDefinedList;
        context = builder.context;
        payGateId = builder.payGateId;
        password = builder.password;
        payGateResponseCallback = builder.payGateResponseCallback;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public void execute() throws Exception {
        if (this.context == null) {
            throw new Exception("Context is null");
        } else if (this.payGateId == null) {
            throw new Exception("PayGateId is required");
        } else if (this.password == null) {
            throw new Exception("Password is required");
        } else if (this.payGateResponseCallback == null) {
            throw new Exception("Please attach callback.");
        } else {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/xml");
            StringBuilder ud = new StringBuilder();
            for (UserDefined u :
                    userDefinedList) {
                ud.append("<ns1:UserDefinedFields>" +
                        "<ns1:key>" + u.getKey() + "</ns1:key>" +
                        "<ns1:value>" + u.getValue() + "</ns1:value>" +
                        "</ns1:UserDefinedFields>");
            }
            Log.e("UD", ud.toString());

            final String data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body><ns1:SingleVaultRequest>\n" +
                    "    <ns1:CardVaultRequest>\n" +
                    "        <ns1:Account>\n" +
                    "            <ns1:PayGateId>" + payGateId + "</ns1:PayGateId>\n" +
                    "            <ns1:Password>" + password + "</ns1:Password>\n" +
                    "        </ns1:Account>\n" +
                    "        <ns1:CardNumber>" + card.getCardNumber() + "</ns1:CardNumber>\n" +
                    "        <ns1:CardExpiryDate>" + card.getExp() + "</ns1:CardExpiryDate>\n" +
                    ud.toString() +
                    "    </ns1:CardVaultRequest>\n" +
                    "</ns1:SingleVaultRequest>\n" +
                    "</SOAP-ENV:Body></SOAP-ENV:Envelope>\n";
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
                    payGateResponseCallback.onSuccess(data, response.body().string());
                }
            });
        }
    }

    /**
     * {@code VaultCreateApi} builder static inner class.
     */
    public static final class Builder {
        private Card card;
        private List<UserDefined> userDefinedList;
        private Context context;
        private String payGateId;
        private String password;
        private PayGateResponseCallback payGateResponseCallback;

        private Builder() {
        }

        /**
         * Sets the {@code card} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code card} to set
         * @return a reference to this Builder
         */
        public Builder withCard(Card val) {
            card = val;
            return this;
        }

        /**
         * Sets the {@code userDefinedList} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code userDefinedList} to set
         * @return a reference to this Builder
         */
        public Builder withUserDefinedList(List<UserDefined> val) {
            userDefinedList = val;
            return this;
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
         * Returns a {@code VaultCreateApi} built from the parameters previously set.
         *
         * @return a {@code VaultCreateApi} built with parameters of this {@code VaultCreateApi.Builder}
         */
        public VaultCreateApi build() {
            return new VaultCreateApi(this);
        }
    }
}
