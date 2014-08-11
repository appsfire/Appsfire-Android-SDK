// Simple BMP decoder implementation

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
#include "BMPDecoder.h"

// Constructor

BMPDecoder::BMPDecoder () :
	m_nWidth (0),
	m_nHeight (0),
	m_lpPixels (NULL) {
}

// Destructor

BMPDecoder::~BMPDecoder () {
}

// Get decoded width

unsigned int BMPDecoder::getImageWidth (void) {
   return m_nWidth;
}

// Get decoded height

unsigned int BMPDecoder::getImageHeight (void) {
   return m_nHeight;
}

// Get decoded pixels

const unsigned int *BMPDecoder::getPixels (void) {
   return m_lpPixels;
}

// Decode BMP

bool BMPDecoder::decode (const unsigned char *lpBuffer, size_t nBufferSize) {
   const BMPHeader *lpBmpHeader;
   const unsigned char *lpCurBuffer = lpBuffer;
   size_t nFileSize, nBitmapOffset, nHeaderSize;
   unsigned int nX, nY, nWidth, nHeight, nBitsPerPixel;
   unsigned int *lpPixels;
   bool bOS2Palette = false;
   bool bTopDown = false;
   int nCompressionType;

   if (nBufferSize < 14) {
      return false;
   }

   if (lpCurBuffer[0] != 'B' || lpCurBuffer[1] != 'M') {
      return false;
   }
   lpCurBuffer += 2;

   lpBmpHeader = (const BMPHeader *) lpCurBuffer;
   nFileSize = flipInt (lpBmpHeader->nFileSize);
   nBitmapOffset = flipInt (lpBmpHeader->nBitmapOffset);
   lpCurBuffer += sizeof (*lpBmpHeader);

   nHeaderSize = flipInt (*((long *) lpCurBuffer));
   if (nHeaderSize == sizeof (BMPBitmapCoreHeader)) {
      const BMPBitmapCoreHeader *lpImageHeader = (const BMPBitmapCoreHeader *) lpCurBuffer;
      long nOrigHeight;

      nWidth = (unsigned int) flipShort (lpImageHeader->nWidth);
      nOrigHeight = (long) flipShort (lpImageHeader->nHeight);
      nBitsPerPixel = (unsigned int) flipShort (lpImageHeader->nBitsPerPixel);
      nCompressionType = 0;
      bOS2Palette = true;

      if (nOrigHeight < 0) {
         nHeight = (unsigned int) (-nOrigHeight);
         bTopDown = true;
      }
      else {
         nHeight = (unsigned int) nOrigHeight;
      }
   }
   else {
      if (nHeaderSize >= sizeof (BMPBitmapInfoHeader)) {
         const BMPBitmapInfoHeader *lpImageHeader = (const BMPBitmapInfoHeader *) lpCurBuffer;
         long nOrigHeight;

         nWidth = (unsigned int) flipInt (lpImageHeader->nWidth);
         nOrigHeight = (long) flipInt (lpImageHeader->nHeight);
         nBitsPerPixel = (unsigned int) flipShort (lpImageHeader->nBitsPerPixel);
         nCompressionType = (int) flipInt (lpImageHeader->nCompressionType);

         if (nOrigHeight < 0) {
            nHeight = (unsigned int) (-nOrigHeight);
            bTopDown = true;
         }
         else {
            nHeight = (unsigned int) nOrigHeight;
         }
      }
      else {
         return false;
      }
   }
   lpCurBuffer = lpBuffer + nBitmapOffset;

   lpPixels = new unsigned int [nWidth * nHeight];

   switch (nCompressionType) {
   case 0 /* RGB */:
      switch (nBitsPerPixel) {
      case 16:
         for (nY = 0; nY < nHeight; nY++) {
            unsigned int nLineOffs = 0;
            unsigned int *lpDstPixel;
            
            if (bTopDown)
               lpDstPixel = lpPixels + nY * nWidth;
            else
               lpDstPixel = lpPixels + (nHeight - 1 - nY) * nWidth;

            for (nX = 0; nX < nWidth; nX++) {
               unsigned short nPixel = lpCurBuffer[nLineOffs] | ((lpCurBuffer[nLineOffs+1]) << 8);
               *lpDstPixel++ = ((nPixel & 0x7c00) >> 7) | ((nPixel & 0x03e0) << 6) | ((nPixel & 0x001f) << 19) | 0xff000000;  /* Read as ABGR */
               nLineOffs += 2;
            }

            /* Align row data on 4 bytes and advance to next row */
            nLineOffs = (nLineOffs + 3) & ~3;
            lpCurBuffer += nLineOffs;
         }
         break;

      case 24:
         for (nY = 0; nY < nHeight; nY++) {
            unsigned int nLineOffs = 0;
            unsigned int *lpDstPixel;
            
            if (bTopDown)
               lpDstPixel = lpPixels + nY * nWidth;
            else
               lpDstPixel = lpPixels + (nHeight - 1 - nY) * nWidth;

            for (nX = 0; nX < nWidth; nX++) {
               *lpDstPixel++ = (lpCurBuffer[nLineOffs] << 16) | (lpCurBuffer[nLineOffs+1] << 8) | lpCurBuffer[nLineOffs+2] | 0xff000000;   /* Read as ABGR */
               nLineOffs += 3;
            }

            nLineOffs = (nLineOffs + 3) & ~3;
            lpCurBuffer += nLineOffs;
         }
         break;

      case 32:
         for (nY = 0; nY < nHeight; nY++) {
            unsigned int nLineOffs = 0;
            unsigned int *lpDstPixel;
            
            if (bTopDown)
               lpDstPixel = lpPixels + nY * nWidth;
            else
               lpDstPixel = lpPixels + (nHeight - 1 - nY) * nWidth;

            for (nX = 0; nX < nWidth; nX++) {
               *lpDstPixel++ = (lpCurBuffer[nLineOffs] << 16) | (lpCurBuffer[nLineOffs+1] << 8) | lpCurBuffer[nLineOffs+2] | (lpCurBuffer[nLineOffs+3] << 24);   /* Read as ABGR */
               nLineOffs += 4;
            }

            /* Advance to next row */
            lpCurBuffer += nLineOffs;
         }
         break;

      default:
         delete [] lpPixels;
         return false;
      }
      break;

   default:
      delete [] lpPixels;
      return false;
   }

   m_nWidth = nWidth;
   m_nHeight = nHeight;
   m_lpPixels = lpPixels;

   return true;
}

// Convert 16-bit value from little-endian to the native endianness

short BMPDecoder::flipShort (short nValue) {
#if defined (_BYTE_ORDER) && _BYTE_ORDER == _BIG_ENDIAN
   unsigned short unValue = (unsigned short) nValue;
   return ((unValue & 0xff00) >> 8) | ((unValue & 0x00ff) << 8);
#else
   return nValue;
#endif
}

// Convert 32-bit value from little-endian to the native endianness

int BMPDecoder::flipInt (int nValue) {
#if defined (_BYTE_ORDER) && _BYTE_ORDER == _BIG_ENDIAN
   unsigned int unValue = (unsigned int) nValue;
   return (long) ((unValue & 0xff000000) >> 24) | ((unValue & 0x00ff0000) >> 8) | ((unValue & 0x0000ff00) << 8) | ((unValue & 0x000000ff) << 24);
#else
   return nValue;
#endif
}

