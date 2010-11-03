/*
 * ADC.h
 *
 *  Created on: May 2, 2010
 *      Author: Administrator
 */

#ifndef ADC_H_
#define ADC_H_

typedef enum {IN1, IN2, IN3} Input_Num;

void ADC_init();

void ADC_init2(Input_Num input);

void ADC_start();

void ADC_set_input(Input_Num input);

#endif /* ADC_H_ */
