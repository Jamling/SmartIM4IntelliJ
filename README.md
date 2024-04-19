[![GitHub release](https://img.shields.io/github/release/jamling/SmartIM4Intellij.svg?maxAge=3600)](https://github.com/Jamling/SmartIM4Intellij)
[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/9816-smartim.svg)](https://plugins.jetbrains.com/plugin/9816-smartim)
[![JetBrains plugins](https://img.shields.io/jetbrains/plugin/d/9816-smartim.svg)](https://plugins.jetbrains.com/plugin/9816-smartim)


## 简介

![screenshot](https://raw.githubusercontent.com/Jamling/SmartIM4IntelliJ/master/screenshot.png)

IntelliJ IDEA上的[SmartIM](https://github.com/Jamling/SmartIM/)插件，适用于大多数的Intellij IDE，

Eclipse IDE插件请访问 https://github.com/Jamling/SmartIM4Eclipse/

## 功能

- [x] 收发文本消息
- [x] 收发图片
- [x] 收发文件
- [x] 发送工程中的文件
- [x] Code Review 发送代码位置及评语
- [x] 超链接点击
- [x] 支持图灵机器人接入

**SmartQQ官方协议不支持收发图片和文件，扩展的文件收发由[第三方实现](http://api.ieclipse.cn/smartqq)**

**微信**

已切换为PC版协议

## 更新日志
- v3.0.0 2024-04-06，微信切换为PC协议，解决饱受诟病的账号限制登录问题
- v2.7.2 2019-08-15，修复微信登录时的NumberFormatException
- v2.7.0 2019-04-25，添加自定义聊天消息样式，添加dark主题下的相关图标
- v2.6.1 2019-03-02, 修复代码评审xml被过滤和选择对象错误的issue 优化微信联系人，不显示emoji
- v2.4.0 2018-05-10, 修复部分微信用户登录异常的问题
- v2.3.0 2018-02-27, 添加发送工程中的文件，图灵机器人，消息群发等功能，在功能上，终于与eclipse版本相差无几了
- v2.2.0 2018-02-06, 添加微信发送文件，接收动画表情、文件、图片功能
- v2.1.0 2018-01-01, 聊天内容支持富文本，优化微信最近联系人，微信好友中添加一个group分组，修复SmartQQ发送失败的问题，忽略SmartQQ获取最近联系人失败...
- v2.0.1 2017-11-17, 修复在某些IDE中，安装插件后，无法打开IDE设置的问题
- v2.0.0 2017-11-11, 基于SmartIM重构，支持微信。 ***此版本在某些IDE上，会导致IDE的设置无法打开（参考[#10](https://github.com/Jamling/SmartIM4IntelliJ/issues/10)），强烈建议升级到新版本***
- v1.0.5/2017/08/16，乃们要的文件发送功能来了。
- v1.0.3/2017/07/14，美化了UI，添加了异常通知。

## 安装

- File->Settings->Plugins打开插件设置界面
- 点击Browser repositories...并输入SmartIM执行搜索
- 点击SmartIM进行安装并重启

## 使用

- View -> ToolWindows 勾选SmartIM
- （可选）点击SmartIM顶部的具体IM（默认为Wechat）
- 点击左侧工具栏的登录图标（第一个）并使用手机扫码登录。

