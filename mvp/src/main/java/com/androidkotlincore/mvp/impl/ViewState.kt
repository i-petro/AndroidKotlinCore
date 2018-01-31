package com.androidkotlincore.mvp.impl

import com.androidkotlincore.mvp.MVPView

internal class ViewState<T : Any?, in TView : MVPView<*, *>>(
        val id: Long,
        val data: T,
        val applier: TView.(data: T) -> Unit,
        val tag: String? = null) {

    companion object {
        val comparator: Comparator<ViewState<*, *>> = Comparator { o1, o2 -> o1.id.compareTo(o2.id) }
    }

    override fun equals(other: Any?): Boolean = other is ViewState<*, *> && other.id == id
    override fun hashCode(): Int = id.hashCode()
}