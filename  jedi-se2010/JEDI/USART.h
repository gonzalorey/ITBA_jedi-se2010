/*
 * uart.h
 *
 *  Created on: May 2, 2010
 *      Author: Administrator
 */

#ifndef USART_H_
#define USART_H_

void USART_init(unsigned int baud);

void USART_transmit(unsigned char data);

void USART_transmit_buf(unsigned char * data, unsigned int len);

unsigned char USART_receive(void);

unsigned char USART_receive_blocking(void);

unsigned int USART_unread_data(void);

#endif /* UART_H_ */
