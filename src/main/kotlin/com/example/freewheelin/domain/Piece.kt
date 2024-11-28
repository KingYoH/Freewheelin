package com.example.freewheelin.domain

import jakarta.persistence.*


@Entity
@Table(
    name = "Piece",
    indexes = [Index(name = "idx_piece_teacher_id", columnList = "`teacher_id`")]
)
class Piece(
    name: String,
    teacher: Member,
):PrimaryKey() {
    @Column(unique = true)
    var name:String = name
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`teacher_id`", nullable = false)
    var teacher: Member = teacher
        protected set
}

