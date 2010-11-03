/*
 * eeprom.h
 *
 *  Created on: May 2, 2010
 *      Author: Administrator
 */

#ifndef EEPROM_H_
#define EEPROM_H_

#define MEMORY_START 52

void EEPROM_write(unsigned char byte);

void EEPROM_write_to(unsigned char byte, unsigned int pos);

unsigned char EEPROM_read(void);

unsigned char EEPROM_read_from(unsigned int pos);

#endif /* EEPROM_H_ */
