// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/kesenhu/AppPituWnsInterface.jce'
// **********************************************************************

package PituClientInterface;

import com.qq.component.json.JSON;
import com.qq.component.json.JSONException;

public final class stGetMusicMaterialsRsp extends com.qq.taf.jce.JceStruct
{
    public String attachInfo = "";

    public java.util.ArrayList<stMusicMaterial> materials = null;

    public int isFinished = 0;

    public stGetMusicMaterialsRsp()
    {
    }

    public stGetMusicMaterialsRsp(String attachInfo, java.util.ArrayList<stMusicMaterial> materials, int isFinished)
    {
        this.attachInfo = attachInfo;
        this.materials = materials;
        this.isFinished = isFinished;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(attachInfo, 0);
        _os.write(materials, 1);
        _os.write(isFinished, 2);
    }

    static java.util.ArrayList<stMusicMaterial> cache_materials;
    static {
        cache_materials = new java.util.ArrayList<stMusicMaterial>();
        stMusicMaterial __var_21 = new stMusicMaterial();
        ((java.util.ArrayList<stMusicMaterial>)cache_materials).add(__var_21);
    }

    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.attachInfo =  _is.readString(0, true);
        this.materials = (java.util.ArrayList<stMusicMaterial>) _is.read(cache_materials, 1, true);
        this.isFinished = (int) _is.read(isFinished, 2, true);
    }

    public String writeToJsonString() throws JSONException
    {
        return JSON.toJSONString(this);
    }

    public void readFromJsonString(String text) throws JSONException
    {
        stGetMusicMaterialsRsp temp = JSON.parseObject(text, stGetMusicMaterialsRsp.class);
        this.attachInfo = temp.attachInfo;
        this.materials = temp.materials;
        this.isFinished = temp.isFinished;
    }

}

