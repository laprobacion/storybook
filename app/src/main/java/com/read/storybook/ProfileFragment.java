package com.read.storybook;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.read.storybook.util.Storage;

public class ProfileFragment extends Fragment{

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        ((TextView)myView.findViewById(R.id.usernameTxt)).setText(Storage.load(getActivity().getApplicationContext()));
        return myView;
    }
}
