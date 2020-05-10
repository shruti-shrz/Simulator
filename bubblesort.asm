Cache1, size, 4096, blocks, 4, associativity, 1, latency, 4
Cache2, size, 4096, blocks, 8, associativity, 1, latency, 10, mempenalty, 100
.data
arr:
.word 11, 3, 21, 20, 9, 1, 10, 100, 13, 2, 99, 27, 12, 8, 4, 104, 5 
.text
.globl main
main:
li t1, 17 #n
li t2, 16 #n-1
li t4, 0 #i
li t6, 1
loop1:
beq t4, t1, exit1
la t0, arr
li t5, 0 #j
loop2:
beq t5, t2, exit2
lw t7, 0(t0)
lw t8, 1(t0)
slt t9, t8, t7
beq t9, t6, if
addi t5, t5, 1
addi t0, t0, 1
j loop2
if:
sw t8, 0(t0)
sw t7, 1(t0)
addi t0, t0, 1
addi t5, t5, 1
j loop2
exit2:
addi t4, t4, 1
j loop1
exit1:
li t19, 10
syscall