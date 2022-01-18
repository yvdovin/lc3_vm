package org.example.lc3_vm.vm.processor;


import static org.example.lc3_vm.vm.memory.Memory.memoryRead;
import static org.example.lc3_vm.vm.memory.Memory.memoryWrite;
import static org.example.lc3_vm.vm.processor.Registers.*;
import static org.example.lc3_vm.vm.trap.TrapHandler.*;
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

    public static void registerWrite(char register, char value) {
        registers[register] = value;
    }

    public static char registerRead(char register) {
        return registers[register];
    }

    /*
     * Инструкция вейтвления. Перепрыгиваем на оффсет, если значение в регистре RCND совпадает
     * с битами NZP(negative, zero, positive)
     */
    public static void br(char i) {
        if ((char) (registers[RCND] & extractNZPBites(i)) > 0) {
            registers[RPC] += extractLiteralValue(i, 9);
        }
    }

    /**
     * Складывает 2 числа. Имеет 2 режима:
     * 1) flag = 0 : складывает 2 числа из 2-х регистров и кладет в регистр назначения
     * 2) flag = 1 : складывает 1 число из регистра с некоторым числом(занимает 5 бит.
     * T.к. первый бит - это знак, принимает значения от -16 до 15), кладет в регистр назначения
     */
    public static void add(char i) {
        char flag = extractTypeFlag(i);
        char destReg = extractDestinationRegister(i);
        char sourceReg1 = extractSourceRegister1(i);
        if (flag == 0) {
            char sourceReg2 = extractSourceRegister2(i);
            registers[destReg] = (char) (registers[sourceReg1] + registers[sourceReg2]);
        } else {
            registers[destReg] = (char) (registers[sourceReg1] + extractLiteralValue(i, 5));
        }
        fillRCND(destReg);
    }

    /**
     * Загружает из памяти в регистр назначения. Берет offset из последних 9 бит и
     * загружает из адреса относительно RPC
     */
    public static void ld(char i) {
        char destReg = extractDestinationRegister(i);
        registers[destReg] = memoryRead((char) (registers[RPC] + extractLiteralValue(i, 9)));
        fillRCND(destReg);
    }

    /**
     * Загружает содержимое регистра в память.
     * Адрес памяти - это значение регистра RPC + оффсет
     */
    public static void st(char i) {
        char sourceReg = extractSourceRegisterSt(i);
        char offset = extractLiteralValue(i, 9);
        memoryWrite((char) (registers[RPC] + offset), registers[sourceReg]);
    }

    /**
     * Переход выполнения подпрограммы
     */
    public static void jsr(char i) {
        registers[R7] = registers[RPC];
        registers[RPC] = (((i >> 10) & 1) == 1) ?
                (char) (registers[RPC] + extractLiteralValue(i, 11)) :
                registers[extractBase(i)];
    }

    /**
     * Инструкция побитого 'и'. Имеет 2 режима:
     * 1) flag = 0 : использует 2 числа из 2-х регистров и кладет в регистр назначения
     * 2) flag = 1 : использует 1 число из регистра с некоторым числом(занимает 5 бит.
     * T.к. первый бит - это знак, принимает значения от -16 до 15), кладет в регистр назначения
     */
    public static void and(char i) {
        char type = extractTypeFlag(i);
        char destReg = extractDestinationRegister(i);
        char sourceReg1 = extractSourceRegister1(i);
        if (type == 0) {
            char sourceReg2 = extractSourceRegister2(i);
            registers[destReg] = (char) (registers[sourceReg1] & registers[sourceReg2]);
        } else {
            registers[destReg] = (char) (registers[sourceReg1] & extractLiteralValue(i, 5));
        }
        fillRCND(destReg);
    }

    /**
     * Инструкция косвенной загрузки конкретного офсета относительно базы.
     */
    public static void ldr(char i) {
        char destReg = extractDestinationRegister(i);
        char base = extractBase(i);
        char literalValue = extractLiteralValue(i, 6);
        registers[destReg] = memoryRead((char) (registers[base] + literalValue));
        fillRCND(destReg);
    }

    /**
     * Записываем содержимое регистра относительно базы
     */
    public static void str(char i) {
        char sourceReg = extractSourceRegisterSt(i);
        char base = extractBase(i);
        memoryWrite((char) (registers[base] + extractLiteralValue(i, 6)), registers[sourceReg]);
    }

    public static void rti(char i) {

    }

    /**
     * Инструкция "не"
     */
    public static void not(char i) {
        char destReg = extractDestinationRegister(i);
        char sourceReg = extractSourceRegister1(i);
        registers[destReg] = (char) ~registers[sourceReg];
        fillRCND(destReg);
    }

    /**
     * Инструкция косвенной загрузки. Позволяет загрузить то, что не позволяет ld.
     */
    public static void ldi(char i) {
        char destReg = extractDestinationRegister(i);
        registers[destReg] = memoryRead(memoryRead((char) (registers[RPC] + extractLiteralValue(i, 9))));
        fillRCND(destReg);
    }

    /**
     * Косвенная загрузка содержимого регистра в память
     */
    public static void sti(char i) {
        char sourceReg = extractSourceRegisterSt(i);
        char address = memoryRead((char) (registers[RPC] + extractLiteralValue(i, 9)));
        memoryWrite(address, registers[sourceReg]);
    }

    /**
     * Изменяет значение RPC на некоторое значение из базы
     */
    public static void jmp(char i) {
        registers[RPC] = registers[extractBase(i)];
    }

    public static void res(char i) {

    }

    /**
     * Загрузка фактического адреса в регистр
     */
    public static void lea(char i) {
        char destReg = extractDestinationRegister(i);
        char literalValue = extractLiteralValue(i, 9);
        registers[destReg] = (char) (registers[RPC] + literalValue);
        fillRCND(destReg);
    }

    public static void trap(char i) {

        char trapVector = (char) ((i & 0x1F) + 0x20);
        switch (trapVector) {
            case (0x20):
                registers[RPC] = memoryRead((char) 0x0020);
                //tgetc(i);
                break;
            case (0x21):
                tout(i);
                break;
            case (0x22):
                tputs(i);
                break;
            case (0x23):
                tin(i);
                break;
            case (0x24):
                tputsp(i);
                break;
            case (0x25):
                thalt(i);
                break;
            case (0x26):
                //tinu16(i);
                registers[R7] = (char) (registers[RPC]);
                registers[RPC] = memoryRead((char) 0x0020);
                break;
            case (0x27):
                toutu16(i);
                break;
        }
    }

    /**
     * Функция для заполнения регистра RCND. Используется для инструкции вейтвления.
     *
     * @param register
     */
    static void fillRCND(char register) {
        if (registers[register] == 0) {
            registers[RCND] = 1 << 1;
        } else if ((registers[register] >> 15) == 0) {
            registers[RCND] = 1 << 0;
        } else {
            registers[RCND] = 1 << 2;
        }
    }
}
