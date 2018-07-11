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

public class FollowUpApi {
    private Card card;
    private List<UserDefined> userDefinedList = new ArrayList<>();

    public static FollowUpApi getInstance() {
        return new FollowUpApi();
    }

    private Context context;
    private String payGateId, password, payRequestID, TransactionType, TransactionId, Amount;
    private PayGateResponseCallback followUpCallBack;
    private int REQUEST_TYPE = 0;

    public FollowUpApi withContext(Context context) {
        this.context = context;
        return this;
    }

    public FollowUpApi card(Card card) {
        this.card = card;
        return this;
    }

    public FollowUpApi account(String payGateID, String password) {
        this.payGateId = payGateID;
        this.password = password;
        return this;
    }

    public FollowUpApi withCallback(PayGateResponseCallback followUpCallBack) {
        this.followUpCallBack = followUpCallBack;
        return this;
    }

    public FollowUpApi doQueryRequest(String payGateRequestId) {
        this.payRequestID = payGateRequestId;
        REQUEST_TYPE = 1;
        return this;
    }

    public FollowUpApi doVoidRequest(String TransactionId, String TransactionType) {
        this.TransactionId = TransactionId;
        this.TransactionType = TransactionType;
        REQUEST_TYPE = 2;
        return this;
    }

    public FollowUpApi doSettlementRequest(String TransactionId) {
        this.TransactionId = TransactionId;
        REQUEST_TYPE = 3;
        return this;
    }

    public FollowUpApi doRefundRequest(String TransactionId, String amount) {
        this.TransactionId = TransactionId;
        this.Amount = amount;
        REQUEST_TYPE = 4;
        return this;
    }


    public void execute() throws Exception {
        if (this.context == null) {
            throw new Exception("Context is null");
        } else if (this.payGateId == null) {
            throw new Exception("PayGateId is required");
        } else if (this.password == null) {
            throw new Exception("Password is required");
        } else if (this.followUpCallBack == null) {
            throw new Exception("Please attach callback.");
        } else if (this.REQUEST_TYPE == 0) {
            throw new Exception("Please do at least one Action.");
        } else {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/xml");
            String data = "";
            switch (REQUEST_TYPE) {
                case 1:
                    data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body>" +
                            "<ns1:SingleFollowUpRequest>" +
                            "<ns1:QueryRequest>" +
                            "<ns1:Account>" +
                            "<ns1:PayGateId>" + payGateId + "</ns1:PayGateId>" +
                            "<ns1:Password>" + password + "</ns1:Password>" +
                            "</ns1:Account>" +
                            "<ns1:PayRequestId>" + payRequestID + "</ns1:PayRequestId>" +
                            "</ns1:QueryRequest>" +
                            "</ns1:SingleFollowUpRequest>" +
                            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
                    break;
                case 2:
                    data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pay=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body>" +
                            "<pay:SingleFollowUpRequest>" +
                            "<pay:VoidRequest>" +
                            "<pay:Account>" +
                            "<pay:PayGateId>" + payGateId + "</pay:PayGateId>" +
                            "<pay:Password>" + password + "</pay:Password>" +
                            "</pay:Account>" +
                            "<pay:TransactionId>" + TransactionId + "</pay:TransactionId>" +
                            "<pay:TransactionType>" + TransactionType + "</pay:TransactionType>" +
                            "</pay:VoidRequest>" +
                            "</pay:SingleFollowUpRequest>" +
                            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
                    break;
                case 3:
                    data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pay=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body>  " +
                            "<pay:SingleFollowUpRequest>" +
                            "<pay:SettlementRequest>" +
                            "<pay:Account>" +
                            "<pay:PayGateId>" + payGateId + "</pay:PayGateId>" +
                            "<pay:Password>" + password + "</pay:Password>" +
                            "</pay:Account>" +
                            "<pay:TransactionId>" + TransactionId + "</pay:TransactionId>" +
                            "</pay:SettlementRequest> " +
                            "</pay:SingleFollowUpRequest>" +
                            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
                    break;
                case 4:
                    data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:pay=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body>  " +
                            "<pay:SingleFollowUpRequest>" +
                            "<pay:RefundRequest>" +
                            "<pay:Account>" +
                            "<pay:PayGateId>" + payGateId + "</pay:PayGateId>" +
                            "<pay:Password>" + password + "</pay:Password>" +
                            "</pay:Account>" +
                            "<pay:TransactionId>" + TransactionId + "</pay:TransactionId>" +
                            "<pay:Amount>" + Amount + "</pay:Amount>" +
                            "</pay:RefundRequest> " +
                            "</pay:SingleFollowUpRequest>" +
                            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
                    break;
            }

            RequestBody body = RequestBody.create(mediaType, data);
            Request request = new Request.Builder()
                    .url("https://secure.paygate.co.za/PayHost/process.trans")
                    .post(body)
                    .addHeader("content-type", "application/xml")
                    .addHeader("cache-control", "no-cache")
                    .build();

            final String finalData = data;
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    followUpCallBack.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    Log.e("res", res);
                    followUpCallBack.onSuccess(finalData, res);
                }
            });
        }
    }

}
