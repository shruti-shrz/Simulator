.data
arr:
.word 0, 0, 0, 0, 0, 0, 0
.text
.globl main
main:
li t0, 0
li t1, 1
li t2, 2
li t3, 3
li t4, 4
li t5, 5
li t6, 6
la t7, arr
sw t0, 0(t7)
addi t7, t7, 1
sw t1, 0(t7)
addi t7, t7, 1
sw t2, 0(t7)
addi t7, t7, 1
sw t3, 0(t7)
addi t7, t7, 1
sw t4, 0(t7)
addi t7, t7, 1
sw t5, 0(t7)
addi t7, t7, 1
sw t6, 0(t7)
syscall