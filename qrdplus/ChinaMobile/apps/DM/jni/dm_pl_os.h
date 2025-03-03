#ifndef _DM_PL_OS_H_
#define _DM_PL_OS_H_

#include "string.h"
#include "stdlib.h"
#include "vdm_pl_types.h"

#define xppStrcpy(target, source)        strcpy(target, source)
#define xppStrncpy(target, source, cnt)  strncpy(target, source, cnt)
#define xppStrcat(target, source)        strcat(target, source)
#define xppStrncat(target, source, cnt)  strncat(target, source, cnt)
#define xppStrcmp(first, second)         strcmp(first, second)
#define xppStrncmp(first, second, cnt)   strncmp(first, second, cnt)
#define xppStrchr(string, c)             strchr(string, c)
#define xppStrstr(string, substr)        strstr(string, substr)
#define xppStrlen(string)                strlen(string)
#define xppAtoi(string)                  atoi(string)

#define xppMemset(s, c, n)               memset(s, c, n)
#define xppMemcpy(target, source, cnt)   memcpy(target, source, cnt)
#define xppMemmove(target, source, cnt)  memmove(target, source, cnt)
#define xppMemcmp(target, source, cnt)   memcmp(target, source, cnt)
#define xppMalloc(size)                  alloc(size)
#define xppRealloc(ptr, size)            realloc(ptr, size)
#define xppFree(ptr)                     free(ptr)

/* These aren't ANSI C functions, but they're pretty common */

#define xppStricmp(first, second)       strcmp(first, second)

// MWERKS support
#define xppMemicmp(first, second, cnt)  memcmp(first, second, cnt)

void *dm_smlLibRealloc(void *pObject, long size);
void dm_smlLibFree(void *pObject);
void *dm_smlLibMemset(void *pObject, int value, long count);
void *dm_smlLibMemcpy(void *pTarget, const void *pSource, long count);
void *dm_smlLibMemmove(void *pTarget, const void *pSource, long count);
int dm_smlLibMemcmp(const void *pTarget, const void *pSource, long count);
void *dm_smlLibMalloc(const char * file, int line, long size);

int dm_smlLibStrlen(const char *pString);
char* dm_smlLibStrcpy(const char *pTarget, const char *pSource);
char* dm_smlLibStrdup(const char *constStringP);
int dm_smlLibStrcmp(const char *pTarget, const char *pSource);
int dm_smlLibStrcmp_ext(const char *pTarget, const char *pSource);
char* dm_smlLibStrcat(const char *pTarget, const char *pSource);
char* dm_smlLibStrncpy(const char *pTarget, const char *pSource, int count);
int dm_smlLibStrncmp(const char *pTarget, const char *pSource, int count); //case sensitive
int dm_smlLibStrncmp_ext(const char *pTarget, const char *pSource, int count); //no case sensitive
char* dm_smlLibStrchr(const char *pString, char character);
char* dm_smlLibStrrchr(const char *pString, char character);
IBOOL dm_smlLibIsDigit(char character);

void dm_smlLibSleep(unsigned long ticks);

IU32 VDM_PL_atoIU32(const char *string, int radix, IBOOL *success);
void VDM_PL_IU32toa(IU32 value, char *string, int radix);
//IBOOL VDM_PL_Async_signal(VDM_Handle_t inAsyncHandle);
IS32 VDM_PL_strncasecmp(const char *string1, const char *string2, IU32 len);
void VDM_Notify_PL_Task_Finished(void);

#endif
