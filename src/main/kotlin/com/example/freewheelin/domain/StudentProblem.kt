package com.example.freewheelin.domain

import jakarta.persistence.*

@Entity
@Table(
    name = "Student_Problem",
    indexes = [
        Index(name = "idx_student_problem_piece_student_id", columnList = "`piece_student_id`"),
        Index(name = "idx_student_problem_problem_id", columnList = "`piece_problem_id`"),
    ]
)
class StudentProblem(
    pieceStudent: PieceStudent,
    pieceProblem: PieceProblem,
):PrimaryKey(){
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`piece_student_id`", nullable = false)
    var pieceStudent: PieceStudent = pieceStudent
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "`piece_problem_id`", nullable = false)
    var pieceProblem: PieceProblem = pieceProblem
        protected set

    @Column(name = "`submit_answer`", nullable = true)
    var submitAnswer: String? = null

    @Column(name = "`is_correct`", nullable = true)
    var isCorrect: Boolean? = null
}
