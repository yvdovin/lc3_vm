package org.example.lc3_vm.vm.processor;

/**
 * R0 - регистр общего назначения, так же используется для чтения из std.in и записи в std.out.
 * R1, R2, R3, R4, R5, R6, R7 - регистры общего назначения.
 * RPC - регистр счетчика программы, указывает на адрес следующей инструкции в памяти.
 * RCND - условный регистр. Нужен для вейтвления.
 */
public interface Registers {
    char R0 = 0;
    char R1 = 1;
    char R2 = 2;
    char R3 = 3;
    char R4 = 4;
    char R5 = 5;
    char R6 = 6;
    char R7 = 7;
    char RPC = 8;
    char RCND = 9;
}
