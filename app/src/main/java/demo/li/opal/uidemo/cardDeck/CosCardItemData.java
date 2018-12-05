package demo.li.opal.uidemo.cardDeck;

/**
 * 卡片数据装载对象
 *
 * @author opalli on 2018/12/01
 */
public class CosCardItemData extends CardItemData {
    String description;

    public CosCardItemData() {
        super();
        this.description = "造型设计 by 谷田达子";
    }

    public CosCardItemData(String imagePath, String userName) {
        super(imagePath);
        this.description = userName;
    }

    public String getDescription() {
        return description;
    }
}
