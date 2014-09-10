package com.thehoick.runnerbecomes;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by adam on 9/4/14.
 * Hold Settings and stuff.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}