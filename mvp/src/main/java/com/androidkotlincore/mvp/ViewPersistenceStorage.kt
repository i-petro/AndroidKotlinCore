package com.androidkotlincore.mvp

import android.os.Bundle

/**
 * Created by Peter on 22.12.17.
 */
/**
 * It is a repository, the contents of which must be preserved when the View(Activity, Fragment) is re-created
 * @property args - bundle with key-data pairs
 */
interface ViewPersistenceStorage {
    val args: Bundle
}