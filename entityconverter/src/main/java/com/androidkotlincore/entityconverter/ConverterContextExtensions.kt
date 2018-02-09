package com.androidkotlincore.entityconverter

import kotlin.reflect.KFunction3

/**
 * Created by Peter on 28.01.2018.
 */

/**
 * Registers converter between two entity classes
 *
 * @param converter convert function reference
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
 * Registers converter between two entity classes
 *
 * @param inClass   input class - abstract as possible
 * @param outClass  output class - concrete as possible
 * @param converter convert function reference
 */
inline fun <IN : Any, OUT : Any> ConvertersContext.registerConverter(
        inClass: Class<IN>,
        outClass: Class<OUT>,
        crossinline converter: (IN, ConvertersContext) -> OUT) {
    val converterWrapper = { input: IN, _: Any?, context: ConvertersContext ->
        converter(input, context)
    }
    registerConverter(inClass, outClass, converterWrapper::invoke)
}

/**
 * Registers converter between two entity classes
 *
 * @param inClass   input class - abstract as possible
 * @param outClass  output class - concrete as possible
 * @param converter convert function reference
 */
inline fun <IN : Any, OUT : Any> ConvertersContext.registerConverter(
        inClass: Class<IN>,
        outClass: Class<OUT>,
        crossinline converter: (IN) -> OUT) {
    val wrapper = { input: IN, _: Any?, _: ConvertersContext ->
        converter(input)
    }
    registerConverter(inClass, outClass, wrapper::invoke)
}

/**
 * Registers converter between two entity classes
 *
 * @param converter convert function reference
 */
inline fun <reified IN : Any, reified OUT : Any> ConvertersContext.registerConverter(
        crossinline converter: (IN) -> OUT) {
    val wrapper = { input: IN, _: Any?, _: ConvertersContext ->
        converter(input)
    }
    registerConverter(IN::class.java, OUT::class.java, wrapper::invoke)
}

/**
 * Registers converters.
 *
 * @param registrationCallbackClass reference to [ConvertersContextRegistrationCallback] cass with default constructor.
 */
fun ConvertersContext.registerConverter(registrationCallbackClass: Class<out ConvertersContextRegistrationCallback>) {
    registrationCallbackClass.getDeclaredConstructor().newInstance().register(this)
}

/**
 * Registers converters.
 *
 * @param registrationCallback instance of converters context registrationCallback
 */
fun ConvertersContext.registerConverter(registrationCallback: ConvertersContextRegistrationCallback) {
    registrationCallback.register(this)
}

/**
 * Converts one entity type to another
 *
 * @param input input entity
 */
inline fun <reified OUT : Any> ConvertersContext.convert(input: Any): OUT {
    return convert(input, OUT::class.java)
}

/**
 * Converts one entity type to another
 *
 * @param input input entity
 * @param token token that will be passed in [ConvertersContext.convert] method.
 */
inline fun <reified OUT : Any> ConvertersContext.convert(input: Any, token: Any?): OUT {
    return convert(input, token, OUT::class.java)
}