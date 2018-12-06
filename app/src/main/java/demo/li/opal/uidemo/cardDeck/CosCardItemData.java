package demo.li.opal.uidemo.cardDeck;

/**
 * 卡片数据装载对象
 *
 * @author opalli on 2018/12/01
 */
public class CosCardItemData extends CardItemData {
    int index;
    String description;

    public CosCardItemData() {
        super();
        this.index = 0;
        this.description = "造型设计 by 谷田达子";
    }

    public CosCardItemData(int index) {
        this();
        this.index = index;
    }

    public CosCardItemData(String imagePath, int index, String userName) {
        super(imagePath);
        this.index = index;
        this.description = userName;
    }

    public String getDescription() {
        return description;
    }
}
