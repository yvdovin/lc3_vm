package org.example.lc3_vm.vm;

import org.example.lc3_vm.vm.processor.Processor;
import org.example.lc3_vm.vm.processor.Register;
import org.example.lc3_vm.vm.utils.Utils;

import static org.example.lc3_vm.vm.memory.Memory.memoryRead;


public class VM {

    public final static char PC_START = 0x3000;
    private static boolean running = true;

    public static void stopVM() {
        running = false;
    }

    public static void startVM(char offset) {
        Processor.registerWrite(Register.RPC, (char) (PC_START + offset));
        while (running) {
            char currentInstructionAddress = Processor.registerRead(Register.RPC);
            char instruction = memoryRead(currentInstructionAddress);
            Processor.registerWrite(Register.RPC, ++currentInstructionAddress);
            switch (Utils.getOptCode(instruction)) {
                case 0:
                    Processor.br(instruction);
                    break;
                case 1:
                    Processor.add(instruction);
                    break;
                case 2:
                    Processor.ld(instruction);
                    break;
                case 3:
                    Processor.st(instruction);
                    break;
                case 4:
                    Processor.jsr(instruction);
                    break;
                case 5:
                    Processor.and(instruction);
                    break;
                case 6:
                    Processor.ldr(instruction);
                    break;
                case 7:
                    Processor.str(instruction);
                    break;
                case 8:
                    Processor.rti(instruction);
                    break;
                case 9:
                    Processor.not(instruction);
                    break;
                case 10:
                    Processor.ldi(instruction);
                    break;
                case 11:
                    Processor.sti(instruction);
                    break;
                case 12:
                    Processor.jmp(instruction);
                    break;
                case 13:
                    Processor.res(instruction);
                    break;
                case 14:
                    Processor.lea(instruction);
                    break;
                case 15:
                    Processor.trap(instruction);
                    break;
            }
        }
    }
}
