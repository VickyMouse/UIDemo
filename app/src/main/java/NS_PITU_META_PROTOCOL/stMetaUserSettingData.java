// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/opalli/pitu_meta_protocol.jce'
// **********************************************************************

package NS_PITU_META_PROTOCOL;

public final class stMetaUserSettingData extends com.qq.taf.jce.JceStruct
{
    public long timestamp_mengou = 0;

    public String mengou_data = "";

    public stMetaUserSettingData()
    {
    }

    public stMetaUserSettingData(long timestamp_mengou, String mengou_data)
    {
        this.timestamp_mengou = timestamp_mengou;
        this.mengou_data = mengou_data;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(timestamp_mengou, 0);
        _os.write(mengou_data, 1);
    }


    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.timestamp_mengou = (long) _is.read(timestamp_mengou, 0, true);
        this.mengou_data =  _is.readString(1, true);
    }

}

