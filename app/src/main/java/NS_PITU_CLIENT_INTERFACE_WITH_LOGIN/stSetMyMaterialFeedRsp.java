// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/opalli/app_pitu_wns_interface_with_login.jce'
// **********************************************************************

package NS_PITU_CLIENT_INTERFACE_WITH_LOGIN;

public final class stSetMyMaterialFeedRsp extends com.qq.taf.jce.JceStruct
{
    public boolean status = true;

    public stSetMyMaterialFeedRsp()
    {
    }

    public stSetMyMaterialFeedRsp(boolean status)
    {
        this.status = status;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(status, 0);
    }


    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.status = (boolean) _is.read(status, 0, false);
    }

}

