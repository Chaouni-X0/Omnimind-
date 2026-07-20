package com.example

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O])
class SecurityTest {
    @Test
    fun testCleartextTrafficIsDisabled() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appInfo = context.applicationInfo
        val isCleartextTrafficAllowed = (appInfo.flags and ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC) != 0
        assertEquals("Cleartext traffic should be disabled", false, isCleartextTrafficAllowed)
    }
}
