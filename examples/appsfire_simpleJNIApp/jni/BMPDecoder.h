// Simple BMP decoder definitions

#ifndef  _BMPDECODER_H
#define  _BMPDECODER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// BMP header

typedef struct {
  unsigned int nFileSize;
  unsigned short nCreator1;
  unsigned short nCreator2;
  unsigned int nBitmapOffset;
} BMPHeader;

// OS/2 info header

typedef struct {
   unsigned int nHeaderSize;
   unsigned short nWidth;
   unsigned short nHeight;
   unsigned short nPlanes;
   unsigned short nBitsPerPixel;
} BMPBitmapCoreHeader;

// Windows DIB header

typedef struct {
   unsigned int nHeaderSize;
   unsigned int nWidth;
   unsigned int nHeight;
   unsigned short nPlanes;
   unsigned short nBitsPerPixel;
   unsigned int nCompressionType;
   unsigned int nBitmapSize;
   unsigned int nXPelsPerMeter;
   unsigned int nYPelsPerMeter;
   unsigned int nUsedColors;
   unsigned int nImportantColors;
} BMPBitmapInfoHeader;

// BMP decoder class for the purposes of this sample app

class BMPDecoder {
public:
   // Constructor
   BMPDecoder ();

   // Destructor
   ~BMPDecoder ();

   // Get decoded width
   unsigned int getImageWidth (void);

   // Get decoded height
   unsigned int getImageHeight (void);

   // Get decoded pixels
   const unsigned int *getPixels (void);

   // Decode BMP
   bool decode (const unsigned char *lpBuffer, size_t nBufferSize);

private:
   // Convert 16-bit value from little-endian to the native endianness
   static short flipShort (short nValue);

   // Convert 32-bit value from little-endian to the native endianness
   static int flipInt (int nValue);

   // Decoded width, in pixels
   unsigned int m_nWidth;

   // Decoded height, in pixels
   unsigned int m_nHeight;

   // Array of decoded pixels
   unsigned int *m_lpPixels;
};

#endif   /* _BMPDECODER_H */
