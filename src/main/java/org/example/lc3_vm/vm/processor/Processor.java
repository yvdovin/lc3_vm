package org.example.lc3_vm.vm.processor;


import org.example.lc3_vm.vm.trap.TrapHandler;

import static org.example.lc3_vm.vm.memory.Memory.memoryRead;
import static org.example.lc3_vm.vm.processor.Register.RCND;
import static org.example.lc3_vm.vm.processor.Register.RPC;
import static org.example.lc3_vm.vm.utils.Utils.*;

/**
 * Процессор состоит из 10 регистров размером 16 бит.
 * Описание регистров см. в org.example.lc3_vm.vm.processor.Register
 * Может выполнять 16 инструкций. На вход каждый метод принимает инструкцию.
 * Описание см. над методами инструкций.
 */
public class Processor {
    private final static int NUMBER_OF_REGISTERS = 10;
    private final static char[] registers = new char[NUMBER_OF_REGISTERS];

    public static void registerWrite(Register register, char value) {
        registers[register.getPosition()] = value;
    }

    public static char registerRead(Register register) {
        return registers[register.getPosition()];
    }

    /*
     * инструкция вейтвления
     */
    public static void br(char i) {
//        if ((char) (registers[RCND.ordinal()] & getDestinationRegister(i)) > 0) {
//            registers[RPC.ordinal()] += getOffset9(i);
//        }
    }

    /**
     * Складывает 2 числа. Имеет 2 режима:
     * 1) flag = 0 : складывает 2 числа из 2-х регистров и кладет в регистр назначения
     * 2) flag = 1 : складывает 1 число из регистра с некоторым числом(занимает 5 бит.
     * T.к. первый бит - это знак, принимает значения от -16 до 15), кладет в регистр назначения
     */
    public static void add(char i) {
        char flag = extractAddTypeFlag(i);
        char destReg = extractDestinationRegister(i);
        char sourceReg1 = extractSourceRegister1(i);
        if (flag == 0) {
            char sourceReg2 = extractSourceRegister2(i);
            registers[destReg] = (char) (registers[sourceReg1] + registers[sourceReg2]);
        } else {
            registers[destReg] = (char) (registers[sourceReg1] + extractLiteralValue(i, 5));
        }
        //TODO
        //uf(getDestinationRegister(i));
    }

    /**
     * Загружает из памяти в регистр назначения. Берет offset из последних 9 бит и
     * загружает из адреса относительно RPC
     */
    public static void ld(char i) {
        char destReg = extractDestinationRegister(i);
        registers[destReg] = memoryRead((char) (registers[RPC.getPosition()] + extractOffset(i, 9)));
    }

    public static void st(char i) {
//        char sourceRegister = getDestinationRegister(i);
//        char offset = getOffset9(i);
//        memoryWrite((char) (RPC.ordinal() + offset), registers[sourceRegister]);
    }

    public static void jsr(char i) {
//        registers[R7.ordinal()] = registers[RPC.ordinal()];
//        registers[RPC.ordinal()] = (((i >> 10) & 1) == 1) ? (char) (registers[RPC.ordinal()] + getOffset11(i)) : registers[getSourceRegister1(i)];
    }

    public static void and(char i) {
//        char type = extractInstructionTypeFlag(i);
//        char destinationRegister = getDestinationRegister(i);
//        char sourceRegister1 = getSourceRegister1(i);
//        if (type == 0) {
//            char sourceRegister2 = getSourceRegister2(i);
//            registers[destinationRegister] = (char) (registers[sourceRegister1] & registers[sourceRegister2]);
//        } else {
//            registers[destinationRegister] = (char) (registers[sourceRegister1] + convertToChar(getIMM5(i), 5));
//        }
    }

    /**
     * Инструкция косвенной загрузки конкретного офсета относительно базы.
     *
     * @param i
     */
    public static void ldr(char i) {
//        char destinationRegister = getDestinationRegister(i);
//        char base = getSourceRegister1(i);
//        registers[destinationRegister] = memoryRead((char) (registers[base] + convertToChar(getIMM6(i), 6)));
//
    }

    public static void str(char i) {
//        memoryWrite((char) (registers[getSourceRegister1(i)] + convertToChar(getIMM6(i), 6)),
//                registers[getDestinationRegister(i)]);
    }

    public static void rti(char i) {

    }

    public static void not(char i) {
//        registers[getDestinationRegister(i)] = (char) ~registers[getSourceRegister1(i)];
    }

    /**
     * Инструкция косвенной загрузки. Позволяет загрузить то, что не позволяет ld.
     */
    public static void ldi(char i) {
        char destReg = extractDestinationRegister(i);
        registers[destReg] = memoryRead(memoryRead((char) (registers[RPC.ordinal()] + extractOffset(i, 9))));
        //TODO
        //uf(destinationReg);
    }

    public static void sti(char i) {
        //memoryWrite(memoryRead(registers[(char) (RPC.ordinal() + getOffset9(i))]), registers[getDestinationRegister(i)]);
    }

    public static void jmp(char i) {
        //registers[RPC.ordinal()] = registers[getSourceRegister1(i)];
    }

    public static void res(char i) {

    }

    public static void lea(char i) {
//        char destinationRegister = getDestinationRegister(i);
//        char offset = getOffset9(i);
//        registers[destinationRegister] = (char) (registers[RPC.ordinal()] + offset);
    }

    public static void trap(char i) {
        char trapVector = (char) ((i & 0x1F) + 0x20);
        switch (trapVector) {
            case (0x20):
                TrapHandler.tgetc(i);
                break;
            case (0x21):
                TrapHandler.tout(i);
                break;
            case (0x22):
                TrapHandler.tputs(i);
                break;
            case (0x23):
                TrapHandler.tin(i);
                break;
            case (0x24):
                TrapHandler.tputsp(i);
                break;
            case (0x25):
                TrapHandler.thalt(i);
                break;
            case (0x26):
                TrapHandler.tinu16(i);
                break;
            case (0x27):
                TrapHandler.toutu16(i);
                break;
        }
    }

    //TODO
    static void uf(char register) {
        if (registers[register] == 0) {
            registers[RCND.ordinal()] = 1 << 1;
        } else if ((registers[register] >> 15) == 0) {
            registers[RCND.ordinal()] = 1 << 0;
        } else {
            registers[RCND.ordinal()] = 1 << 2;
        }
    }
}
