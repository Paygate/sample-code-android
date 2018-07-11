package com.appinlet.payhost.Api;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.appinlet.payhost.Model.Card;
import com.appinlet.payhost.Model.Customer;
import com.appinlet.payhost.Model.Order;
import com.appinlet.payhost.Model.PaymentType;
import com.appinlet.payhost.Model.Redirect;
import com.appinlet.payhost.Model.Risk;
import com.appinlet.payhost.Model.UserDefined;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.w3c.dom.DOMException;
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


public class WebPaymentApi {

    private Card card;
    private Customer customer;
    private Redirect redirect;
    private Order order;
    private Risk risk;
    private PaymentType paymentType;
    private String payGateId, password;
    private List<UserDefined> userDefinedList = new ArrayList<>();
    private Activity context;
    private PayGateResponseCallback payGateResponseCallback;

    private WebPaymentApi(Builder builder) {
        card = builder.card;
        customer = builder.customer;
        redirect = builder.redirect;
        order = builder.order;
        risk = builder.risk;
        paymentType = builder.paymentType;
        payGateId = builder.payGateId;
        password = builder.password;
        userDefinedList = builder.userDefinedList;
        context = builder.context;
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
                e.printStackTrace();
                // skip the user fields
            }


            final String data = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.paygate.co.za/PayHOST\">" +
                    "   <SOAP-ENV:Body>" +
                    "      <ns1:SinglePaymentRequest>" +
                    "         <ns1:WebPaymentRequest>" +
                    "            <!-- Account Details -->" +
                    "            <ns1:Account>" +
                    "               <ns1:PayGateId>" + payGateId + "</ns1:PayGateId>" +
                    "               <ns1:Password>" + password + "</ns1:Password>" +
                    "            </ns1:Account>" +
                    "            <!-- Customer Details -->" +
                    (customer == null ? "" : customer.toxml()) +
                    "            <!-- Payment Type Details -->" +
                    (paymentType == null ? "" : paymentType.toXml()) +
                    "            <!-- Redirect Details -->" +
                    (redirect == null ? "" : redirect.toXml()) +
                    "            <!-- Order Details -->" +
                    (order == null ? "" : order.toXml()) +
                    "            <!-- Risk Details -->" +
                    (risk == null ? "" : risk.toXml()) +
                    "            <!-- User Fields -->" +
                    "         </ns1:WebPaymentRequest>" +
                    "      </ns1:SinglePaymentRequest>" +
                    "   </SOAP-ENV:Body>" +
                    "</SOAP-ENV:Envelope>";

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
                    final String res = response.body().string();
                    Log.e("RES", res);
                    try {
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
                        Node cardRes = element.getElementsByTagName("ns2:WebPaymentResponse").item(0);
                        String status = cardRes.getChildNodes().item(0).getChildNodes().item(0).getTextContent();
                        if (status.equalsIgnoreCase("WebRedirectRequired")) {
                            Node r = cardRes.getChildNodes().item(1);
                            Log.e("redirect", r.getChildNodes().item(0).getTextContent());
                            Node t = r.getChildNodes().item(1);

                            NodeList c = element.getElementsByTagName("ns2:UrlParams");
                            HashMap<String, String> hashMap = new HashMap<>(c.getLength());
                            for (int i = 0; i < c.getLength(); i++) {
                                hashMap.put(c.item(i).getChildNodes().item(0).getTextContent(), c.item(i).getChildNodes().item(1).getTextContent());
                            }

                            Bundle b = new Bundle();
                            b.putString("URL", r.getChildNodes().item(0).getTextContent());
                            b.putSerializable("DATA", hashMap);
                            b.putString("RETURN_URL", redirect.getReturnUrl());
                            CardPaymentActivity.start(context, b);
                            CardPaymentActivity.attachCallback(new CardPaymentActivity.onFinishedTransaction() {
                                @Override
                                public void onFinish(HashMap<String, String> map) {
                                    if (map == null) {
                                        payGateResponseCallback.onSuccess(data, res);
                                    } else {
                                        String csString = payGateId.concat(map.get("PAY_REQUEST_ID")).concat(map.get("TRANSACTION_STATUS")).concat(order.getMerchantOrderId()).concat(password);
                                        String checksum = new String(Hex.encodeHex(DigestUtils.md5(csString)));
                                        if (checksum.equalsIgnoreCase(map.get("CHECKSUM"))) {
                                            payGateResponseCallback.onSuccess(data, "1");
                                        } else {
                                            payGateResponseCallback.onSuccess(data, "0");
                                        }
                                    }
                                }
                            });

                        } else {
                            payGateResponseCallback.onSuccess(data, res);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DOMException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    /**
     * {@code WebPaymentApi} builder static inner class.
     */
    public static final class Builder {
        private Card card;
        private Customer customer;
        private Redirect redirect;
        private Order order;
        private Risk risk;
        private PaymentType paymentType;
        private String payGateId;
        private String password;
        private List<UserDefined> userDefinedList;
        private Activity context;
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
         * Sets the {@code redirect} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code redirect} to set
         * @return a reference to this Builder
         */
        public Builder withRedirect(Redirect val) {
            redirect = val;
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
         * Sets the {@code risk} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code risk} to set
         * @return a reference to this Builder
         */
        public Builder withRisk(Risk val) {
            risk = val;
            return this;
        }

        /**
         * Sets the {@code paymentType} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code paymentType} to set
         * @return a reference to this Builder
         */
        public Builder withPaymentType(PaymentType val) {
            paymentType = val;
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
        public Builder withContext(Activity val) {
            context = val;
            return this;
        }

        /**
         * Sets the {@code payGateResponseCallback} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code payGateResponseCallback} to set
         * @return a reference to this Builder
         */
        public WebPaymentApi.Builder withPayGateResponseCallback(PayGateResponseCallback val) {
            payGateResponseCallback = val;
            return this;
        }

        /**
         * Returns a {@code WebPaymentApi} built from the parameters previously set.
         *
         * @return a {@code WebPaymentApi} built with parameters of this {@code WebPaymentApi.Builder}
         */
        public WebPaymentApi build() {
            return new WebPaymentApi(this);
        }
    }
}
