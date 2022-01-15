package org.example.lc3_vm.vm;

import org.example.lc3_vm.vm.processor.Processor;
import org.example.lc3_vm.vm.processor.Register;
import org.example.lc3_vm.vm.utils.Utils;

import static org.example.lc3_vm.vm.memory.Memory.memoryRead;
import static org.example.lc3_vm.vm.processor.Processor.*;


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
                    br(instruction);
                    break;
                case 1:
                    add(instruction);
                    break;
                case 2:
                    ld(instruction);
                    break;
                case 3:
                    st(instruction);
                    break;
                case 4:
                    jsr(instruction);
                    break;
                case 5:
                    and(instruction);
                    break;
                case 6:
                    ldr(instruction);
                    break;
                case 7:
                    str(instruction);
                    break;
                case 8:
                    rti(instruction);
                    break;
                case 9:
                    not(instruction);
                    break;
                case 10:
                    ldi(instruction);
                    break;
                case 11:
                    sti(instruction);
                    break;
                case 12:
                    jmp(instruction);
                    break;
                case 13:
                    res(instruction);
                    break;
                case 14:
                    lea(instruction);
                    break;
                case 15:
                    trap(instruction);
                    break;
            }
        }
    }
}
