package com.kfabija.test.service.dto

import java.util.Objects
import java.time.LocalDate
import java.io.Serializable


/**
 * A DTO for the [com.kfabija.test.domain.MeterReading] entity.
 */
data class MeterReadingDTO(

    var id: Long? = null,

    var date: LocalDate? = null,

    var electricityConsumption: Double? = null
) : Serializable {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MeterReadingDTO) return false
        val meterReadingDTO = other
        if (this.id == null){
            return false;
        }
        return Objects.equals(this.id, meterReadingDTO.id);
    }

    override fun hashCode() =        Objects.hash(this.id)
}
