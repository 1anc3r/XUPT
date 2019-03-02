# 西邮记(XUPT)
<img src="https://github.com/1anc3r/XUPT/blob/master/app/src/main/ic_launcher-web.png?raw=true" width = "96" height = "96" alt="icon"/>

## 应用简介:
西邮记是一款教务信息查询应用（已上线），应用实现了课表、成绩查询，考勤记 录查询、统计及申诉，图书借阅查询、续借及收藏，英语四六级查询。 

## 相关链接:
* Github: [![](https://img.shields.io/badge/github-1anc3r-yellowgreen.svg)](https://github.com/1anc3r)
* 我的博客: [![](https://img.shields.io/badge/blog-1anc3r-green.svg)](http://1anc3r.github.io/)
* 实验室主页: [![](https://img.shields.io/badge/wiki-xiyoumobile-brightgreen.svg)](http://www.xiyoumobile.com/)
* 下载链接: [![](https://img.shields.io/badge/download-v1.2-blue.svg)](http://fir.im/xupt)

## 功能介绍:
1. 教务处: 查询课表、成绩和个人信息
2. 考勤表: 查看考勤记录明细和统计，也可以申诉
3. 图书馆: 检索图书，查看当前借阅、历史借阅和收藏, 也可以(取消)收藏、续借
4. 四六级: 查询英语四六级考试成绩, 包括听力、阅读、写作和总分
5. 桌面组件：展示课表
6. 接入豆瓣书评、影评、乐评

## 常见问题:
1. 教务处: 请使用西邮教务管理系统的账号和密码登录
2. 考勤表: 请使用一卡通账号和密码登录, 密码默认是学号后六位
3. 图书馆: 请使用图书馆账号和密码登录, 密码默认是123456
4. 应用内意见反馈通道尚未开启, 遇到Bug请发送邮件至huangfangzhi0@foxmail.com

## 应用界面:

<img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/动图1.gif" width = "288" height = "512" alt="" /><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/动图2.gif" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/动图3.gif" width = "288" height = "512" alt=""/>

<img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/课表.jpeg" width = "288" height = "369" alt="" /><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/成绩单.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/个人信息.jpeg" width = "288" height = "512" alt=""/>

<img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/成绩单_夜间.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/课表_夜间.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/课表_桌面.jpeg" width = "288" height = "512" alt=""/>

<img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/图书馆_检索.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/图书馆_详情.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/图书馆_我的.jpeg" width = "288" height = "512" alt=""/>

<img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/考勤表_明细.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/考勤表_申诉.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/考勤表_统计.jpeg" width = "288" height = "512" alt=""/>

<img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/侧边栏.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/设置.jpeg" width = "288" height = "512" alt=""/><img src="https://github.com/1anc3r/XUPT/master/blob/master/Screenshots/应用信息.jpeg" width = "288" height = "512" alt=""/>

## 技术要点
项目采用 MVP 架构，通过 Fiddler 抓包并分析，模拟网络数据请求；使用自定义 View 实现课表布局；使用 RemoteView 实现课表桌面小组件。通过此项目熟悉了 MVP 设计 思想，学习了如何抓包，掌握了基于HTTP的网络编程，掌握了自定义View。 
