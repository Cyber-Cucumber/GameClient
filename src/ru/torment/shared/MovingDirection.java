package ru.torment.shared;

// Направление движения объекта
//   LEFT_up - означает, что объект двигается налево-вверх, НО отклонение вверх не большое;
//   UP_LEFT - означает, что объект двигается налево-вверх, примерно по диогонали;
//   UP_left - означает, что объект двигается налево-вверх, НО отклонение налево не большое
public enum MovingDirection { LEFT, LEFT_up, UP_LEFT, UP_left, UP, UP_right, UP_RIGHT, RIGHT_up, RIGHT, RIGHT_down, DOWN_RIGHT, DOWN_right, DOWN, DOWN_left, DOWN_LEFT, LEFT_down }
