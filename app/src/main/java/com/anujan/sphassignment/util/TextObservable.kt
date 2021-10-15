package com.anujan.sphassignment.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class TextObservable : BaseObservable() {
    private var stored: String? = null

    var text: String?
        @Bindable get() = stored
        set(value) {
            if (stored != value) {
                stored = value
                notifyPropertyChanged(BR.text)
            }
        }
}