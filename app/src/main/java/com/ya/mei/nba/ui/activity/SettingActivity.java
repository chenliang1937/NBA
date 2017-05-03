package com.ya.mei.nba.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.ya.mei.nba.R;
import com.ya.mei.nba.app.App;
import com.ya.mei.nba.event.Event;
import com.ya.mei.nba.ui.widget.RevealBackgroundView;
import com.ya.mei.nba.utils.AppUtils;
import com.ya.mei.nba.utils.DataClearManager;
import com.ya.mei.nba.utils.PreferenceUtils;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/7.
 */
public class SettingActivity extends SwipeBackActivity implements RevealBackgroundView.OnStateChangeListener {

    @Bind(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    @Bind(R.id.setting_layout)
    View settingView;

    private SettingFragment settingFragment;

    public static void navigateFrom(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews() {
        super.initViews();
        settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction().replace(R.id.setting_container, settingFragment).commit();
        setTitle();
        setupRevealBackground();
    }

    @Override
    void setTitle() {
        toolbar.setTitle(R.string.setting);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state){
            settingView.setVisibility(View.VISIBLE);
        }else {
            settingView.setVisibility(View.INVISIBLE);
        }
    }

    private void setupRevealBackground(){
        revealBackgroundView.setOnStateChangeListener(this);
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        final int[] startingLocation = {screenWidth, 0};
        revealBackgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                revealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                revealBackgroundView.startFromLocation(startingLocation);
                return true;
            }
        });
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener{

        Preference fontSizePre;
        Preference clearCachePre;
        Preference updatePre;
        Resources resources;
        View view;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = super.onCreateView(inflater, container, savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
            resources = getResources();
            fontSizePre = findPreference(resources.getString(R.string.pre_fontsize_key));
            clearCachePre = findPreference(resources.getString(R.string.clear_cache));
            updatePre = findPreference(resources.getString(R.string.version_update));
            String font_size = PreferenceUtils.getPrefString(getActivity(), getString(R.string.pre_fontsize_key), "16");
            fontSizePre.setSummary(showFontSize(font_size));
            fontSizePre.setOnPreferenceChangeListener(this);
            clearCachePre.setOnPreferenceClickListener(this);
            updatePre.setSummary("V" + AppUtils.getVersionName(view.getContext()));
            updateCache();
            return view;
        }

        private String showFontSize(String value){
            switch (value){
                case "16" : value = "小号字体"; break;
                case "18" : value = "中号字体"; break;
                case "20" : value = "大号字体"; break;
                default: break;
            }
            return value;
        }

        private void updateCache(){
            clearCachePre.setSummary(DataClearManager.getApplicationDataSize(App.getContext()));
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.equals(clearCachePre)){
                DataClearManager.cleanApplicationData(App.getContext());
                updateCache();
                AppUtils.showSnackBar(view, R.string.data_cleared);
            }
            if (preference.equals(fontSizePre)){
                fontSizePre.setDefaultValue(PreferenceUtils.getPrefString(getActivity(), "font_size", "16"));
            }
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.equals(fontSizePre)){
                String prefsValue = newValue.toString();
                fontSizePre.setSummary(showFontSize(prefsValue));
            }
            return true;
        }
    }

    public void onEventMainThread(Event event){

    }

}
