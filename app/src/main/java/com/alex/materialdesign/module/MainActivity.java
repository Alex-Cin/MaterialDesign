package com.alex.materialdesign.module;

import android.os.Bundle;
import android.view.View;

import com.alex.materialdesign.R;
import com.alex.materialdesign.library.MDActivity;
import com.alex.materialdesign.module.coorwraprefresh.CWRActivity;
import com.alex.materialdesign.module.fadeactionbutton.FadeActionActivity;
import com.alex.materialdesign.module.floatexpandsearch.FloatExpandSearchActivity;
import com.alex.materialdesign.module.scrollportrait.ScrollPortraitActivity;

import org.alex.helper.IntentHelper;

public class MainActivity extends MDActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onCreateData(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (R.id.bt_1 == id) {
            IntentHelper.getInstance().startActivity(CWRActivity.class);
        } else if (R.id.bt_2 == id) {
            IntentHelper.getInstance().startActivity(FadeActionActivity.class);
        } else if (R.id.bt_3 == id) {
            IntentHelper.getInstance().startActivity(ScrollPortraitActivity.class);
        } else if (R.id.bt_4 == id) {
            IntentHelper.getInstance().startActivity(FloatExpandSearchActivity.class);
        }

    }
}
