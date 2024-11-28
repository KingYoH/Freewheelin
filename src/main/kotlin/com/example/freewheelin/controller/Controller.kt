package com.example.freewheelin.controller

import com.example.freewheelin.dto.*
import com.example.freewheelin.dto.common.BaseResponse
import com.example.freewheelin.enum.PieceLevel
import com.example.freewheelin.enum.ProblemType
import com.example.freewheelin.service.Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class Controller(
    private val service: Service,
) {
    /* 1. 문제 조회
        - 선생님은 총 문제 수, 유형코드 리스트, 난이도, 문제 유형(주관식, 객관식, 전체)을 조건으로 문제를 조회합니다.
     */
    @GetMapping("/problems")
    fun getProblems(
        @RequestParam totalCount: Int,
        @RequestParam unitCodeList: List<String>,
        @RequestParam level: PieceLevel,
        @RequestParam problemType: ProblemType
    ): BaseResponse<GetProblemDto.Response>
    = BaseResponse(data=service.getProblems(totalCount,unitCodeList,level, problemType))
//
//    /* 2. 학습지 생성
//        - 선생님은 1번에서 조회했던 문제 리스트를 바탕으로 학습지를 생성합니다.
//        - 학습지 생성 시 포함될 수 있는 최대 문제 수는 50개 입니다.
//        - 학습지는 아래의 정보를 가지고 있습니다.
//            · 학습지 이름
//            · 만든 유저 정보
//     */
//    @PostMapping("/piece")
//    fun createPiece(
//        @RequestBody request: CreatePieceDto.Request,
//    ):CreatePieceDto.Response{
//        return CreatePieceDto.Response()
//    }
//    /* 3. 학생에게 학습지 출제하기
//        - 선생님은 학생에게 2번 문제에서 생성했던 학습지 1개의 학습지를 출제합니다.
//        - 선생님은 자신이 만든 학습지만 학생에게 출제가 가능합니다.
//        - 학습지는 동시에 2명이상의 학생에게 출제가 가능합니다.
//        - 이미 존재하는 학습지를 부여받는 경우 에러로 간주하지 않습니다.
//        - 만약 동시에 2명 이상의 학생에게 1개의 학습지를 출제하는데 이미 같은 학습지를 받은 경우가 있는 경우 이미 같은 학습지를 받은 학생을 제외하고 나머지 인원만 학습지를 받습니다.
//     */
//    @PostMapping("/pieces/{pieceId}")
//    fun submitPiece(
//        @PathVariable pieceId: Long,
//        @RequestParam studentIds: List<Long>,
//    ){
//
//    }
//
//    /* 4. 학습지의 문제 조회하기
//        - 학생은 자신에게 출제된 학습지의 문제 목록을 확인할 수 있습니다.
//        - 학습지 1개에 대한 문제목록을 확인하는 것입니다.
//        - 클라이언트는 이 api를 바탕으로 문제풀이 화면을 구현합니다.
//     */
//    @GetMapping("/piece/problems")
//    fun getPieceProblems(
//        @RequestParam pieceId: Long,
//    ):GetPieceProblemsDto.Response{
//        return GetPieceProblemsDto.Response()
//    }
//
//    /* 5. 채점하기
//        - 학생은 4번 문제에서 조회했던 문제들을 채점할 수 있습니다.
//        - 문제는 2개이상 한번에 채점이 가능합니다.
//        - 채점 결과는 맞음, 틀림 2가지가 존재합니다.
//     */
//    @PutMapping("/piece/problems")
//    fun gradePiece(
//        @RequestParam pieceId: Long,
//        @RequestBody request: GradePieceDto.Request,
//    ): GradePieceDto.Resposne{
//        return GradePieceDto.Resposne()
//    }
//
//    /* 6. 학습지 학습 통계 분석하기
//    - 선생님은 1개의 학습지에 대해 학생들의 학습 통계를 파악할 수 있습니다.
//    - 선생님은 자신이 만든 학습지에 대해 학생들의 학습 통계 데이터를 분석할 수 있습니다.
//    - 선생님은 조회한 1개의 학습지에 대해 아래의 정보들을 파악 할 수 있습니다.
//        · 학습지 ID
//        · 학습지 이름
//        · 출제한 학생들의 목록
//        · 학생들의 학습 데이터 - 학생 개별의 학습지 정답률
//        · 학습지의 문제별 정답률 (출제받은 학생들에 한에서)
//     */
//    @GetMapping("/pieces/analyze")
//    fun analyzePiece(
//        @RequestParam pieceId: Long,
//    ): AnalyzePieceDto.Response{
//        return AnalyzePieceDto.Response()
//    }
}