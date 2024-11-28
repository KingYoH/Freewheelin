package com.example.freewheelin.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "Piece_Student",
    indexes = [
        Index(name = "idx_piece_student_piece_id", columnList = "`piece_id`"),
        Index(name = "idx_piece_student_student_id", columnList = "`student_id`"),
    ]
)
class PieceStudent(
    piece: Piece,
    student: Member,
):PrimaryKey() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`piece_id`", nullable = false)
    var piece: Piece = piece
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`student_id`", nullable = false)
    var student: Member = student
        protected set

    @Column(name = "`attempt_count`", nullable = false)
    var attemptCount: Int = 0
        protected set

    @Column(name = "`correct_count`", nullable = false)
    var correctCount: Int = 0
        protected set
}