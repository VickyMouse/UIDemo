// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/kesenhu/AppPituWnsInterface.jce'
// **********************************************************************

package PituClientInterface;

import com.qq.component.json.JSON;
import com.qq.component.json.JSONException;

public final class stGetSimpleConfigRsp extends com.qq.taf.jce.JceStruct
{
    public java.util.Map<String, String> config = null;

    public stGetSimpleConfigRsp()
    {
    }

    public stGetSimpleConfigRsp(java.util.Map<String, String> config)
    {
        this.config = config;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(config, 0);
    }

    static java.util.Map<String, String> cache_config;
    static {
        cache_config = new java.util.HashMap<String, String>();
        String __var_19 = "";
        String __var_20 = "";
        cache_config.put(__var_19, __var_20);
    }

    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.config = (java.util.Map<String, String>) _is.read(cache_config, 0, true);
    }

    public String writeToJsonString() throws JSONException
    {
        return JSON.toJSONString(this);
    }

    public void readFromJsonString(String text) throws JSONException
    {
        stGetSimpleConfigRsp temp = JSON.parseObject(text, stGetSimpleConfigRsp.class);
        this.config = temp.config;
    }

}

