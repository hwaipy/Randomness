package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400device;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 *
 * @author Hwaipy
 */
interface HydraHarp400JNA extends Library {

    int HH_GetErrorString(Pointer errstring, int errcode);

    int HH_GetLibraryVersion(Pointer vers);

    int HH_OpenDevice(int devidx, Pointer serial);

    int HH_CloseDevice(int devidx);

    int HH_Initialize(int devidx, int mode, int refsource);

    int HH_GetHardwareInfo(int devidx, Pointer model, Pointer partno);

    int HH_GetSerialNumber(int devidx, Pointer serial);

    int HH_GetNumOfInputChannels(int devidx, Pointer nchannels);

    int HH_Calibrate(int devidx);

    int HH_SetInputCFDLevel(int devidx, int channel, int value);

    int HH_SetInputCFDZeroCross(int devidx, int channel, int value);

    int HH_GetSyncRate(int devidx, Pointer syncrate);

    int HH_GetCountRate(int devidx, int channel, Pointer cntrate);

    int HH_GetWarnings(int devidx);

    int HH_GetWarningsText(int devidx, Pointer text, int warnings);

    int HH_StartMeas(int devidx, int tacq);

    int HH_StopMeas(int devidx);

    int HH_GetFlags(int devidx, Pointer flags);

    int HH_ReadFiFo(int devidx, Pointer buffer, int count, Pointer nactual);

    int HH_CTCStatus(int devidx, Pointer ctcstatus);
}
