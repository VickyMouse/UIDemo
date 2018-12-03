## Opal 的补充
### 已优化项
1. 可通过 VIEW_COUNT 设置卡堆错位显示的卡片张数，即卡堆显示的卡片张数等于 VIEW_COUNT - 1（最下方一张和倒数第二张在同一位置，移动顶部卡片，最后一张渐显）；
2. 在加载更多卡片动画过程中，禁用"加载更多"按钮；
3. 简化初始化和数据绑定逻辑；
4. 加入顶部卡片随着拖动倾斜的效果；
5. 重构并加入大量注释；
6. 移除冗余代码；

### To Do List
1. 解决倾斜时的锯齿问题；
2. 上部的圆角问题；
3. 区分左右划；加入按钮控制；
4. 加入更多的 View（拖动卡片时的效果）

### 仿探探首页卡片滑动效果
在这个仓库竣工之时，有一个小伙伴发我另一个开源工程，颇有相似之处。我发现它存在一些问题：卡片飞到两侧，如果动画没有结束，则不允许下一轮拖动。这对强迫症的用户来说，应该是很不爽的。<br><br>
然而，探探却克服了所有这些问题。或许，这个问题只有积淀过这些知识点的人才能琢磨的透吧。我确实思考了很久，想到了一个还不错的方案。<br>

### 无耻一点
如果我能不要脸一些，我会说这个项目有以下优点：<br>
* 快。真的流畅，滑动的手速再快也赶不上代码刷新 view 的速度快。<br>
* 高效。仅仅四个卡片 View 轻松搞定任意多的数据。<br>
* 灵活。自定义 ViewGroup 对卡片 view 的高度实现了自适应。<br>
* 细节。卡片之间联动的视觉效果，是像素级的精确。<br>

### 使用方法
#### 1. 在 xml 文件中引入 CardSlidePanel
```xml
<com.stone.card.library.CardSlidePanel
        android:id="@+id/image_slide_panel"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        card:bottomMarginTop="38dp"
        card:itemMarginTop="10dp"
        card:yOffsetStep="13dp" />
```
#### 2. 左右滑动监听<br>
```java
cardSwitchListener = new CardSlidePanel.CardSwitchListener() {

        @Override
        public void onShow(int index) {
            Log.d("Card", "正在显示-" + dataList.get(index).userName);
        }

        @Override
        public void onCardVanish(int index, int type) {
            Log.d("Card", "正在消失-" + dataList.get(index).userName + " 消失type=" + type);
        }
};
slidePanel.setCardSwitchListener(cardSwitchListener);
```
#### 3. 绑定Adapter<br>
```java
slidePanel.setAdapter(new CardAdapter() {
        @Override
        public int getLayoutId() {
            // layout文件
            return R.layout.card_item;
        }

        @Override
        public int getCount() {
            // 卡片个数
            return dataList.size();
        }
        
        @Override
        public Rect obtainDraggableArea(View view) {
            // 可滑动区域定制，仅调用一次
            return new Rect(....)
        }

        @Override
        public void bindView(View view, int index) {
            // 数据绑定，参看demo
            viewHolder.bindData(dataList.get(index));
        }
});
```
#### 4. 数据更新<br>
```java
// appendDataList
adapter.notifyDataSetChanged();
```

## License

    Copyright 2016, xmuSistone

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

