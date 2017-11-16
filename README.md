[![GitHub release](https://img.shields.io/github/release/jamling/SmartQQ4Intellij.svg?maxAge=3600)](https://github.com/Jamling/SmartQQ4Intellij)

## 简介

![screenshot](https://raw.githubusercontent.com/Jamling/SmartQQ4IntelliJ/master/screenshot.png)

IntelliJ Idea上的SmartQQ插件，适用于大多数的Intellij IDE，

Eclipse的插件请访问 https://github.com/Jamling/SmartQQ4Eclipse/

## 功能

**注：IntelliJ插件版本功能没有Eclipse插件版本功能多**

- 收发文本消息
- [发送文件](http://api.ieclipse.cn/smartqq)

**微信**

微信需加JVM运行参数，如快捷方式目标指向：`E:\Android\eclipse\eclipse.exe -nl=en -Djsse.enableSNIExtension=false`，注意，exe所在的路径不能带空格，比如如果你的路径为`"E:\Android\Android Studio\studio64.exe"`需要先改路径，消灭`Andnroid Studio`中的空格，然后再加`-Djsse.enableSNIExtension=false`参数。

## 更新日志

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


