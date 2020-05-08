# Simulator

**Simulator** is a *JAVA* based tool to simulate *.asm* files similar to *MIPS* architecture.
Our Simulator supports these features:-
- 20 registers each of size 4 bytes.
- 32 bit architecture.
- A GUI for seeing the content of registers and memory in floating-number format.
- consists of 2 caches L1 and L2.
- shows basic informations such as:-
    - registers
    - number of cycles
    - number of stalls
    - miss rates in L1 and L2 cache
    - IPC

## Installation

clone this repo into your system, build all the files and run the **Main.java**
```bash
javac Opcodes.java
javac Assembler.java
javac Main.java
java Main
```

## Usage
JDK should be present in your system
Change *<your_file_name.asm>* in the Main.java file
```java
try {
    file = new BufferedReader(new FileReader("<your_file_name.asm>"));
    new Extra(file);
}
```
Use it to simulate and perform required functons written in given assembly language format
- Note: the *.asm* file must be written based on our architecture

#### Example
 
**BubbleSort**
run our given *bubblesort.asm*
Initial array value stored in memory 
```
arr:
    .word 34,12,1,78,45
```
Final array value stored in memory after running the given bubblesort.asm
> arr: 1, 12, 34, 45, 78

## How it works
- UI- it will show the contents of memory and registers pre and post simulation along with a simulate button.
And a *step-by-step* button is provided for step wise visuals. 
##### classes
- Main - It is the main function, used for calling all the functions as well as the UI which has been created using javax.swing library. When UI is created, it calls the Parser.
- Parser - It iniliasises the registers and the memory by using storeMem function and calls PreParser
  - storeMem - Stores the value of data segment into an ArrayList memory and generates a dictionary storing the base address i.e memory-counter for each label.
- Opcode -  This class will be called to generate opcodes for given commands.
- PreParser - Reads the file as input nd splits them to store line by line and generates a dictionary storing line number for each corresponding label.
- Cache1
- Cache2
- Registers
- ALU
- Assembler
##### simulate
- On clicking *simulate* , parser's execute method is called.
  - execute - Sets the counter to *main* and reads line by line to perform desired instructions by comparing with opcode values and registers and increments the counter until it reaches EOF.
 
***Note:** For reference to instruction set refer to *Instruction chart*.

## Contributing
First fork this repo then clone it into your system.
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

