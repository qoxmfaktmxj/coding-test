# 2편 · SQL 심화 (1) 집계 & 분석함수 (Oracle)

> 1편 기본기 위에 **경력직 변별 포인트**(윈도우/분석함수)를 얹습니다. 스키마는 1편 [schema.sql](../sql/schema.sql) 동일.
> 모든 결과는 검증 완료. 문법은 Oracle.

---

## 문제 1. 단골 회원 매출 (다중조인 + 집계 + HAVING)

> **완료 주문 기준**, **주문을 2건 이상** 한 회원의 `회원ID·주문건수·총매출`을 매출 내림차순으로.

**정답 (Oracle)**
```sql
SELECT o.member_id,
       COUNT(DISTINCT o.order_id)   AS cnt,
       SUM(oi.quantity * p.price)   AS amt
FROM ORDERS o
JOIN ORDER_ITEM oi ON o.order_id   = oi.order_id
JOIN PRODUCT p     ON oi.product_id = p.product_id
WHERE o.status = '완료'
GROUP BY o.member_id
HAVING COUNT(DISTINCT o.order_id) >= 2
ORDER BY amt DESC;
```
**해설**
- 조인으로 행이 뻥튀기되므로 주문 건수는 `COUNT(DISTINCT o.order_id)`로 세야 정확(그냥 `COUNT(*)`면 상품 라인 수가 됨).
- 결과: 이영희(2건, 1,663,000) → 김철수(2건, 1,416,000).

**⚠️ 함정**: 조인 후 `COUNT(*)`/`SUM`이 중복 계산되는지 항상 의심. 건수는 DISTINCT, 금액은 라인별 `수량×단가` 합.

---

## 문제 2. 직전 주문과의 간격 (LAG)

> 각 회원의 주문을 **날짜순**으로 나열하고, **직전 주문일로부터 며칠 만의 주문인지**(gap_days)를 구하세요.

**정답 (Oracle)**
```sql
SELECT member_id, order_id, ordered_at,
       ordered_at - LAG(ordered_at)
         OVER (PARTITION BY member_id ORDER BY ordered_at) AS gap_days
FROM ORDERS
ORDER BY member_id, ordered_at;
```
**해설**
- `LAG(col) OVER(...)`는 **같은 파티션에서 바로 앞 행의 값**을 가져옴(첫 행은 NULL).
- Oracle은 **날짜 - 날짜 = 일수(숫자)**라 `ordered_at - LAG(...)`이 바로 gap.
- 결과 예: 김철수 101→102 = 15일, 이영희 103→108 = 51일, 박민수 104→105 = 3일.

**⚠️ 함정**: `LEAD`는 반대(다음 행). 정렬 기준을 안 주면 결과가 비결정적. MySQL엔 `LAG`가 8.0부터라 버전 의존이지만 **Oracle은 기본 지원**.

---

## 문제 3. 매출 누적합 (SUM OVER ORDER BY)

> **완료 주문**을 날짜순으로 정렬하면서 **누적 매출**을 함께 보여주세요.

**정답 (Oracle)**
```sql
SELECT order_id, ordered_at,
       SUM(amt) OVER (ORDER BY ordered_at, order_id) AS running_total
FROM (
    SELECT o.order_id, o.ordered_at,
           SUM(oi.quantity * p.price) AS amt
    FROM ORDERS o
    JOIN ORDER_ITEM oi ON o.order_id   = oi.order_id
    JOIN PRODUCT p     ON oi.product_id = p.product_id
    WHERE o.status = '완료'
    GROUP BY o.order_id, o.ordered_at
)
ORDER BY ordered_at, order_id;
```
**해설**
- 먼저 인라인 뷰에서 **주문별 매출**을 만든 뒤, 바깥에서 `SUM() OVER(ORDER BY ...)`로 누적.
- `OVER`에 `ORDER BY`가 있으면 기본 윈도우가 "처음부터 현재 행까지" → 누적합.
- 결과 마지막 행 = 전체 합 3,257,000.

**⚠️ 함정**: `SUM() OVER(ORDER BY ...)`(누적)와 `SUM() OVER(PARTITION BY ...)`(그룹 전체 동일값)를 혼동하지 말 것.

---

## 문제 4. 부서별 급여 상위 2명 (RANK ≤ 2)

> 각 부서에서 **급여 상위 2명**의 `사번·부서·순위`를 조회하세요.

**정답 (Oracle)**
```sql
SELECT emp_id, dept_id, rnk
FROM (
    SELECT emp_id, dept_id,
           RANK() OVER (PARTITION BY dept_id ORDER BY salary DESC) AS rnk
    FROM EMPLOYEE
    WHERE dept_id IS NOT NULL
)
WHERE rnk <= 2
ORDER BY dept_id, rnk;
```
**해설**
- 1편의 "그룹별 1위(rn=1)"를 **상위 N(rnk≤2)**으로 확장한 형태.
- 결과: 개발=조개발/김개발, 영업=윤영업/박세일, 인사=최인사(1명뿐).

**⚠️ 함정**: 동점 처리 정책에 따라 `RANK`(1,1,3) vs `DENSE_RANK`(1,1,2) vs `ROW_NUMBER`(1,2,3) 선택. "상위 2명 정확히"면 `ROW_NUMBER` + tie-breaker.

---

## 문제 5. 월별 매출 & 전월 대비 증감 (TO_CHAR + LAG)

> **완료 주문**을 **월별**로 집계하고, **전월 대비 증감액**을 구하세요.

**정답 (Oracle)**
```sql
SELECT ym, sales,
       sales - LAG(sales) OVER (ORDER BY ym) AS diff
FROM (
    SELECT TO_CHAR(o.ordered_at, 'YYYY-MM')     AS ym,
           SUM(oi.quantity * p.price)            AS sales
    FROM ORDERS o
    JOIN ORDER_ITEM oi ON o.order_id   = oi.order_id
    JOIN PRODUCT p     ON oi.product_id = p.product_id
    WHERE o.status = '완료'
    GROUP BY TO_CHAR(o.ordered_at, 'YYYY-MM')   -- 별칭 말고 식 반복!
)
ORDER BY ym;
```
**해설**
- `TO_CHAR(d,'YYYY-MM')`로 월 버킷 생성 → 집계 → `LAG`로 전월 값과 비교.
- 결과: 2024-05 = 113,000 / 06 = 1,594,000(+1,481,000) / 07 = 1,550,000(−44,000).

**⚠️ 함정**: Oracle은 `GROUP BY` 별칭 불가 → `TO_CHAR(...)` 식을 그대로 반복. 월 정렬은 `'YYYY-MM'` 문자열이라 사전순=시간순이 성립(자리수 고정 덕분).

---

### ✅ 이 장 핵심
- 조인 후 집계는 **중복(DISTINCT/라인합)** 주의
- `LAG/LEAD`(이웃 행), `SUM() OVER(ORDER BY)`(누적)
- 그룹별 상위 N = 분석함수 서브쿼리 + `rnk ≤ N`
- 월버킷은 `TO_CHAR(d,'YYYY-MM')`, GROUP BY엔 식 반복
