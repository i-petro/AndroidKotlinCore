# AndroidKotlinCore [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="images/label-android.svg" height="19">

## MVP implementation [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/mvp/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/mvp/_latestVersion)
### Features:
* saving Presenter's instance during _screen rotation_
* _don't keep activities_ mode support
* _onActivityResult_ events handling
* _permissions_ management
* _lifecycle_ events handling
* saving and restoring View state without onSaveInstanceState() and Bundle
* Kotlin coroutines support

## MVP-RX support library
### MVP-RX 1 [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/mvp-rx1/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/mvp-rx1/_latestVersion)
### MVP-RX 2 [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/mvp-rx2/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/mvp-rx2/_latestVersion)

## Entity converters [ ![Download](https://api.bintray.com/packages/peterilchenko/AndroidKotlinCore/entityconverter/images/download.svg) ](https://bintray.com/peterilchenko/AndroidKotlinCore/entityconverter/_latestVersion)
* support for converting models of different levels of abstraction
* support for collections converting
* support for interfaces converting

## Installation
You can use any of this dependencies
by gradle : 
```groovy
dependencies {
    implementation 'com.androidkotlincore:mvp:x.y.z'
    implementation 'com.androidkotlincore:mvp-rx1:x.y.z'
    implementation 'com.androidkotlincore:mvp-rx2:x.y.z'
    implementation 'com.androidkotlincore:entityconverter:x.y.z'
}
```
or just download zip and import module to be able to modify the sources

#### License ####
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
