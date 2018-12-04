package demo.li.opal.uidemo.cardDeck;

import demo.li.opal.uidemo.Utils.FileUtils;

/**
 * 卡片数据装载对象
 *
 * @author opalli on 2018/12/01
 */
public class CosCardItemData extends CardItemData {
    String description;

    public CosCardItemData() {
        super();
        this.imagePath = FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall09.jpg";
    }

    public CosCardItemData(String imagePath, String userName) {
        this.imagePath = imagePath;
        this.description = userName;
    }

    public String getDescription() {
        return description;
    }
}
