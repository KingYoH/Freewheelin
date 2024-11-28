package com.example.freewheelin.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import java.io.Serializable
import java.util.*
import kotlin.jvm.Transient

@MappedSuperclass
abstract class PrimaryKey : Persistable<Long> {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "`id`", nullable = false, updatable = false)
    private val id: Long = 0L
    override fun getId(): Long = id

    @Transient
    private var _isNew = true
    override fun isNew(): Boolean = _isNew
    @PostPersist @PostLoad
    protected fun updateNew() {
        _isNew = false
    }

    override fun equals(other: Any?): Boolean {
        if ((other == null)|| (other !is HibernateProxy && this::class != other::class)) {
            return false
        }
        val identifier = if (other is HibernateProxy) {
            other.hibernateLazyInitializer.identifier
        } else {
            (other as PrimaryKey).id
        }
        return id == identifier
    }

    override fun hashCode() = Objects.hashCode(id)
}