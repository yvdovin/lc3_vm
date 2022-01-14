package org.example.lc3_vm.vm.trap;

import java.util.Scanner;

import static org.example.lc3_vm.vm.VM.stopVM;
import static org.example.lc3_vm.vm.processor.Processor.registerRead;
import static org.example.lc3_vm.vm.processor.Processor.registerWrite;
import static org.example.lc3_vm.vm.processor.Register.R0;

public class TrapHandler {

    private final static Scanner scanner = new Scanner(System.in);

    public static void tgetc(char i) {
        registerWrite(R0, (char) scanner.nextInt());
    }

    public static void tout(char i) {
        System.out.print(registerRead(R0));
    }

    public static void tputs(char i) {

    }

    public static void tin(char i) {
        char c = (char) scanner.nextInt();
        registerWrite(R0, c);
        System.out.print(c);
    }

    public static void tputsp(char i) {

    }

    public static void thalt(char i) {
        stopVM();
    }

    public static void tinu16(char i) {
        char c = (char) scanner.nextInt();
        registerWrite(R0, c);
    }

    public static void toutu16(char i) {
        System.out.print((int) (registerRead(R0)));
    }
}
