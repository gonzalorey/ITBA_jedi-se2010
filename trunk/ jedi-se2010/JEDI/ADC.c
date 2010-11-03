/*
 * ADC.c
 *
 *  Created on: May 2, 2010
 *      Author: Administrator
 */
#include "ADC.h"
#include <avr/io.h>
#include <avr/iom644.h>

void
ADC_init()
{
	//disable the ADC
	ADCSRA &= ~(1 << ADEN);

	//Set the division factor in 8
	ADCSRA |= (1 << ADPS1) | (1 << ADPS0);

	//set the refference voltage
	//ADMUX |= (1 << REFS0);

	//set the ADC resolution to 8
	ADMUX |= (1 << ADLAR);

	//set the ADC in free running modes
	ADCSRB &= ~(1 << ADTS2);
	ADCSRB &= ~(1 << ADTS1);
	ADCSRB &= ~(1 << ADTS0);

	//Default MUX 0

	//enable the ADC
	ADCSRA |= (1 << ADEN);
}

void
ADC_init2(Input_Num input)
{
	//disable the ADC
	ADCSRA &= ~(1 << ADEN);

	//Set the division factor in 8
	ADCSRA |= (1 << ADPS1) | (1 << ADPS0);

	//set the refference voltage
	//ADMUX |= (1 << REFS0);

	//set the ADC resolution to 8
	ADMUX |= (1 << ADLAR);

	//set the ADC in free running modes
	ADCSRB &= ~(1 << ADTS2);
	ADCSRB &= ~(1 << ADTS1);
	ADCSRB &= ~(1 << ADTS0);

	//blank the MUX configuration
	ADMUX &= 0xE0;

	switch(input)
	{
		case IN1:
			break;
		case IN2:
			ADMUX |= (1 << MUX1);
			break;
		case IN3:
			ADMUX |= (1 << MUX2);
			break;
	}

	//enable the ADC
	ADCSRA |= (1 << ADEN);
}

void
ADC_set_input(Input_Num input)
{
	//disable the ADC
	ADCSRA &= ~(1 << ADEN);

	//blank the MUX configuration
	ADMUX &= 0xE0;

	switch(input)
	{
		case IN1:
			ADMUX |= (1 << MUX0) | (1 << MUX2);
			break;
		case IN2:
			ADMUX |= (1 << MUX1) | (1 << MUX2);
			break;
		case IN3:
			ADMUX |= (1 << MUX0) | (1 << MUX1) | (1 << MUX2);
			break;
	}

	//enable the ADC
	ADCSRA |= (1 << ADEN);
}

void
ADC_start()
{
	ADCSRA |= (1 << ADSC);
}
