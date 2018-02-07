package com.androidkotlincore.mvp.addons

import java.util.*

/**
 * A container object which may or may not contain a non-null value.
 * If a value is present, `isPresent()` will return `true` and
 * `get()` will return the value.
 *
 *
 * Additional methods that depend on the presence or absence of a contained
 * value are provided, such as [orElse()][.orElse]
 * (return a default value if value not present) and
 * [ifPresent()][.ifPresent] (execute a block
 * of code if the value is present).
 *
 */
class Optional<T> {

    /**
     * If non-null, the value; if null, indicates no value is present
     */
    private val value: T?

    /**
     * Return `true` if there is a value present, otherwise `false`.
     *
     * @return `true` if there is a value present, otherwise `false`
     */
    val isPresent: Boolean
        get() = value != null

    /**
     * Constructs an empty instance.
     *
     * @implNote Generally only one empty instance, [Optional.EMPTY],
     * should exist per VM.
     */
    private constructor() {
        this.value = null
    }

    /**
     * Constructs an instance with the value present.
     *
     * @param value the non-null value to be present
     * @throws NullPointerException if value is null
     */
    private constructor(value: T) {
        this.value = Objects.requireNonNull(value)
    }

    /**
     * If a value is present in this `Optional`, returns the value,
     * otherwise throws `NoSuchElementException`.
     *
     * @return the non-null value held by this `Optional`
     * @throws NoSuchElementException if there is no value present
     *
     * @see Optional.isPresent
     */
    fun get(): T {
        if (value == null) {
            throw NoSuchElementException("No value present")
        }
        return value
    }

    /**
     * If a value is present, invoke the specified consumer with the value,
     * otherwise do nothing.
     *
     * @param consumer block to be executed if a value is present
     * @throws NullPointerException if value is present and `consumer` is
     * null
     */
    fun ifPresent(consumer: ((value: T) -> Unit)) {
        if (value != null)
            consumer.invoke(value)
    }

    /**
     * If a value is present, and the value matches the given predicate,
     * return an `Optional` describing the value, otherwise return an
     * empty `Optional`.
     *
     * @param predicate a predicate to apply to the value, if present
     * @return an `Optional` describing the value of this `Optional`
     * if a value is present and the value matches the given predicate,
     * otherwise an empty `Optional`
     * @throws NullPointerException if the predicate is null
     */
    fun filter(predicate: ((value: T) -> Boolean)): Optional<T> {
        return if (!isPresent) this
        else if (predicate.invoke(value!!)) this else empty()
    }

    /**
     * If a value is present, apply the provided mapping function to it,
     * and if the result is non-null, return an `Optional` describing the
     * result.  Otherwise return an empty `Optional`.
     *
     * @apiNote This method supports post-processing on optional values, without
     * the need to explicitly check for a return status.  For example, the
     * following code traverses a stream of file names, selects one that has
     * not yet been processed, and then opens that file, returning an
     * `Optional<FileInputStream>`:
     *
     * <pre>`Optional<FileInputStream> fis =
     * names.stream().filter(name -> !isProcessedYet(name))
     * .findFirst()
     * .map(name -> new FileInputStream(name));
    `</pre> *
     *
     * Here, `findFirst` returns an `Optional<String>`, and then
     * `map` returns an `Optional<FileInputStream>` for the desired
     * file if one exists.
     *
     * @param <U> The type of the result of the mapping function
     * @param mapper a mapping function to apply to the value, if present
     * @return an `Optional` describing the result of applying a mapping
     * function to the value of this `Optional`, if a value is present,
     * otherwise an empty `Optional`
     * @throws NullPointerException if the mapping function is null
    </U> */
    fun <U> map(mapper: ((inValue: T) -> U?)): Optional<U> {
        return if (!isPresent) empty()
        else ofNullable(mapper.invoke(value!!))
    }

    /**
     * If a value is present, apply the provided `Optional`-bearing
     * mapping function to it, return that result, otherwise return an empty
     * `Optional`.  This method is similar to [.map],
     * but the provided mapper is one whose result is already an `Optional`,
     * and if invoked, `flatMap` does not wrap it with an additional
     * `Optional`.
     *
     * @param <U> The type parameter to the `Optional` returned by
     * @param mapper a mapping function to apply to the value, if present
     * the mapping function
     * @return the result of applying an `Optional`-bearing mapping
     * function to the value of this `Optional`, if a value is present,
     * otherwise an empty `Optional`
     * @throws NullPointerException if the mapping function is null or returns
     * a null result
    </U> */
    fun <U> flatMap(mapper: ((inValue: T) -> Optional<U>)): Optional<U> {
        return if (!isPresent) empty()
        else mapper.invoke(value!!)
    }

    /**
     * Return the value if present, otherwise return `other`.
     *
     * @param other the value to be returned if there is no value present, may
     * be null
     * @return the value, if present, otherwise `other`
     */
    fun orElse(other: T): T {
        return value ?: other
    }

    /**
     * Return the value if present, otherwise invoke `other` and return
     * the result of that invocation.
     *
     * @param other a `Supplier` whose result is returned if no value
     * is present
     * @return the value if present otherwise the result of `other.get()`
     * @throws NullPointerException if value is not present and `other` is
     * null
     */
    fun orElseGet(other: (() -> T)): T {
        return value ?: other.invoke()
    }

    /**
     * Return the contained value, if present, otherwise throw an exception
     * to be created by the provided supplier.
     *
     * @apiNote A method reference to the exception constructor with an empty
     * argument list can be used as the supplier. For example,
     * `IllegalStateException::new`
     *
     * @param <X> Type of the exception to be thrown
     * @param exceptionSupplier The supplier which will return the exception to
     * be thrown
     * @return the present value
     * @throws X if there is no value present
     * @throws NullPointerException if no value is present and
     * `exceptionSupplier` is null
    </X> */
    fun <X : Throwable> orElseThrow(exceptionSupplier: (() -> X)): T {
        return value ?: throw exceptionSupplier.invoke()
    }

    /**
     * Indicates whether some other object is "equal to" this Optional. The
     * other object is considered equal if:
     *
     *  * it is also an `Optional` and;
     *  * both instances have no value present or;
     *  * the present values are "equal to" each other via `equals()`.
     *
     *
     * @param other an object to be tested for equality
     * @return {code true} if the other object is "equal to" this object
     * otherwise `false`
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is Optional<*>) {
            return false
        }

        val res = other as Optional<*>?
        return value == res!!.value
    }

    /**
     * Returns the hash code value of the present value, if any, or 0 (zero) if
     * no value is present.
     *
     * @return hash code value of the present value or 0 if no value is present
     */
    override fun hashCode(): Int {
        return Objects.hashCode(value)
    }

    /**
     * Returns a non-empty string representation of this Optional suitable for
     * debugging. The exact presentation format is unspecified and may vary
     * between implementations and versions.
     *
     * @implSpec If a value is present the result must include its string
     * representation in the result. Empty and present Optionals must be
     * unambiguously differentiable.
     *
     * @return the string representation of this instance
     */
    override fun toString(): String {
        return if (value != null)
            String.format("Optional[%s]", value)
        else
            "Optional.empty"
    }

    companion object {
        /**
         * Common instance for `empty()`.
         */
        private val EMPTY = Optional<Any>()

        /**
         * Returns an empty `Optional` instance.  No value is present for this
         * Optional.
         *
         * @apiNote Though it may be tempting to do so, avoid testing if an object
         * is empty by comparing with `==` against instances returned by
         * `Option.empty()`. There is no guarantee that it is a singleton.
         * Instead, use [.isPresent].
         *
         * @param <T> Type of the non-existent value
         * @return an empty `Optional`
        </T> */
        fun <T> empty(): Optional<T> {
            @Suppress("UNCHECKED_CAST")
            return EMPTY as Optional<T>
        }

        /**
         * Returns an `Optional` with the specified present non-null value.
         *
         * @param <T> the class of the value
         * @param value the value to be present, which must be non-null
         * @return an `Optional` with the value present
         * @throws NullPointerException if value is null
        </T> */
        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }

        /**
         * Returns an `Optional` describing the specified value, if non-null,
         * otherwise returns an empty `Optional`.
         *
         * @param <T> the class of the value
         * @param value the possibly-null value to describe
         * @return an `Optional` with a present value if the specified value
         * is non-null, otherwise an empty `Optional`
        </T> */
        fun <T> ofNullable(value: T?): Optional<T> {
            return if (value == null) empty() else of(value)
        }
    }
}