# SQL 07 — NULL · CASE · 그룹별 TOP-N

> NULL 처리와 조건 분기(CASE)는 잔실수 1순위. 마지막으로 그룹별 1위 뽑기를 종합.

---

## 문제 1. 나이대별 회원 수 (CASE)

> 회원을 **10대 / 20대 / 30대 이상**으로 나눠 각 구간의 회원 수를 조회하세요.

**정답 (Oracle)**
```sql
SELECT CASE
         WHEN age < 20 THEN '10대'
         WHEN age < 30 THEN '20대'
         ELSE '30대 이상'
       END AS age_group,
       COUNT(*) AS cnt
FROM MEMBER
GROUP BY CASE
           WHEN age < 20 THEN '10대'
           WHEN age < 30 THEN '20대'
           ELSE '30대 이상'
         END
ORDER BY age_group;
```
**해설**
- `CASE WHEN`은 위에서부터 처음 참인 조건을 채택 → 조건 순서가 중요(작은 값부터).
- 결과: 10대 1명(정하늘), 20대 2명(김철수·최지은), 30대 이상 3명(이영희·박민수·강보라).
- ⭐ **Oracle은 `GROUP BY`에 별칭(`age_group`)을 못 씀** → CASE식을 그대로 반복해야 함. (단 `ORDER BY`엔 별칭 사용 가능) — MySQL과 결정적 차이.

**⚠️ 함정**: 구간 겹침 주의. `WHEN age<30`이 `age<20`보다 아래 있어야 10대가 20대에 안 먹힘.

**🔗 프로그래머스 대응**: "나이 정보가 없는 회원 수 구하기" 등 CASE/IS NULL 조합.

---

## 문제 2. 직원 부서명 표시 (LEFT JOIN + NULL 처리)

> **모든 직원**의 이름과 소속 부서명을 조회하되, 부서가 없으면 **'미배정'**으로 표시하세요.

**정답 (Oracle)**
```sql
SELECT e.name,
       COALESCE(d.dept_name, '미배정') AS dept_name
FROM EMPLOYEE e
LEFT JOIN DEPARTMENT d ON e.dept_id = d.dept_id
ORDER BY e.emp_id;
```
**해설**
- 직원 전부가 기준 → `LEFT JOIN`. 부서 없는 한사장은 `d.dept_name`이 NULL.
- `COALESCE(값, 대체값)` = 처음 만나는 non-NULL 반환(다중 인자 가능). 2개면 Oracle `NVL(값, 대체값)`과 동일.
- 결과: 한사장 → '미배정', 나머지는 소속 부서명.

**⚠️ 함정**: `d.dept_name = NULL` (X). NULL 비교는 `IS NULL`, 치환은 Oracle에선 `NVL`/`COALESCE` (`IFNULL`은 MySQL 전용이라 오라클에서 에러).

**🔗 프로그래머스 대응**: IS NULL 카테고리.

---

## 문제 3. 부서별 최고 연봉자 (그룹별 TOP-N 종합)

> **각 부서에서 연봉이 가장 높은 직원 1명**을 뽑아 이름·부서·연봉을 조회하세요.
> 동점이면 사번(emp_id)이 빠른 사람.

**정답 (Oracle)**
```sql
SELECT name, dept_id, salary
FROM (
    SELECT name, dept_id, salary,
           ROW_NUMBER() OVER (
               PARTITION BY dept_id
               ORDER BY salary DESC, emp_id ASC   -- tie-breaker
           ) AS rn
    FROM EMPLOYEE
    WHERE dept_id IS NOT NULL
) t
WHERE rn = 1
ORDER BY dept_id;
```
**해설**
- [05](05_윈도우함수_공략.md)의 그룹별 TOP-N 패턴 + **동점 tie-breaker**(`emp_id ASC`)로 "정확히 1명" 보장.
- 결과: 개발=조개발(8,000,000), 영업=윤영업(7,000,000), 인사=최인사(4,500,000).

**⚠️ 함정**: tie-breaker 없이 `ORDER BY salary DESC`만 쓰면 동점 시 누가 rn=1일지 비결정적 → 채점에서 흔들림.

**🔗 프로그래머스 대응**: JOIN/GROUP BY + 순위(고난도).

---

### ✅ 이 장 핵심
- NULL: 비교는 `IS NULL`, 치환은 `NVL`/`COALESCE`
- `CASE WHEN`은 조건 순서(작은 값부터), `ELSE` 잊지 말기
- 그룹별 1위 = **ROW_NUMBER + tie-breaker + rn=1**
