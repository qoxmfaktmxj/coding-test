# Day 1: 구현·시뮬레이션: 상태를 그대로 옮기기

- 기준 문제: 14503 로봇 청소기, 20055 컨베이어 벨트 위의 로봇
- Master 함수: `public int[] solution(int[][] board, int[] start, String commands)`
- Practice 함수: `public int[] solution(int[][] warehouse, int[] start, String[] commands)`
- 예시: warehouse={{0,0,0,0},{0,1,1,0},{0,0,0,0}}, start={2,0,0}, commands={"GO 2","RIGHT","GO 3","RIGHT","GO 2"} -> {2,3,2,7}
