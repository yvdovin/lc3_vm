package org.example.lc3_vm.vm.memory;

/**
 * Модель памяти представляет из себя массив ячеек размером 16 бит.
 * Размер памяти 65535 ячеек.
 */
public class Memory {

    private final static char[] memory = new char[Character.MAX_VALUE];

    public static char memoryRead(char address) {
        return memory[address];
    }

    public static void memoryWrite(char address, char value) {
        memory[address] = value;
    }
}
