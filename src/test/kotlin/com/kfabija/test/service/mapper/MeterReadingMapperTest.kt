package com.kfabija.test.service.mapper

import org.junit.jupiter.api.BeforeEach

class MeterReadingMapperTest {

    private lateinit var meterReadingMapper: MeterReadingMapper

    @BeforeEach
    fun setUp() {
        meterReadingMapper = MeterReadingMapperImpl()
    }
}
