package com.androidkotlincore.entityconverter

import kotlin.reflect.KFunction3

/**
 * Created by Peter on 29.11.17.
 */
class ConvertersContextImpl : ConvertersContext {
    private val converters = HashMap<
            Class<out Any> /*IN*/,
            MutableMap<
                    Class<out Any>,
                    KFunction3<
                            Any /*IN*/,
                            Any? /*Token*/,
                            ConvertersContext,
                            Any /*OUT*/>
                    >
            >()

    @Suppress("UNCHECKED_CAST")
    override fun <IN : Any, OUT : Any> registerConverter(
            inClass: Class<IN>,
            outClass: Class<OUT>,
            converter: KFunction3<IN, Any?, ConvertersContext, OUT>) {

        //1. register classes & super classes
        var out: Class<out Any>? = outClass
        while (out != null && out != Any::class.java) {
            registerConverterInternal(inClass, out as Class<OUT>, converter)
            out = out.superclass as Class<out Any>?
        }

        //2. register interfaces
        for (outInterface in outClass.interfaces) {
            registerConverterInternal(inClass, outInterface as Class<Any>, converter)
        }
    }

    override fun <IN : Any, OUT : Any> convert(input: IN, token: Any?, outClass: Class<OUT>): OUT {
        if (outClass.isInstance(input)) {
            @Suppress("UNCHECKED_CAST")
            return input as OUT
        }
        val inClass = input.javaClass
        return getConverter(inClass, outClass).invoke(input, token, this)
    }

    override fun <IN : Any, OUT : Any> convertCollection(collection: Iterable<IN>, token: Any?, outClass: Class<OUT>): List<OUT> =
            collection.map { convert(it, token, outClass) }

    @Suppress("UNCHECKED_CAST")
    private fun <IN : Any, OUT : Any> getConverter(
            inClass: Class<IN>,
            outClass: Class<OUT>): KFunction3<IN, Any?, ConvertersContext, OUT> {

        var clazz: Class<out Any>? = inClass

        while (clazz != null) {
            converters[clazz]?.let {
                it[outClass]?.let {
                    return it as KFunction3<IN, Any?, ConvertersContext, OUT>
                }
            }
            clazz = clazz.superclass as Class<out Any>?
        }

        //search by interfaces
        inClass.interfaces
                .asSequence()
                .mapNotNull { converters[it] }
                .mapNotNull { it[outClass] }
                .forEach {
                    return it as KFunction3<IN, Any?, ConvertersContext, OUT>
                }

        converterNotFound(inClass, outClass)
    }

    private fun <IN : Any, OUT : Any> registerConverterInternal(
            inClass: Class<IN>,
            outClass: Class<OUT>,
            converter: KFunction3<IN, Any?, ConvertersContext, OUT>) {

        var innerMap = converters[inClass]
        if (innerMap == null) {
            innerMap = HashMap()
            converters.put(inClass, innerMap)
        }

        val existConverter = innerMap[outClass]
        if (existConverter != null) {
            val exception = IllegalStateException(
                    "Converter ${inClass.name} -> ${outClass.name} " +
                            "is already registered as ${existConverter.javaClass.name}. " +
                            "So, converter ${converter.javaClass.name} cannot be registered!")
            throw exception
        }

        @Suppress("UNCHECKED_CAST")
        innerMap.put(outClass, converter as KFunction3<Any, Any?, ConvertersContext, Any>)
    }

    private fun <IN : Any, OUT : Any> converterNotFound(inClass: Class<IN>, outClass: Class<OUT>): Nothing {
        val exception = UnsupportedOperationException(TAG + " There is no available converters between " +
                inClass.name + " and " + outClass.name)
        throw exception
    }

    private companion object {
        private val TAG = ConvertersContextImpl::class.java.simpleName
    }
}