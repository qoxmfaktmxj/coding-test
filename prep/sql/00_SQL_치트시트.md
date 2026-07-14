# 🗄️ SQL 치트시트 (⭐ Oracle)

> 롯데 코테 SQL은 **Oracle**. MySQL 습관(LIMIT/IFNULL/DATE_FORMAT)이 그대로 나가면 에러.
> 지하철에서 이 한 장만 반복해서 눈에 바르면 Lv.2~3 SQL은 대부분 커버.

## 1. 절 실행 순서 (⭐ 가장 중요)

작성 순서와 **실행 순서가 다르다.**

```
작성:  SELECT → FROM → JOIN → WHERE → GROUP BY → HAVING → ORDER BY
실행:  FROM → JOIN → WHERE → GROUP BY → HAVING → SELECT → ORDER BY
```

**결론**:
- `WHERE`는 그룹핑 **전** 행 필터 / `HAVING`은 그룹핑 **후** 집계 필터
- `SELECT` 별칭은 `WHERE`/`GROUP BY`/`HAVING`에선 **못 씀**, `ORDER BY`에선 **쓸 수 있음**
- 집계함수(`SUM`,`COUNT`…)는 `WHERE`에 못 넣음 → `HAVING`
- ⚠️ **Oracle은 `GROUP BY`에 별칭 불가** → SELECT에서 만든 별칭 말고 **식을 그대로** 반복해야 함 (MySQL과 결정적 차이)

## 2. 기본 골격

```sql
SELECT   컬럼, 집계함수(...)
FROM     테이블 t                 -- Oracle 테이블 별칭엔 AS 안 씀 (컬럼 별칭엔 AS 가능)
JOIN     다른테이블 o ON t.id = o.id
WHERE    행_조건
GROUP BY 그룹_기준식
HAVING   집계_조건
ORDER BY 정렬_기준 [ASC|DESC];
```

## 3. 상위 N개 (⭐ Oracle의 최대 함정 — LIMIT 없음!)

```sql
-- ❌ 틀림: ROWNUM은 ORDER BY보다 먼저 매겨짐
SELECT * FROM PRODUCT WHERE ROWNUM <= 3 ORDER BY price DESC;

-- ✅ 방법 A (모든 버전): 정렬한 인라인 뷰를 ROWNUM으로 자르기
SELECT * FROM (
    SELECT name, price FROM PRODUCT ORDER BY price DESC
) WHERE ROWNUM <= 3;

-- ✅ 방법 B (12c+): FETCH FIRST
SELECT name, price FROM PRODUCT ORDER BY price DESC
FETCH FIRST 3 ROWS ONLY;
```
> 💡 "왜 ROWNUM<=3에 ORDER BY 붙이면 틀리나?"는 오라클 면접 단골. (ROWNUM은 정렬 전에 부여되기 때문)

## 4. JOIN

| 종류 | 의미 |
|---|---|
| `INNER JOIN` | 양쪽 다 있는 행만 |
| `LEFT JOIN` | 왼쪽 전부 + 매칭 안 되면 오른쪽 NULL |

```sql
-- 주문 없는 회원 (LEFT JOIN + IS NULL 국룰)
SELECT m.name
FROM MEMBER m
LEFT JOIN ORDERS o ON m.member_id = o.member_id
WHERE o.order_id IS NULL;
```
> Oracle 구식 표기 `(+)`도 존재: `WHERE m.member_id = o.member_id(+)` (LEFT). 신문법(ANSI JOIN) 권장.

## 5. 집계함수

`COUNT(*)` 전체 / `COUNT(col)` NULL 제외 / `COUNT(DISTINCT col)` 중복 제외. `SUM AVG MAX MIN`.

```sql
SELECT category, COUNT(*) AS cnt, AVG(price) AS avg_price
FROM PRODUCT
GROUP BY category                 -- 실제 컬럼 OK
HAVING COUNT(*) >= 2
ORDER BY avg_price DESC;           -- ORDER BY엔 별칭 OK
```

## 6. 서브쿼리

```sql
-- 스칼라: 전체 평균보다 비싼 상품
SELECT name FROM PRODUCT
WHERE price > (SELECT AVG(price) FROM PRODUCT);

-- IN: 완료 주문한 회원
SELECT name FROM MEMBER
WHERE member_id IN (SELECT member_id FROM ORDERS WHERE status='완료');

-- 상관: 카테고리별 최고가 상품
SELECT p.* FROM PRODUCT p
WHERE p.price = (SELECT MAX(price) FROM PRODUCT x WHERE x.category = p.category);
```

## 7. 윈도우(분석)함수 — Oracle이 원조, 완전 지원

```sql
함수() OVER (PARTITION BY 그룹 ORDER BY 정렬)
```
| 함수 | 동점 처리 |
|---|---|
| `ROW_NUMBER()` | 1,2,3 |
| `RANK()` | 1,1,3 |
| `DENSE_RANK()` | 1,1,2 |

```sql
-- 부서별 연봉 1위 (그룹별 TOP-N 정석)
SELECT * FROM (
  SELECT name, dept_id, salary,
         ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) rn
  FROM EMPLOYEE
) WHERE rn = 1;
```

## 8. 날짜 (⭐ Oracle)

```sql
SYSDATE                              -- 현재 (MySQL의 NOW())
TO_CHAR(ordered_at, 'YYYY-MM-DD')    -- 포맷팅 (MySQL DATE_FORMAT)
TO_DATE('2024-06-01','YYYY-MM-DD')   -- 문자 → 날짜
DATE '2024-06-01'                    -- 날짜 리터럴 (권장)
EXTRACT(YEAR FROM d)                 -- 연도 (MySQL YEAR())
EXTRACT(MONTH FROM d)
d1 - d2                              -- 일수 차 (숫자)
d + 7                                -- 7일 뒤
MONTHS_BETWEEN(d1, d2)               -- 개월 차
-- 범위: WHERE d >= DATE '2024-06-01' AND d < DATE '2024-07-01'
```
> ⚠️ DATE 컬럼을 문자열('2024-06-01')과 직접 비교하면 NLS 설정 의존 → **`DATE` 리터럴이나 `TO_DATE`로 명시**.

## 9. 문자열 (⭐ Oracle)

```sql
a || '-' || b            -- 연결 (MySQL CONCAT), CONCAT은 인자 2개만
SUBSTR(s, 1, 3)          -- 1번째부터 3글자 (1-index)
LENGTH(s)                -- 글자 수 (Oracle은 문자 기준! 바이트는 LENGTHB)
INSTR(s, 'x')            -- 위치 찾기
UPPER LOWER TRIM
REPLACE(s, 'a', 'b')
LPAD(s, 5, '0')          -- 자리 채우기
s LIKE '김%'             -- 김으로 시작
REGEXP_LIKE(s, '^[0-9]+$')  -- 정규식 (MySQL의 REGEXP)
```
> 💡 MySQL과 달리 Oracle `LENGTH`는 이미 글자 수. (한글도 1글자=1)

## 10. NULL·조건 분기 (⭐ Oracle)

```sql
NVL(col, 0)                    -- NULL이면 0 (MySQL IFNULL)
NVL2(col, '있음', '없음')       -- NULL 아니면 첫째, NULL이면 둘째
COALESCE(a, b, c)              -- 처음 non-NULL (표준, Oracle도 OK)
CASE WHEN age < 20 THEN '10대'
     WHEN age < 30 THEN '20대'
     ELSE '30대+' END AS age_group
DECODE(gender,'M','남','F','여','기타')  -- Oracle 전용 간이 분기
```
> ⚠️ `col = NULL` (X) → `col IS NULL` (O).

## 11. 자주 하는 실수 TOP 8 (Oracle)
1. **`LIMIT` 습관** → Oracle엔 없음. ROWNUM 인라인뷰 / FETCH FIRST
2. **`ROWNUM <= n` 에 `ORDER BY` 직접** → 정렬 전에 잘림. 인라인뷰로 감싸기
3. `IFNULL` → **`NVL`**, `DATE_FORMAT` → **`TO_CHAR`**, `YEAR()` → **`EXTRACT`**
4. **`GROUP BY 별칭` 사용** → Oracle 불가, 식을 반복
5. 집계 조건을 `WHERE`에 → `HAVING`
6. `CONCAT(a,b,c)` 3개 인자 → Oracle CONCAT은 2개만, `||` 사용
7. DATE 컬럼을 문자열과 직접 비교 (NLS 의존)
8. 정렬 동점 처리 안 함 → 2차 정렬 키 추가
