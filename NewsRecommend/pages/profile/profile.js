// pages/profile/profile.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: 'https://www.marcel0637.cn',
    // host: 'https://47.111.112.196',
    tabsFixed: false, // Tabs是否吸顶
    tabIndex: 0,
    stars: [], // 收藏列表
    views: [], // 浏览列表
    interestList: [], // 关键词列表
    tagEle: [], // 关键词+样式列表
    interval: '', //定时器
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
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getViews()
    this.getStars()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  /**
   * Tab切换
   */
  changeTabs(event) {
    const tabIndex = event.detail.currentIndex
    this.setData({
      tabIndex: tabIndex
    })
    if (tabIndex == 0) {
      this.getWordsList()
    }
    if (tabIndex == 1) {
      this.getViews()
    }
    if (tabIndex == 2) {
      this.getStars()
    }
  },
  gotoNewsDetail: function(e) {
    let id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/news/news?id='+id,
    })
  },
  getStars: function() {
    const that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/collectList',
      header: header,
      method: 'GET',
      success:function(res){
        that.setData({
          stars: res.data
        })
      }
    })

    // var res=[
    //   {docId:'kknscsi2187490', title:'不戴口罩还故意咳嗽！美国网约车司机遭乘客粗暴对待', cat:'world', commentCount:303, newsTime:'Fri Mar 12 09:01:00 CST 2021',newsTimeStamp:1615736435000, url:'https://news.sina.com.cn/w/2021-03-12/doc-ikknscsi2187490.shtml',img:'http://n.sinaimg.cn/ent/transform/500/w300h200/20210312/460c-kmeeiut2336332.png', recommendSource:0},{docId:'kkntiam0204603', title:'快递不送货上门，消费者如何应对？专家支招', cat:'society', commentCount:55, newsTime:'Sat Mar 13 12:20:00 CST 2021',newsTimeStamp:1615650035000, url:'https://news.sina.com.cn/s/2021-03-13/doc-ikkntiam0204603.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:1},{docId:'kkntiak6001339', title:'开玩笑把邻居“笑”死了 赔偿6万元！', cat:'society', commentCount:2662, newsTime:'Mon Mar 08 09:46:00 CST 2021',newsTimeStamp:1614786035000, url:'https://news.sina.com.cn/o/2021-03-08/doc-ikkntiak6001339.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:2}
    // ]
    // this.setData({
    //   stars: res
    // })
  },
  getViews: function() {
    const that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/viewList',
      header: header,
      method: 'GET',
      success:function(res){
        that.setData({
          views: res.data
        })
      }
    })

    // var res=[
    //   {docId:'kknscsi2187490', title:'不戴口罩还故意咳嗽！美国网约车司机遭乘客粗暴对待', cat:'world', commentCount:303, newsTime:'Fri Mar 12 09:01:00 CST 2021',newsTimeStamp:1615736435000, url:'https://news.sina.com.cn/w/2021-03-12/doc-ikknscsi2187490.shtml',img:'http://n.sinaimg.cn/ent/transform/500/w300h200/20210312/460c-kmeeiut2336332.png', recommendSource:0},{docId:'kkntiam0204603', title:'快递不送货上门，消费者如何应对？专家支招', cat:'society', commentCount:55, newsTime:'Sat Mar 13 12:20:00 CST 2021',newsTimeStamp:1615650035000, url:'https://news.sina.com.cn/s/2021-03-13/doc-ikkntiam0204603.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:1},{docId:'kkntiak6001339', title:'开玩笑把邻居“笑”死了 赔偿6万元！', cat:'society', commentCount:2662, newsTime:'Mon Mar 08 09:46:00 CST 2021',newsTimeStamp:1614786035000, url:'https://news.sina.com.cn/o/2021-03-08/doc-ikkntiak6001339.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:2}
    // ]
    // this.setData({
    //   views: res
    // })
  },
  getColor: function () {
    var letter = "0123456789ABCDEF";
    var color = [];
    var c = "#";
    for (var i = 0; i < 6; i++) {
      c = c + letter[Math.floor(Math.random() * 16)]
    }
    color.push(c);
    return color;
  },
  getWords: function(wordList) {
    var that = this
    clearInterval(this.data.interval)    
    console.log(wordList)
    let tagEle = []
    for(let i = 0;i<wordList.length;i++){
      tagEle.push({
        title: wordList[i],
        x: 0,
        y: 0,
        z: 0,
        s: 0,
        o: 1,
        f: 15,
        angleX: 0,
        angleY: 0,
        color: this.getColor()
      })
    }
    for (var i = 0; i < tagEle.length; i++) {
      var fallLength = 100 //圆的焦点
      var angleX = Math.PI / 100
      var angleY = Math.PI / 100
      var k = (2 * (i + 1) - 1) / tagEle.length - 1;
      //计算按圆形旋转
      var a = Math.acos(k);
      var b = a * Math.sqrt(tagEle.length * Math.PI);
     //计算元素x，y坐标
      var numx = 120 * Math.sin(a) * Math.cos(b)
      var numy = 120 * Math.sin(a) * Math.sin(b);
      var numz = 220 * Math.cos(a);

     // console.log(numo)
      //计算元素缩放大小
      tagEle[i].x = numx * 2
      tagEle[i].y = numy * 2
      tagEle[i].z = numz
      tagEle[i].s = 250 / (400 - tagEle[i].z)
    }
    console.log(tagEle)
     //动画
     var interval = setInterval( () =>{
        for (var i = 0; i < tagEle.length; i++) {
          var a = Math.acos(k);
          var numz = 240 * Math.cos(a);
          tagEle[i].s = 250 / (400 - tagEle[i].z)
          var cos = Math.cos(angleX);
          var sin = Math.sin(angleX);
          var y1 = tagEle[i].y * cos - tagEle[i].z * sin;
          var z1 = tagEle[i].z * cos + tagEle[i].y * sin;
          tagEle[i].y = y1;
          tagEle[i].z = z1;

          var cos = Math.cos(angleY);
          var sin = Math.sin(angleY);
          var x1 = tagEle[i].x * cos - tagEle[i].z * sin;
          var z1 = tagEle[i].z * cos + tagEle[i].x * sin;
          tagEle[i].x = x1;
          tagEle[i].z = z1;
          that.setData({
            tagEle: tagEle
          })
        }
      }, 100)
      that.setData({
        interval:interval
      })
      console.log(that.data.interval)
  },
  getWordsList: function(){
    const that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/interestWords/15',
      header: header,
      method: 'GET',
      success:function(res){
        that.setData({
          interestList: res.data
        })
        that.getWords(res.data)
      }
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getWordsList()
    this.getViews()
    this.getStars()
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

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})