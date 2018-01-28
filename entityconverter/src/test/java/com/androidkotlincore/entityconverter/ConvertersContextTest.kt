package com.androidkotlincore.entityconverter

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Created by Peter on 28.01.2018.
 */
class ConvertersContextTest {

    @Test
    fun withoutConversion() {
        val context: ConvertersContext = ConvertersContextImpl()
        val input: User = UserRest("Name1", "Password1")
        assertEquals(input, context.convert(input, UserRest::class.java))
        assertEquals(input, context.convert(input, User::class.java))
        assertEquals(input, context.convert(input, Any::class.java))
    }

    @Test(expected = UnsupportedOperationException::class)
    fun converterNotFound() {
        val context: ConvertersContext = ConvertersContextImpl()
        val input: User = UserRest("Name1", "Password1")
        val out = context.convert(input, UserDb::class.java) //throws UnsupportedOperationException here
    }

    @Test(expected = IllegalStateException::class)
    fun duplicateConverters() {
        val context: ConvertersContext = ConvertersContextImpl()
        context.registerConverter(User::class.java, UserDb::class.java, ::toDb)
        context.registerConverter { input: User -> UserDb(input) } //throws IllegalStateException here
    }

    @Test
    fun simpleConversion() {
        val context: ConvertersContext = ConvertersContextImpl()
        context.registerConverter { input: User -> UserDb(input) }

        val userDb = UserDb("Name1", "Password1")
        val userRest = UserDb("Name1", "Password1")
        val userImpl = UserImpl("Name1", "Password1")

        assertEquals(userDb, context.convert(userRest, UserDb::class.java))
        assertEquals(userDb, context.convert(userImpl, UserDb::class.java))
    }

    /**
     * Note! When registering converter, use input type abstract as possible and
     * output type concrete as possible
     */
    @Test(expected = UnsupportedOperationException::class)
    fun wrongSimpleConversion() {
        val context: ConvertersContext = ConvertersContextImpl()
        context.registerConverter { input: UserRest -> UserDb(input) }

        val userDb = UserDb("Name1", "Password1")
        val userImpl = UserImpl("Name1", "Password1")

        //converter does not know how to convert UserImpl to UserDb
        assertEquals(userDb, context.convert(userImpl, UserDb::class.java)) //throws UnsupportedOperationException here
    }

}

interface User {
    val name: String
    val password: String
}

data class UserRest(override val name: String, override val password: String) : User {
    constructor(copy: User): this(copy.name, copy.password)
}
data class UserDb(override val name: String, override val password: String) : User {
    constructor(copy: User): this(copy.name, copy.password)
}
data class UserImpl(override val name: String, override val password: String) : User {
    constructor(copy: User): this(copy.name, copy.password)
}

private fun toDb(input: User, token: Any?, converter: ConvertersContext) = UserDb(input)
private fun toRest(input: User, token: Any?, converter: ConvertersContext) = UserRest(input)

