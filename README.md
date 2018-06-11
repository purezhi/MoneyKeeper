# 那样记账

## 诞生记

>生活越复杂，记账越有用。

2017 年回家过年的时候，查看了一下自己这一年攒的钱，少的让人费解，钱都花哪去了。

于是，决定从 2018 年 3 月开始规划自己的支出，首先得先知道自己每个月花了多少钱吧，为了这个目的，疯狂的找记账软件，国内的，国外的，但是总是不那么满意，国内的普遍包含理财部分，并且占用资源很大，国外的虽然简洁但是用起来没有国内的顺手，最后选择了网易出品的《网易有钱记账》。

记了一段时间的账，突然想自己能不能做一个只是纯粹、轻量的记账应用，于是在站酷上找到了一个[颜值很高的设计](https://www.zcool.com.cn/work/ZMjExOTI4OTY=.html)，征得作者同意后（非常感谢），经历了从开发到部分设计，直到第一个完整的版本出世。

## 下载使用

第一个版本开发完成后，已经发布在了酷安上， 后续更新也会在酷安。https://www.coolapk.com/apk/188475

预览：
![img.png](https://github.com/Bakumon/MoneyKeeper/raw/master/imgs/img.png)

## 架构和技术

架构使用了 Google 官方的 [Architecture Components](https://developer.android.com/topic/libraries/architecture/)，包括 Lifecycle、LiveData 和 ViewModel，数据库使用 [Room](https://developer.android.com/topic/libraries/architecture/room)。

![Architecture Components](https://ws2.sinaimg.cn/large/006tKfTcly1fs78mhnw17j30mb06edft.jpg)

Room 和 RxJava 配合起来使用真的很方便。

```java
@Query("SELECT * FROM record")
Flowable<List<Record>> getRecords();
```

Room 返回的数据使用 Flowable 包装，并且使用 ViewModel 在 Activity 中被观察，当数据库中 `record` 表发生改变时，不需要手动更新 View，ViewModel 会自动触发回调。

比如，列表界面展示`record` 表的所有数据，我们在详情界面中修改了`record` 表中的某一条数据，列表界面会自动回调数据改变的方法，而不用我们去手动再次请求`record` 表的所有数据来刷新界面。

## 相关链接：

1. App下载地址: https://www.coolapk.com/apk/188475
2. 设计稿: https://www.zcool.com.cn/work/ZMjExOTI4OTY=.html
3. Architecture Components: https://developer.android.com/topic/libraries/architecture/
4. Room: https://developer.android.com/topic/libraries/architecture/room