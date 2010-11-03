#ifndef DISP_H_
#define DISP_H_

#define DISP_B1 5
#define DISP_B2 4

#define DEBUG_D1 2
#define DEBUG_D2 3
#define DEBUG_D3 6
#define DEBUG_D4 7

#define DISP_PORT PORTD

void DISP_init(void);

void DISP_set(unsigned int bit);

void DISP_reset(unsigned int bit);

void DISP_reset_all();

unsigned int DISP_get(unsigned int bit);

void DISP_setAll(unsigned int bits);

unsigned int DISP_getAll(void);

void DISP_setBCD(unsigned int bits);

unsigned int DISP_getBCD(void);

void DISP_flush(void);

void DISP_toggle(unsigned int bit);

#endif /* DISP_H_ */
