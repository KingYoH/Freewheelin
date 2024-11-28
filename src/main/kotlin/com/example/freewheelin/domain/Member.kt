package com.example.freewheelin.domain

import com.example.freewheelin.enum.MemberType
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "Member",
    indexes = [Index(name = "idx_member_type", columnList = "`type`")]
)
class Member(
    name: String,
    password: String,
    memberType: MemberType,
):PrimaryKey(){
    @Column(name = "`name`", nullable = false, unique = true)
    var name: String = name
        protected set

    @Column(name = "`password`", nullable = false)
    var password: String = password
        protected set

    @Column(name = "`type`", columnDefinition= "VARCHAR(7)",nullable = false)
    var type: String = memberType.name
        protected set

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    @JsonIgnore
    val pieceStudents: MutableSet<PieceStudent> = mutableSetOf()
    fun addPiece(pieceStudent: PieceStudent) {
        pieceStudents.add(pieceStudent)
    }
    @get:Transient
    val pieces: List<Piece>
        get() = pieceStudents.map { it.piece }
}