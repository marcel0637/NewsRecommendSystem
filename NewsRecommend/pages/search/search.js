// pages/search/search.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: 'https://www.marcel0637.cn',
    // host: 'https://47.111.112.196',
    searchValue: "",
    searchs: [],
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
  gotoNewsDetail: function(e) {
    let id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/news/news?id='+id,
    })
  },
  bindSearchInput: function (event) {
    var value = event.detail.value;
    this.setData({
      searchValue:value
    })
    this.getSearchs(value)
  },
  getSearchs: function(key) {
    const that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/news/search/'+key,
      header: header,
      method: 'GET',
      success:function(res){
        var realRes = that.highLightWord(res.data,key)
        console.log(realRes)
        that.setData({
          searchs: realRes
        })
      }
    })
    // var res=[
    //   {docId:'kknscsi2187490', title:'不戴口罩还故意咳嗽！美国网约车司机遭乘客粗暴对待', cat:'world', commentCount:303, newsTime:'Fri Mar 12 09:01:00 CST 2021',newsTimeStamp:1615736435000, url:'https://news.sina.com.cn/w/2021-03-12/doc-ikknscsi2187490.shtml',img:'http://n.sinaimg.cn/ent/transform/500/w300h200/20210312/460c-kmeeiut2336332.png', recommendSource:0},{docId:'kkntiam0204603', title:'快递不送货上门，消费者如何应对？专家支招', cat:'society', commentCount:55, newsTime:'Sat Mar 13 12:20:00 CST 2021',newsTimeStamp:1615650035000, url:'https://news.sina.com.cn/s/2021-03-13/doc-ikkntiam0204603.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:1},{docId:'kkntiak6001339', title:'开玩笑把邻居“笑”死了 赔偿6万元！', cat:'society', commentCount:2662, newsTime:'Mon Mar 08 09:46:00 CST 2021',newsTimeStamp:1614786035000, url:'https://news.sina.com.cn/o/2021-03-08/doc-ikkntiak6001339.shtml',img:'http://n.sinaimg.cn/news/transform/194/w550h444/20210314/5339-kmkptxc3132058.png', recommendSource:2}
    // ]
    // var ress = this.highLightWord(res, key)
    // this.setData({
    //   searchs: ress
    // })
  },
  highLightWord: function(data, key){
    for(var i = 0; i<data.length;i++){
      var title = (data[i].title).replace(new RegExp(`${key}`,'g'), `%%${key}%%`).split('%%');
      data[i].title = title
    }
    return data
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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