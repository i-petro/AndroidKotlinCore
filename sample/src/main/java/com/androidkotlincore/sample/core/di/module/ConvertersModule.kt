package com.androidkotlincore.sample.core.di.module

import com.androidkotlincore.entityconverter.ConvertersContext
import com.androidkotlincore.entityconverter.ConvertersContextImpl
import com.androidkotlincore.entityconverter.registerConverter
import com.androidkotlincore.sample.data.converter.DataConverter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Peter on 06.02.18.
 */
@Module
class ConvertersModule {

    @Provides
    @Singleton
//    @DataConverterQualifier
//    @DomainConverterQualifier
    fun provideDataConverter(): ConvertersContext {
        val converter: ConvertersContext = ConvertersContextImpl()
        converter.registerConverter(DataConverter::class.java)
        return converter
    }
}