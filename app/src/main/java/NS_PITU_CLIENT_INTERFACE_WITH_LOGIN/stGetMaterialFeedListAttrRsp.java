// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/opalli/app_pitu_wns_interface_with_login.jce'
// **********************************************************************

package NS_PITU_CLIENT_INTERFACE_WITH_LOGIN;

public final class stGetMaterialFeedListAttrRsp extends com.qq.taf.jce.JceStruct
{
    public java.util.Map<Integer, Integer> numbers = null;

    public stGetMaterialFeedListAttrRsp()
    {
    }

    public stGetMaterialFeedListAttrRsp(java.util.Map<Integer, Integer> numbers)
    {
        this.numbers = numbers;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        if (null != numbers)
        {
            _os.write(numbers, 0);
        }
    }

    static java.util.Map<Integer, Integer> cache_numbers;
    static {
        cache_numbers = new java.util.HashMap<Integer, Integer>();
        Integer __var_6 = 0;
        Integer __var_7 = 0;
        cache_numbers.put(__var_6, __var_7);
    }

    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.numbers = (java.util.Map<Integer, Integer>) _is.read(cache_numbers, 0, false);
    }

}

