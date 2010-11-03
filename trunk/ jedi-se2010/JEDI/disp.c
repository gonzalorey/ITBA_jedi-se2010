#ifndef DISP_C_
#define DISP_C_

#include <avr/io.h>

#include "disp.h"

volatile unsigned int status = 0xFF;

void DISP_init(void){
	DISP_PORT = 0xFF; //salida
}

void DISP_set(unsigned int bit){
	status |= 1<<bit;
}

void DISP_reset(unsigned int bit){
	status &= ~(1<<bit);
}

void DISP_reset_all(){
	status = 0x00;
}

unsigned int DISP_get(unsigned int bit){
	return (status>>bit)%2;
}

void DISP_setAll(unsigned int bits){
	status = bits;
}

unsigned int DISP_getAll(void){
	return status;
}

void DISP_setBCD(unsigned int number){
	status = (number/10)<<4;
	status += number%10;
}

unsigned int DISP_getBCD(void){
	return ((status<<4)*10) + (status&0x0F);
}

void DISP_toggle(unsigned int bit){
	if(DISP_get(bit))
		DISP_set(bit);
	else
		DISP_reset(bit);
}

void DISP_flush(void){
	DISP_PORT = status;
}

#endif /* DISP_C_ */
