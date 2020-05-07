.data
arr:
.word 1, 2, 3, 4, 5, 6, 7
.text
.globl main
main:
li t0, 0
li t1, 7
la t2, arr
loop1:
beq t0, t1, end
lw t3, 0(t2)
sw t3, 0(t2)
addi t0, t0, 1
addi t2, t2, 1
syscall