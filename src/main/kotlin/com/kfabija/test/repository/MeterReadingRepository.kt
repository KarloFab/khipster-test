package com.kfabija.test.repository

import com.kfabija.test.domain.MeterReading
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data SQL repository for the [MeterReading] entity.
 */
@Suppress("unused")
@Repository
interface MeterReadingRepository : JpaRepository<MeterReading, Long> {
}
