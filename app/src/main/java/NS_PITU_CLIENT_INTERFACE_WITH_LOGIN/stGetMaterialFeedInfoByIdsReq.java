// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/opalli/app_pitu_wns_interface_with_login.jce'
// **********************************************************************

package NS_PITU_CLIENT_INTERFACE_WITH_LOGIN;

public final class stGetMaterialFeedInfoByIdsReq extends com.qq.taf.jce.JceStruct
{
    public java.util.ArrayList<String> feed_ids = null;

    public stGetMaterialFeedInfoByIdsReq()
    {
    }

    public stGetMaterialFeedInfoByIdsReq(java.util.ArrayList<String> feed_ids)
    {
        this.feed_ids = feed_ids;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        if (null != feed_ids)
        {
            _os.write(feed_ids, 0);
        }
    }

    static java.util.ArrayList<String> cache_feed_ids;
    static {
        cache_feed_ids = new java.util.ArrayList<String>();
        String __var_5 = "";
        ((java.util.ArrayList<String>)cache_feed_ids).add(__var_5);
    }

    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.feed_ids = (java.util.ArrayList<String>) _is.read(cache_feed_ids, 0, false);
    }

}

