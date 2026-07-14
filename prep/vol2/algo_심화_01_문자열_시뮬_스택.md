# 2편 · 알고리즘 심화 (1) 문자열·시뮬레이션·스택

> 프로그래머스 Lv.2 단골. 알고리즘보다 **정확한 구현**이 승부. 아래 코드는 전부 컴파일·실행 검증됨.

---

## 문제 1. 문자열 압축

> 문자열 `s`를 1글자 이상 **같은 길이 단위로 잘라** 연속 반복을 `횟수+문자열`로 압축할 때,
> 만들 수 있는 **가장 짧은 압축 문자열의 길이**를 반환하세요.
> 예: `"aabbaccc"` → `"2a2ba3c"` = 7

**정답 (검증됨)**
```java
class Solution {
    public int solution(String s) {
        int n = s.length();
        if (n == 1) return 1;
        int best = n;                                  // 안 자르는 경우
        for (int unit = 1; unit <= n / 2; unit++) {    // 자르는 단위
            int comp = 0, i = 0;
            while (i < n) {
                String cur = s.substring(i, Math.min(i + unit, n));
                int cnt = 1, j = i + unit;
                while (j + unit <= n && s.substring(j, j + unit).equals(cur)) {
                    cnt++; j += unit;                  // 같은 조각 연속 카운트
                }
                comp += (cnt == 1 ? 0 : String.valueOf(cnt).length()) + cur.length();
                i = j;
            }
            best = Math.min(best, comp);
        }
        return best;
    }
}
```
**해설**
- 단위 길이를 `1 ~ n/2`까지 **완전탐색**(그 이상은 반복이 안 생김). 각 단위로 앞에서부터 조각을 만들어 연속 동일 조각을 셈.
- 반복 1회면 숫자 안 붙임(`cnt==1` → 길이 0), 2회 이상이면 `숫자자릿수 + 조각길이`.
- 검증: aabbaccc=7, ababcdcdababcdcd=9, abcabcdede=8, abcabcabcabcdededededede=14, xababcdcdababcdcd=17.

**⚠️ 함정**: 숫자 자릿수(`10`은 2자리)를 `String.valueOf(cnt).length()`로 정확히. 끝부분 잘린 조각(unit보다 짧음) 길이 처리. 단위 상한 `n/2`.

**🔗 프로그래머스**: Lv.2 "문자열 압축".

---

## 문제 2. 행렬 90도 회전

> `N × M` 정수 행렬을 **시계방향 90도** 회전한 `M × N` 행렬을 반환하세요.
> 예: `[[1,2,3],[4,5,6]]` → `[[4,1],[5,2],[6,3]]`

**정답 (검증됨)**
```java
class Solution {
    public int[][] solution(int[][] a) {
        int R = a.length, C = a[0].length;
        int[][] r = new int[C][R];
        for (int i = 0; i < R; i++)
            for (int j = 0; j < C; j++)
                r[j][R - 1 - i] = a[i][j];   // 핵심 인덱스 매핑
        return r;
    }
}
```
**해설**
- 시계 90도 회전의 좌표 규칙: **`(i, j) → (j, R-1-i)`**. 결과 배열 크기는 `[C][R]`로 뒤바뀜.
- 반시계면 `(i,j) → (C-1-j, i)`. 180도면 두 번 적용.

**⚠️ 함정**: 회전 후 **행/열 개수가 바뀐다**(정사각형이 아니면 특히). 새 배열을 `[C][R]`로 잡아야 함. 인덱스 매핑 공식을 손으로 2×3 예제에 대입해 확인.

**🔗 프로그래머스/백준**: 배열 회전, 삼성 SW 역량테스트 시뮬 단골.

---

## 문제 3. 올바른 괄호

> 여는 `'('` 과 닫는 `')'` 만으로 이루어진 문자열 `s`가 **올바른 괄호쌍**이면 `true`.

**정답 (검증됨)**
```java
class Solution {
    public boolean solution(String s) {
        int bal = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') bal++;
            else { bal--; if (bal < 0) return false; }  // 닫는데 열린 게 없음
        }
        return bal == 0;                                  // 남은 여는 괄호 없어야
    }
}
```
**해설**
- 여는 +1, 닫는 −1. **중간에 음수가 되면 즉시 실패**(닫는 괄호가 먼저 옴). 끝에 0이어야 완전 매칭.
- 괄호 종류가 여러 개(`()[]{}`)면 **스택**에 여는 괄호를 넣고 닫을 때 짝을 맞춰 pop.

**⚠️ 함정**: 단순 개수 비교(여는 수 == 닫는 수)만으론 `)(` 를 통과시켜 버림. **진행 중 음수 체크**가 필수.

**🔗 프로그래머스**: Lv.2 "올바른 괄호", 스택/큐 카테고리.

---

### ✅ 이 장 핵심
- 문자열 압축: 단위 `1~n/2` 완전탐색 + 숫자 자릿수 주의
- 회전: `(i,j)→(j,R-1-i)`, 결과 크기 `[C][R]`
- 괄호: 진행 중 음수 즉시 실패 + 끝 0 (종류 여러 개면 스택)
