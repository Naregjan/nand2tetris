// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.

	@black //whether screen is black or white
	M=-1 //set to black at first so screen is cleared
	D=0 //screen bit
	@SETUP
	0;JMP


//listen to keyboard
(KEY)
	@KBD
	D=M
	@SETUP
	D;JEQ
	D=-1 //screen bit flipped

//setup screen
(SETUP)
	@newbit //saving the bit
	M=D
	@black
	D=D-M //-1-1=0, 0-0=0, 0-1=-1 ; same status =0
	@KEY
	D;JEQ

//set black to new bit
	@newbit
	D=M 
	@black
	M=D
	
	@SCREEN
	D=A
	@8192
	D=D+A
	@i
	M=D //setting total word size to increment down from
		
(LOOP)
	@i
	D=M-1
	M=D
	@KEY
	D;JLT
	
	@black
	D=M
	@i
	A=M //set address to address in i
	M=D //set bit to black's value
	@LOOP
	0;JMP