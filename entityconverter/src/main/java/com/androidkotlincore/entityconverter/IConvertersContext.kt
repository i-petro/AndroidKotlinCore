package com.androidkotlincore.entityconverter

import kotlin.reflect.KFunction3


/**
 * Context of converters interface.
 */
interface IConvertersContext {

    /**
     * Register new converter.
     *
     * @param converter instance of converter
     */
    fun <IN: Any, OUT: Any> registerConverter(
            inClass: Class<IN>,
            outClass: Class<OUT>,
            converter: KFunction3<
                    @ParameterName(name = "in") IN,
                    @ParameterName(name = "token") Any?,
                    @ParameterName(name = "convertersContext") IConvertersContext,
                    OUT>)

    fun registerConverter(visitorClass: Class<out IConvertersContextVisitor>)

    fun registerConverter(visitor: IConvertersContextVisitor)

    /**
     * Convert single object to another type.
     *
     * @param object   some object
     * @param token    token that will be passed in [IEntityConverter.convert] (Object, Object, IConvertersContext)} method.
     * @param outClass class of type you want convert object to
     * @return converted object
     */
    fun <IN: Any, OUT: Any> convert(input: IN, token: Any?, outClass: Class<OUT>): OUT

    fun <IN: Any, OUT: Any> convert(input: IN, outClass: Class<OUT>): OUT

    /**
     * Convert collection of objects to another type.
     *
     * @param collection collection of objects
     * @param token      token that will be passed in [IEntityConverter.convert] (Object, Object, IConvertersContext)} method.
     * @param outClass   class of type you want convert collection to
     * @return collection of converted objects
     */
    fun <IN: Any, OUT: Any> convertCollection(collection: Iterable<IN>, token: Any?, outClass: Class<OUT>): List<OUT>

    fun <IN: Any, OUT: Any> convertCollection(collection: Iterable<IN>, outClass: Class<OUT>): List<OUT>

    /**
     * Use this converter for Rx.map() operator
     *
     * @param outClass output class
     * @param <IN>     type of input object
     * @param <OUT>    type of output object
     * @return converted object with type OUT
    </OUT></IN> */
    fun <IN: Any, OUT: Any> convert(outClass: Class<OUT>): (input: IN) -> OUT

    fun <IN: Any, OUT: Any> convert(outClass: Class<OUT>, token: Any?): (input: IN) -> OUT

    /**
     * Use this converter for Rx.map() operator
     *
     * @param outClass output class
     * @param <IN>     type of input objects
     * @param <OUT>    type of output objects
     * @return converted list of objects with type OUT
    </OUT></IN> */
    fun <IN: Any, OUT: Any> convertCollection(outClass: Class<OUT>): (input: Iterable<IN>) -> List<OUT>
}
