package com.kfabija.test.web.rest


import com.kfabija.test.IntegrationTest
import com.kfabija.test.domain.MeterReading
import com.kfabija.test.repository.MeterReadingRepository
import com.kfabija.test.service.dto.MeterReadingDTO
import com.kfabija.test.service.mapper.MeterReadingMapper

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.time.LocalDate
import java.time.ZoneId
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Stream

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


/**
 * Integration tests for the [MeterReadingResource] REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MeterReadingResourceIT  {
    @Autowired
    private lateinit var meterReadingRepository: MeterReadingRepository

    @Autowired
    private lateinit var meterReadingMapper: MeterReadingMapper

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var validator: Validator


    @Autowired
    private lateinit var em: EntityManager


    @Autowired
    private lateinit var restMeterReadingMockMvc: MockMvc

    private lateinit var meterReading: MeterReading


    @BeforeEach
    fun initTest() {
        meterReading = createEntity(em)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createMeterReading() {
        val databaseSizeBeforeCreate = meterReadingRepository.findAll().size

        // Create the MeterReading
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)
        restMeterReadingMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(meterReadingDTO))
        ).andExpect(status().isCreated)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeCreate + 1)
        val testMeterReading = meterReadingList[meterReadingList.size - 1]

        assertThat(testMeterReading.date).isEqualTo(DEFAULT_DATE)
        assertThat(testMeterReading.electricityConsumption).isEqualTo(DEFAULT_ELECTRICITY_CONSUMPTION)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun createMeterReadingWithExistingId() {
        // Create the MeterReading with an existing ID
        meterReading.id = 1L
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)

        val databaseSizeBeforeCreate = meterReadingRepository.findAll().size

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeterReadingMockMvc.perform(
            post(ENTITY_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(meterReadingDTO))
        ).andExpect(status().isBadRequest)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    @Transactional
    @Throws(Exception::class)
    fun getAllMeterReadings() {
        // Initialize the database
        meterReadingRepository.saveAndFlush(meterReading)

        // Get all the meterReadingList
        restMeterReadingMockMvc.perform(get(ENTITY_API_URL+ "?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meterReading.id?.toInt())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].electricityConsumption").value(hasItem(DEFAULT_ELECTRICITY_CONSUMPTION.toDouble())))    }
    
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getMeterReading() {
        // Initialize the database
        meterReadingRepository.saveAndFlush(meterReading)

        val id = meterReading.id
        assertNotNull(id)

        // Get the meterReading
        restMeterReadingMockMvc.perform(get(ENTITY_API_URL_ID, meterReading.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(meterReading.id?.toInt()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.electricityConsumption").value(DEFAULT_ELECTRICITY_CONSUMPTION.toDouble()))    }
    @Test
    @Transactional
    @Throws(Exception::class)
    fun getNonExistingMeterReading() {
        // Get the meterReading
        restMeterReadingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    @Transactional
    fun putNewMeterReading() {
        // Initialize the database
        meterReadingRepository.saveAndFlush(meterReading)

        val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size

        // Update the meterReading
        val updatedMeterReading = meterReadingRepository.findById(meterReading.id).get()
        // Disconnect from session so that the updates on updatedMeterReading are not directly saved in db
        em.detach(updatedMeterReading)
        updatedMeterReading.date = UPDATED_DATE
        updatedMeterReading.electricityConsumption = UPDATED_ELECTRICITY_CONSUMPTION
        val meterReadingDTO = meterReadingMapper.toDto(updatedMeterReading)

        restMeterReadingMockMvc.perform(
            put(ENTITY_API_URL_ID, meterReadingDTO.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(meterReadingDTO))
        ).andExpect(status().isOk)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
        val testMeterReading = meterReadingList[meterReadingList.size - 1]
        assertThat(testMeterReading.date).isEqualTo(UPDATED_DATE)
        assertThat(testMeterReading.electricityConsumption).isEqualTo(UPDATED_ELECTRICITY_CONSUMPTION)
    }

    @Test
    @Transactional
    fun putNonExistingMeterReading() {
        val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size
        meterReading.id = count.incrementAndGet()

        // Create the MeterReading
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
            restMeterReadingMockMvc.perform(put(ENTITY_API_URL_ID, meterReadingDTO.id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(meterReadingDTO)))
            .andExpect(status().isBadRequest)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithIdMismatchMeterReading() {
        val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size
        meterReading.id = count.incrementAndGet()

        // Create the MeterReading
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterReadingMockMvc.perform(
            put(ENTITY_API_URL_ID, count.incrementAndGet())
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(meterReadingDTO))
        ).andExpect(status().isBadRequest)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun putWithMissingIdPathParamMeterReading() {
        val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size
        meterReading.id = count.incrementAndGet()

        // Create the MeterReading
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMeterReadingMockMvc.perform(put(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(meterReadingDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
    }

    
    @Test
    @Transactional
    @Throws(Exception::class)
    fun partialUpdateMeterReadingWithPatch() {
        meterReadingRepository.saveAndFlush(meterReading)
        
        
val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size

// Update the meterReading using partial update
val partialUpdatedMeterReading = MeterReading().apply {
    id = meterReading.id

    
        date = UPDATED_DATE
}


restMeterReadingMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedMeterReading.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedMeterReading)))
.andExpect(status().isOk)

// Validate the MeterReading in the database
val meterReadingList = meterReadingRepository.findAll()
assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
val testMeterReading = meterReadingList.last()
    assertThat(testMeterReading.date).isEqualTo(UPDATED_DATE)
    assertThat(testMeterReading.electricityConsumption).isEqualTo(DEFAULT_ELECTRICITY_CONSUMPTION)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun fullUpdateMeterReadingWithPatch() {
        meterReadingRepository.saveAndFlush(meterReading)
        
        
val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size

// Update the meterReading using partial update
val partialUpdatedMeterReading = MeterReading().apply {
    id = meterReading.id

    
        date = UPDATED_DATE
        electricityConsumption = UPDATED_ELECTRICITY_CONSUMPTION
}


restMeterReadingMockMvc.perform(patch(ENTITY_API_URL_ID, partialUpdatedMeterReading.id)
.contentType("application/merge-patch+json")
.content(convertObjectToJsonBytes(partialUpdatedMeterReading)))
.andExpect(status().isOk)

// Validate the MeterReading in the database
val meterReadingList = meterReadingRepository.findAll()
assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
val testMeterReading = meterReadingList.last()
    assertThat(testMeterReading.date).isEqualTo(UPDATED_DATE)
    assertThat(testMeterReading.electricityConsumption).isEqualTo(UPDATED_ELECTRICITY_CONSUMPTION)
    }

    @Throws(Exception::class)
    fun patchNonExistingMeterReading() {
        val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size
        meterReading.id = count.incrementAndGet()

        // Create the MeterReading
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
            restMeterReadingMockMvc.perform(patch(ENTITY_API_URL_ID, meterReadingDTO.id)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(meterReadingDTO)))
            .andExpect(status().isBadRequest)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithIdMismatchMeterReading() {
        val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size
        meterReading.id = count.incrementAndGet()

        // Create the MeterReading
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
            restMeterReadingMockMvc.perform(patch(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(meterReadingDTO)))
            .andExpect(status().isBadRequest)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun patchWithMissingIdPathParamMeterReading() {
        val databaseSizeBeforeUpdate = meterReadingRepository.findAll().size
        meterReading.id = count.incrementAndGet()

        // Create the MeterReading
        val meterReadingDTO = meterReadingMapper.toDto(meterReading)

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
            restMeterReadingMockMvc.perform(patch(ENTITY_API_URL)
            .contentType("application/merge-patch+json")
            .content(convertObjectToJsonBytes(meterReadingDTO)))
            .andExpect(status().isMethodNotAllowed)

        // Validate the MeterReading in the database
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    fun deleteMeterReading() {
        // Initialize the database
        meterReadingRepository.saveAndFlush(meterReading)

        val databaseSizeBeforeDelete = meterReadingRepository.findAll().size

        // Delete the meterReading
        restMeterReadingMockMvc.perform(
            delete(ENTITY_API_URL_ID, meterReading.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val meterReadingList = meterReadingRepository.findAll()
        assertThat(meterReadingList).hasSize(databaseSizeBeforeDelete - 1)
    }


    companion object {

        private val DEFAULT_DATE: LocalDate = LocalDate.ofEpochDay(0L)
        private val UPDATED_DATE: LocalDate = LocalDate.now(ZoneId.systemDefault())

        private const val DEFAULT_ELECTRICITY_CONSUMPTION: Double = 1.0
        private const val UPDATED_ELECTRICITY_CONSUMPTION: Double = 2.0


        private val ENTITY_API_URL: String = "/api/meter-readings"
        private val ENTITY_API_URL_ID: String = ENTITY_API_URL + "/{id}"

        private val random: Random = Random()
        private val count: AtomicLong = AtomicLong(random.nextInt().toLong() + ( 2 * Integer.MAX_VALUE ))




        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): MeterReading {
            val meterReading = MeterReading(

                date = DEFAULT_DATE,

                electricityConsumption = DEFAULT_ELECTRICITY_CONSUMPTION

            )


            return meterReading
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): MeterReading {
            val meterReading = MeterReading(

                date = UPDATED_DATE,

                electricityConsumption = UPDATED_ELECTRICITY_CONSUMPTION

            )


            return meterReading
        }

    }
}
