/*
 * main.h
 *
 *  Created on: Apr 21, 2010
 *      Author: gorey
 */

#ifndef MAIN_H_
#define MAIN_H_

#include <avr/io.h>
#include <avr/iom644.h>
#include <util/delay.h>
#include <avr/interrupt.h>
#include <avr/eeprom.h>

#include "disp.h"
#include "keyboard.h"
#include "USART.h"
#include "ADC.h"
#include "EEPROM.h"

#define JEDI_ID 101

//EEMEM static uint8_t timesResetted = 0;

#define DISCOVER 11
#define OFFER 12
#define INFORM 20
#define LISTENING 30

//joystick number
volatile unsigned int joystick_num = -1;

//server id
volatile unsigned int SERVER_ID = 0;

//timer counters
volatile unsigned int count = 0;
volatile unsigned int time = 0;

//medition counter
volatile unsigned int meditionX[50];
volatile unsigned int meditionY[50];
volatile unsigned int meditionZ[50];
volatile unsigned int medition_index = 0;
volatile unsigned int mapped_axis = 0;

//buttons
volatile unsigned int button1_pressed = -1;
volatile unsigned int button2_pressed = -1;

//states
typedef enum stages{DISCOVERING, INFORMING} stages;
stages stage;

//receive buffer
volatile unsigned char recv_buffer[20];
volatile unsigned int recv_buffer_length = -1;
volatile unsigned int recv_buffer_index = 0;
volatile unsigned int zeroes_count = 0;

//listening condition
volatile unsigned int listening_interval = 0;

volatile unsigned int aux;
volatile unsigned int do_inform;

//functions
void init(void);

void ACC_read(Input_Num input);

//methods used to process the data from the accelerometer
unsigned int get_mode_value(volatile unsigned int buffer[], unsigned int len);

unsigned int get_median_value(volatile unsigned int buffer[], unsigned int len);

unsigned int get_mean_value(volatile unsigned int buffer[], unsigned int len);

//generic accelerator method (function pointer)
unsigned int (*acc_method)(volatile unsigned int buffer[], unsigned int len);

#define ACC_METHOD_MODE 0
#define ACC_METHOD_MEDIAN 1
#define ACC_METHOD_MEAN 2

unsigned int recv_packet(void);

void process_packet(void);

void send_inform(void);

void send_discover(void);

#endif /* MAIN_H_ */
