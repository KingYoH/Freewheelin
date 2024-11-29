package com.example.freewheelin.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(
    name = "Piece_Problem",
    indexes = [
        Index(name = "idx_piece_problem_piece_id", columnList = "`piece_id`"),
        Index(name = "idx_piece_problem_problem_id", columnList = "`problem_id`"),
    ]
)
class PieceProblem(
    piece: Piece,
    problem: Problem,
):PrimaryKey() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`piece_id`", nullable = false)
    var piece: Piece = piece
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`problem_id`", nullable = false)
    var problem: Problem = problem
        protected set

    @OneToMany(mappedBy = "pieceProblem", cascade = [CascadeType.PERSIST])
    @JsonIgnore
    val studentProblems: MutableSet<StudentProblem> = mutableSetOf()
}