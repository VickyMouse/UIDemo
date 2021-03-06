// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/geraldzhu/CommClientInterface.jce'
// **********************************************************************

package CommonClientInterface;

public final class stReqComm extends com.qq.taf.jce.JceStruct
{
    public int iAppId = 0;

    public int iPlat = 0;

    public String sAppVersion = "";

    public String sDeviceName = "";

    public String sOSVersion = "";

    public String sDeviceID = "";

    public int iAuthType = 0;

    public String sUid = "";

    public String sSessionKey = "";

    public String sReserved1 = "";

    public String sReserved2 = "";

    public String sChid = "";

    public String language = "";

    public String country = "";

    public String uid = "";

    public stReqComm()
    {
    }

    public stReqComm(int iAppId, int iPlat, String sAppVersion, String sDeviceName, String sOSVersion, String sDeviceID, int iAuthType, String sUid, String sSessionKey, String sReserved1, String sReserved2, String sChid, String language, String country, String uid)
    {
        this.iAppId = iAppId;
        this.iPlat = iPlat;
        this.sAppVersion = sAppVersion;
        this.sDeviceName = sDeviceName;
        this.sOSVersion = sOSVersion;
        this.sDeviceID = sDeviceID;
        this.iAuthType = iAuthType;
        this.sUid = sUid;
        this.sSessionKey = sSessionKey;
        this.sReserved1 = sReserved1;
        this.sReserved2 = sReserved2;
        this.sChid = sChid;
        this.language = language;
        this.country = country;
        this.uid = uid;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(iAppId, 0);
        _os.write(iPlat, 1);
        _os.write(sAppVersion, 2);
        _os.write(sDeviceName, 3);
        _os.write(sOSVersion, 4);
        _os.write(sDeviceID, 5);
        _os.write(iAuthType, 6);
        if (null != sUid)
        {
            _os.write(sUid, 7);
        }
        if (null != sSessionKey)
        {
            _os.write(sSessionKey, 8);
        }
        if (null != sReserved1)
        {
            _os.write(sReserved1, 9);
        }
        if (null != sReserved2)
        {
            _os.write(sReserved2, 10);
        }
        if (null != sChid)
        {
            _os.write(sChid, 11);
        }
        if (null != language)
        {
            _os.write(language, 12);
        }
        if (null != country)
        {
            _os.write(country, 13);
        }
        if (null != uid)
        {
            _os.write(uid, 14);
        }
    }


    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.iAppId = (int) _is.read(iAppId, 0, true);
        this.iPlat = (int) _is.read(iPlat, 1, true);
        this.sAppVersion =  _is.readString(2, true);
        this.sDeviceName =  _is.readString(3, true);
        this.sOSVersion =  _is.readString(4, true);
        this.sDeviceID =  _is.readString(5, true);
        this.iAuthType = (int) _is.read(iAuthType, 6, false);
        this.sUid =  _is.readString(7, false);
        this.sSessionKey =  _is.readString(8, false);
        this.sReserved1 =  _is.readString(9, false);
        this.sReserved2 =  _is.readString(10, false);
        this.sChid =  _is.readString(11, false);
        this.language =  _is.readString(12, false);
        this.country =  _is.readString(13, false);
        this.uid =  _is.readString(14, false);
    }

}

