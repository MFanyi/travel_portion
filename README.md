# 基于SpringBoot+html实现的旅游网站
后台主要采用SpringBoot进行开发，持久化采用的是Mybatis，数据库系统用的是mysql，数据库连接池采用了Hikari<br/>
用户注册信息采用redis暂时存储，用户未激活之前用户信息不进行持久化，只有用户通过邮件进行激活之后才进行持久化操作并移除redis中存储的用户信息，如果用户超过1小时没有激活则redis会自动移除注册信息<br/>
页面显示采用的是html+ajax的搭配，显示效果基本由jquery实现，排版问题使用bootstrap解决<br/>
