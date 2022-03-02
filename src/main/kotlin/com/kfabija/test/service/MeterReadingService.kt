package com.kfabija.test.service
import com.kfabija.test.service.dto.MeterReadingDTO

import java.util.Optional

/**
 * Service Interface for managing [com.kfabija.test.domain.MeterReading].
 */
interface MeterReadingService {

    /**
     * Save a meterReading.
     *
     * @param meterReadingDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(meterReadingDTO: MeterReadingDTO): MeterReadingDTO

    /**
     * Partially updates a meterReading.
     *
     * @param meterReadingDTO the entity to update partially.
     * @return the persisted entity.
     */
    fun partialUpdate(meterReadingDTO: MeterReadingDTO): Optional<MeterReadingDTO>

    /**
     * Get all the meterReadings.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<MeterReadingDTO>

    /**
     * Get the "id" meterReading.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: Long): Optional<MeterReadingDTO>

    /**
     * Delete the "id" meterReading.
     *
     * @param id the id of the entity.
     */
    fun delete(id: Long)


    
}
