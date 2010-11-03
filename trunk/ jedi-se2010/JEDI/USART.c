/*
 * uart.c
 *
 *  Created on: May 2, 2010
 *      Author: Administrator
 */

#include "USART.h"
#include <avr/io.h>
#include <avr/iom644.h>

void
USART_init(unsigned int baud)
{
	unsigned int b;

	//BAUDRATE = (((F_CPU / (USART_BAUD * 16UL))) - 1)
	//F_CPU 1.000 MHz

	switch(baud)
	{
	case 2400:		//2400 con 0.2 de error
		b = 207;
		break;
	case 4800: 		//4800 con 0.2% de error
		b = 103;
		break;
	case 9600:		//9600 con -7.0% de error
		b = 51;
		break;
	case 14400:		//14400	con 8.5% de error
		b = 34;
		break;
	case 19200:		//19200	con 8.5% de error
		b = 25;
		break;
		default:		//none...
			return;
	}

	/* Set baud rate */
	UBRR0H = (unsigned char)(b>>8);
	UBRR0L = (unsigned char)b;

	/* Enable receiver and transmitter */
	UCSR0B = (1<<RXEN0)|(1<<TXEN0);

	/* Set frame format: 8data, 2stop bit */
	UCSR0C = (1<<USBS0)|(3<<UCSZ00);
}

void
USART_transmit( unsigned char data )
{
	/* Wait for empty transmit buffer */
	while(!( UCSR0A & (1<<UDRE0)))
		;

	/* Put data into buffer, sends the data */
	UDR0 = data;

	return;
}

void
USART_transmit_buf(unsigned char * data, unsigned int len)
{
	int i;
	for(i = 0; i < len; i++)
	{
//		/* Wait for empty transmit buffer */
//		while(!( UCSR0A & (1<<UDRE0)))
//			;
//
//		/* Put data into buffer, sends the data */
//		UDR0 = data[i];

		USART_transmit(data[i]);
	}

	return;
}

unsigned char
USART_receive_blocking(void)
{
	/* Wait for data to be received */
	while (!(UCSR0A & (1<<RXC0)))
		;

	/* Get and return received data from buffer */
	return UDR0;
}

unsigned int
USART_unread_data(void)
{
	/* If data is available, return 1, else 0 */
	return (UCSR0A & (1<<RXC0));
}

unsigned char
USART_receive()
{
	/* Get and return received data from buffer */
	return UDR0;
}
