package com.kfabija.test.domain

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import com.kfabija.test.web.rest.equalsVerifier

import java.util.UUID

class MeterReadingTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(MeterReading::class)
        val meterReading1 = MeterReading()
        meterReading1.id = 1L
        val meterReading2 = MeterReading()
        meterReading2.id = meterReading1.id
        assertThat(meterReading1).isEqualTo(meterReading2)
        meterReading2.id = 2L
        assertThat(meterReading1).isNotEqualTo(meterReading2)
        meterReading1.id = null
        assertThat(meterReading1).isNotEqualTo(meterReading2)
    }
}
