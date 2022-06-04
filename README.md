# MediaSessionPlayer
项目要求
音乐播放器基本需求

本需求不代表普通音乐播放器的设计需求，
作为练手用，设计了不切实际的需求，增加逻辑复杂性。

技术要求
①要求使用MVVM实现，播放列表使用RecyclerView。
②音乐播放使用MediaSession框架。
设计要求
做成RTM， sequence， 状态迁移(STM) 设计。

1. 播放器画面
①启动后显示播放画面：显示歌曲名称歌手名称专辑名称播放时间（进度条不做）。
以及后台播放开关。
②启动后自动播放当前播放列表中第一首歌，按循序依次播放。
③暂停/播放button ：歌曲正在播放时，点击暂停键，暂停播放当前歌曲。
④上一曲/下一曲：点击，播放列表中（当前顺序的）上一首/下一首歌曲
⑤播放列表button点击：切换到歌曲列表
⑥后台播放开关（默认开）

2. 播放列表
①固定播放列表内容（app内准备，30首固定歌曲，仅MP3文件）
②默认播放列表按照歌曲名称正序排序
③点击播放列表中的歌曲进行播放，跳转播放界面。
④播放列表显示歌曲名歌手名，歌曲时长
⑤点击排序按键，可在歌曲名正序/倒序两种循序中切换。

3.AudioFocus
① app启动时申请AudioFocus
②声音焦点被抢夺，暂停播放，声音焦点恢复继续播放。

4.后台播放
①后台播放开关开启，app切换到后台继续播放。
②后台播放开关关闭，app切换到后台暂停播放，切换回前台继续播放。


使用KOTLIN语言开发
借助了MVVM框架和MediaSession框架
通过AudioFocus处理焦点事务