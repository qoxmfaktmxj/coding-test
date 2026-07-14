# 📖 롯데이노베이트 코테 공략집 — 2편 (심화)

> 1편(기본기·유형)을 뗐다는 전제의 **심화 문제집 + 실전 모의고사** 편입니다.
> 경력직 변별 포인트(분석함수·계층쿼리·파라메트릭 서치)에 집중. **SQL은 Oracle**, 스키마는 1편 [schema.sql](../sql/schema.sql) 그대로.

## 구성

| 파트 | 파일 | 내용 |
|---|---|---|
| SQL 심화 ① | [sql_심화_01_분석함수](sql_심화_01_분석함수.md) | 다중조인 집계, LAG, 누적합, 그룹별 상위N, 월별 전월대비 |
| SQL 심화 ② | [sql_심화_02_계층_집합_페이징](sql_심화_02_계층_집합_페이징.md) | CONNECT BY 계층, 셀프조인, NOT EXISTS, MINUS, ROWNUM 페이징 |
| SQL 심화 ③ | [sql_심화_03_고급분석](sql_심화_03_고급분석.md) | NTILE, FIRST/LAST_VALUE, RATIO_TO_REPORT, ROLLUP, LISTAGG |
| 알고 심화 ① | [algo_심화_01_문자열_시뮬_스택](algo_심화_01_문자열_시뮬_스택.md) | 문자열 압축, 행렬 90도 회전, 올바른 괄호 |
| 알고 심화 ② | [algo_심화_02_탐색_그리디_이분](algo_심화_02_탐색_그리디_이분.md) | 슬라이딩 윈도우, 타겟넘버 DFS, 구명보트, 랜선 자르기 |
| 알고 심화 ③ | [algo_심화_03_DP_그래프](algo_심화_03_DP_그래프.md) | 편집거리, LIS, 2×N 타일링, 동전 교환(DP) |
| 실전 | [실전모의고사_02](실전모의고사_02.md) · [실전모의고사_03](실전모의고사_03.md) | SQL1+알고2, 120분 × 2세트 |

## 이 편의 핵심 무기 (경력직 어필 포인트)
- **분석함수**: `RANK/ROW_NUMBER`, `LAG/LEAD`, `SUM() OVER(ORDER BY)` 누적합
- **오라클 고유**: `CONNECT BY`(계층), `MINUS`(차집합), `ROWNUM`(페이징 3단 구조)
- **알고리즘 결정打**: 파라메트릭 서치, 슬라이딩 윈도우, DFS 완전탐색

## 검증
- Java 답지 11종: **컴파일·실행·출력 대조 완료**
- SQL 답지(심화 15 + 모의 2): **결과 sqlite 대조 완료**, 문법 Oracle 검수

> ✅ 핵심 오라클 구문(CONNECT BY·MINUS·ROWNUM 페이징·NTILE·RATIO_TO_REPORT·ROLLUP·LAST_VALUE 등)은 **실제 Oracle 23c(dbfiddle)에서 실행 검증 완료**. 나머지 표준 쿼리는 sqlite로 결과 대조.
