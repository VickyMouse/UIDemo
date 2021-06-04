package demo.li.opal.uidemo.Utils

import android.graphics.Typeface

object FontUtils {

    /**
     * lynx也使用了对应的名称，修改名称需要通知前端
     *
     *
     * 获取字体
     * @param context
     * @param font:字体名称，根据字体名称，去font目录映射对应的字体文件。
     * @return
     */
    private val fontHashMap = hashMapOf<String,Typeface>()
    @JvmStatic
    fun getTypeface(font: String): Typeface? {
        if (fontHashMap[font] != null) {
            return fontHashMap[font]
        }
        else {
            synchronized(fontHashMap) {
                return if (fontHashMap[font] != null) {
                    fontHashMap[font]
                } else {
                    if (font == "Bytedance-Roboto-Medium") {
                        fontHashMap[font] = Typeface.create("Roboto-Medium", Typeface.NORMAL) // Typeface.createFromFile("system/fonts/Bytedance-Roboto-Medium.ttf") ?: Typeface.DEFAULT   //Typeface源码实现里，已经对application级别的font有做缓存管理，不会持续创建新的
                    }
                    else if (font == "Roboto-Black") {
                        fontHashMap[font] = Typeface.create("Roboto-Black", Typeface.NORMAL) // Typeface.createFromFile("system/fonts/Bytedance-Roboto-Black.ttf") ?: Typeface.DEFAULT
                    }
                    else if (font == "Bytedance-FZLanTingHei-R-GBK") {
                        fontHashMap[font] = Typeface.create("FZLanTingHei-R-GBK" , Typeface.NORMAL)
                    }
                    else if (font == "Bytedance-FZLanTingHeiS-DB1-GB") {
                        fontHashMap[font] = Typeface.create("FZLanTingHeiS-DB1-GB", Typeface.NORMAL) // Typeface.createFromFile("system/fonts/Bytedance-FZLanTingHeiS-DB1-GB.ttf") ?: Typeface.DEFAULT
                    }
                    else if (font == "Bytedance-FZNewKai-Z03S") {
                        fontHashMap[font] = Typeface.create("FZNewKai-Z03S", Typeface.NORMAL) // Typeface.createFromFile("system/fonts/Bytedance-FZNewKai-Z03S.ttf") ?: Typeface.DEFAULT
                    }
                    else if (font == "Bytedance-FZLanTingHei-H-GBK") {
                        fontHashMap[font] = Typeface.create("FZLanTingHei-H-GBK", Typeface.NORMAL) // Typeface.createFromFile("system/fonts/Bytedance-FZLanTingHei-H-GBK.ttf") ?: Typeface.DEFAULT
                    }
                    fontHashMap[font]
                }
            }
        }
    }
}