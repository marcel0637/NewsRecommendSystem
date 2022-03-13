// pages/newsList/newsList.js

let utils = require('../../utils/util.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: 'https://www.marcel0637.cn',
    // host: 'https://47.111.112.196',
    barIndex:0,
    // barList: ["推荐","热门","国际", "社会", "体育", "娱乐","军事","科技","财经","股市"],
    barList: ["推荐","热门","国际"],
    barNavX:0,
    sourceName:["猜你喜欢","近期热点","随机推荐"],
    sourceImg:["recommend.jpg","hot.png","random.png"],
    catMap:{
      "world":"国际",
      "society":"社会",
      "sports":"体育",
      "entertainment":"娱乐",
      "military":"军事",
      "technology":"科技",
      "finance":"财经",
      "stock":"股市"
    },
    revCatMap:{
      "推荐":"all",
      "热门":"hot",
      "国际":"world",
      "社会":"society",
      "体育":"sports",
      "娱乐":"entertainment",
      "军事":"military",
      "科技":"technology",
      "财经":"finance",
      "股市":"stock",
    },
    newsList:[],
    isRefreshing: false,
    isLoadingMoreData: false 
  },
  bindSearchNavigate:function(e){
    wx.navigateTo({
      url: '/pages/search/search'
    })
  },
  clickTopBar:function(e){
  　var w=wx.getSystemInfoSync().windowWidth;
　  var leng=this.data.barList.length;
  　var i = e.currentTarget.dataset.index;
　  var disX = (i - 2) * w / leng;
  　if(i!=this.data.barIndex){
　  　this.setData({
  　　  　barIndex: i
  　　})
      if(this.data.newsList[i].length==0){
        this.getNewsList('false')
      }
  　}
   this.setData({
     barNavX: disX
   })
  },
  goNewsDetail: function(e) {
    let id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/news/news?id='+id,
    })
  },
  initNewsList: function() { // 初始化newsList
    var list = []
    for(var i=0;i<this.data.barList.length;i++){
      var tmpList = []
      list[i] = tmpList
    }
    this.setData({
      newsList: list
    })
  },
  getNewsList: function(reverse) {
    var that =  this;
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/newsList/'+that.data.revCatMap[that.data.barList[that.data.barIndex]],
      header: header,
      success:function(res){
        var tmpList = that.data.newsList;
        for (let i in tmpList) {
          //遍历列表数据      
          if (i == that.data.barIndex) {
            //根据下标找到目标,改变状态
            if(reverse=='false'){
              tmpList[i] = tmpList[i].concat(res.data);
            }else{
              tmpList[i] = res.data.concat(tmpList[i]);
            }  
            // console.log(tmpList[i])
          }
        }
        // console.log(tmpList)
        that.setData({
          newsList: tmpList
        })
        // console.log(res.data)
      }
    })
    // var res=[
    //   {docId:'kknscsi2187490', title:'不戴口罩还故意咳嗽！美国网约车司机遭乘客粗暴对待', cat:'world', commentCount:303, newsTime:'Fri Mar 12 09:01:00 CST 2021',newsTimeStamp:1615736435000, url:'https://news.sina.com.cn/w/2021-03-12/doc-ikknscsi2187490.shtml',img:'http://n.sinaimg.cn/ent/transform/500/w300h200/20210312/460c-kmeeiut2336332.png', recommendSource:0},{docId:'kkntiam0204603', title:'快递不送货上门，消费者如何应对？专家支招', cat:'society', commentCount:55, newsTime:'Sat Mar 13 12:20:00 CST 2021',newsTimeStamp:1615650035000, url:'https://news.sina.com.cn/s/2021-03-13/doc-ikkntiam0204603.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:1},{docId:'kkntiak6001339', title:'开玩笑把邻居“笑”死了 赔偿6万元！', cat:'society', commentCount:2662, newsTime:'Mon Mar 08 09:46:00 CST 2021',newsTimeStamp:1614786035000, url:'https://news.sina.com.cn/o/2021-03-08/doc-ikkntiak6001339.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:2}
    // ]
    // var tmpList = this.data.newsList;
    // for (let i in tmpList) {
    //   //遍历列表数据      
    //   if (i == this.data.barIndex) {
    //     //根据下标找到目标,改变状态  
    //     tmpList[i] = tmpList[i].concat(res);
    //     // tmpList[i] = tmpList[i].concat(res.data);
    //     // console.log(tmpList[i])
    //   }
    // }
    // // console.log(tmpList)
    // that.setData({
    //   newsList: tmpList
    // })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.initNewsList()
    let token = wx.getStorageSync('userid');
    if(!token){
      console.log('index without token')
      //向app实例注册一个回调函数
      getApp().tokenCallBack= (token)=>{
        if(token){
          wx.setStorageSync('userid', token)
          console.log('已回调，设置了token')
          this.getNewsList('false');
        }
      }
    }else{
      this.getNewsList('false');
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    var that = this
    console.log('下拉刷新列表...')
    if (this.data.isRefreshing || this.data.isLoadingMoreData) {
      return
    }
    this.setData({
      isRefreshing: true
    });
    this.getNewsList('true');
    setTimeout(function () {
      wx.stopPullDownRefresh() 
      that.setData({
        isRefreshing: false
      });
    }, 1500);
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    if (this.data.isRefreshing || this.data.isLoadingMoreData){
      return
    }
    this.setData({
      isLoadingMoreData: true
    });
    this.getNewsList('false');
    // 等待一段时间再加载数据
    setTimeout(()=>{
      this.setData({
        isLoadingMoreData: false,
      });
    }, 1500);
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})