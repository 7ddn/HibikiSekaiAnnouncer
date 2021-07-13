# hibiki-sekai-announcer

> 为プロジェクトセカイ feat.初音ミク 开发的支持插件
> 部分数据默认从pjsek.ai获取，该站的api开放使用，并且已经得到维护者Erik Chan的允许，也欢迎大家在patreon上支持该开发者

## 功能

- 活动开始/结束提醒
- 卡查
- 抽卡模拟

## 下载
可以从[releases](https://github.com/7ddn/HibikiSekaiAnnouncer/releases]https://github.com/7ddn/HibikiSekaiAnnouncer/releases)里下载

## 配置
```yaml
# UserAgent
# 从API获取数据是使用的UserAgent伪装
UserAgent: 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36'

# WorkingDir
# 工作地址，获取的各项资源和抽卡模拟生成的图片文件会保存在该目录下的各项文件夹内
WorkingDir: \

# ExternalResourceUrls
# 一些静态的外部资源的地址，目前主要包括抽卡模拟所需要的各种素材
ExternalResourceUrls: 
  gachaTemplate: 'https://z3.ax1x.com/2021/07/12/WisHaT.png'
  cardFrame: 'https://pjsek.ai/images/member/cardFrame_S_'
  rarityStar: 'https://pjsek.ai/images/member/rarity_star_normal.png'
  attributeIcon: 'https://pjsek.ai/images/member/icon_attribute_'
  
# API
# 获取游戏本身内容的API地址，默认从pjsek.ai的API处读取数据
APIs: 
  song: 'https://api.pjsek.ai/database/master/musics?id='
  card: 'https://api.pjsek.ai/database/master/cards?id='
  event: 'https://api.pjsek.ai/database/master/events?id='
  gacha: 'https://api.pjsek.ai/database/master/gachas?id='
  difficulties: 'https://api.pjsek.ai/database/master/musicDifficulties?musicId='
  
# iconCoordinate
# 抽卡模拟时合成图片的坐标，不确定放在这里是否合适……不过姑且还是当做一项config来设定
```

## 食用方法

暂时只支持部分自然语言解析的命令

### 刷新资源
```
刷新资源
```
重新获取各项资源，一般在新活动附近使用来获取新的活动、卡面和抽卡信息

### 提醒
```
添加活动订阅
```
为每个活动添加数个预设文本的闹钟，目前包括活动开始前、活动分数结算当天和结算前、活动截止前。

```
@bot <time>叫我<content>
```
设置一条内容为&lt;content>在&lt;time>时触发的闹钟，&lt;time>分为日期和时间两部分，日期可以用(M?M月)?(d?d)日的形式，或今天/明天/后天的形式，时间为HH:mm(:ss)?的格式，例如，以下&lt;time>都是合法的
```
08月02日18:00:00
明天17:00
5日12:33
```  

### 抽卡模拟
```
抽卡模拟
```
模拟一次最近常规卡池的抽卡

```
fes模拟
```
模拟一次最近fes卡池的抽卡

```
活动up模拟
```
模拟一次最近活动up卡池的抽卡
