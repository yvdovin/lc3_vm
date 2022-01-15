package org.example.lc3_vm;

import org.example.lc3_vm.vm.VM;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.example.lc3_vm.vm.VM.startVM;
import static org.example.lc3_vm.vm.memory.Memory.memoryWrite;

public class Application {

    public static void main(String[] args) throws FileNotFoundException {
        //writeProgramIntoMemory(args[0]);
        writeProgramIntoMemory("C://research/lc3_vm/src/main/resources/br.txt");
        startVM((char) 0x0);
    }

    private static void writeProgramIntoMemory(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
        char i = VM.PC_START;
        while (scanner.hasNext()) {
            memoryWrite(i, (char) Integer.parseInt(scanner.nextLine().substring(2), 16));
            i++;
        }
        scanner.close();
    }
}
