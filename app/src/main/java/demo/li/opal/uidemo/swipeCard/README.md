https://github.com/pop1234o/CustomViewApp
https://blog.csdn.net/u012203641/article/details/78074196

简单说一下实现原理：
1. 我们用两个自定义的 ViewGroup(SwipeLayout) 来定义两个 Card，一个在上，一个在下，且重写它的`onTouchEvent()`方法，来实现跟随手指来滑动。
2. 当我们松开手指的时候，如果 Card 移动的距离短，那么就执行动画将 Card 重置到原来位置；如果移动的距离比较远，我们就执行动画将 Card 移出屏幕，当动画结束后，我们将下面的 Card 通过 View 的`bringToFront()`方法移动到上层，而刚刚移出屏幕的那个 Card 就会到下层，然后再将它重置到起始位置即可。

这样我们通过两个 Card 交替来实现了视图的复用，这是这个控件的核心部分。