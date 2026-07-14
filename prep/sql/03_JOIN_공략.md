# SQL 03 — JOIN

> 실무·시험 모두 최빈출. **INNER vs LEFT**, 그리고 **LEFT JOIN + IS NULL**(없는 것 찾기)이 핵심.

---

## 문제 1. 주문한 적 없는 회원 (LEFT JOIN + IS NULL)

> **한 번도 주문한 적이 없는 회원**의 이름을 조회하세요.

**정답 (Oracle)**
```sql
SELECT m.name
FROM MEMBER m
LEFT JOIN ORDERS o ON m.member_id = o.member_id
WHERE o.order_id IS NULL;
```
**해설**
- `LEFT JOIN`은 왼쪽(MEMBER) 전부를 남기고, 매칭되는 주문이 없으면 오른쪽을 NULL로 채움.
- 매칭이 없던 회원만 골라내려면 `WHERE o.order_id IS NULL`. 결과: 강보라.
- **"~한 적 없는" = LEFT JOIN + IS NULL** 은 통째로 암기하는 패턴.

**⚠️ 함정**: `NOT IN (서브쿼리)`로도 가능하지만, 서브쿼리에 NULL이 섞이면 결과가 통째로 사라지는 함정이 있음. LEFT JOIN 방식이 안전.

**🔗 프로그래머스 대응**: JOIN — "없어진 기록 찾기", "있었는데요 없었습니다".

---

## 문제 2. 완료 주문 내역 (INNER JOIN)

> **완료된 주문**에 대해 **주문번호, 주문한 회원 이름, 주문일**을
> 주문번호 순으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT o.order_id, m.name, o.ordered_at
FROM ORDERS o
JOIN MEMBER m ON o.member_id = m.member_id
WHERE o.status = '완료'
ORDER BY o.order_id;
```
**해설**
- `JOIN`(=INNER JOIN)은 양쪽에 다 존재하는 행만. 여기선 모든 주문에 회원이 있으므로 결과 손실 없음.
- 결과: 6건(주문 101,102,103,105,107,108).
- 별칭(`o`, `m`)을 쓰면 컬럼 출처가 명확 → 다중 테이블에서 필수 습관.

**⚠️ 함정**: Oracle `DATE` 컬럼은 세션 설정에 따라 `24/06/05` 또는 시간까지 표시될 수 있음. 형식을 고정하려면 `TO_CHAR(o.ordered_at, 'YYYY-MM-DD')`.

**🔗 프로그래머스 대응**: JOIN 기본.

---

## 문제 3. 상품별 총 판매 수량 (안 팔린 상품 포함)

> **모든 상품**에 대해 **총 판매 수량**을 구하세요. 한 번도 안 팔린 상품은 **0**으로.
> 상품 ID 순으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT p.product_id, p.name,
       NVL(SUM(oi.quantity), 0) AS total_qty
FROM PRODUCT p
LEFT JOIN ORDER_ITEM oi ON p.product_id = oi.product_id
GROUP BY p.product_id, p.name
ORDER BY p.product_id;
```
**해설**
- "모든 상품"이 기준이므로 `PRODUCT`를 왼쪽에 두고 `LEFT JOIN`.
- 안 팔린 상품은 `SUM`이 NULL → `NVL(..., 0)`으로 0 처리. (MySQL `IFNULL`의 오라클판)
- 이 스키마에선 모든 상품이 팔렸지만, "0 포함" 요구는 LEFT JOIN + NVL 패턴을 쓰라는 신호.

**⚠️ 함정**: INNER JOIN으로 하면 안 팔린 상품이 결과에서 사라짐. "모든/전체 ~에 대해"라는 표현은 LEFT JOIN 신호.

**🔗 프로그래머스 대응**: JOIN + 집계.

---

## 문제 4. 회원별 총 결제 금액 (다중 조인)

> **완료된 주문 기준**으로 **회원별 총 결제 금액**(수량 × 단가 합)을
> 많은 순으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT m.member_id, m.name,
       SUM(oi.quantity * p.price) AS total_amount
FROM MEMBER m
JOIN ORDERS o     ON m.member_id = o.member_id AND o.status = '완료'
JOIN ORDER_ITEM oi ON o.order_id = oi.order_id
JOIN PRODUCT p    ON oi.product_id = p.product_id
GROUP BY m.member_id, m.name
ORDER BY total_amount DESC;
```
**해설**
- 4개 테이블 체인 조인: 회원→주문→주문상세→상품.
- 금액 = `quantity * price`의 합. 상태 필터를 **JOIN의 ON 절**에 넣어 완료 주문만.
- 결과: 이영희(1,663,000) → 김철수(1,416,000) → 박민수(153,000) → 정하늘(25,000).

**⚠️ 함정**: 상태 조건을 `WHERE o.status='완료'`에 넣어도 이 경우엔 동일하지만, LEFT JOIN 상황에선 `ON`과 `WHERE`의 위치가 결과를 바꿈(WHERE에 두면 LEFT가 INNER처럼 동작). 금액 합은 큰 수라 실무에선 `BIGINT`/`long` 의식.

**🔗 프로그래머스 대응**: JOIN 다중 + 집계(고난도 단골).

---

### ✅ 이 장 핵심
- **INNER**: 양쪽 다 있는 것 / **LEFT**: 왼쪽 전부 + 없으면 NULL
- **"~한 적 없는" → LEFT JOIN + IS NULL**
- **"모든/전체 ~" → LEFT JOIN (+ NVL 0)**
- 다중 조인은 별칭 + ON 조건 위치 주의
