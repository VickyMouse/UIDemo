// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/kesenhu/AppPituWnsInterface_3.jce'
// **********************************************************************

package PituClientInterface;

import com.qq.component.json.JSON;
import com.qq.component.json.JSONException;

public final class stConvertShortUrlRsp extends com.qq.taf.jce.JceStruct
{
    public String shortUrl = "";

    public stConvertShortUrlRsp()
    {
    }

    public stConvertShortUrlRsp(String shortUrl)
    {
        this.shortUrl = shortUrl;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(shortUrl, 0);
    }


    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.shortUrl =  _is.readString(0, true);
    }

    public String writeToJsonString() throws JSONException
    {
        return JSON.toJSONString(this);
    }

    public void readFromJsonString(String text) throws JSONException
    {
        stConvertShortUrlRsp temp = JSON.parseObject(text, stConvertShortUrlRsp.class);
        this.shortUrl = temp.shortUrl;
    }

}

