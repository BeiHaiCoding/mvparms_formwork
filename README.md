# android_formwork

#### 介绍
MVPArms安卓程序开发模板，适合想要在mvparms中接入bugly的新手。关于mvparms框架有任何使用疑问，可以去jess神mvparms项目下的wiki中查看使用说明

#### 软件架构
本项目在MVPArms开源框架的基础上，接入了Bugly以及Tinker热修复，增加了混淆配置文件，以及一个自用的第三方library


#### 安装教程
将项目clone下来，可以直接作为待开发项目的主结构（自行修改包名，修改包名后全局替换包名，包括混淆文件下的包名）

#### 使用说明

1.  bugly的功能接入需要自行去bugly官网注册appid
2.  混淆文件包含了大多数需要keep的类，如mvparms、bugly、四大组件等等
3.  mylibrary是一个自用的第三方库合集，常用的类有SPHelper(App缓存工具)、GActHelper(带参数启动activity)、BaseQuickAdapter(recyclerview的adapter帮助类)。其他的功能请自行研究，如不需要此库可在settings.gradle类关闭此库。





