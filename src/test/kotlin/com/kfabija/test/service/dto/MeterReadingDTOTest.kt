package com.kfabija.test.service.dto

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import com.kfabija.test.web.rest.equalsVerifier

import java.util.UUID

class MeterReadingDTOTest {

    @Test
    fun dtoEqualsVerifier(){
        equalsVerifier(MeterReadingDTO::class)
        val meterReadingDTO1 = MeterReadingDTO()
        meterReadingDTO1.id = 1L
        val meterReadingDTO2 = MeterReadingDTO()
        assertThat(meterReadingDTO1).isNotEqualTo(meterReadingDTO2)
        meterReadingDTO2.id = meterReadingDTO1.id
        assertThat(meterReadingDTO1).isEqualTo(meterReadingDTO2)
        meterReadingDTO2.id = 2L
        assertThat(meterReadingDTO1).isNotEqualTo(meterReadingDTO2)
        meterReadingDTO1.id = null
        assertThat(meterReadingDTO1).isNotEqualTo(meterReadingDTO2)
    }
}
