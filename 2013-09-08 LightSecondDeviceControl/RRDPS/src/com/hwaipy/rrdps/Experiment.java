package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.StreamTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class Experiment {

    private static final boolean DEBUG = false;
    private static final int CHANNEL_APD1 = 2;
    private static final int CHANNEL_APD2 = 3;
    public static final Map<String, String[]> FILENAME_MAP = new HashMap<>();

    static {
//        //发射端时间测量数据，发射端随机数数据，接收端时间测量数据，接收端随机数数据，(接收端稳相数据)
        //20150131
//        FILENAME_MAP.put("20150130125829", new String[]{"20150130125829-S-固定-2_时间测量数据.dat", "20150130125829-S-固定-2_发射端随机数.dat", "20150130125829-R-固定-单路-APD2-2_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130125949", new String[]{"20150130125949-S-固定-3_时间测量数据.dat", "20150130125949-S-固定-3_发射端随机数.dat", "20150130125949-R-固定-单路-APD1-3_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130130214", new String[]{"20150130130214-S-随机-4_时间测量数据.dat", "20150130130214-S-随机-4_发射端随机数.dat", "20150130130214-R-随机-单路-APD1-4_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130130316", new String[]{"20150130130316-S-随机-5_时间测量数据.dat", "20150130130316-S-随机-5_发射端随机数.dat", "20150130130316-R-随机-单路-APD2-5_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150123003253", new String[]{"20150123003253-S-随机-3_时间测量数据.dat", "20150123003253-S-随机-3_发射端随机数.dat", "20150123003253-R-随机-APD1-3_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150130134530", new String[]{"20150130134530-S-固定-PC12-3_时间测量数据.dat", "20150130134530-S-固定-PC12-3_发射端随机数.dat", "20150130134530-R-固定-PC12-APD1-3_时间测量数据.dat", "20150130134530-R-固定-PC12-APD1-3_接收端随机数.dat"});
//        FILENAME_MAP.put("20150130134722", new String[]{"20150130134722-S-随机-PC12-4_时间测量数据.dat", "20150130134722-S-随机-PC12-4_发射端随机数.dat", "20150130134722-R-随机-PC12-APD1-4_时间测量数据.dat", "20150130134722-R-随机-PC12-APD1-4_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128185754", new String[]{"20150128185754-S-固定-7_时间测量数据.dat","20150128185754-S-固定-7_发射端随机数.dat","20150128185754-R-固定-PC-APD1-7_时间测量数据.dat","20150128185754-R-固定-PC-APD1-7_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128185603", new String[]{"20150128185603-S-固定-6_时间测量数据.dat", "20150128185603-S-固定-6_发射端随机数.dat", "20150128185603-R-固定-PC1-APD2-6_时间测量数据.dat", "20150128185603-R-固定-PC1-APD2-6_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128190139", new String[]{"20150128190139-S-固定-8_时间测量数据.dat", "20150128190139-S-固定-8_发射端随机数.dat", "20150128190139-R-固定-PC12-APD2-8_时间测量数据.dat", "20150128190139-R-固定-PC12-APD2-8_接收端随机数.dat"});
//        FILENAME_MAP.put("20150128190431", new String[]{"20150128190431-S-固定-9_时间测量数据.dat", "20150128190431-S-固定-9_发射端随机数.dat", "20150128190431-R-固定-PC12-APD1-9_时间测量数据.dat", "20150128190431-R-固定-PC12-APD1-9_接收端随机数.dat"});
//        FILENAME_MAP.put("20150127174646", new String[]{"20150127174646-S-固定-10_时间测量数据.dat","20150127174646-S-固定-10_发射端随机数.dat","20150127174647-R-PC16-APD2-10_时间测量数据.dat","20150127174647-R-PC16-APD2-10_接收端随机数.dat"});
//        FILENAME_MAP.put("20150127174833", new String[]{"20150127174833-S-固定-11_时间测量数据.dat","20150127174833-S-固定-11_发射端随机数.dat","20150127174833-R-PC16-APD1-11_时间测量数据.dat","20150127174833-R-PC16-APD1-11_接收端随机数.dat"});

//        //20150206第二次
//        FILENAME_MAP.put("20150206235224", new String[]{"20150206235224-s-固定随机数单路1_时间测量数据.dat", "20150206235224-s-固定随机数单路1_发射端随机数.dat", "20150206235224-R-固定-APD1-1_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150206235449", new String[]{"20150206235449-s-固定随机数单路2_时间测量数据.dat", "20150206235449-s-固定随机数单路2_发射端随机数.dat", "20150206235449-R-固定-APD2-2_时间测量数据.dat", "rnd-00.dat"});
//
//        //2015-2-7第一次
//        FILENAME_MAP.put("20150207162939", new String[]{"20150207162939-s-固定随机数单路1_时间测量数据.dat", "20150207162939-s-固定随机数单路1_发射端随机数.dat", "20150207162939-R-固定-APD2-1_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207163135", new String[]{"20150207163135-s-固定随机数单路2_时间测量数据.dat", "20150207163135-s-固定随机数单路2_发射端随机数.dat", "20150207163135-R-固定-APD1-2_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207163609", new String[]{"20150207163609-s-真随机数单路4_时间测量数据.dat", "20150207163609-s-真随机数单路4_发射端随机数.dat", "20150207163609-R-随机-APD2-4_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207163313", new String[]{"20150207163313-s-真随机数单路3_时间测量数据.dat", "20150207163313-s-真随机数单路3_发射端随机数.dat", "20150207163313-R-随机-APD1-3_时间测量数据.dat", "rnd-00.dat"});
//        FILENAME_MAP.put("20150207164011", new String[]{"20150207164011-s-固定随机数2路5_时间测量数据.dat", "20150207164011-s-固定随机数2路5_发射端随机数.dat", "20150207164012-R-固定-PC2-APD2-5_时间测量数据.dat", "20150207164012-R-固定-PC2-APD2-5_接收端随机数.dat"});
//        FILENAME_MAP.put("20150207164339", new String[]{"20150207164339-s固定随机数2路6_时间测量数据.dat", "20150207164339-s固定随机数2路6_发射端随机数.dat", "20150207164340-R-固定-PC2-APD1-6_时间测量数据.dat", "20150207164340-R-固定-PC2-APD1-6_接收端随机数.dat"});
//        FILENAME_MAP.put("20150207171149", new String[]{"20150207171149-s-固定随机数7路14_时间测量数据.dat", "20150207171149-s-固定随机数7路14_发射端随机数.dat", "20150207171149-R-固定-PC-APD1-14_时间测量数据.dat", "20150207171149-R-固定-PC-APD1-14_接收端随机数.dat"});
//        FILENAME_MAP.put("20150207171035", new String[]{"20150207171035-s-固定随机数7路13_时间测量数据.dat", "20150207171035-s-固定随机数7路13_发射端随机数.dat", "20150207171036-R-固定-PC-APD2-13_时间测量数据.dat", "20150207171036-R-固定-PC-APD2-13_接收端随机数.dat"});
        //2015-2-8//稳相测试
//        FILENAME_MAP.put("20150212024655", new String[]{"20150212024655_时间测量数据.dat", "20150212024655_发射端随机数.dat", "20150212024655-PC-APD1-6_时间测量数据.dat", "20150212024655-PC-APD1-6_接收端随机数.dat", "20150212024655-PC-APD1-6_稳相结果.csv", "20150212024655-PC-APD1-6_稳相数据.csv"});
        //2015-2-12
//        FILENAME_MAP.put("20150212224328", new String[]{"20150212224328-S-2_时间测量数据.dat", "20150212224328-S-2_发射端随机数.dat", "20150212224328-单路-固定-APD1-2_时间测量数据.dat", "rnd-00.dat", "20150212224328-单路-固定-APD1-2_稳相结果.csv", "20150212224328-单路-固定-APD1-2_稳相数据.csv"});
//        FILENAME_MAP.put("20150213011049", new String[]{"20150213011049-s-7_时间测量数据.dat", "20150213011049-s-7_发射端随机数.dat", "20150213011049-PC13456-随机-APD1-7_时间测量数据.dat", "20150213011049-PC13456-随机-APD1-7_接收端随机数-unmask.dat", "20150213011049-PC13456-随机-APD1-7_稳相结果.csv", "20150213011049-PC13456-随机-APD1-7_稳相数据.csv"});
//        FILENAME_MAP.put("20150213011520", new String[]{"20150213011520-s-8_时间测量数据.dat", "20150213011520-s-8_发射端随机数.dat", "20150213011520-PC345-随机-APD1-8_时间测量数据.dat", "20150213011520-PC345-随机-APD1-8_接收端随机数-unmask.dat", "20150213011520-PC345-随机-APD1-8_稳相结果.csv", "20150213011520-PC345-随机-APD1-8_稳相数据.csv"});
//        FILENAME_MAP.put("20150213031107", new String[]{"20150213031107-s-11_时间测量数据.dat", "20150213031107-s-11_发射端随机数.dat", "20150213031107-PCALL-随机-APD1-11_时间测量数据.dat", "20150213031107-PCALL-随机-APD1-11_接收端随机数.dat", "20150213031107-PCALL-随机-APD1-11_稳相结果.csv", "20150213031107-PCALL-随机-APD1-11_稳相数据.csv"});
        //2015-2-13
//        FILENAME_MAP.put("20150214010623", new String[]{"20150214010623-s-2_时间测量数据.dat", "20150214010623-s-2_发射端随机数.dat", "20150214010623-R-APD1-随机-2_时间测量数据.dat", "20150214010623-R-APD1-随机-2_接收端随机数.dat", "20150214010623-R-APD1-随机-2_稳相结果.csv", "20150214010623-R-APD1-随机-2_稳相数据.csv"});
//        FILENAME_MAP.put("20150214013233", new String[]{"20150214013233-S-随机-3_时间测量数据.dat", "20150214013233-S-随机-3_发射端随机数.dat", "20150214013233-R-APD1-随机-3_时间测量数据.dat", "20150214013233-R-APD1-随机-3_接收端随机数.dat", "20150214013233-R-APD1-随机-3_稳相结果.csv", "20150214013233-R-APD1-随机-3_稳相数据.csv"});
//        FILENAME_MAP.put("20150214013826", new String[]{"20150214013826-S-随机-4_时间测量数据.dat", "20150214013826-S-随机-4_发射端随机数.dat", "20150214013826-R-APD2-随机-4_时间测量数据.dat", "20150214013826-R-APD2-随机-4_接收端随机数.dat", "20150214013826-R-APD2-随机-4_稳相结果.csv", "20150214013826-R-APD2-随机-4_稳相数据.csv"});
//        FILENAME_MAP.put("20150214015153", new String[]{"20150214015153-S-随机-5_时间测量数据.dat", "20150214015153-S-随机-5_发射端随机数.dat", "20150214015153-R-APD2-随机-5_时间测量数据.dat", "20150214015153-R-APD2-随机-5_接收端随机数.dat", "20150214015153-R-APD2-随机-5_稳相结果.csv", "20150214015153-R-APD2-随机-5_稳相数据.csv"});
//        FILENAME_MAP.put("20150214020530", new String[]{"20150214020530-s-随机-6_时间测量数据.dat", "20150214020530-s-随机-6_发射端随机数.dat", "20150214020530-R-APD2-随机-6_时间测量数据.dat", "20150214020530-R-APD2-随机-6_接收端随机数.dat", "20150214020530-R-APD2-随机-6_稳相结果.csv", "20150214020530-R-APD2-随机-6_稳相数据.csv"});
//        FILENAME_MAP.put("20150214021412", new String[]{"20150214021412-S-随机-7_时间测量数据.dat", "20150214021412-S-随机-7_发射端随机数.dat", "20150214021412-R-APD2-随机-7_时间测量数据.dat", "20150214021412-R-APD2-随机-7_接收端随机数.dat", "20150214021412-R-APD2-随机-7_稳相结果.csv", "20150214021412-R-APD2-随机-7_稳相数据.csv"});
//        FILENAME_MAP.put("20150214022712", new String[]{"20150214022712-S-随机-8_时间测量数据.dat", "20150214022712-S-随机-8_发射端随机数.dat", "20150214022712-R-APD2-随机-8_时间测量数据.dat", "20150214022712-R-APD2-随机-8_接收端随机数.dat", "20150214022712-R-APD2-随机-8_稳相结果.csv", "20150214022712-R-APD2-随机-8_稳相数据.csv"});
//        FILENAME_MAP.put("20150214023231", new String[]{"20150214023231-S-9_时间测量数据.dat", "20150214023231-S-9_发射端随机数.dat", "20150214023231-R-APD2-无编码-9_时间测量数据.dat", "20150214023231-R-APD2-无编码-9_接收端随机数.dat", "20150214023231-R-APD2-无编码-9_稳相结果.csv", "20150214023231-R-APD2-无编码-9_稳相数据.csv"});
//        FILENAME_MAP.put("20150214024713", new String[]{"20150214024713-S-随机-10_时间测量数据.dat", "20150214024713-S-随机-10_发射端随机数.dat", "20150214024713-APD2-固定-10_时间测量数据.dat", "20150214024713-APD2-固定-10_接收端随机数.dat", "20150214024713-APD2-固定-10_稳相结果.csv", "20150214024713-APD2-固定-10_稳相数据.csv"});
//        FILENAME_MAP.put("20150214025947", new String[]{"20150214025947-S-随机-11_时间测量数据.dat", "20150214025947-S-随机-11_发射端随机数.dat", "20150214025947-R-APD2-随机-11_时间测量数据.dat", "20150214025947-R-APD2-随机-11_接收端随机数.dat", "20150214025947-R-APD2-随机-11_稳相结果.csv", "20150214025947-R-APD2-随机-11_稳相数据.csv"});
//        FILENAME_MAP.put("20150214032623", new String[]{"20150214032623-S-12_时间测量数据.dat", "20150214032623-S-12_发射端随机数.dat", "20150214032623-R-APD2-固定-12_时间测量数据.dat", "20150214032623-R-APD2-固定-12_接收端随机数.dat", "20150214032623-R-APD2-固定-12_稳相结果.csv", "20150214032623-R-APD2-固定-12_稳相数据.csv"});
//        FILENAME_MAP.put("20150214035035", new String[]{"20150214035035-s-13_时间测量数据.dat", "20150214035035-s-13_发射端随机数.dat", "20150214035035-R-APD2-固定-13_时间测量数据.dat", "20150214035035-R-APD2-固定-13_接收端随机数.dat", "20150214035035-R-APD2-固定-13_稳相结果.csv", "20150214035035-R-APD2-固定-13_稳相数据.csv"});
//        FILENAME_MAP.put("20150214040342", new String[]{"20150214040342-S-15_时间测量数据.dat", "20150214040342-S-15_发射端随机数.dat", "20150214040342-R-APD2-固定-15_时间测量数据.dat", "20150214040342-R-APD2-固定-15_接收端随机数.dat", "20150214040342-R-APD2-固定-15_稳相结果.csv", "20150214040342-R-APD2-固定-15_稳相数据.csv"});
//        FILENAME_MAP.put("20150214042025", new String[]{"20150214042025pc7-1_时间测量数据.dat", "20150214042025pc7-1_发射端随机数.dat", "20150214042025-PCALL-APD2-1_时间测量数据.dat", "20150214042025-PCALL-APD2-1_接收端随机数.dat", "20150214042025-PCALL-APD2-1_稳相结果.csv", "20150214042025-PCALL-APD2-1_稳相数据.csv"});
//        FILENAME_MAP.put("20150214042619", new String[]{"20150214042619pc7-2_时间测量数据.dat", "20150214042619pc7-2_发射端随机数.dat", "20150214042619-PCALL-APD2-2_时间测量数据.dat", "20150214042619-PCALL-APD2-2_接收端随机数.dat", "20150214042619-PCALL-APD2-2_稳相结果.csv", "20150214042619-PCALL-APD2-2_稳相数据.csv"});
//        FILENAME_MAP.put("20150214045145", new String[]{"20150214045145pc345_时间测量数据.dat", "20150214045145pc345_发射端随机数.dat", "20150214045145-PC345-APD2-3_时间测量数据.dat", "20150214045145-PC345-APD2-3_接收端随机数.dat", "20150214045145-PC345-APD2-3_稳相结果.csv", "20150214045145-PC345-APD2-3_稳相数据.csv"});
//        FILENAME_MAP.put("20150214050344", new String[]{"20150214050344pc45_时间测量数据.dat", "20150214050344pc45_发射端随机数.dat", "20150214050344-PC45-APD2-4_时间测量数据.dat", "20150214050344-PC45-APD2-4_接收端随机数.dat", "20150214050344-PC45-APD2-4_稳相结果.csv", "20150214050344-PC45-APD2-4_稳相数据.csv"});
//        FILENAME_MAP.put("20150214051020", new String[]{"20150214051020pc2345_时间测量数据.dat", "20150214051020pc2345_发射端随机数.dat", "20150214051020-PC2345-APD2-5_时间测量数据.dat", "20150214051020-PC2345-APD2-5_接收端随机数.dat", "20150214051020-PC2345-APD2-5_稳相结果.csv", "20150214051020-PC2345-APD2-5_稳相数据.csv"});
//        FILENAME_MAP.put("20150214051628", new String[]{"20150214051628pc2467_时间测量数据.dat", "20150214051628pc2467_发射端随机数.dat", "20150214051628-PC2467-APD2-6_时间测量数据.dat", "20150214051628-PC2467-APD2-6_接收端随机数.dat", "20150214051628-PC2467-APD2-6_稳相结果.csv", "20150214051628-PC2467-APD2-6_稳相数据.csv"});
//        FILENAME_MAP.put("20150214052547", new String[]{"20150214052547pc1_时间测量数据.dat", "20150214052547pc1_发射端随机数.dat", "20150214052547-PC1-APD2-7_时间测量数据.dat", "20150214052547-PC1-APD2-7_接收端随机数.dat", "20150214052547-PC1-APD2-7_稳相结果.csv", "20150214052547-PC1-APD2-7_稳相数据.csv"});
//        FILENAME_MAP.put("20150214063022", new String[]{"20150214063022-s-pc7_8_时间测量数据.dat", "20150214063022-s-pc7_8_发射端随机数.dat", "20150214063022-PCALL-APD2-8_时间测量数据.dat", "20150214063022-PCALL-APD2-8_接收端随机数.dat", "20150214063022-PCALL-APD2-8_稳相结果.csv", "20150214063022-PCALL-APD2-8_稳相数据.csv"});
//        FILENAME_MAP.put("20150214064828", new String[]{"20150214064828-s-pc7_9_时间测量数据.dat", "20150214064828-s-pc7_9_发射端随机数.dat", "20150214064828-PCALL-APD2-9_时间测量数据.dat", "20150214064828-PCALL-APD2-9_接收端随机数.dat", "20150214064828-PCALL-APD2-9_稳相结果.csv", "20150214064828-PCALL-APD2-9_稳相数据.csv"});
//        FILENAME_MAP.put("20150214070219", new String[]{"20150214070219-s-pc7_10_时间测量数据.dat", "20150214070219-s-pc7_10_发射端随机数.dat", "20150214070219-PCALL-APD2-10_时间测量数据.dat", "20150214070219-PCALL-APD2-10_接收端随机数.dat", "20150214070219-PCALL-APD2-10_稳相结果.csv", "20150214070219-PCALL-APD2-10_稳相数据.csv"});
//        FILENAME_MAP.put("20150214074442", new String[]{"20150214074442-s-11_时间测量数据.dat", "20150214074442-s-11_发射端随机数.dat", "20150214074442-R-固定-APD1-11_时间测量数据.dat", "20150214074442-R-固定-APD1-11_接收端随机数.dat", "20150214074442-R-固定-APD1-11_稳相结果.csv", "20150214074442-R-固定-APD1-11_稳相数据.csv"});
//        FILENAME_MAP.put("20150214081508", new String[]{"20150214081508-S-1_时间测量数据.dat", "20150214081508-S-1_发射端随机数.dat", "20150214081508-R-APD2-1_时间测量数据.dat", "20150214081508-R-APD2-1_接收端随机数.dat", "20150214081508-R-APD2-1_稳相结果.csv", "20150214081508-R-APD2-1_稳相数据.csv"});
//        FILENAME_MAP.put("20150214083007", new String[]{"20150214083007-S-1_时间测量数据.dat", "20150214083007-S-1_发射端随机数.dat", "20150214083007-APD2-1_时间测量数据.dat", "20150214083007-APD2-1_接收端随机数.dat", "20150214083007-APD2-1_稳相结果.csv", "20150214083007-APD2-1_稳相数据.csv"});
//        FILENAME_MAP.put("20150214083958", new String[]{"20150214083958-s-pc7-1_时间测量数据.dat", "20150214083958-s-pc7-1_发射端随机数.dat", "20150214083958-PCALL-APD2-2_时间测量数据.dat", "20150214083958-PCALL-APD2-2_接收端随机数.dat", "20150214083958-PCALL-APD2-2_稳相结果.csv", "20150214083958-PCALL-APD2-2_稳相数据.csv"});
//        FILENAME_MAP.put("20150214085253", new String[]{"20150214085253_时间测量数据.dat", "20150214085253_发射端随机数.dat", "20150214085253-R-APD2-3_时间测量数据.dat", "20150214085253-R-APD2-3_接收端随机数.dat", "20150214085253-R-APD2-3_稳相结果.csv", "20150214085253-R-APD2-3_稳相数据.csv"});
//        FILENAME_MAP.put("20150214092147", new String[]{"20150214092147-s-pc7-2_时间测量数据.dat", "20150214092147-s-pc7-2_发射端随机数.dat", "20150214092147-PCALL-APD2-1_时间测量数据.dat", "20150214092147-PCALL-APD2-1_接收端随机数.dat", "20150214092147-PCALL-APD2-1_稳相结果.csv", "20150214092147-PCALL-APD2-1_稳相数据.csv"});
//        FILENAME_MAP.put("20150214095756", new String[]{"20150214095756-s-pc7-2_时间测量数据.dat", "20150214095756-s-pc7-2_发射端随机数.dat", "20150214095756-PCall-apd2-2_时间测量数据.dat", "20150214095756-PCall-apd2-2_接收端随机数.dat", "20150214095756-PCall-apd2-2_稳相结果.csv", "20150214095756-PCall-apd2-2_稳相数据.csv"});
//        FILENAME_MAP.put("20150214100455", new String[]{"20150214100455-s-pc7-3_时间测量数据.dat", "20150214100455-s-pc7-3_发射端随机数.dat", "20150214100455-PCALL-APD2-3_时间测量数据.dat", "20150214100455-PCALL-APD2-3_接收端随机数.dat", "20150214100455-PCALL-APD2-3_稳相结果.csv", "20150214100455-PCALL-APD2-3_稳相数据.csv"});
//        FILENAME_MAP.put("20150214101614", new String[]{"20150214101614-s-pc7-4_时间测量数据.dat", "20150214101614-s-pc7-4_发射端随机数.dat", "20150214101614-PCALL-APD2-4_时间测量数据.dat", "20150214101614-PCALL-APD2-4_接收端随机数.dat", "20150214101614-PCALL-APD2-4_稳相结果.csv", "20150214101614-PCALL-APD2-4_稳相数据.csv"});
//        FILENAME_MAP.put("20150214102130", new String[]{"20150214102130-s-pc7-5_时间测量数据.dat", "20150214102130-s-pc7-5_发射端随机数.dat", "20150214102130-PCALL-APD2-5_时间测量数据.dat", "20150214102130-PCALL-APD2-5_接收端随机数.dat", "20150214102130-PCALL-APD2-5_稳相结果.csv", "20150214102130-PCALL-APD2-5_稳相数据.csv"});
//        FILENAME_MAP.put("20150214102642", new String[]{"20150214102642-s-pc7-6_时间测量数据.dat", "20150214102642-s-pc7-6_发射端随机数.dat", "20150214102642-PCALL-APD2-6_时间测量数据.dat", "20150214102642-PCALL-APD2-6_接收端随机数.dat", "20150214102642-PCALL-APD2-6_稳相结果.csv", "20150214102642-PCALL-APD2-6_稳相数据.csv"});
//        FILENAME_MAP.put("20150214103609", new String[]{"20150214103609-s-pc7-7_时间测量数据.dat", "20150214103609-s-pc7-7_发射端随机数.dat", "20150214103609-PCALL-APD2-7_时间测量数据.dat", "20150214103609-PCALL-APD2-7_接收端随机数.dat", "20150214103609-PCALL-APD2-7_稳相结果.csv", "20150214103609-PCALL-APD2-7_稳相数据.csv"});
//        FILENAME_MAP.put("20150214105152", new String[]{"20150214105152-s-pc7-9_时间测量数据.dat", "20150214105152-s-pc7-9_发射端随机数.dat", "20150214105152-PCALL-APD2-9_时间测量数据.dat", "20150214105152-PCALL-APD2-9_接收端随机数.dat", "20150214105152-PCALL-APD2-9_稳相结果.csv", "20150214105152-PCALL-APD2-9_稳相数据.csv"});
//        FILENAME_MAP.put("20150214105852", new String[]{"20150214105852-s-pc7-10_时间测量数据.dat", "20150214105852-s-pc7-10_发射端随机数.dat", "20150214105852-PCALL-APD2-10_时间测量数据.dat", "20150214105852-PCALL-APD2-10_接收端随机数.dat", "20150214105852-PCALL-APD2-10_稳相结果.csv", "20150214105852-PCALL-APD2-10_稳相数据.csv"});
//        FILENAME_MAP.put("20150214110452", new String[]{"20150214110452-s-pc7-11_时间测量数据.dat", "20150214110452-s-pc7-11_发射端随机数.dat", "20150214110452-PCALL-APD2-11_时间测量数据.dat", "20150214110452-PCALL-APD2-11_接收端随机数.dat", "20150214110452-PCALL-APD2-11_稳相结果.csv", "20150214110452-PCALL-APD2-11_稳相数据.csv"});
//        FILENAME_MAP.put("20150214115755", new String[]{"20150214115755-s-pc7-19_时间测量数据.dat", "20150214115755-s-pc7-19_发射端随机数.dat", "20150214115755-PCALL-APD2-19_时间测量数据.dat", "20150214115755-PCALL-APD2-19_接收端随机数.dat", "20150214115755-PCALL-APD2-19_稳相结果.csv", "20150214115755-PCALL-APD2-19_稳相数据.csv"});
//        FILENAME_MAP.put("20150214112513", new String[]{"20150214112513-s-pc7-13_时间测量数据.dat", "20150214112513-s-pc7-13_发射端随机数.dat", "20150214112513-PCALL-APD2-13_时间测量数据.dat", "20150214112513-PCALL-APD2-13_接收端随机数.dat", "20150214112513-PCALL-APD2-13_稳相结果.csv", "20150214112513-PCALL-APD2-13_稳相数据.csv"});
//        FILENAME_MAP.put("20150214113034", new String[]{"20150214113034-s-pc7-14_时间测量数据.dat", "20150214113034-s-pc7-14_发射端随机数.dat", "20150214113034-PCALL-APD2-14_时间测量数据.dat", "20150214113034-PCALL-APD2-14_接收端随机数.dat", "20150214113034-PCALL-APD2-14_稳相结果.csv", "20150214113034-PCALL-APD2-14_稳相数据.csv"});
//        FILENAME_MAP.put("20150214113743", new String[]{"20150214113743-s-pc7-15_时间测量数据.dat", "20150214113743-s-pc7-15_发射端随机数.dat", "20150214113743-PCALL-APD2-15_时间测量数据.dat", "20150214113743-PCALL-APD2-15_接收端随机数.dat", "20150214113743-PCALL-APD2-15_稳相结果.csv", "20150214113743-PCALL-APD2-15_稳相数据.csv"});
    }
    private final String id;
    private final File path;
    private TimeEventList aliceRandomList;
    private TimeEventList bobRandomList;
    private TimeEventList apd1List;
    private TimeEventList apd2List;
    private TimeEventList apdList;
    private PhaseLockingResultSet phaseLockingResults;
    private final int pcMask;

    public Experiment(String id, File path) {
        this(id, path, 0b11111111);
    }

    public Experiment(String id, File path, int pcMask) {
        this.id = id;
        this.path = path;
        this.pcMask = pcMask;
    }

    public void loadData() throws IOException, DeviceException {
        String[] fileNames = FILENAME_MAP.get(id);
        if (fileNames == null) {
            throw new RuntimeException();
        }
        File AliceTDCFile = new File(path, fileNames[0]);
        File AliceQRNGFile = new File(path, fileNames[1]);
        File BobTDCFile = new File(path, fileNames[2]);
        File BobQRNGFile = new File(path, fileNames[3]);
        File plrFile = new File(path, fileNames[4]);
        File pldFile = new File(path, fileNames[5]);
        TimeEventSegment aliceSegment = loadTDCFile(AliceTDCFile);
        TimeEventSegment bobSegment = loadTDCFile(BobTDCFile);

        ArrayList<EncodingRandom> aliceQRNGList = loadEncodingQRNGFile(AliceQRNGFile);
        ArrayList<DecodingRandom> bobQRNGList = loadDecodingQRNGFile(BobQRNGFile);
        aliceRandomList = timingQRNG(aliceQRNGList, aliceSegment.getEventList(1));
        bobRandomList = timingQRNG(bobQRNGList, bobSegment.getEventList(1));
//        System.out.println(aliceQRNGList.size() + "\t" + bobRandomList.size());
        apd1List = bobSegment.getEventList(CHANNEL_APD1);
        apd2List = bobSegment.getEventList(CHANNEL_APD2);
//        System.out.println(apd1List.size()+"\t"+apd2List.size());
        phaseLockingResults = PhaseLockLoader.load(plrFile, pldFile);
    }

    public int[] getEncodingCounts(int diff) {
        int[] counts = new int[2];
        Iterator<TimeEvent> iterator = aliceRandomList.iterator();
        while (iterator.hasNext()) {
            ExtandedTimeEvent<EncodingRandom> randomeEvent = (ExtandedTimeEvent<EncodingRandom>) iterator.next();
            EncodingRandom random = randomeEvent.getProperty();
            for (int i = 0; i < 128; i++) {
                int r = random.getEncode(i, diff);
                if (r == -1) {
                    continue;
                }
//                if (r > 0) {
//                    r = 1;
//                }
                counts[r]++;
            }
        }
        return counts;
    }

    public void sync(long delay1, long delay2) {
//        TimeEventList aliceGPSList = aliceSegment.getEventList(0);
//        TimeEventList aliceSyncList = aliceSegment.getEventList(1);
//        TimeEventList bobGPSList = bobSegment.getEventList(0);
//        TimeEventList bobSyncList = bobSegment.getEventList(1);
//        long coarseDelay = aliceGPSList.get(0).getTime() - bobGPSList.get(0).getTime();
//        RecursionCoincidenceMatcher syncMatcher = new RecursionCoincidenceMatcher(bobSyncList, aliceSyncList, 1000000, coarseDelay);

        for (int i = 0; i < apd1List.size(); i++) {
            TimeEvent e = apd1List.get(i);
            apd1List.set(new TimeEvent(e.getTime() - delay1, e.getChannel()), i);
        }
        for (int i = 0; i < apd2List.size(); i++) {
            TimeEvent e = apd2List.get(i);
            apd2List.set(new TimeEvent(e.getTime() - delay2, e.getChannel()), i);
        }
    }

    public void filterAndMerge(long before, long after) {
//        System.out.println(apd1List.size());
//        System.out.println(apd2List.size());
        apd1List = doFilter(apd1List, bobRandomList, before, after);
        apd2List = doFilter(apd2List, bobRandomList, before, after);
//        System.out.println(apd1List.size());
//        System.out.println(apd2List.size());
        apdList = new MergedTimeEventList(apd1List, apd2List);
//        System.out.println("-" + apdList.size());
    }

    public ArrayList<Decoder.Entry> decoding(long gate) {
        Tagger tagger = new Tagger(bobRandomList, apdList, gate);
        ArrayList<Tagger.Entry> tags = tagger.tag();
        Decoder decoder = new Decoder(tags, aliceRandomList, bobRandomList);
        ArrayList<Decoder.Entry> result = decoder.decode();
        return result;
    }

    public ArrayList<Decoder.Entry> decodingWithPhase(long gate, int contrast) {
        ArrayList<Decoder.Entry> decoding = decoding(gate);
        ArrayList<Decoder.Entry> results = new ArrayList<>();
        for (Decoder.Entry r : decoding) {
            int roundIndex = r.getRoundIndex();
            if (roundIndex >= phaseLockingResults.size()) {
                continue;
            }
            PhaseLockingResult plr = phaseLockingResults.get(roundIndex);
            if (plr.contrast() >= contrast) {
                results.add(r);
            }
        }
        return results;
    }

    public PhaseLockingResultSet getPhaseLockingResults() {
        return this.phaseLockingResults;
    }

    private TimeEventList doFilter(TimeEventList apdList, TimeEventList bobRandomList, long before, long after) {
        Iterator<TimeEvent> apdIterator = apdList.iterator();
        Iterator<TimeEvent> syncIterator = bobRandomList.iterator();
        TimeEvent syncEvent = syncIterator.next();
        long startTime = syncEvent.getTime() - before;
        long endTime = syncEvent.getTime() + after;
        TimeEvent apdEvent = apdIterator.next();
        StreamTimeEventList newList = new StreamTimeEventList();
        while (true) {
            long time = apdEvent.getTime();
            if (time <= endTime) {
                if (time >= startTime) {
                    newList.offer(apdEvent);
                }
                if (apdIterator.hasNext()) {
                    apdEvent = apdIterator.next();
                } else {
                    apdEvent = null;
                }
            } else {
                if (syncIterator.hasNext()) {
                    syncEvent = syncIterator.next();
                    startTime = syncEvent.getTime() - before;
                    endTime = syncEvent.getTime() + after;
                } else {
                    syncEvent = null;
                }
            }
            if (apdEvent == null || syncEvent == null) {
                break;
            }
        }
        return newList;
    }

    private TimeEventSegment loadTDCFile(File file) throws IOException, DeviceException {
        TimeEventLoader loader = new PXI40PS1Loader(file, null);
        return TimeEventDataManager.loadTimeEventSegment(loader);
    }

    private ArrayList<EncodingRandom> loadEncodingQRNGFile(File file) throws IOException, DeviceException {
        FileInputStream input = new FileInputStream(file);
        byte[] b = new byte[16];
        int index = 0;
        ArrayList<EncodingRandom> list = new ArrayList<>();
        while (true) {
            int[] randomList = new int[128];
            int read = input.read(b);
            if (read < 16) {
                break;
            }
            for (int i = 0; i < 128; i++) {
                if (((b[(i / 8)] >>> (7 - (i % 8))) & 0x01) == 0x01) {
                    randomList[i] = 0;
                } else {
                    randomList[i] = 1;
                }
            }
//            System.out.println(Arrays.toString(randomList));
            EncodingRandom encodingRandom = new EncodingRandom(randomList);
            list.add(encodingRandom);
            index++;
        }
//        for (EncodingRandom encodingRandom : list) {
//            System.out.println(encodingRandom);
//        }
        return list;
    }

    private ArrayList<DecodingRandom> loadDecodingQRNGFile(File file) throws IOException, DeviceException {
        FileInputStream input = new FileInputStream(file);
        int b;
        ArrayList<DecodingRandom> list = new ArrayList<>();
        while (true) {
            b = input.read();
            if (b == -1) {
                break;
            }
            b = b & pcMask;
            list.add(new DecodingRandom(b));
        }
        return list;
    }

    private <T> TimeEventList timingQRNG(ArrayList<T> QRNGList, TimeEventList timingList) {
        //数据校验
        if (DEBUG) {
            Iterator<TimeEvent> iterator = timingList.iterator();
            TimeEvent t1 = iterator.next();
            while (iterator.hasNext()) {
                TimeEvent t2 = iterator.next();
                long deltaT = t2.getTime() - t1.getTime();
                if (Math.abs(deltaT) > 150000000 && Math.abs(deltaT) < 700000000000l) {
                    throw new RuntimeException();
                }
                t1 = t2;
            }
        }

        int length = Math.min(QRNGList.size(), timingList.size());
        StreamTimeEventList streamTimeEventList = new StreamTimeEventList();
        Iterator<TimeEvent> iterator = timingList.iterator();
        for (int i = 0; i < length; i++) {
            T random = QRNGList.get(i);
            TimeEvent timeEvent;
            if (iterator.hasNext()) {
                timeEvent = iterator.next();
            } else {
                throw new RuntimeException();
            }
            ExtandedTimeEvent<T> ete = new ExtandedTimeEvent<>(timeEvent.getTime(), timeEvent.getChannel(), random);
            streamTimeEventList.offer(ete);
        }
        return streamTimeEventList;
    }

    public TimeEventList getBobRandomList() {
        return bobRandomList;
    }

    public void measureTiming(int apdIndex, long start, long binWidth, int binCount) {
        Iterator<TimeEvent> syncIterator = bobRandomList.iterator();
        Iterator<TimeEvent> apdIterator = apdIndex == 0 ? apd1List.iterator() : apd2List.iterator();
        int[] results = new int[binCount];
        long beginTime = syncIterator.next().getTime();
        long endTime = syncIterator.next().getTime();
        TimeEvent event = apdIterator.next();
        while (event != null) {
            long time = event.getTime();
            if (time < beginTime) {
                event = apdIterator.hasNext() ? apdIterator.next() : null;
            } else if (time < endTime) {
                long delta = time - beginTime - start;
                int index = (int) (delta / binWidth);
                if (index < binCount) {
                    results[index]++;
                }
                event = apdIterator.hasNext() ? apdIterator.next() : null;
            } else {
                beginTime = endTime;
                if (syncIterator.hasNext()) {
                    endTime = syncIterator.next().getTime();
                } else {
                    break;
                }
            }
        }
        for (int i = 0; i < binCount; i++) {
            System.out.println((start + i * binWidth) + "\t" + (results[i]));
        }
    }

    void test() {
        MergedTimeEventList m = new MergedTimeEventList(apd1List, apd2List);
//        System.out.println(m.size());
        Iterator<TimeEvent> iterator = m.iterator();
        long t = 0;
        int i = 0;
        while (iterator.hasNext()) {
            TimeEvent next = iterator.next();
            if (next.getTime() < t) {
                System.out.println("wrong");
            } else {
                t = next.getTime();
            }
        }
    }
}
