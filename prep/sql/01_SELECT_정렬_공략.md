# SQL 01 — SELECT · 조건 · 정렬

> 스키마: [schema.sql](schema.sql) · 기본기지만 **정렬 동점 처리**와 **LIMIT**에서 실수가 잦음.

---

## 문제 1. 서울 사는 회원 (나이 순)

> `MEMBER`에서 **서울에 사는 회원**의 이름(name)과 나이(age)를,
> **나이가 어린 순**으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT name, age
FROM MEMBER
WHERE city = '서울'
ORDER BY age ASC;
```
**해설**
- `WHERE`로 행을 먼저 거르고 `ORDER BY`로 정렬. `ASC`는 생략 가능(기본값)이지만 명시하면 실수 줄어듦.
- 결과: 정하늘(19) → 김철수(25) → 박민수(41).

**⚠️ 함정**: 문자열 조건은 따옴표 필수(`'서울'`). 나이 같은 사람이 있으면 순서가 흔들리므로, 실전에선 `ORDER BY age, name`처럼 2차 키를 붙이는 습관.

**🔗 프로그래머스 대응**: SELECT 카테고리 — "동물 보호소" 계열 기본 조회.

---

## 문제 2. 10만 원 초과 상품 (비싼 순)

> `PRODUCT`에서 **가격이 100000원을 초과**하는 상품의 이름과 가격을,
> **비싼 순**으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT name, price
FROM PRODUCT
WHERE price > 100000
ORDER BY price DESC;
```
**해설**
- 숫자 비교엔 따옴표 X. `>`(초과)와 `>=`(이상) 구분 — 지문의 "초과/이상/미만/이하"에 밑줄 긋는 습관.
- 결과: 노트북(1,200,000) → 모니터(350,000).

**⚠️ 함정**: "초과"인데 `>=`로 쓰면 경계값 하나 틀림. 롯데는 점수를 안 보여주므로 이런 경계 실수를 스스로 잡아야 함.

**🔗 프로그래머스 대응**: SELECT — 조건 필터 + 정렬.

---

## 문제 3. 회원이 사는 도시 목록

> `MEMBER`에 등장하는 **도시(city)를 중복 없이** 가나다순으로 조회하세요.

**정답 (Oracle)**
```sql
SELECT DISTINCT city
FROM MEMBER
ORDER BY city ASC;
```
**해설**
- `DISTINCT`는 조회 결과 행 전체의 중복을 제거. 결과: 대구, 부산, 서울.
- 여러 컬럼에 `DISTINCT`를 쓰면 "그 조합"이 유일한 행만 남음.

**⚠️ 함정**: `DISTINCT`는 특정 한 컬럼에만 거는 게 아니라 `SELECT` 목록 전체에 적용됨. `SELECT DISTINCT a, b`는 `a`만 유일이 아니라 `(a,b)` 조합이 유일.

**🔗 프로그래머스 대응**: SELECT — "중복 제거".

---

## 문제 4. 가장 비싼 상품 3개 (LIMIT)

> `PRODUCT`에서 **가장 비싼 상품 3개**의 이름과 가격을 비싼 순으로 조회하세요.

**정답 (Oracle)** — ⭐ Oracle엔 `LIMIT`이 없다
```sql
-- 방법 A (모든 버전): 정렬한 인라인 뷰를 ROWNUM으로 자르기
SELECT name, price
FROM (
    SELECT name, price FROM PRODUCT ORDER BY price DESC
)
WHERE ROWNUM <= 3;

-- 방법 B (Oracle 12c+): FETCH FIRST
SELECT name, price
FROM PRODUCT
ORDER BY price DESC
FETCH FIRST 3 ROWS ONLY;
```
**해설**
- 결과: 노트북, 모니터, 이어폰.
- **핵심 함정**: `SELECT * FROM PRODUCT WHERE ROWNUM<=3 ORDER BY price DESC`는 **틀림**. `ROWNUM`은 `ORDER BY`보다 **먼저** 부여되므로, 정렬 안 된 상태에서 3개를 자른 뒤 정렬함 → 엉뚱한 결과.
- 그래서 **정렬을 인라인 뷰 안에서 끝낸 뒤** 바깥에서 `ROWNUM`으로 자르는 게 정석. (그룹별 상위 K는 윈도우함수 → [05](05_윈도우함수_공략.md))

**⚠️ 함정**: MySQL의 `LIMIT 3` 습관이 그대로 나가면 Oracle에서 문법 에러. "왜 ROWNUM에 ORDER BY 직접 쓰면 안 되나"는 **오라클 면접 단골 질문**이니 원리까지 기억.

**🔗 프로그래머스 대응**: SELECT — "상위 n개"(Oracle 환경 선택).

---

### ✅ 이 장 핵심
- 지문의 **초과/이상**, **오름/내림**, **상위 n**에 밑줄.
- 정렬은 **동점 2차 키**까지 생각.
- `DISTINCT`는 컬럼 조합 전체에 적용.
