package org.example.lc3_vm.vm.utils;

/**
 * Класс вспомогательных функций для выполнении инструкций
 */
public class Utils {

    /**
     * Тип инструкции add и and находится в 5-м бите
     */
    public static char extractTypeFlag(char instruction) {
        return (char) ((instruction >> 5) & 1);
    }

    /**
     * Для всех инстукций находится с 5 по 7 битах
     */
    public static char extractDestinationRegister(char instruction) {
        return (char) ((instruction >> 9) & 0x7);
    }

    /**
     * Для инстукции br, находится с 5 по 7 битах
     */
    public static char extractNZPBites(char instruction) {
        return (char) ((instruction >> 9) & 0x7);
    }

    /**
     * Для всех инструкций находится с 8 по 10 бит
     */
    public static char extractSourceRegister1(char instruction) {
        return (char) ((instruction >> 6) & 0x7);
    }

    /**
     * Для всех инструкций находится в последних трех битах
     */
    public static char extractSourceRegister2(char instruction) {
        return (char) (instruction & 0x7);
    }

    /**
     * Достает число из конца инструкции исходя из @param offset,
     * конвертирует его в char.
     */
    public static char extractLiteralValue(char instruction, int offset) {
        char literalValue = (char) (instruction & 0x1F);
        return convertToChar(literalValue, offset);
    }

    /**
     * если b-й бит n равен 1 (число отрицательно),
     * заполняем оставшиеся 15 бит 1-ми.
     * В противном случае возвращаем число как есть
     */
    private static char convertToChar(char n, int b) {
        return (((n >> (b - 1)) & 1) == 1) ?
                (char) (n | (0xFFFF << b)) : n;
    }

    public static char getOptCode(char instruction) {
        return (char) (instruction >> 12);
    }

    /**
     * @param instruction
     * @return
     */
    public static char extractSourceRegisterSt(char instruction) {
        return (char) ((instruction >> 9) & 0x7);
    }

    public static char extractBase(char instruction) {
        return (char) ((instruction >> 6) & 0x7);
    }

}
