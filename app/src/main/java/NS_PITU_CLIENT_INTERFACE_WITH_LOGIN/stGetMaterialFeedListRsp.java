// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/opalli/app_pitu_wns_interface_with_login.jce'
// **********************************************************************

package NS_PITU_CLIENT_INTERFACE_WITH_LOGIN;

public final class stGetMaterialFeedListRsp extends com.qq.taf.jce.JceStruct
{
    public String attach_info = "";

    public java.util.ArrayList<NS_PITU_META_PROTOCOL.stMetaMaterialFeed> feedlist = null;

    public boolean is_finished = true;

    public stGetMaterialFeedListRsp()
    {
    }

    public stGetMaterialFeedListRsp(String attach_info, java.util.ArrayList<NS_PITU_META_PROTOCOL.stMetaMaterialFeed> feedlist, boolean is_finished)
    {
        this.attach_info = attach_info;
        this.feedlist = feedlist;
        this.is_finished = is_finished;
    }

    public void writeTo(com.qq.taf.jce.JceOutputStream _os)
    {
        if (null != attach_info)
        {
            _os.write(attach_info, 0);
        }
        if (null != feedlist)
        {
            _os.write(feedlist, 1);
        }
        _os.write(is_finished, 2);
    }

    static java.util.ArrayList<NS_PITU_META_PROTOCOL.stMetaMaterialFeed> cache_feedlist;
    static {
        cache_feedlist = new java.util.ArrayList<NS_PITU_META_PROTOCOL.stMetaMaterialFeed>();
        NS_PITU_META_PROTOCOL.stMetaMaterialFeed __var_1 = new NS_PITU_META_PROTOCOL.stMetaMaterialFeed();
        ((java.util.ArrayList<NS_PITU_META_PROTOCOL.stMetaMaterialFeed>)cache_feedlist).add(__var_1);
    }

    public void readFrom(com.qq.taf.jce.JceInputStream _is)
    {
        this.attach_info =  _is.readString(0, false);
        this.feedlist = (java.util.ArrayList<NS_PITU_META_PROTOCOL.stMetaMaterialFeed>) _is.read(cache_feedlist, 1, false);
        this.is_finished = (boolean) _is.read(is_finished, 2, false);
    }

}

