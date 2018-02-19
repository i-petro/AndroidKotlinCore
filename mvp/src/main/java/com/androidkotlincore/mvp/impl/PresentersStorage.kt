package com.androidkotlincore.mvp.impl

import com.androidkotlincore.mvp.MVPPresenter
import com.androidkotlincore.mvp.MVPView
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by Peter on 23.01.18.
 */
/**
 * Storage for presenters
 * */
interface PresentersStorage<TPresenter : MVPPresenter<TPresenter, TView>, TView : MVPView<TView, TPresenter>> {

    /**
     * Provides unique presenter id
     *
     * @return presenter id
     * */
    fun generateId(): Long

    /**
     * Removes presenter from storage
     *
     * @param view - [MVPView], first key
     * @param id - unique id, second key
     * */
    fun remove(view: MVPView<TView, TPresenter>, id: Long)

    /**
     * Returns presenter from storage
     *
     * @param view - [MVPView], first key
     * @param id - unique id, second key
     * @return [TPresenter]
     * */
    fun get(view: MVPView<TView, TPresenter>, id: Long): TPresenter?

    /**
     * Puts presenter into storage
     *
     * @param view - [MVPView], first key
     * @param id - unique id, second key
     * @return [TPresenter]
     * */
    fun put(view: MVPView<TView, TPresenter>, id: Long, presenter: TPresenter): TPresenter
}

/**
 * Default presenter's storage implementation
 * Saves presenters during View re-creation
 */
class PresentersStorageImpl<TPresenter, TView> : PresentersStorage<TPresenter, TView>

        where TPresenter : MVPPresenter<TPresenter, TView>,
              TView : MVPView<TView, TPresenter> {

    override fun generateId(): Long = NEXT_ID.incrementAndGet()

    override fun remove(view: MVPView<TView, TPresenter>, id: Long) {
        ALL_PRESENTERS.remove(PresenterHash(view::class.java, id))
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(view: MVPView<TView, TPresenter>, id: Long): TPresenter? =
            ALL_PRESENTERS[PresenterHash(view::class.java, id)] as TPresenter?

    override fun put(view: MVPView<TView, TPresenter>, id: Long, presenter: TPresenter): TPresenter {
        ALL_PRESENTERS[PresenterHash(view::class.java, id)] = presenter
        return presenter
    }

    private data class PresenterHash(val viewClass: Class<*>, val id: Long)

    private companion object {
        private val NEXT_ID = AtomicLong(80_000)
        private val ALL_PRESENTERS = HashMap<PresenterHash, MVPPresenter<*, *>>()
    }
}