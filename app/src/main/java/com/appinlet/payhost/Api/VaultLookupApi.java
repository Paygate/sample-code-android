package com.appinlet.payhost.Api;

import android.content.Context;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VaultLookupApi {
    private String vaultId;

    private Context context;
    private String payGateId, password;
    private PayGateResponseCallback payGateResponseCallback;

    private VaultLookupApi(Builder builder) {
        vaultId = builder.vaultId;
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
        } else if (this.vaultId == null) {
            throw new Exception("Vault ID is required");
        } else if (this.payGateResponseCallback == null) {
            throw new Exception("Please attach callback.");
        } else {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/xml");
            final String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body>" +
                    "<ns1:SingleVaultRequest>" +
                    "<ns1:LookUpVaultRequest>" +
                    "<ns1:Account>" +
                    "<ns1:PayGateId>" + payGateId + "</ns1:PayGateId>" +
                    "<ns1:Password>" + password + "</ns1:Password>" +
                    "</ns1:Account>" +
                    "<ns1:VaultId>" + vaultId + "</ns1:VaultId>" +
                    "</ns1:LookUpVaultRequest>" +
                    "</ns1:SingleVaultRequest>" +
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
                    payGateResponseCallback.onSuccess(data, response.body().string());
                }
            });
        }
    }

    /**
     * {@code VaultLookupApi} builder static inner class.
     */
    public static final class Builder {
        private String vaultId;
        private Context context;
        private String payGateId;
        private String password;
        private PayGateResponseCallback payGateResponseCallback;

        private Builder() {
        }

        /**
         * Sets the {@code vaultId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code vaultId} to set
         * @return a reference to this Builder
         */
        public Builder withVaultId(String val) {
            vaultId = val;
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
         * Returns a {@code VaultLookupApi} built from the parameters previously set.
         *
         * @return a {@code VaultLookupApi} built with parameters of this {@code VaultLookupApi.Builder}
         */
        public VaultLookupApi build() {
            return new VaultLookupApi(this);
        }
    }
}
