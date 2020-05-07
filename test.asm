.data
arr:
.word 5, 3, 21, 20, 9, 1, 100
.text
.globl main
main:
li t1, 4 #a
li t0, 1 #b
li t4, 0 #i
li t3, 8 #n
la t5, arr
loop1:
beq t3, t4, exit
addi t0, t0, 1
addi t4, t4, 1
addi t1, t1, 1
j loop1
exit:
syscall