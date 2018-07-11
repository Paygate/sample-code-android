package com.appinlet.payhost.Api;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appinlet.payhost.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardPaymentActivity extends AppCompatActivity {

    @BindView(R.id.web)
    WebView web;

    static onFinishedTransaction callback;
    @BindView(R.id.progress)
    ContentLoadingProgressBar progress;
    private boolean isproceesedData = false;

    public static void start(Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, CardPaymentActivity.class);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, 9966);
    }

    public static void attachCallback(onFinishedTransaction c) {
        callback = c;
    }

    public interface onFinishedTransaction {
        void onFinish(HashMap<String, String> map);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_payment);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        final String url = b.getString("URL");
        final String return_url = b.getString("RETURN_URL");
        HashMap<String, String> fields = (HashMap<String, String>) b.getSerializable("DATA");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        for (String s :
                fields.keySet()) {
            builder.appendQueryParameter(s, fields.get(s));
        }
        final String finalPostData = builder.build().getEncodedQuery();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    web.setWebContentsDebuggingEnabled(true);
                }
                web.getSettings().setJavaScriptEnabled(true);
                web.getSettings().setDomStorageEnabled(true);
                web.addJavascriptInterface(new FormDataInterface(), "FORMOUT");
                web.setWebViewClient(new WebViewClient() {

                    private final String jsCode = "" + "function parseForm(form){" +
                            "var values='';" +
                            "for(var i=0 ; i< form.elements.length; i++){" +
                            "   values+=form.elements[i].name+'='+form.elements[i].value+'&'" +
                            "}" +
                            "var url=form.action;" +
                            "console.log('parse form fired');" +
                            "window.FORMOUT.processFormData(url,values);" +
                            "   }" +
                            "console.log(document.body.innerHTML);" +
                            "for(var i=0 ; i< document.forms.length ; i++){" +
                            "   parseForm(document.forms[i]);" +
                            "};";


                    @Override
                    public void onPageFinished(final WebView view, String url) {
                        Log.e("url", url);
                        if (url.toLowerCase().contains(return_url.toLowerCase())) {

                            view.loadUrl("javascript:(function() { " + jsCode + "})()");
                            view.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isproceesedData)
                                        callback.onFinish(null);
                                    finish();
                                }
                            }, 200);
                            //finish();
                            return;
                        }

                        progress.hide();

                        super.onPageFinished(view, url);

                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        progress.show();
                    }
                });
                web.postUrl(url, finalPostData.getBytes());
            }
        });
    }

    private class FormDataInterface {

        @JavascriptInterface
        public void processFormData(String url, String formData) {
            isproceesedData = true;
            HashMap<String, String> map = new HashMap<>();
            String[] values = formData.split("&");
            for (String pair : values) {
                String[] nameValue = pair.split("=");
                if (nameValue.length == 2) {
                    map.put(nameValue[0], nameValue[1]);
                }
            }
            finish();
            callback.onFinish(map);
            return;

        }
    }
}
