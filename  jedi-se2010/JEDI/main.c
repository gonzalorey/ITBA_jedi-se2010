/*
 * main.c
 *
 *  Created on: Apr 5, 2010
 *      Author: gorey
 */

#include "main.h"

int
main(void)
{
	init();

	while(1)
	{
		//button1
		if(KEY_on(0))
			button1_pressed = 1;
		else
			button1_pressed = -1;

		//button2
		if(KEY_on(1))
			button2_pressed = 1;
		else
			button2_pressed = -1;

		if(do_inform){
			//send the inform packet
			send_inform();
			do_inform = 0;
		}

		//flush the displays
		DISP_flush();
	}

	return 0;
}

void
init(void)
{
	//enable COMPARE1A interrupt
	TIMSK1 = _BV(OCIE1A);

	//64 prescaler | CTC mode, TOP = 0CR1A
	TCCR1B = _BV(CS11) | _BV(CS10) | _BV(WGM12);

	//the interruption compare time
	OCR1A = 15 * 8;

	//enable the interruptions
	sei();

	//enter
	DDRB = 0x00;

	//exit
	DDRD = 0xFF;

	//initialize the display
	DISP_init();

	//initialize the keyboard
	KEY_init();

	//initialize the USART
	USART_init(9600);

	//initialize the ADC
	ADC_init();

	//set the initial stage
	stage = DISCOVERING;
}

SIGNAL(SIG_OUTPUT_COMPARE1A)
{
	//increment the counter
	count++;

	//increment time
	if((count % 1000) == 0){
		count = 0;
		time++;
	}

	//take the medition and put it in the medition buffer
	if((count % 2) == 0)
	{
		ACC_read(mapped_axis++);
		if(mapped_axis == 3)
			mapped_axis = 0;
	}

	//receive from serial
	if(USART_unread_data()){
		if((aux = recv_packet()) != -1)
		{
			//TODO:DO SOMETHING IN CASE OF AN ERROR PACKET RECEIVED
		}
	}

	if((count % 200) == 0 && stage == DISCOVERING)
	{
		//send the DISCOVER packet
		send_discover();
	}

	//set the leds to tilt when descovering the console
	if(stage == DISCOVERING)
	{
		if((count % 500) < 250)
		{
			DISP_set(DISP_B1);
			DISP_set(DISP_B2);
		}
		else
		{
			DISP_reset(DISP_B1);
			DISP_reset(DISP_B2);
		}
	}

	if((count % 40) == 0 && stage == INFORMING)
	{
		do_inform = 1;
	}

	//for each tick, the timer increments the counter
	//when it reaches a high number, it is set to DISCOVER
	//again
	listening_interval++;

	//watchdog for the discovery stage
	if(listening_interval >= 500)
	{
		joystick_num = 0;
		stage = DISCOVERING;
	}
}

unsigned int
recv_packet(void)
{
	unsigned char c = USART_receive();

	// count the amount of zeroes received so as to sync with the packet
	if(zeroes_count < 4)
	{
		if(c == 0)
		{
			zeroes_count++;
			return 0;
		}
		else
		{
			//not the begin of a packet
			zeroes_count = 0;
			return 1;
		}
	}

	//receive the length of the packet
	if(recv_buffer_length == -1)
	{
		if(c > 0)
			recv_buffer_length = c;
		else
		{
			zeroes_count = 0;
			return 2;
		}
	}

	//receive the data
	recv_buffer[recv_buffer_index++] = c;

	//if the recv data reached it's length, reset his counters
	if(recv_buffer_length == recv_buffer_index)
	{
		process_packet();

		recv_buffer_index = 0;
		recv_buffer_length = -1;
		zeroes_count = 0;
	}

	return -1;
}

void
process_packet(void)
{
	unsigned int acc_method_selector;

	//an OFFER was received
	if(recv_buffer[3] == OFFER)
	{
		SERVER_ID = recv_buffer[1];
		joystick_num = recv_buffer[4];
		acc_method_selector = recv_buffer[5];

		stage = INFORMING;

		//turn on the joystick corresponding to the led
		if(joystick_num == 1)
		{
			DISP_set(DISP_B1);
			DISP_reset(DISP_B2);
		}
		else if(joystick_num == 2){
			DISP_set(DISP_B2);
			DISP_reset(DISP_B1);
		}

		switch(acc_method_selector)
		{
			case ACC_METHOD_MODE:
				acc_method = get_mode_value;
				DISP_set(DEBUG_D1);
				DISP_reset(DEBUG_D2);
				break;
			case ACC_METHOD_MEDIAN:
				acc_method = get_median_value;
				DISP_reset(DEBUG_D1);
				DISP_set(DEBUG_D2);
				break;
			case ACC_METHOD_MEAN:
				acc_method = get_mean_value;
				DISP_set(DEBUG_D1);
				DISP_set(DEBUG_D2);
				break;
			default:
				acc_method = get_median_value;
				DISP_reset(DEBUG_D1);
				DISP_set(DEBUG_D2);
				break;
		}
	}

	if(recv_buffer[3] == LISTENING)
	{
		listening_interval = 0;
	}
}

void
send_discover(void)
{
	// complete with a couple of zeros
	unsigned char buffer[10] = {0};

	// TRANSPORT
	buffer[4] = 5;				// length
	buffer[5] = JEDI_ID;		// orig
	buffer[6] = 0;				// dest (not known, broadcast)

	// DISCOVER
	buffer[7] = DISCOVER;		// type = DISCOVER

	// CHECKSUM
	buffer[8] = 0xFF;			// checksum

	USART_transmit_buf(buffer, 9);
}

void
send_inform(void)
{
	// complete with a couple of zeros
	unsigned char buffer[15] = {0};

	unsigned int x = get_mode_value(meditionX, medition_index);
	//unsigned int x = get_median_value(meditionX, medition_index);
	//unsigned int x = get_mean_value(meditionX, medition_index);

	unsigned int y = get_mode_value(meditionY, medition_index);
	//unsigned int y = get_median_value(meditionY, medition_index);
	//unsigned int y = get_mean_value(meditionY, medition_index);

	unsigned int z = get_mode_value(meditionZ, medition_index);
	//unsigned int z = get_median_value(meditionZ, medition_index);
	//unsigned int z = get_mean_value(meditionZ, medition_index);

	// TRANSPORT
	buffer[4] = 10;					// length
	buffer[5] = JEDI_ID;			// orig
	buffer[6] = SERVER_ID;			// dest

	// INFORM PACKAGE
	buffer[7] = INFORM;				// type = INFORM
	buffer[8] = x;				// X
	buffer[9] = y;				// Y
	buffer[10] = z;				// Z
	buffer[11] = button1_pressed;	// B1
	buffer[12] = button2_pressed;	// B2

	// CHECKSUM
//	buffer[13] = 0xFF;				// checksum
	buffer[13] = aux;				// checksum

	USART_transmit_buf(buffer, 14);

	//reset the medition index
	medition_index = 0;
}

void
ACC_read(Input_Num input)
{
	//if max medition is reached, exit
	if(medition_index >= 50)
		return;
	switch(input)
	{
		case IN1:
			//take a medition from X
			ADC_set_input(IN1);
		//	ADC_init2(IN1);
			ADC_start();
			meditionX[medition_index] = ADCH;
			break;

		case IN2:
			//take a medition from Y
			ADC_set_input(IN2);
		//	ADC_init2(IN2);
			ADC_start();
			meditionY[medition_index] = ADCH;
			break;

		case IN3:
			//take a medition from Z
			ADC_set_input(IN3);
		//	ADC_init2(IN3);
			ADC_start();
			meditionZ[medition_index++] = ADCH;
			break;
	}
}

unsigned int
get_mode_value(volatile unsigned int buffer[], unsigned int len)
{
	unsigned int i;
	unsigned frequency[256] = {0};
	unsigned int ansIndex = 0;
	unsigned int ansFreq = 0;

	//set the frequency for each element
	for(i = 0; i < len; i++)
		frequency[buffer[i]]++;

	for(i = 0; i < 256; i++)
	{
		if(frequency[i] > ansFreq)
		{
			ansFreq = frequency[i];
			ansIndex = i;
		}
	}

	return ansIndex;
}

unsigned int
get_median_value(volatile unsigned int buffer[], unsigned int len)
{
	unsigned int i;
	unsigned frequency[256] = {0};
	unsigned int aux = 0;

	//set the frequency for each element
	for(i = 0; i < len; i++)
		frequency[buffer[i]]++;

	//get the len/2 item
	for(i = 0; i < 256; i++)
	{
		aux += frequency[i];
		if(aux > len/2)
			return i;
	}

	//default 0g
	return 128;
}

unsigned int
get_mean_value(volatile unsigned int buffer[], unsigned int len)
{
	unsigned int i;
	double ans = 0;

	//sum all the bytes from the buffer
	for(i = 0; i < len; i++)
	{
		ans += buffer[i];
	}

	//get the mean
	return (unsigned int)(ans / len);

}
