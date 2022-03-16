package com.kfabija.test.domain

import javax.persistence.*
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import java.io.Serializable
import java.time.LocalDate


/**
 * A MeterReading.
 */

@Entity
@Table(name = "meter_reading")

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
data class MeterReading(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "date")
    var date: LocalDate? = null,

    @Column(name = "electricity_consumption")
    var electricityConsumption: Double? = null,
    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MeterReading) return false
        return id != null && other.id != null && id == other.id
    }

    @Override
    override fun toString(): String {
        return "MeterReading{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", electricityConsumption=" + electricityConsumption +
            "}";
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
