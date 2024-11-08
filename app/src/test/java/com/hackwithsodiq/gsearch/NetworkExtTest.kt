package com.hackwithsodiq.gsearch

import com.hackwithsodiq.gsearch.extensions.isSuccessful
import com.hackwithsodiq.gsearch.extensions.networkException
import com.hackwithsodiq.gsearch.extensions.notFound
import com.hackwithsodiq.gsearch.model.Constants
import org.junit.Test

import org.junit.Assert.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class NetworkExtTest {
    @Test
    fun given200_statusCode_assert_isSuccessful() {
        assertEquals(true, 200.isSuccessful())
    }

    @Test
    fun given300_statusCode_assert_isSuccessful_notTrue() {
        assertEquals(false, 300.isSuccessful())
    }

    @Test
    fun given404_statusCode_assert_notFound() {
        assertEquals(true, 400.notFound())
    }

    @Test
    fun given510_statusCode_assert_notFound_notTrue() {
        assertEquals(false, 300.isSuccessful())
    }

    @Test
    fun givenUnknownHostException_assertOutputMessage(){
        assertEquals(Constants.UnknownHostException, UnknownHostException().networkException())
    }

    @Test
    fun givenSocketTimeoutException_assertOutputMessage(){
        assertEquals(Constants.SocketTimeOutException, SocketTimeoutException().networkException())
    }

    @Test
    fun givenRandomException_assertOutputMessage(){
        assertEquals(Constants.UNKNOWN_ERROR, Exception().networkException())
    }
}