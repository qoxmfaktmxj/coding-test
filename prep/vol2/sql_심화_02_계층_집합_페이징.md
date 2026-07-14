# 2편 · SQL 심화 (2) 계층·셀프조인·집합·페이징 (Oracle)

> **오라클 고유 기능**(CONNECT BY, MINUS, ROWNUM 페이징)이 핵심. 다른 DB엔 없거나 문법이 달라 오라클 면접에서 자주 물음.

---

## 문제 6. 조직도 계층 조회 (CONNECT BY) ⭐오라클 시그니처

> 사장(`manager_id IS NULL`)을 최상위로, **조직 계층 레벨(LEVEL)**과 함께 전 직원을 조회하세요.

**정답 (Oracle)**
```sql
SELECT LEVEL AS lvl, emp_id, name, manager_id
FROM EMPLOYEE
START WITH manager_id IS NULL          -- 루트: 사장
CONNECT BY PRIOR emp_id = manager_id   -- 부모.emp_id = 자식.manager_id
ORDER SIBLINGS BY emp_id;
```
**해설**
- `START WITH`가 루트, `CONNECT BY PRIOR 부모키 = 자식키`로 아래로 전개. `LEVEL`은 의사컬럼(깊이).
- 결과: L1=한사장 / L2=조개발·윤영업·최인사 / L3=김개발·이코딩·박세일.
- 표준 SQL로는 **재귀 CTE**로 동일 구현:
```sql
WITH org (emp_id, name, lvl) AS (
    SELECT emp_id, name, 1 FROM EMPLOYEE WHERE manager_id IS NULL
    UNION ALL
    SELECT e.emp_id, e.name, o.lvl + 1
    FROM EMPLOYEE e JOIN org o ON e.manager_id = o.emp_id
)
SELECT lvl, emp_id, name FROM org ORDER BY lvl, emp_id;
```

**⚠️ 함정**: `PRIOR` 위치가 방향을 정함(`PRIOR emp_id = manager_id` = 위→아래). 반대로 쓰면 거꾸로 올라감. 사이클 있으면 `CONNECT BY NOCYCLE`.

---

## 문제 7. 같은 도시 회원 쌍 (셀프 조인)

> **같은 도시에 사는 회원 쌍**을 (회원ID 작은 쪽, 큰 쪽) 형태로 조회하세요.

**정답 (Oracle)**
```sql
SELECT a.member_id AS m1, b.member_id AS m2, a.city
FROM MEMBER a
JOIN MEMBER b ON a.city = b.city
             AND a.member_id < b.member_id     -- 중복/자기자신 제거
ORDER BY a.member_id, b.member_id;
```
**해설**
- 같은 테이블을 두 번(`a`,`b`) 조인 = 셀프 조인. `a.id < b.id` 조건이 `(1,3)`과 `(3,1)` 중복, `(1,1)` 자기쌍을 한 번에 제거.
- 결과: 서울 (1,3)(1,5)(3,5), 부산 (2,6) — 총 4쌍.

**⚠️ 함정**: 조건을 `<>`(부등호)로 하면 `(1,3)`과 `(3,1)`이 둘 다 나와 2배가 됨. 반드시 `<`.

---

## 문제 8. 특정 상품 안 산 회원 (NOT EXISTS)

> **노트북(product_id=1)을 한 번도 주문하지 않은** 회원의 ID를 조회하세요.

**정답 (Oracle)**
```sql
SELECT member_id
FROM MEMBER m
WHERE NOT EXISTS (
    SELECT 1
    FROM ORDERS o
    JOIN ORDER_ITEM oi ON o.order_id = oi.order_id
    WHERE o.member_id = m.member_id
      AND oi.product_id = 1
)
ORDER BY member_id;
```
**해설**
- `NOT EXISTS`는 상관 서브쿼리가 **한 건도 없을 때** 참. 결과: 박민수·최지은·정하늘·강보라(3,4,5,6).
- 노트북 산 사람은 김철수(101)·이영희(108)뿐이므로 나머지가 정답.

**⚠️ 함정**: `NOT IN (서브쿼리)`은 서브쿼리에 NULL이 하나라도 있으면 **전체가 공집합**이 되는 함정. "~아닌"은 `NOT EXISTS`가 안전.

---

## 문제 9. 완료했지만 취소 이력 없는 회원 (MINUS) ⭐오라클

> **완료 주문을 한 회원**에서 **취소 주문 이력이 있는 회원**을 뺀 목록을 조회하세요.

**정답 (Oracle)**
```sql
SELECT member_id FROM ORDERS WHERE status = '완료'
MINUS
SELECT member_id FROM ORDERS WHERE status = '취소'
ORDER BY member_id;
```
**해설**
- `MINUS`는 위 집합에서 아래 집합을 뺌(차집합), 중복도 자동 제거.
- 완료 회원 {1,2,3,5} − 취소 회원 {3} = **{1,2,5}**.
- (표준/타 DB는 `EXCEPT`. Oracle은 `MINUS`.) 합집합은 `UNION`(중복제거)/`UNION ALL`(중복유지), 교집합 `INTERSECT`.

**⚠️ 함정**: `MINUS`/`UNION`은 **양쪽 SELECT의 컬럼 수·타입이 일치**해야 함. `ORDER BY`는 맨 마지막에 한 번만.

---

## 문제 10. 페이징 — 가격 3~5위 (ROWNUM) ⭐오라클

> 상품을 **가격 내림차순**으로 정렬했을 때 **3위~5위** 상품을 조회하세요.

**정답 (Oracle)**
```sql
-- 방법 A (모든 버전): 이중 인라인 뷰 + ROWNUM
SELECT product_id, name, price
FROM (
    SELECT p.*, ROWNUM AS rn
    FROM (
        SELECT product_id, name, price
        FROM PRODUCT
        ORDER BY price DESC
    ) p
    WHERE ROWNUM <= 5           -- 상한(5위)까지 자른 뒤
)
WHERE rn >= 3;                  -- 하한(3위)부터

-- 방법 B (12c+): OFFSET / FETCH
SELECT product_id, name, price
FROM PRODUCT
ORDER BY price DESC
OFFSET 2 ROWS FETCH NEXT 3 ROWS ONLY;
```
**해설**
- `ROWNUM`은 정렬 전에 매겨지므로 **① 정렬 인라인뷰 → ② ROWNUM으로 상한 → ③ 바깥에서 하한** 3단 구조가 오라클 페이징 정석.
- 결과: 3~5위 = 이어폰(89,000)·청바지(59,000)·자바의정석(38,000).

**⚠️ 함정**: `WHERE ROWNUM >= 3`을 **직접** 쓰면 항상 공집합(ROWNUM은 1부터 순차 확정이라 첫 행이 3이 될 수 없음). 하한 필터는 반드시 **바깥 단계**에서 별칭(rn)으로.

---

### ✅ 이 장 핵심 (Oracle 고유)
- 계층: `START WITH ... CONNECT BY PRIOR`, 의사컬럼 `LEVEL`
- 셀프조인 쌍은 `a.id < b.id`
- "~아닌"은 `NOT EXISTS`(NOT IN + NULL 함정 회피)
- 집합: `MINUS`/`UNION`/`INTERSECT` (컬럼 수·타입 일치)
- 페이징: `ROWNUM`은 이중 인라인뷰, 또는 12c+ `OFFSET/FETCH`
