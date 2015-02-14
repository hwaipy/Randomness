package com.hwaipy.unifieddeviceinterface.timeeventdevice.picoharp300device;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 *
 * @author Hwaipy
 */
interface PicoHarp300JNA extends Library {

    int PH_GetErrorString(Pointer errstring, int errcode);

    int PH_GetLibraryVersion(Pointer vers);

    int PH_OpenDevice(int devidx, Pointer serial);

    int PH_CloseDevice(int devidx);

    int PH_Initialize(int devidx, int mode);

    int PH_GetHardwareInfo(int devidx, Pointer model, Pointer vers);

    int PH_GetSerialNumber(int devidx, Pointer serial);

    int PH_Calibrate(int devidx);

    int PH_SetInputCFDLevel(int devidx, int channel, int value);

    int PH_SetInputCFDZeroCross(int devidx, int channel, int value);

    int PH_GetCountRate(int devidx, int channel);

    int PH_GetWarnings(int devidx);

    int PH_GetWarningsText(int devidx, int warnings, Pointer text);

    int PH_StartMeas(int devidx, int tacq);

    int PH_StopMeas(int devidx);

    int PH_GetFlags(int devidx);

    int PH_TTReadData(int devidx, Pointer buffer, int count);

    int PH_CTCStatus(int devidx);
}
