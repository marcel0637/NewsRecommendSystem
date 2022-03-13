// pages/news/news.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: 'https://www.marcel0637.cn',
    // host: 'https://47.111.112.196',
    id: 'kknscsi2187490',
    newsDetail:{},
    comments: [],
    comment: null,
    placeholder: "评论点什么吧~",
    isCollect: false,
  },
  getNewsDeatil: function(){
    var that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/news/'+that.data.id,
      header: header,
      success:function(res){
        that.setData({
          newsDetail: res.data
        })
        console.log(res.data)
      }
    })
    // var res={docId:'kknscsi2187490', title:'不戴口罩还故意咳嗽！美国网约车司机遭乘客粗暴对待', tag:'world', commentCount:303, newsTime:'Fri Mar 12 09:01:00 CST 2021',newsTimeStamp:1615736435000, url:'https://news.sina.com.cn/w/2021-03-12/doc-ikknscsi2187490.shtml',img:'http://n.sinaimg.cn/ent/transform/500/w300h200/20210312/460c-kmeeiut2336332.png',newsFrom:'新华社',artibody:'\n\n哈里斯与《辛普森一家》中的丽萨撞衫\n　　新浪娱乐讯 美国总统就职典礼之后热议不断，近日网友又发现，动画《辛普森一家》中与就职典礼实际场面中的“神相似”，包括哈里斯就职当天穿的衣服与动画中的丽萨撞衫等。\n　　网友还指出，动画片中丽萨提到，从特朗普总统那里“继承”到吃紧的预算；动画片中汤姆·汉克斯在“国家混乱”时刻向公众发表著名演讲，这些场面都曾出现在现实生活中。\n\n\n\n\n\n\n\n'}
    // that.setData({
    //   newsDetail: res
    // })
  },
    /**
   * 获取评论
   */
  getComments() {
    const that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/commentList/'+that.data.id,
      header: header,
      method: 'GET',
      success:function(res){
        that.setData({
          comments: res.data
        })
      }
    })

    // var commentDetail = {
    //   user:{
    //     avatar:'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132',
    //     nick_name:'marcel'
    //   },
    //   create_time:'2021-04-08 10:10:01',
    //   content:'helw哈哈哈哈哈哈哈哈哈哈哈哈哈哈lo'
    // }
    // this.setData({
    //   comments: this.data.comments.concat(commentDetail).concat(commentDetail).concat(commentDetail).concat(commentDetail)
    // })
  },
  /**
   * 设置评论
   */
  inputComment(event) {
    this.setData({
      comment: event.detail.value
    })
  },
    /**
   * 发送评论
   */
  onCommntBtnTap() {
    const comment = this.data.comment
    if (comment==null) {
      wx.lin.showMessage({
        type: "error",
        content: "评论不能为空！"
      })
      return
    }
    const that = this
    var header;
    header = {
      'content-type': 'application/json;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    var newComment = {
      'docId':that.data.id,
      'content':that.data.comment,
      'avatar':wx.getStorageSync("avatar"),
      'nickName':wx.getStorageSync("nickName")
    }
    wx.request({
      url: that.data.host+'/comment',
      header: header,
      data: newComment,
      method: 'POST',
      success:function(res){
        if(res.statusCode==200){
          wx.lin.showMessage({
            type: "success",
            content: "评论成功！"
          })
          setTimeout(function () {
            wx.pageScrollTo({
              scrollTop: 10000
            })
          }, 1000)
        }else{
          wx.lin.showMessage({
            type: "error",
            content: "评论失败！"
          })
        }
        // 重新获取评论列表
        that.getComments()
        that.setData({
          comment: null,
          placeholder: "评论点什么吧..."
        })
      }
    })
  },
  uploadAction: function() {
    var that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/news/'+that.data.id,
      header: header,
      method: 'POST',
      success:function(res){
        console.log('上传点击事件成功')
      }
    })
  },
  goToComment: function() {
    wx.pageScrollTo({
      scrollTop:10000,
      duration: 300,
    })
  },
  getIsCollect: function() {
    var that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    wx.request({
      url: that.data.host+'/news/collect/'+that.data.id,
      header: header,
      method: 'GET',
      success:function(res){
        that.setData({
          isCollect: res.data
        })
      }
    })
  },
  changeCollect: function() {
    var that = this
    var header;
    header = {
      'content-type': 'application/x-www-form-urlencoded;charset=utf-8',
      'cookie':wx.getStorageSync("userid")//读取cookie
    };
    var str;
    if(this.data.isCollect) str='取消收藏成功'
    else str='收藏成功'
    wx.request({
      url: that.data.host+'/news/collect/'+that.data.id,
      header: header,
      method: 'POST',
      success:function(res){
        that.setData({
          isCollect: !that.data.isCollect
        })
        wx.lin.showMessage({
          type: "success",
          content: str
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      id: options.id
    })
    console.log(this.id)
    this.uploadAction()
    this.getIsCollect()
    this.getNewsDeatil()
    this.getComments()
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