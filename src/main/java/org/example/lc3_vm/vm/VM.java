package org.example.lc3_vm.vm;

import org.example.lc3_vm.vm.processor.Processor;
import org.example.lc3_vm.vm.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.example.lc3_vm.vm.memory.Memory.memoryRead;
import static org.example.lc3_vm.vm.processor.Processor.registerRead;
import static org.example.lc3_vm.vm.processor.Processor.registerWrite;
import static org.example.lc3_vm.vm.processor.Registers.RPC;


public class VM {

    public final static char PC_START = 0x3000;
    private static boolean running = true;
    private static final String[] instructionsNames = {"br", "add", "ld", "st", "jsr", "and", "ldr", "str", "rti", "not", "ldi", "sti", "jmp", "res", "lea", "trap"};
    private final static Map<String, Consumer<Character>> instructions;

    static {
        instructions = new HashMap<>();
        instructions.put("br", Processor::br);
        instructions.put("add", Processor::add);
        instructions.put("ld", Processor::ld);
        instructions.put("st", Processor::st);
        instructions.put("jsr", Processor::jsr);
        instructions.put("and", Processor::and);
        instructions.put("ldr", Processor::ldr);
        instructions.put("str", Processor::str);
        instructions.put("rti", Processor::rti);
        instructions.put("not", Processor::not);
        instructions.put("ldi", Processor::ldi);
        instructions.put("sti", Processor::sti);
        instructions.put("jmp", Processor::jmp);
        instructions.put("res", Processor::res);
        instructions.put("lea", Processor::lea);
        instructions.put("trap", Processor::trap);
    }

    public static void stopVM() {
        running = false;
    }

    public static void startVM(char offset) {
        registerWrite(RPC, (char) (PC_START + offset));
        while (running) {
            char currentInstructionAddress = registerRead(RPC);
            char instruction = memoryRead(currentInstructionAddress);
            registerWrite(RPC, ++currentInstructionAddress);
            invokeInstruction(instruction);
        }
    }

    private static void invokeInstruction(char instruction) {
        instructions.get(instructionsNames[Utils.getOptCode(instruction)]).accept(instruction);
    }
}
