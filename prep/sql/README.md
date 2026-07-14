# 🗄️ SQL 트랙 (경력직 최우선)

> 롯데이노베이트 코테는 **SQL 1문제**가 고정 출제되고, 경력직은 체감 비중이 더 큽니다.
> "함수 암기"가 아니라 **패턴을 눈에 바르고 손으로 빠르게** 나오게 하는 게 목표.
> ⭐ **DBMS는 Oracle.** 모든 답지는 **Oracle 문법**으로 작성했습니다 (LIMIT❌ → ROWNUM/FETCH, IFNULL❌ → NVL 등).

## 📚 공략본 순서 (읽기용, 각 파일 = [지문→정답→해설→함정])

| # | 파일 | 핵심 | 프로그래머스 고득점 키트 대응 |
|---|---|---|---|
| 00 | [SQL 치트시트](00_SQL_치트시트.md) | 절 실행순서·문법 요약 | (전체) |
| 01 | [SELECT·정렬·조건](01_SELECT_정렬_공략.md) | WHERE, ORDER BY, LIMIT, DISTINCT | SELECT |
| 02 | [집계·GROUP BY·HAVING](02_GROUP_BY_HAVING_공략.md) | SUM/AVG/COUNT, HAVING | 합계·평균 / GROUP BY |
| 03 | [JOIN](03_JOIN_공략.md) | INNER/LEFT, 다중조인, IS NULL | JOIN / IS NULL |
| 04 | [서브쿼리](04_서브쿼리_공략.md) | 스칼라·IN·상관 서브쿼리 | GROUP BY 심화 |
| 05 | [윈도우함수](05_윈도우함수_공략.md) | RANK/ROW_NUMBER/집계 OVER | (고난도 단골) |
| 06 | [날짜·문자열](06_날짜_문자열_공략.md) | TO_CHAR, EXTRACT, SUBSTR, LIKE | String, Date |
| 07 | [NULL·CASE·그룹별 TOP-N](07_NULL_CASE_TOPN_공략.md) | NVL, CASE, 그룹 1위 | IS NULL 심화 |

## 🏋️ 학습법 (지하철 + 저녁 10분)

1. **지하철(읽기)**: 공략본을 위→아래로 읽으며 "이 지문이면 이 절"을 눈에 바름.
2. **저녁(손코딩)**: [프로그래머스 SQL 고득점 키트](https://school.programmers.co.kr/learn/challenges?tab=sql_practice_kit)에서 같은 유형 2~3문제를 **직접 타이핑**. 눈으로 아는 것과 손이 아는 건 다름.
3. 틀리면 → 해당 공략본의 "흔한 실수" 섹션 재독.

## 🧪 스키마 연습 환경

- `schema.sql` = **Oracle용**. Oracle(로컬/사내 계정)이나 온라인 오라클(예: Oracle Live SQL, dbfiddle Oracle)에 붙여넣어 답지 쿼리를 돌려보세요.
- 환경이 번거로우면 [프로그래머스 SQL 고득점 키트]에서 **Oracle을 선택**해 바로 연습(실전과 동일 환경).
- 도메인: **전자상거래**(MEMBER·PRODUCT·ORDERS·ORDER_ITEM) + **인사**(DEPARTMENT·EMPLOYEE).

## ⏱️ 시험장 SQL 30분 컷 루틴

1. 요구사항에서 **출력 컬럼 · 정렬 · 필터**를 먼저 밑줄 (지문 독해가 절반)
2. FROM/JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY 순으로 뼈대
3. 그룹별 1위/순위 요구면 **윈도우함수** 먼저 떠올리기
4. 제출 전: 정렬 기준·동점 처리·NULL 포함 여부 재확인
