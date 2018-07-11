package com.appinlet.payhost.Api;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.appinlet.payhost.Model.Card;
import com.appinlet.payhost.Model.Customer;
import com.appinlet.payhost.Model.OrderItems;
import com.appinlet.payhost.Model.UserDefined;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SinglePaymentApi {
    private Card card;
    private Customer customer;
    private List<UserDefined> userDefinedList = new ArrayList<>();
    private List<OrderItems> orderItemsList = new ArrayList<>();

    private Activity context;
    private String payGateId, password, BudgetPeriod, NotifyUrl, ReturnUrl, MerchantOrderId, Currency, Amount;
    private PayGateResponseCallback payGateResponseCallback;

    private SinglePaymentApi(Builder builder) {
        card = builder.card;
        customer = builder.customer;
        userDefinedList = builder.userDefinedList;
        orderItemsList = builder.orderItemsList;
        context = builder.context;
        payGateId = builder.payGateId;
        password = builder.password;
        BudgetPeriod = builder.BudgetPeriod;
        NotifyUrl = builder.NotifyUrl;
        ReturnUrl = builder.ReturnUrl;
        MerchantOrderId = builder.MerchantOrderId;
        Currency = builder.Currency;
        Amount = builder.Amount;
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
            try {
                for (UserDefined u :
                        userDefinedList) {
                    ud.append("<ns1:UserDefinedFields>" +
                            "<ns1:key>" + u.getKey() + "</ns1:key>" +
                            "<ns1:value>" + u.getValue() + "</ns1:value>" +
                            "</ns1:UserDefinedFields>");
                }
            } catch (NullPointerException e) {
                //skip user defined fields
            }
            StringBuilder od = new StringBuilder();
            for (OrderItems o :
                    orderItemsList) {
                od.append("<ns1:OrderItems>" +
                        "<ns1:ProductCode>" + o.getProductCode() + "</ns1:ProductCode>" +
                        "<ns1:ProductDescription>" + o.getProductDescription() + "</ns1:ProductDescription>" +
                        "<ns1:ProductCategory>" + o.getProductCategory() + "</ns1:ProductCategory>" +
                        "<ns1:ProductRisk>" + o.getProductRisk() + "</ns1:ProductRisk>" +
                        "<ns1:OrderQuantity>" + o.getOrderQuantity() + "</ns1:OrderQuantity>" +
                        "<ns1:UnitPrice>" + o.getUnitPrice() + "</ns1:UnitPrice>" +
                        "<ns1:Currency>" + o.getCurrency() + "</ns1:Currency>" +
                        "</ns1:OrderItems>");
            }

            final String data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.paygate.co.za/PayHOST\"><SOAP-ENV:Body><ns1:SinglePaymentRequest>" +
                    "<ns1:CardPaymentRequest>" +
                    "<ns1:Account>" +
                    "<ns1:PayGateId>" + payGateId + "</ns1:PayGateId>" +
                    "<ns1:Password>" + password + "</ns1:Password>" +
                    "</ns1:Account>" +
                    "<ns1:Customer>" +

                    "<ns1:Title>" + customer.getTitle() + "</ns1:Title>" +
                    "<ns1:FirstName>" + customer.getFirstName() + "</ns1:FirstName>" +

                    "<ns1:LastName>" + customer.getLastName() + "</ns1:LastName>" +

                    "<ns1:Telephone>" + customer.getTelephone() + "</ns1:Telephone>" +

                    "<ns1:Mobile>" + customer.getMobile() + "</ns1:Mobile>" +

                    "<ns1:Email>" + customer.getEmail() + "</ns1:Email>" +
                    "</ns1:Customer>" +
                    "<ns1:CardNumber>" + card.getCardNumber() + "</ns1:CardNumber>" +
                    "<ns1:CardExpiryDate>" + card.getExp() + "</ns1:CardExpiryDate>" +

                    "<ns1:CVV>987</ns1:CVV>" +
                    "<ns1:BudgetPeriod>" + BudgetPeriod + "</ns1:BudgetPeriod>" +

                    "<ns1:Redirect>" +
                    "<ns1:NotifyUrl>" + NotifyUrl + "</ns1:NotifyUrl>" +
                    "<ns1:ReturnUrl>" + ReturnUrl + "</ns1:ReturnUrl>" +

                    "</ns1:Redirect>" +
                    "<ns1:Order>" +
                    "<ns1:MerchantOrderId>" + MerchantOrderId + "</ns1:MerchantOrderId>" +
                    "<ns1:Currency>" + Currency + "</ns1:Currency>" +
                    "<ns1:Amount>" + Amount + "</ns1:Amount>" +
                    od.toString() +
                    "</ns1:Order>" +
                    "</ns1:CardPaymentRequest>" +
                    "</ns1:SinglePaymentRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>";

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
                    payGateResponseCallback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = null;
                    try {
                        dBuilder = dbFactory.newDocumentBuilder();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                    Document doc = null;
                    try {
                        doc = dBuilder.parse(new ByteArrayInputStream(res.getBytes()));
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }

                    Element element = doc.getDocumentElement();
                    element.normalize();
                    Node cardRes = element.getElementsByTagName("ns2:CardPaymentResponse").item(0);
                    String status = cardRes.getChildNodes().item(0).getChildNodes().item(0).getTextContent();
                    if (status.equalsIgnoreCase("ThreeDSecureRedirectRequired")) {
                        Node redirect = cardRes.getChildNodes().item(1);
                        Log.e("redirect", redirect.getChildNodes().item(0).getTextContent());
                        NodeList c = element.getElementsByTagName("ns2:UrlParams");
                        HashMap<String, String> hashMap = new HashMap<>(c.getLength());
                        for (int i = 0; i < c.getLength(); i++) {
                            hashMap.put(c.item(i).getChildNodes().item(0).getTextContent(), c.item(i).getChildNodes().item(1).getTextContent());
                        }

                        Bundle b = new Bundle();
                        b.putString("URL", redirect.getChildNodes().item(0).getTextContent());
                        b.putSerializable("DATA", hashMap);
                        b.putString("RETURN_URL", ReturnUrl);
                        CardPaymentActivity.start(context, b);
                        CardPaymentActivity.attachCallback(new CardPaymentActivity.onFinishedTransaction() {
                            @Override
                            public void onFinish(HashMap<String, String> map) {

                                String csString = payGateId.concat(map.get("PAY_REQUEST_ID")).concat(map.get("TRANSACTION_STATUS")).concat(MerchantOrderId).concat(password);
                                String checksum = new String(Hex.encodeHex(DigestUtils.md5(csString)));
                                StringBuilder r = new StringBuilder();
                                for (String s :
                                        map.keySet()) {
                                    r.append(s).append(" : ").append(map.get(s)).append("\n");
                                }
                                if (checksum.equalsIgnoreCase(map.get("CHECKSUM"))) {
                                    payGateResponseCallback.onSuccess(data, r.toString());
                                } else {
                                    payGateResponseCallback.onSuccess(data, "0");
                                }

                            }
                        });

                    } else {
                        payGateResponseCallback.onSuccess(data, res);
                    }
                }
            });
        }
    }


    /**
     * {@code SinglePaymentApi} builder static inner class.
     */
    public static final class Builder {
        private Card card;
        private Customer customer;
        private List<UserDefined> userDefinedList;
        private List<OrderItems> orderItemsList;
        private Activity context;
        private String payGateId;
        private String password;
        private String BudgetPeriod;
        private String NotifyUrl;
        private String ReturnUrl;
        private String MerchantOrderId;
        private String Currency;
        private String Amount;
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
         * Sets the {@code customer} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code customer} to set
         * @return a reference to this Builder
         */
        public Builder withCustomer(Customer val) {
            customer = val;
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
         * Sets the {@code orderItemsList} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code orderItemsList} to set
         * @return a reference to this Builder
         */
        public Builder withOrderItemsList(List<OrderItems> val) {
            orderItemsList = val;
            return this;
        }

        /**
         * Sets the {@code context} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code context} to set
         * @return a reference to this Builder
         */
        public Builder withContext(Activity val) {
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
         * Sets the {@code BudgetPeriod} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code BudgetPeriod} to set
         * @return a reference to this Builder
         */
        public Builder withBudgetPeriod(String val) {
            BudgetPeriod = val;
            return this;
        }

        /**
         * Sets the {@code NotifyUrl} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code NotifyUrl} to set
         * @return a reference to this Builder
         */
        public Builder withNotifyUrl(String val) {
            NotifyUrl = val;
            return this;
        }

        /**
         * Sets the {@code ReturnUrl} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code ReturnUrl} to set
         * @return a reference to this Builder
         */
        public Builder withReturnUrl(String val) {
            ReturnUrl = val;
            return this;
        }

        /**
         * Sets the {@code MerchantOrderId} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code MerchantOrderId} to set
         * @return a reference to this Builder
         */
        public Builder withMerchantOrderId(String val) {
            MerchantOrderId = val;
            return this;
        }

        /**
         * Sets the {@code Currency} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code Currency} to set
         * @return a reference to this Builder
         */
        public Builder withCurrency(String val) {
            Currency = val;
            return this;
        }

        /**
         * Sets the {@code Amount} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code Amount} to set
         * @return a reference to this Builder
         */
        public Builder withAmount(String val) {
            Amount = val;
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
         * Returns a {@code SinglePaymentApi} built from the parameters previously set.
         *
         * @return a {@code SinglePaymentApi} built with parameters of this {@code SinglePaymentApi.Builder}
         */
        public SinglePaymentApi build() {
            return new SinglePaymentApi(this);
        }
    }
}
