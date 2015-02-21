package ru.noties.performance_test.ui.configure;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class BaseFragment extends Fragment {

    protected <V> V findView(@NonNull View view, @IdRes int viewId) {
        //noinspection unchecked
        return (V) view.findViewById(viewId);
    }
}
