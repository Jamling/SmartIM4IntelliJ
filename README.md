[![GitHub release](https://img.shields.io/github/release/jamling/SmartQQ4Intellij.svg?maxAge=3600)](https://github.com/Jamling/SmartQQ4Intellij)
[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/9816-smartqq.svg)](https://plugins.jetbrains.com/plugin/9816-smartqq)
[![JetBrains plugins](https://img.shields.io/jetbrains/plugin/d/9816-smartqq.svg)](https://plugins.jetbrains.com/plugin/9816-smartqq)


## 简介

![screenshot](https://raw.githubusercontent.com/Jamling/SmartQQ4IntelliJ/master/screenshot.png)

IntelliJ Idea上的SmartQQ插件，适用于大多数的Intellij IDE，

Eclipse的插件请访问 https://github.com/Jamling/SmartQQ4Eclipse/

## 功能

**注：IntelliJ插件版本功能没有Eclipse插件版本功能多**

- [x] 收发文本消息
- [x] 收发图片
- [x] 收发文件
- [x] 发送工程中的文件
- [x] Code Review 发送代码位置及评语
- [x] 超链接点击
- [x] 支持图灵机器人接入

**SmartQQ官方协议不支持收发图片和文件，扩展的文件收发由[第三方实现](http://api.ieclipse.cn/smartqq)**

**微信**

微信需加JVM运行参数

找到你的IDE所在的安装目录下的bin文件夹，打开，找到你的exe运行参数文件，如(idea64.exe.vmoptions，studio64.exe.vmoptions）使用文件编辑器打开此文件，在最后一行添加`-Djsse.enableSNIExtension=false`保存后重启IDE即可。

也可以通过exe快捷方式添加参数，如快捷方式目标指向：`E:\Android\eclipse\eclipse.exe -nl=en -Djsse.enableSNIExtension=false`，注意这种方式要求exe所在的路径不能带空格。

## 更新日志

- v2.4.0 2018-05-10, 修复部分微信用户登录异常的问题
- v2.3.0 2018-02-27, 添加发送工程中的文件，图灵机器人，消息群发等功能，在功能上，终于与eclipse版本相差无几了
- v2.2.0 2018-02-06, 添加微信发送文件，接收动画表情、文件、图片功能
- v2.1.0 2018-01-01, 聊天内容支持富文本，优化微信最近联系人，微信好友中添加一个group分组，修复SmartQQ发送失败的问题，忽略SmartQQ获取最近联系人失败...
- v2.0.1 2017-11-17, 修复在某些IDE中，安装插件后，无法打开IDE设置的问题
- v2.0.0 2017-11-11, 基于SmartIM重构，支持微信。 ***此版本在某些IDE上，会导致IDE的设置无法打开（参考[#10](https://github.com/Jamling/SmartQQ4IntelliJ/issues/10)），强烈建议升级到新版本***
- v1.0.5/2017/08/16，乃们要的文件发送功能来了。
- v1.0.3/2017/07/14，美化了UI，添加了异常通知。

## 安装

- File->Settings->Plugins打开插件设置界面
- 点击Browser repositories...并输入SmartQQ执行搜索
- 点击SmartQQ进行安装并重启

## 使用

- 安装成功后，会在底部栏出现一个Smart的tab（如果没有底部栏，则在菜单View中把ToolButtons勾选上）
- 点击Smart中的二维码使用手机QQ扫码登录


