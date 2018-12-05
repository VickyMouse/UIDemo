package demo.li.opal.uidemo.cardDeck;

/**
 * 卡片数据装载对象
 *
 * @author opalli on 2018/12/01
 */
public class NormalCardItemData extends CardItemData {
    String userName;
    int likeNum;
    int imageNum;

    public NormalCardItemData() {
        super();
        this.userName = "Default Template";
        this.likeNum = (int) (Math.random() * 10);
        this.imageNum = (int) (Math.random() * 6);
    }

    public NormalCardItemData(String imagePath, String userName, int likeNum, int imageNum) {
        super(imagePath);
        this.userName = userName;
        this.likeNum = likeNum;
        this.imageNum = imageNum;
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
