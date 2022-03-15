package com.kfabija.test.web.rest

import com.kfabija.test.repository.MeterReadingRepository
import com.kfabija.test.service.MeterReadingService
import com.kfabija.test.web.rest.errors.BadRequestAlertException
import com.kfabija.test.service.dto.MeterReadingDTO

import tech.jhipster.web.util.HeaderUtil
import tech.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.net.URI
import java.net.URISyntaxException
import java.util.Objects

private const val ENTITY_NAME = "meterReading"
/**
 * REST controller for managing [com.kfabija.test.domain.MeterReading].
 */
@RestController
@RequestMapping("/api")
class MeterReadingResource(
    private val meterReadingService: MeterReadingService,
    private val meterReadingRepository: MeterReadingRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        const val ENTITY_NAME = "meterReading"
    }

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /meter-readings` : Create a new meterReading.
     *
     * @param meterReadingDTO the meterReadingDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new meterReadingDTO, or with status `400 (Bad Request)` if the meterReading has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meter-readings")
    fun createMeterReading(@RequestBody meterReadingDTO: MeterReadingDTO): ResponseEntity<MeterReadingDTO> {
        log.debug("REST request to save MeterReading : $meterReadingDTO")
        if (meterReadingDTO.id != null) {
            throw BadRequestAlertException(
                "A new meterReading cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = meterReadingService.save(meterReadingDTO)
        return ResponseEntity.created(URI("/api/meter-readings/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * {@code PUT  /meter-readings/:id} : Updates an existing meterReading.
     *
     * @param id the id of the meterReadingDTO to save.
     * @param meterReadingDTO the meterReadingDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated meterReadingDTO,
     * or with status `400 (Bad Request)` if the meterReadingDTO is not valid,
     * or with status `500 (Internal Server Error)` if the meterReadingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meter-readings/{id}")
    fun updateMeterReading(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody meterReadingDTO: MeterReadingDTO
    ): ResponseEntity<MeterReadingDTO> {
        log.debug("REST request to update MeterReading : {}, {}", id, meterReadingDTO)
        if (meterReadingDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }

        if (!Objects.equals(id, meterReadingDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }


        if (!meterReadingRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }

        val result = meterReadingService.save(meterReadingDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     meterReadingDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
    * {@code PATCH  /meter-readings/:id} : Partial updates given fields of an existing meterReading, field will ignore if it is null
    *
    * @param id the id of the meterReadingDTO to save.
    * @param meterReadingDTO the meterReadingDTO to update.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated meterReadingDTO,
    * or with status {@code 400 (Bad Request)} if the meterReadingDTO is not valid,
    * or with status {@code 404 (Not Found)} if the meterReadingDTO is not found,
    * or with status {@code 500 (Internal Server Error)} if the meterReadingDTO couldn't be updated.
    * @throws URISyntaxException if the Location URI syntax is incorrect.
    */
    @PatchMapping(value = ["/meter-readings/{id}"], consumes = ["application/json", "application/merge-patch+json"])
    @Throws(URISyntaxException::class)
    fun partialUpdateMeterReading(
        @PathVariable(value = "id", required = false) id: Long,
        @RequestBody meterReadingDTO:MeterReadingDTO
    ): ResponseEntity<MeterReadingDTO> {
        log.debug("REST request to partial update MeterReading partially : {}, {}", id, meterReadingDTO)
        if (meterReadingDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        if (!Objects.equals(id, meterReadingDTO.id)) {
            throw BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid")
        }

        if (!meterReadingRepository.existsById(id)) {
            throw BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound")
        }



            val result = meterReadingService.partialUpdate(meterReadingDTO)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, meterReadingDTO.id.toString())
        )
    }

    /**
     * `GET  /meter-readings` : get all the meterReadings.
     *

     * @return the [ResponseEntity] with status `200 (OK)` and the list of meterReadings in body.
     */
    @GetMapping("/meter-readings")
    fun getAllMeterReadings(): MutableList<MeterReadingDTO> {
        log.debug("REST request to get all MeterReadings")

        return meterReadingService.findAll()
            }

    /**
     * `GET  /meter-readings/:id` : get the "id" meterReading.
     *
     * @param id the id of the meterReadingDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the meterReadingDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/meter-readings/{id}")
    fun getMeterReading(@PathVariable id: Long): ResponseEntity<MeterReadingDTO> {
        log.debug("REST request to get MeterReading : $id")
        val meterReadingDTO = meterReadingService.findOne(id)
        return ResponseUtil.wrapOrNotFound(meterReadingDTO)
    }
    /**
     *  `DELETE  /meter-readings/:id` : delete the "id" meterReading.
     *
     * @param id the id of the meterReadingDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/meter-readings/{id}")
    fun deleteMeterReading(@PathVariable id: Long): ResponseEntity<Void> {
        log.debug("REST request to delete MeterReading : $id")

        meterReadingService.delete(id)
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build()
    }
}
