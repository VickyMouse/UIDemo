package demo.li.opal.uidemo.cardDeck;

import demo.li.opal.uidemo.Utils.FileUtils;

/**
 * 卡片数据装载对象
 *
 * @author opalli on 2018/12/01
 */
public class CardItemData {
    private String imagePath;

    public CardItemData() {
        this.imagePath = FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall09.jpg";
    }

    public CardItemData(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }
}
