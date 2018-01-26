package com.androidkotlincore.entityconverter

import kotlin.reflect.KFunction3

/**
 * Created by Peter on 29.11.17.
 */
class ConvertersContextImpl : IConvertersContext {
    private val TAG = ConvertersContextImpl::class.java.simpleName
    private val converters = HashMap<Class<*>, HashMap<Class<*>, KFunction3<*/*IN*/, Any?, IConvertersContext, */*OUT*/>>>()

    override fun <IN : Any, OUT : Any> registerConverter(inClass: Class<IN>, outClass: Class<OUT>, converter: KFunction3<IN, Any?, IConvertersContext, OUT>) {
        var out: Class<*>? = outClass
        do {
            registerConverterInternal(inClass, out as Class<Any>, converter as KFunction3<IN, Any?, IConvertersContext, Any>)
            out = out.superclass
        } while (out != null && out != Any::class.java)

        for (outInterface in outClass.interfaces) {
            registerConverterInternal(inClass, outInterface as Class<Any>, converter as KFunction3<IN, Any?, IConvertersContext, Any>)
        }
    }

    override fun registerConverter(visitorClass: Class<out IConvertersContextVisitor>) {
        registerConverter(visitorClass.getDeclaredConstructor().newInstance())
    }

    override fun registerConverter(visitor: IConvertersContextVisitor) = visitor.visit(this)

    override fun <IN : Any, OUT : Any> convert(input: IN, token: Any?, outClass: Class<OUT>): OUT {
        if (outClass.isInstance(input)) {
            return input as OUT
        }
        val inClass = input.javaClass
        return getConverter(inClass, outClass).invoke(input, token, this)
    }

    override fun <IN : Any, OUT : Any> convert(input: IN, outClass: Class<OUT>): OUT = convert(input, Unit, outClass)

    override fun <IN : Any, OUT : Any> convertCollection(collection: Iterable<IN>, token: Any?, outClass: Class<OUT>): List<OUT> =
            collection.map { convert(it, token, outClass) }

    override fun <IN : Any, OUT : Any> convert(outClass: Class<OUT>, token: Any?): (input: IN) -> OUT = {
        convert(it, token, outClass)
    }

    override fun <IN : Any, OUT : Any> convert(outClass: Class<OUT>): (input: IN) -> OUT = {
        convert(it, outClass)
    }

    override fun <IN : Any, OUT : Any> convertCollection(collection: Iterable<IN>, outClass: Class<OUT>): List<OUT> =
            convertCollection(collection, Unit, outClass)


    override fun <IN : Any, OUT : Any> convertCollection(outClass: Class<OUT>): (input: Iterable<IN>) -> List<OUT> = {
        convertCollection(it, outClass)
    }

    private fun <IN : Any, OUT : Any> getConverter(
            inClass: Class<IN>,
            outClass: Class<OUT>): KFunction3<IN, Any?, IConvertersContext, OUT> {

        var clazz: Class<*>? = inClass
        do {
            val fromConverters = converters[clazz]
            if (fromConverters != null) {
                val entityConverter = fromConverters[outClass]
                if (entityConverter != null) {
                    return entityConverter as KFunction3<IN, Any?, IConvertersContext, OUT>
                }
            }

            clazz = clazz?.superclass
        } while (clazz != null)

        //search by interfaces
        inClass.interfaces
                .asSequence()
                .mapNotNull { converters[it] }
                .mapNotNull { it[outClass] }
                .forEach {
                    return it as KFunction3<IN, Any?, IConvertersContext, OUT>
                }

        converterNotFound(inClass, outClass)
    }

    private fun <IN : Any, OUT : Any> registerConverterInternal(inClass: Class<IN>, outClass: Class<OUT>, converter: KFunction3<IN, Any?, IConvertersContext, OUT>) {
        var innerMap = converters[inClass]
        if (innerMap == null) {
            innerMap = java.util.HashMap()
            converters.put(inClass, innerMap)
        }

        val existConverter = innerMap[outClass]
        if (existConverter != null) {
            val exception = IllegalStateException(
                    "Converter ${inClass.name} -> ${outClass.name} " +
                            "is already registered as ${existConverter.javaClass.name}. " +
                            "So, converter ${converter.javaClass.canonicalName} cannot be registered!")
            throw exception
        }

        innerMap.put(outClass, converter)
    }

    private fun <IN : Any, OUT : Any> converterNotFound(inClass: Class<IN>, outClass: Class<OUT>): Nothing {
        val exception = UnsupportedOperationException(TAG + " There is no available converters between " +
                inClass.canonicalName + " and " + outClass.canonicalName)
        throw exception
    }
}