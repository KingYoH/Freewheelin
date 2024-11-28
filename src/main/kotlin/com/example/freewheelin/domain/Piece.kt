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

    @OneToMany(mappedBy = "piece", cascade = [CascadeType.PERSIST])
    val pieceProblems: MutableSet<PieceProblem> = mutableSetOf()
    fun addProblem(problem: Problem) {
        val pieceProblem = PieceProblem(piece = this, problem = problem)
        pieceProblems.add(pieceProblem)
        problem.pieceProblems.add(pieceProblem)
    }
}

