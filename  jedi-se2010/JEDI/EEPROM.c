/*
 * eeprom.c
 *
 *  Created on: May 2, 2010
 *      Author: Administrator
 */

#include "EEPROM.h"
#include <avr/eeprom.h>

void
EEPROM_write(unsigned char byte)
{
	eeprom_busy_wait();
	eeprom_write_byte((uint8_t*)MEMORY_START, byte);
}

void
EEPROM_write_to(unsigned char byte, unsigned int pos)
{
	eeprom_write_byte((uint8_t*)(MEMORY_START + pos), byte);
}

unsigned char
EEPROM_read(void)
{
	eeprom_busy_wait();
	return eeprom_read_byte((uint8_t*)MEMORY_START);
}

unsigned char
EEPROM_read_from(unsigned int pos)
{
	return eeprom_read_byte((uint8_t*)(MEMORY_START + pos));
}
