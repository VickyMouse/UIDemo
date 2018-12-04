package demo.li.opal.uidemo.cardDeck;

import demo.li.opal.uidemo.Utils.FileUtils;

/**
 * 卡片数据装载对象
 *
 * @author opalli on 2018/12/01
 */
public class CardItemData {
    String imagePath;
    String userName;
    int likeNum;
    int imageNum;

    public CardItemData() {
        this.userName = "Default Template";
        this.imagePath = FileUtils.FRESCO_SCHEME_ASSETS + "cards/wall09.jpg";
        this.likeNum = (int) (Math.random() * 10);
        this.imageNum = (int) (Math.random() * 6);
    }

    public CardItemData(String userName, String imagePath, int likeNum, int imageNum) {
        this.userName = userName;
        this.imagePath = imagePath;
        this.likeNum = likeNum;
        this.imageNum = imageNum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getUserName() {
        return userName;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public int getImageNum() {
        return imageNum;
    }
}
