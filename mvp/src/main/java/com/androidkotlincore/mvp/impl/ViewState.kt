package com.androidkotlincore.mvp.impl

import com.androidkotlincore.mvp.MVPView

/**
 * Created by Peter on 07.01.2017.
 */

/**
 * Represents a view state where
 * @property id - view state unique identifier
 * @property data - data, which will be passed to the applier
 * @property applier - function, which will be executed on [MVPView]
 * @property tag - some state name for debugging
 */
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