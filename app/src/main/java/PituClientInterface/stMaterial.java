// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/kesenhu/AppPituWnsInterface.jce'
// **********************************************************************

package PituClientInterface;

import com.qq.component.json.JSON;
import com.qq.component.json.JSONException;

public final class stMaterial extends com.qq.taf.jce.JceStruct
{
    public String id = "";

    public String name = "";

    public String description = "";

    public String thumbUrl = "";

    public String bigThumbUrl = "";

    public String packageUrl = "";

    public int miniSptVersion = 0;

    public int mask = 0;

    public int version = 1;

    public int priority = 1;

    public int width = 0;

    public int height = 0;

    public int miniShowVersion = 0;

    public String localizedMap = "";

    public String language = "";

    public java.util.ArrayList<stMaterialSubItem> subItems = null;

    public int maxShowVersion = 99999;

    public int displayType = 0;

    public stMaterial()
    {
    }

    public stMaterial(String id, String name, String description, String thumbUrl, String bigThumbUrl, String packageUrl, int miniSptVersion, int mask, int version, int priority, int width, int height, int miniShowVersion, String localizedMap, String language, java.util.ArrayList<stMaterialSubItem> subItems, int maxShowVersion, int displayType)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbUrl = thumbUrl;
        this.bigThumbUrl = bigThumbUrl;
        this.packageUrl = packageUrl;
        this.miniSptVersion = miniSptVersion;
        this.mask = mask;
        this.version = version;
        this.priority = priority;
        this.width = width;
        this.height = height;
        this.miniShowVersion = miniShowVersion;
        this.localizedMap = localizedMap;
        this.language = language;
        this.subItems = subItems;
        this.maxShowVersion = maxShowVersion;
        this.displayType = displayType;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        _os.write(id, 0);
        _os.write(name, 1);
        _os.write(description, 2);
        _os.write(thumbUrl, 3);
        _os.write(bigThumbUrl, 4);
        _os.write(packageUrl, 5);
        _os.write(miniSptVersion, 6);
        _os.write(mask, 7);
        _os.write(version, 8);
        _os.write(priority, 9);
        _os.write(width, 10);
        _os.write(height, 11);
        _os.write(miniShowVersion, 12);
        if (null != localizedMap)
        {
            _os.write(localizedMap, 13);
        }
        if (null != language)
        {
            _os.write(language, 14);
        }
        if (null != subItems)
        {
            _os.write(subItems, 15);
        }
        _os.write(maxShowVersion, 16);
        _os.write(displayType, 17);
    }

    static java.util.ArrayList<stMaterialSubItem> cache_subItems;
    static {
        cache_subItems = new java.util.ArrayList<stMaterialSubItem>();
        stMaterialSubItem __var_1 = new stMaterialSubItem();
        ((java.util.ArrayList<stMaterialSubItem>)cache_subItems).add(__var_1);
    }

    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.id =  _is.readString(0, true);
        this.name =  _is.readString(1, true);
        this.description =  _is.readString(2, true);
        this.thumbUrl =  _is.readString(3, true);
        this.bigThumbUrl =  _is.readString(4, true);
        this.packageUrl =  _is.readString(5, true);
        this.miniSptVersion = (int) _is.read(miniSptVersion, 6, true);
        this.mask = (int) _is.read(mask, 7, true);
        this.version = (int) _is.read(version, 8, true);
        this.priority = (int) _is.read(priority, 9, true);
        this.width = (int) _is.read(width, 10, false);
        this.height = (int) _is.read(height, 11, false);
        this.miniShowVersion = (int) _is.read(miniShowVersion, 12, false);
        this.localizedMap =  _is.readString(13, false);
        this.language =  _is.readString(14, false);
        this.subItems = (java.util.ArrayList<stMaterialSubItem>) _is.read(cache_subItems, 15, false);
        this.maxShowVersion = (int) _is.read(maxShowVersion, 16, false);
        this.displayType = (int) _is.read(displayType, 17, false);
    }

    public String writeToJsonString() throws JSONException
    {
        return JSON.toJSONString(this);
    }

    public void readFromJsonString(String text) throws JSONException
    {
        stMaterial temp = JSON.parseObject(text, stMaterial.class);
        this.id = temp.id;
        this.name = temp.name;
        this.description = temp.description;
        this.thumbUrl = temp.thumbUrl;
        this.bigThumbUrl = temp.bigThumbUrl;
        this.packageUrl = temp.packageUrl;
        this.miniSptVersion = temp.miniSptVersion;
        this.mask = temp.mask;
        this.version = temp.version;
        this.priority = temp.priority;
        this.width = temp.width;
        this.height = temp.height;
        this.miniShowVersion = temp.miniShowVersion;
        this.localizedMap = temp.localizedMap;
        this.language = temp.language;
        this.subItems = temp.subItems;
        this.maxShowVersion = temp.maxShowVersion;
        this.displayType = temp.displayType;
    }

}

