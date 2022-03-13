// app.js
App({
  onLaunch() {
    wx.clearStorageSync()
    this.getAuth();
    this.setToken();
  },
  getAuth(){
    // An highlighted block
    wx.showModal({
      title: '温馨提示',
      content: '正在请求您的个人信息',
      success(res) {
        if (res.confirm) {
          wx.getUserProfile({
          desc: "获取你的昵称、头像、地区及性别",
          success: res => {
            console.log(res)
            wx.setStorageSync("avatar", res.userInfo.avatarUrl);
            wx.setStorageSync("nickName", res.userInfo.nickName);
          },
          fail: res => {
            //拒绝授权
            that.showErrorModal('您拒绝了请求');
            return;
          }
        })} else if (res.cancel) {
          //拒绝授权 showErrorModal是自定义的提示
          that.showErrorModal('您拒绝了请求');
          return;
        }
      }
    })
  },
  setToken() {
    let that = this;
    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
          console.log(res)
          wx.request({
            url: that.globalData.host + '/login',
            method: 'POST',
            data: {
              code: res.code
            },
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            success(res) {
              console.log('用户登录成功！id=' + res.data.session_key);
              wx.setStorageSync("userid", res.data.userid);//将用户id保存到缓存中
              wx.setStorageSync("session_key", res.data.session_key);//将session_key保存到缓存中
              if(getApp().tokenCallBack){
                console.log('调用callback')
                getApp().tokenCallBack(res.data.userid)
              }
            }
          })
        }
    })
  },
  globalData: {
    host: 'https://www.marcel0637.cn'
    // host: 'https://47.111.112.196'
  }
})
