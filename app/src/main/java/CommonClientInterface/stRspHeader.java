// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/geraldzhu/CommClientInterface.jce'
// **********************************************************************

package CommonClientInterface;

public final class stRspHeader extends com.qq.taf.jce.JceStruct
{
    public int iRet = 0;

    public String sErrmsg = "";

    public stRspHeader()
    {
    }

    public stRspHeader(int iRet, String sErrmsg)
    {
        this.iRet = iRet;
        this.sErrmsg = sErrmsg;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(iRet, 0);
        _os.write(sErrmsg, 1);
    }


    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.iRet = (int) _is.read(iRet, 0, true);
        this.sErrmsg =  _is.readString(1, true);
    }

}

