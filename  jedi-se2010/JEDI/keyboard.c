/*
 * keyboard.c
 *
 *  Created on: Apr 21, 2010
 *      Author: gorey
 */

#include "keyboard.h"

void
KEY_init (void)
{
	state = NOTHING;
	time_start = 0;
}

unsigned char
KEY_get_all_pressed(unsigned int tick)
{

	switch (state) {
		case NOTHING:
			if (SWITCH_PORT != 0xFF) {
				state = GOING_DOWN;
				time_start = tick;
			}
			break;
		case GOING_DOWN:
			if (SWITCH_PORT == 0xFF) {
				state = NOTHING;
			} else {
				if (tick > time_start + TIME_STEP) {
					state = DOWN;
					return SWITCH_PORT;
				}
			}
			break;
		case GOING_UP:
			if (SWITCH_PORT != 0xFF) {
				state = DOWN;
			} else {
				if (tick > time_start + TIME_STEP) {
					state = NOTHING;
				}
			}
			break;
		case DOWN:
			if (SWITCH_PORT == 0xFF) {
				state = GOING_UP;
				time_start = tick;
			}
			break;
	}
	return SW_NONE;
}

unsigned int
KEY_pressed(unsigned int key, unsigned int tick)
{
	unsigned char pressed = KEY_get_all_pressed(tick);

	return (~pressed >> key) % 2;
}

unsigned int
KEY_on(unsigned int key)
{
	return (~SWITCH_PORT >> key) % 2;
}
