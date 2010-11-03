/*
 * keyboard.h
 *
 *  Created on: Apr 21, 2010
 *      Author: gorey
 */

#ifndef KEYBOARD_H_
#define KEYBOARD_H_

#include <avr/io.h>
#include <avr/iom644.h>

//Switch constants
#define SW0 0xFE
#define SW1 0xFD
#define SW2 0xFB
#define SW3 0xF7
#define SW4 0xEF
#define SW5 0xDF
#define SW6 0xBF
#define SW7 0x7F
#define SW_NONE 0xFF

#define SWITCH_PORT PINB

#define TIME_STEP 10

//States for the pressed keys
typedef enum states{NOTHING, GOING_UP, GOING_DOWN, DOWN} states;
states state;

unsigned int time_start;

//functions
void KEY_init (void);

unsigned char KEY_get_all_pressed(unsigned int tick);

unsigned int KEY_pressed(unsigned int key, unsigned int tick);

unsigned int KEY_on(unsigned int key);

#endif /* SWITCHES_H_ */
