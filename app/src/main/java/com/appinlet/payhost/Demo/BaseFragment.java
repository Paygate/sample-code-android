package com.appinlet.payhost.Demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.kaopiz.kprogresshud.KProgressHUD;

public class BaseFragment extends Fragment {

    KProgressHUD kProgressHUD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kProgressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    public void show() {
        if (kProgressHUD != null)
            kProgressHUD.show();
    }

    public void hide() {
        if (kProgressHUD != null)
            kProgressHUD.dismiss();
    }

}
