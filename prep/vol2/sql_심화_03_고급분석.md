# 2편 · SQL 심화 (3) 고급 분석함수 & 집계 (Oracle)

> 여기까지 익히면 SQL은 상위권. 오라클 전용 함수(RATIO_TO_REPORT·LISTAGG·ROLLUP)가 많아 면접 어필용으로도 좋음.

---

## 문제 11. 급여 4분위 (NTILE)

> 전 직원을 **급여 내림차순으로 4개 등급(사분위)**으로 나눠 `사번·급여·분위`를 조회하세요.

**정답 (Oracle)**
```sql
SELECT emp_id, salary,
       NTILE(4) OVER (ORDER BY salary DESC) AS quartile
FROM EMPLOYEE
ORDER BY salary DESC;
```
**해설**
- `NTILE(n)`은 정렬된 행을 **n개 그룹으로 최대한 균등 분배**. 7명/4 → 앞 그룹부터 2,2,2,1명.
- 결과: 1분위(한사장·조개발), 2분위(윤영업·김개발), 3분위(이코딩·박세일), 4분위(최인사).

**⚠️ 함정**: 나눠떨어지지 않으면 **앞쪽 그룹이 1명 더** 많음. "상위 25%"는 `quartile = 1` 필터.

---

## 문제 12. 부서별 최고/최저 급여 나란히 (FIRST_VALUE / LAST_VALUE)

> 각 직원 행에 **자기 부서의 최고 급여와 최저 급여**를 함께 표시하세요.

**정답 (Oracle)**
```sql
SELECT emp_id, dept_id, salary,
       FIRST_VALUE(salary) OVER (
           PARTITION BY dept_id ORDER BY salary DESC
           ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS dept_max,
       LAST_VALUE(salary) OVER (
           PARTITION BY dept_id ORDER BY salary DESC
           ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS dept_min
FROM EMPLOYEE
WHERE dept_id IS NOT NULL
ORDER BY dept_id, salary DESC;
```
**해설**
- 결과: 개발 max 8,000,000 / min 5,000,000, 영업 7,000,000 / 4,800,000, 인사 4,500,000 동일.
- ⭐ **`LAST_VALUE`의 함정**: 기본 윈도우 프레임은 "시작~현재행"이라 그대로 쓰면 **현재 행 값**이 나옴. 반드시 **`ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`**로 프레임을 열어야 진짜 마지막 값.

**⚠️ 함정**: 프레임 미지정 `LAST_VALUE`는 십중팔구 의도와 다름 → 프레임 명시. (또는 그냥 `MAX/MIN OVER(PARTITION BY ...)` 사용)

---

## 문제 13. 부서 내 급여 비중 % (RATIO_TO_REPORT)

> 각 직원의 급여가 **자기 부서 총급여에서 차지하는 비율(%)**을 조회하세요.

**정답 (Oracle)**
```sql
-- 방법 A: 오라클 전용 RATIO_TO_REPORT
SELECT emp_id, dept_id,
       ROUND(RATIO_TO_REPORT(salary) OVER (PARTITION BY dept_id) * 100, 2) AS pct
FROM EMPLOYEE
WHERE dept_id IS NOT NULL
ORDER BY dept_id, pct DESC;

-- 방법 B: 표준 (SUM OVER)
SELECT emp_id, dept_id,
       ROUND(salary * 100 / SUM(salary) OVER (PARTITION BY dept_id), 2) AS pct
FROM EMPLOYEE
WHERE dept_id IS NOT NULL
ORDER BY dept_id, pct DESC;
```
**해설**
- 결과: 개발 43.24/29.73/27.03, 영업 59.32/40.68, 인사 100.
- `RATIO_TO_REPORT`는 "그룹 합 대비 비율"을 바로 주는 오라클 함수. 표준으로는 `값 / SUM() OVER(PARTITION BY)`.

**⚠️ 함정**: 정수 나눗셈 주의(오라클 NUMBER는 소수 OK지만 `ROUND` 자릿수 지정). 반올림 합이 100%가 안 될 수 있음(반올림 오차).

---

## 문제 14. 카테고리 매출 + 총계 (ROLLUP)

> **완료 주문** 카테고리별 매출과 **맨 아래 전체 합계 행**을 함께 조회하세요.

**정답 (Oracle)**
```sql
SELECT NVL(p.category, '전체합계')     AS category,
       SUM(oi.quantity * p.price)       AS sales
FROM ORDERS o
JOIN ORDER_ITEM oi ON o.order_id   = oi.order_id
JOIN PRODUCT p     ON oi.product_id = p.product_id
WHERE o.status = '완료'
GROUP BY ROLLUP(p.category);
```
**해설**
- `GROUP BY ROLLUP(category)`는 카테고리별 합 + **소계/총계 행**을 자동 생성(총계 행은 category가 NULL → `NVL`로 라벨).
- 결과: 전자 3,017,000 / 도서 102,000 / 식품 100,000 / 의류 38,000 / **전체합계 3,257,000**.
- 다차원이면 `GROUP BY ROLLUP(a, b)` 또는 `CUBE(a, b)`, 어떤 컬럼이 소계인지 `GROUPING()`으로 판별.

**⚠️ 함정**: 총계 행의 category는 NULL → `NVL`/`GROUPING`으로 표시. MySQL은 `WITH ROLLUP` 문법이라 다름.

---

## 문제 15. 부서별 직원 명단 한 줄로 (LISTAGG)

> 부서별로 **소속 직원 이름을 사번순으로 콤마 연결**해 한 줄로 조회하세요.

**정답 (Oracle)**
```sql
SELECT dept_id,
       LISTAGG(name, ', ') WITHIN GROUP (ORDER BY emp_id) AS members
FROM EMPLOYEE
WHERE dept_id IS NOT NULL
GROUP BY dept_id
ORDER BY dept_id;
```
**해설**
- `LISTAGG(컬럼, 구분자) WITHIN GROUP (ORDER BY ...)` = 그룹 내 값을 한 문자열로. (MySQL의 `GROUP_CONCAT`)
- 결과: 개발 "조개발, 김개발, 이코딩" / 영업 "윤영업, 박세일" / 인사 "최인사".

**⚠️ 함정**: 연결 결과가 4000자(VARCHAR2 한계) 초과하면 에러 → `ON OVERFLOW TRUNCATE` 옵션(12c+). `WITHIN GROUP` 정렬 빠뜨리면 순서 비결정적.

---

### ✅ 이 장 핵심 (Oracle 고급)
- `NTILE(n)` 균등 분위(앞 그룹이 +1)
- `LAST_VALUE`는 **프레임 열기** 필수
- 비율 `RATIO_TO_REPORT` 또는 `값/SUM() OVER`
- 소계/총계 `ROLLUP`(+`NVL`/`GROUPING`)
- 문자열 집계 `LISTAGG ... WITHIN GROUP`
