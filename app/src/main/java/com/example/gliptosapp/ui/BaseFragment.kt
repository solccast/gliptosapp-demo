package com.example.gliptosapp.ui


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gliptosapp.ui.settings.appearance.applyFontScale

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        (view as? ViewGroup)
            ?.applyFontScale()
    }
}