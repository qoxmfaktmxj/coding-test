# SQL 02 — 집계 · GROUP BY · HAVING

> 경력직 SQL의 **8할이 여기서 갈림**. "행 필터(WHERE) vs 집계 필터(HAVING)"를 몸에 발라야 함.

---

## 문제 1. 카테고리별 상품 통계

> `PRODUCT`에서 **카테고리별 상품 수와 평균 가격**을 구하되,
> **상품이 2개 이상인 카테고리만**, **평균 가격이 비싼 순**으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT category,
       COUNT(*)   AS cnt,
       AVG(price) AS avg_price
FROM PRODUCT
GROUP BY category
HAVING COUNT(*) >= 2
ORDER BY avg_price DESC;
```
**해설**
- `GROUP BY category`로 카테고리 단위로 묶고, 각 그룹에 집계함수 적용.
- **"2개 이상인 카테고리만"은 집계 결과에 대한 조건 → `HAVING`** (WHERE 불가).
- 결과: 전자(3개, 546,333) → 의류(2개, 39,000) → 도서(2개, 35,000). (식품은 1개라 제외)

**⚠️ 함정**: `HAVING COUNT(*)>=2`를 `WHERE COUNT(*)>=2`로 쓰면 에러. WHERE는 그룹핑 전이라 집계함수를 모름.

**🔗 프로그래머스 대응**: GROUP BY — "고양이와 개는 몇 마리", "카테고리별 도서 판매량".

---

## 문제 2. 회원별 완료 주문 건수

> `ORDERS`에서 **상태가 '완료'인 주문**을 대상으로, **회원별 주문 건수**를
> **많은 순**으로 조회하세요. (건수 같으면 회원 ID 오름차순)

**정답 (Oracle)**
```sql
SELECT member_id, COUNT(*) AS order_cnt
FROM ORDERS
WHERE status = '완료'
GROUP BY member_id
ORDER BY order_cnt DESC, member_id ASC;
```
**해설**
- **'완료'만 남기는 건 행 조건 → `WHERE`** (그룹핑 전에 걸러야 빠르고 정확).
- 그 다음 `GROUP BY member_id`로 세기. 결과: m1=2, m2=2, m3=1, m5=1.
- 동점(2건)인 m1·m2는 2차 정렬 키 `member_id`로 순서 확정.

**⚠️ 함정**: WHERE와 HAVING을 헷갈리면 논리는 맞아도 성능/정확성이 흔들림. **"행 조건=WHERE, 집계 조건=HAVING"** 암기.

**🔗 프로그래머스 대응**: GROUP BY + 동점 정렬.

---

## 문제 3. 평균 연봉이 높은 부서

> `EMPLOYEE`에서 **부서(dept_id)가 있는 직원**을 대상으로,
> **부서별 평균 연봉이 500만 원 이상**인 부서를 평균 연봉 내림차순으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT dept_id, AVG(salary) AS avg_sal
FROM EMPLOYEE
WHERE dept_id IS NOT NULL
GROUP BY dept_id
HAVING AVG(salary) >= 5000000
ORDER BY avg_sal DESC;
```
**해설**
- `WHERE dept_id IS NOT NULL` = 사장(부서 없음) 제외 → **행 조건**.
- `HAVING AVG(salary) >= 5000000` = 집계 결과 조건.
- 결과: 개발(6,166,666) → 영업(5,900,000). 인사(4,500,000)는 미달로 제외.

**⚠️ 함정**: NULL은 `= NULL`이 아니라 `IS NULL`/`IS NOT NULL`. 그리고 이 필터는 집계 전이라 WHERE가 맞음.

**🔗 프로그래머스 대응**: GROUP BY + HAVING 조합(고득점 단골).

---

### ✅ 이 장 핵심
- **WHERE = 그룹핑 전 행 필터 / HAVING = 그룹핑 후 집계 필터**
- 집계함수: `COUNT(*)`(전체행), `COUNT(col)`(NULL 제외), `SUM/AVG/MAX/MIN`
- 정렬은 항상 **동점 2차 키**까지
