package com.kfabija.test.service.mapper


import com.kfabija.test.domain.MeterReading
import com.kfabija.test.service.dto.MeterReadingDTO


import org.mapstruct.*

/**
 * Mapper for the entity [MeterReading] and its DTO [MeterReadingDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface MeterReadingMapper :
    EntityMapper<MeterReadingDTO, MeterReading>
