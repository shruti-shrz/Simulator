.data
arr:
.word 1, 2, 3, 4, 5, 6, 7
.text
.globl main
main:
li t0, 0
li t1, 7
la t2, arr
lw t3, 0(t2)
addi t2, t2, 1
lw t4, 0(t2)
addi t2, t2, 1
lw t5, 0(t2)
addi t2, t2, 1
lw t6, 0(t2)
addi t2, t2, 1
lw t7, 0(t2)
addi t2, t2, 1
lw t8, 0(t2)
addi t2, t2, 1
syscall