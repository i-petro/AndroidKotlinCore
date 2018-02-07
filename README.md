# AndroidKotlinCore [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="images/label-android.svg" height="19">

## MVP implementation
### Features:
* saving _Presenter_'s instance during _screen rotation_ ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Presenter's-lifecycle)***
* _don't keep activities_ mode support ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Presenter's-lifecycle)***
* _onActivityResult_ events handling ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Handling-OnActivityResult-events)***
* _permissions_ management ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Permissions-management)***
* _lifecycle_ events handling ***[(See wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Handling-View-Lifecycle-events)***
* saving and restoring _View_ state without _onSaveInstanceState()_ and _Bundle_ ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Saving-and-restoring-View-state)***
* Kotlin _coroutines_ support ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Kotlin-Coroutines-extensions)***
* _RxJava 1, 2_ support ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/RxJava-1,-2-extensions)***
### Sample:
See ***[project sample](/sample)*** and ***[Wiki](https://github.com/i-petro/AndroidKotlinCore/wiki/1.-Getting-Started)***

## Entity converters
### Features: 
* support for converting models of different levels of abstraction ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Entity-converter)***
* support for collections converting ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Entity-converter)***
* support for interfaces converting ***[(See Wiki)](https://github.com/i-petro/AndroidKotlinCore/wiki/Entity-converter)***
### Sample:
```kotlin
    interface User {
        val name: String
        val password: String
    }

    data class UserRest(val name: String, val password: String)
    data class UserDb(val name: String, val password: String) 
    data class UserImpl(override val name: String, override val password: String): User

    val context: ConvertersContext = ConvertersContextImpl()
    context.registerConverter { input: User -> UserDb(input.name, input.password) }
    context.registerConverter { input: UserRest -> UserImpl(input.name, input.password) }

    val userDb = UserDb("Name1", "Password1")
    val userRest = UserRest("Name1", "Password1")
    val userImpl = UserImpl("Name1", "Password1")

    val convertedRestToImpl: User = context.convert(userRest)
    val convertedImplToDb: UserDb = context.convert(userImpl)

    assertEquals(userImpl, convertedRestToImpl)
    assertEquals(userDb, convertedImplToDb)
```

## Installation
You can use any of this dependencies

| Module  |  Version  | Dependency |
|---|---|---|
| MVP core implementation  | [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/mvp/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/mvp/_latestVersion)  | ```'com.androidkotlincore:mvp:x.y.z'``` |
|   MVP extensions for RxJava 1 | [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/mvp-rx1/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/mvp-rx1/_latestVersion)  | ```'com.androidkotlincore:mvp-rx1:x.y.z'``` |
| MVP extensions for RxJava 2  |   [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/mvp-rx2/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/mvp-rx2/_latestVersion) | ```'com.androidkotlincore:mvp-rx2:x.y.z'``` |
| Entity converter |  [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/entityconverter/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/entityconverter/_latestVersion) | ```'com.androidkotlincore:entityconverter:x.y.z'``` |

or just download zip and import module to be able to modify the sources

## License
* * *
    MIT License

    Copyright (c) 2018 i-petro

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
