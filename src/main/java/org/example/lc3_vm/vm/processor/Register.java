package org.example.lc3_vm.vm.processor;

/**
 * R0 - регистр общего назначения, так же используется для чтения из std.in и записи в std.out.
 * R1, R2, R3, R4, R5, R6, R7 - регистры общего назначения.
 * RPC - регистр счетчика программы, указывает на адрес следующей инструкции в памяти.
 * RCND - условный регистр. Нужен для вейтвления.
 */
public enum Register {
    R0(0),
    R1(1),
    R2(2),
    R3(3),
    R4(4),
    R5(5),
    R6(6),
    R7(7),
    RPC(8),
    RCND(9);

    private final int position;

    Register(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

}
