// **********************************************************************
// This file was generated by a TAF parser!
// TAF version 3.2.1.6 by WSRD Tencent.
// Generated from `/data/jcetool/taf//upload/geraldzhu/CommClientInterface.jce'
// **********************************************************************

package CommonClientInterface;

public final class E_APP_ID implements java.io.Serializable
{
    private static E_APP_ID[] __values = new E_APP_ID[18];
    private int __value;
    private String __T = new String();

    public static final int _QQIMAGE_ANDROID = 1000;
    public static final E_APP_ID QQIMAGE_ANDROID = new E_APP_ID(0,_QQIMAGE_ANDROID,"QQIMAGE_ANDROID");
    public static final int _QQIMAGE_IPHONE = 2000;
    public static final E_APP_ID QQIMAGE_IPHONE = new E_APP_ID(1,_QQIMAGE_IPHONE,"QQIMAGE_IPHONE");
    public static final int _WATERMARK_CAMERA_ANDROID = 1001;
    public static final E_APP_ID WATERMARK_CAMERA_ANDROID = new E_APP_ID(2,_WATERMARK_CAMERA_ANDROID,"WATERMARK_CAMERA_ANDROID");
    public static final int _WATERMARK_CAMERA_IPHONE = 2001;
    public static final E_APP_ID WATERMARK_CAMERA_IPHONE = new E_APP_ID(3,_WATERMARK_CAMERA_IPHONE,"WATERMARK_CAMERA_IPHONE");
    public static final int _QQPLAYER_ANDROID = 1002;
    public static final E_APP_ID QQPLAYER_ANDROID = new E_APP_ID(4,_QQPLAYER_ANDROID,"QQPLAYER_ANDROID");
    public static final int _QQPLAYER_IPHONE = 2002;
    public static final E_APP_ID QQPLAYER_IPHONE = new E_APP_ID(5,_QQPLAYER_IPHONE,"QQPLAYER_IPHONE");
    public static final int _QZONE_ANDROID = 1003;
    public static final E_APP_ID QZONE_ANDROID = new E_APP_ID(6,_QZONE_ANDROID,"QZONE_ANDROID");
    public static final int _QZONE_IPHONE = 2003;
    public static final E_APP_ID QZONE_IPHONE = new E_APP_ID(7,_QZONE_IPHONE,"QZONE_IPHONE");
    public static final int _MOBILEQQ_ANDROID = 1004;
    public static final E_APP_ID MOBILEQQ_ANDROID = new E_APP_ID(8,_MOBILEQQ_ANDROID,"MOBILEQQ_ANDROID");
    public static final int _MOBILEQQ_IPHONE = 2004;
    public static final E_APP_ID MOBILEQQ_IPHONE = new E_APP_ID(9,_MOBILEQQ_IPHONE,"MOBILEQQ_IPHONE");
    public static final int _TTPIC_ANDROID = 1006;
    public static final E_APP_ID TTPIC_ANDROID = new E_APP_ID(10,_TTPIC_ANDROID,"TTPIC_ANDROID");
    public static final int _TTPIC_IPHONE = 2006;
    public static final E_APP_ID TTPIC_IPHONE = new E_APP_ID(11,_TTPIC_IPHONE,"TTPIC_IPHONE");
    public static final int _MCAM_ANDROID = 1007;
    public static final E_APP_ID MCAM_ANDROID = new E_APP_ID(12,_MCAM_ANDROID,"MCAM_ANDROID");
    public static final int _MCAM_IPHONE = 2007;
    public static final E_APP_ID MCAM_IPHONE = new E_APP_ID(13,_MCAM_IPHONE,"MCAM_IPHONE");
    public static final int _WESION_ANDROID = 1008;
    public static final E_APP_ID WESION_ANDROID = new E_APP_ID(14,_WESION_ANDROID,"WESION_ANDROID");
    public static final int _WESION_IPHONE = 2008;
    public static final E_APP_ID WESION_IPHONE = new E_APP_ID(15,_WESION_IPHONE,"WESION_IPHONE");
    public static final int _SELFIEE_ANDROID = 1009;
    public static final E_APP_ID SELFIEE_ANDROID = new E_APP_ID(16,_SELFIEE_ANDROID,"SELFIEE_ANDROID");
    public static final int _SELFIEE_IPHONE = 2009;
    public static final E_APP_ID SELFIEE_IPHONE = new E_APP_ID(17,_SELFIEE_IPHONE,"SELFIEE_IPHONE");

    public static E_APP_ID convert(int val)
    {
        for(int __i = 0; __i < __values.length; ++__i)
        {
            if(__values[__i].value() == val)
            {
                return __values[__i];
            }
        }
        assert false;
        return null;
    }

    public static E_APP_ID convert(String val)
    {
        for(int __i = 0; __i < __values.length; ++__i)
        {
            if(__values[__i].toString().equals(val))
            {
                return __values[__i];
            }
        }
        assert false;
        return null;
    }

    public int value()
    {
        return __value;
    }

    public String toString()
    {
        return __T;
    }

    private E_APP_ID(int index, int val, String s)
    {
        __T = s;
        __value = val;
        __values[index] = this;
    }

}
