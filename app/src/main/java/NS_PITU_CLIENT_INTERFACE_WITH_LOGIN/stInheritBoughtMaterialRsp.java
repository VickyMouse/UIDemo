// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/opalli/app_pitu_wns_interface_with_login.jce'
// **********************************************************************

package NS_PITU_CLIENT_INTERFACE_WITH_LOGIN;

public final class stInheritBoughtMaterialRsp extends com.qq.taf.jce.JceStruct
{
    public int answer1 = 0;

    public stInheritBoughtMaterialRsp()
    {
    }

    public stInheritBoughtMaterialRsp(int answer1)
    {
        this.answer1 = answer1;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(answer1, 0);
    }


    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.answer1 = (int) _is.read(answer1, 0, false);
    }

}

