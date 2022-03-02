package com.kfabija.test.service.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class MeterReadingMapperTest {

    private lateinit var meterReadingMapper: MeterReadingMapper

    @BeforeEach
    fun setUp() {
        meterReadingMapper = MeterReadingMapperImpl()
    }
}
