//
// Created by xuhua on 2018/3/15.
//

#ifndef NFCSDKTEST_NTAG_H
#define NFCSDKTEST_NTAG_H

#include "define.h"


class NTag {
public:
    NTag();
public:
    enum NTagTech {
        NTAG_TECH_UNKNOW,
        NTAG_TECH_A,//android.nfc.tech.NfcA
        NTAG_TECH_B,//android.nfc.tech.NfcB
    };

    enum NTagExtTech {
        NTAG_EXT_TECH_UNKNOW,
        NTAG_EXT_TECH_MU,//android.nfc.tech.MifareUltralight
        NTAG_EXT_TECH_MC,//android.nfc.tech.MifareClassic
    };

    enum NTagType{
        NTAG_TYPE_NONE,
        NTAG_TYPE_213,
        NTAG_TYPE_215,
        NTAG_TYPE_216,
    };

    enum NtagExtType {
        NTAG_EXT_TYPE_NONE,
        NTAG_EXT_TYPE_TT,//213TT
    };
public:
    NTagTech tech;
    NTagExtTech extTech;
    NTagType type;
    NtagExtType extType;
};


#endif //NFCSDKTEST_NTAG_H
