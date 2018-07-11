package com.appinlet.payhost.Api;

public interface PayGateResponseCallback {
    public void onSuccess(String request, String response);

    public void onError(String e);
}
