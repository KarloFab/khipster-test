package com.kfabija.test.service.impl


import com.kfabija.test.domain.MeterReading
import com.kfabija.test.repository.MeterReadingRepository
import com.kfabija.test.service.MeterReadingService
import com.kfabija.test.service.dto.MeterReadingDTO
import com.kfabija.test.service.mapper.MeterReadingMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Service Implementation for managing [MeterReading].
 */
@Service
@Transactional
class MeterReadingServiceImpl(
    private val meterReadingRepository: MeterReadingRepository,
    private val meterReadingMapper: MeterReadingMapper
) : MeterReadingService {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun save(meterReadingDTO: MeterReadingDTO): MeterReadingDTO {
        log.debug("Request to save MeterReading : $meterReadingDTO")
        var meterReading = meterReadingMapper.toEntity(meterReadingDTO)
        meterReading = meterReadingRepository.save(meterReading)
        return meterReadingMapper.toDto(meterReading)
    }

    override fun partialUpdate(meterReadingDTO: MeterReadingDTO): Optional<MeterReadingDTO> {
        log.debug("Request to partially update MeterReading : {}", meterReadingDTO)


        return meterReadingRepository.findById(meterReadingDTO.id)
            .map {
                meterReadingMapper.partialUpdate(it, meterReadingDTO)
                it
            }
            .map { meterReadingRepository.save(it) }
            .map { meterReadingMapper.toDto(it) }

    }

    @Transactional(readOnly = true)
    override fun findAll(): MutableList<MeterReadingDTO> {
        log.debug("Request to get all MeterReadings")
        return meterReadingRepository.findAll()
            .mapTo(mutableListOf(), meterReadingMapper::toDto)
    }


    @Transactional(readOnly = true)
    override fun findOne(id: Long): Optional<MeterReadingDTO> {
        log.debug("Request to get MeterReading : $id")
        return meterReadingRepository.findById(id)
            .map(meterReadingMapper::toDto)
    }

    override fun delete(id: Long) {
        log.debug("Request to delete MeterReading : $id")

        meterReadingRepository.deleteById(id)
    }
}
