package com.androidkotlincore.entityconverter

import kotlin.reflect.KFunction3

/**
 * Created by Peter on 28.01.2018.
 */

/**
 *
 */
inline fun <reified IN : Any, reified OUT : Any> ConvertersContext.registerConverter(
        converter: KFunction3<
                @ParameterName(name = "input") IN,
                @ParameterName(name = "token") Any?,
                @ParameterName(name = "convertersContext") ConvertersContext,
                OUT>) {
    registerConverter(IN::class.java, OUT::class.java, converter)
}

/**
 *
 */
fun <IN : Any, OUT : Any> ConvertersContext.registerConverter(
        inClass: Class<IN>,
        outClass: Class<OUT>,
        converter: (IN, Any?, ConvertersContext) -> OUT) {
    registerConverter(inClass, outClass, converter::invoke)
}

/**
 *
 */
inline fun <IN : Any, OUT : Any> ConvertersContext.registerConverter(
        inClass: Class<IN>,
        outClass: Class<OUT>,
        crossinline converter: (IN, ConvertersContext) -> OUT) {
    val converterWrapper = { input: IN, _: Any?, context: ConvertersContext ->
        converter.invoke(input, context)
    }
    registerConverter(inClass, outClass, converterWrapper::invoke)
}

/**
 *
 */
inline fun <IN : Any, OUT : Any> ConvertersContext.registerConverter(
        inClass: Class<IN>,
        outClass: Class<OUT>,
        crossinline converter: (IN) -> OUT) {
    val wrapper = { input: IN, _: Any?, _: ConvertersContext ->
        converter.invoke(input)
    }
    registerConverter(inClass, outClass, wrapper::invoke)
}

/**
 *
 */
inline fun <reified IN : Any, reified OUT : Any> ConvertersContext.registerConverter(
        crossinline converter: (IN) -> OUT) {
    val wrapper = { input: IN, _: Any?, _: ConvertersContext ->
        converter.invoke(input)
    }
    registerConverter(IN::class.java, OUT::class.java, wrapper::invoke)
}

/**
 * Registers converters.
 *
 * @param visitorClass reference to [ConvertersContextVisitor] cass with default constructor.
 */
fun ConvertersContext.registerConverter(visitorClass: Class<out ConvertersContextVisitor>) {
    visitorClass.getDeclaredConstructor().newInstance().visit(this)
}

/**
 * Registers converters.
 *
 * @param visitor instance of converters context visitor
 */
fun ConvertersContext.registerConverter(visitor: ConvertersContextVisitor) {
    visitor.visit(this)
}