package com.xiaoyv.extension.hanime

import com.xiaoyv.flexiflix.extension.model.FlexKeyValue
import com.xiaoyv.flexiflix.extension.utils.currentYear

/**
 * [SearchOptions]
 *
 * @author why
 * @since 5/18/24
 */
internal object SearchOptions {
    internal val tags: List<FlexKeyValue>
        get() = listOf(
            FlexKeyValue("無碼", "無碼"),
            FlexKeyValue("AI解碼", "AI解碼"),
            FlexKeyValue("中文字幕", "中文字幕"),
            FlexKeyValue("1080p", "1080p"),
            FlexKeyValue("60FPS", "60FPS"),
            FlexKeyValue("ASMR", "ASMR"),
            FlexKeyValue("近親", "近親"),
            FlexKeyValue("姐", "姐"),
            FlexKeyValue("妹", "妹"),
            FlexKeyValue("母", "母"),
            FlexKeyValue("女兒", "女兒"),
            FlexKeyValue("師生", "師生"),
            FlexKeyValue("情侶", "情侶"),
            FlexKeyValue("青梅竹馬", "青梅竹馬"),
            FlexKeyValue("同事", "同事"),
            FlexKeyValue("JK", "JK"),
            FlexKeyValue("處女", "處女"),
            FlexKeyValue("御姐", "御姐"),
            FlexKeyValue("熟女", "熟女"),
            FlexKeyValue("人妻", "人妻"),
            FlexKeyValue("老師", "老師"),
            FlexKeyValue("女醫護士", "女醫護士"),
            FlexKeyValue("OL", "OL"),
            FlexKeyValue("大小姐", "大小姐"),
            FlexKeyValue("偶像", "偶像"),
            FlexKeyValue("女僕", "女僕"),
            FlexKeyValue("巫女", "巫女"),
            FlexKeyValue("修女", "修女"),
            FlexKeyValue("風俗娘", "風俗娘"),
            FlexKeyValue("公主", "公主"),
            FlexKeyValue("女戰士", "女戰士"),
            FlexKeyValue("魔法少女", "魔法少女"),
            FlexKeyValue("異種族", "異種族"),
            FlexKeyValue("妖精", "妖精"),
            FlexKeyValue("魔物娘", "魔物娘"),
            FlexKeyValue("獸娘", "獸娘"),
            FlexKeyValue("碧池", "碧池"),
            FlexKeyValue("痴女", "痴女"),
            FlexKeyValue("雌小鬼", "雌小鬼"),
            FlexKeyValue("不良少女", "不良少女"),
            FlexKeyValue("傲嬌", "傲嬌"),
            FlexKeyValue("病嬌", "病嬌"),
            FlexKeyValue("無口", "無口"),
            FlexKeyValue("偽娘", "偽娘"),
            FlexKeyValue("扶他", "扶他"),
            FlexKeyValue("短髮", "短髮"),
            FlexKeyValue("馬尾", "馬尾"),
            FlexKeyValue("雙馬尾", "雙馬尾"),
            FlexKeyValue("巨乳", "巨乳"),
            FlexKeyValue("貧乳", "貧乳"),
            FlexKeyValue("黑皮膚", "黑皮膚"),
            FlexKeyValue("眼鏡娘", "眼鏡娘"),
            FlexKeyValue("獸耳", "獸耳"),
            FlexKeyValue("美人痣", "美人痣"),
            FlexKeyValue("肌肉女", "肌肉女"),
            FlexKeyValue("白虎", "白虎"),
            FlexKeyValue("陰毛", "陰毛"),
            FlexKeyValue("腋毛", "腋毛"),
            FlexKeyValue("大屌", "大屌"),
            FlexKeyValue("水手服", "水手服"),
            FlexKeyValue("體操服", "體操服"),
            FlexKeyValue("泳裝", "泳裝"),
            FlexKeyValue("比基尼", "比基尼"),
            FlexKeyValue("和服", "和服"),
            FlexKeyValue("兔女郎", "兔女郎"),
            FlexKeyValue("圍裙", "圍裙"),
            FlexKeyValue("啦啦隊", "啦啦隊"),
            FlexKeyValue("旗袍", "旗袍"),
            FlexKeyValue("絲襪", "絲襪"),
            FlexKeyValue("吊襪帶", "吊襪帶"),
            FlexKeyValue("熱褲", "熱褲"),
            FlexKeyValue("迷你裙", "迷你裙"),
            FlexKeyValue("性感內衣", "性感內衣"),
            FlexKeyValue("丁字褲", "丁字褲"),
            FlexKeyValue("高跟鞋", "高跟鞋"),
            FlexKeyValue("淫紋", "淫紋"),
            FlexKeyValue("純愛", "純愛"),
            FlexKeyValue("戀愛喜劇", "戀愛喜劇"),
            FlexKeyValue("後宮", "後宮"),
            FlexKeyValue("開大車", "開大車"),
            FlexKeyValue("公眾場合", "公眾場合"),
            FlexKeyValue("NTR", "NTR"),
            FlexKeyValue("精神控制", "精神控制"),
            FlexKeyValue("藥物", "藥物"),
            FlexKeyValue("痴漢", "痴漢"),
            FlexKeyValue("阿嘿顏", "阿嘿顏"),
            FlexKeyValue("精神崩潰", "精神崩潰"),
            FlexKeyValue("獵奇", "獵奇"),
            FlexKeyValue("BDSM", "BDSM"),
            FlexKeyValue("綑綁", "綑綁"),
            FlexKeyValue("眼罩", "眼罩"),
            FlexKeyValue("項圈", "項圈"),
            FlexKeyValue("調教", "調教"),
            FlexKeyValue("異物插入", "異物插入"),
            FlexKeyValue("肉便器", "肉便器"),
            FlexKeyValue("胃凸", "胃凸"),
            FlexKeyValue("強制", "強制"),
            FlexKeyValue("輪姦", "輪姦"),
            FlexKeyValue("凌辱", "凌辱"),
            FlexKeyValue("性暴力", "性暴力"),
            FlexKeyValue("逆強制", "逆強制"),
            FlexKeyValue("女王樣", "女王樣"),
            FlexKeyValue("母女丼", "母女丼"),
            FlexKeyValue("姐妹丼", "姐妹丼"),
            FlexKeyValue("出軌", "出軌"),
            FlexKeyValue("攝影", "攝影"),
            FlexKeyValue("睡眠姦", "睡眠姦"),
            FlexKeyValue("機械姦", "機械姦"),
            FlexKeyValue("性轉換", "性轉換"),
            FlexKeyValue("百合", "百合"),
            FlexKeyValue("耽美", "耽美"),
            FlexKeyValue("異世界", "異世界"),
            FlexKeyValue("怪獸", "怪獸"),
            FlexKeyValue("世界末日", "世界末日"),
            FlexKeyValue("手交", "手交"),
            FlexKeyValue("指交", "指交"),
            FlexKeyValue("乳交", "乳交"),
            FlexKeyValue("肛交", "肛交"),
            FlexKeyValue("腳交", "腳交"),
            FlexKeyValue("拳交", "拳交"),
            FlexKeyValue("3P", "3P"),
            FlexKeyValue("群交", "群交"),
            FlexKeyValue("口交", "口交"),
            FlexKeyValue("口爆", "口爆"),
            FlexKeyValue("吞精", "吞精"),
            FlexKeyValue("舔蛋蛋", "舔蛋蛋"),
            FlexKeyValue("舔穴", "舔穴"),
            FlexKeyValue("69", "69"),
            FlexKeyValue("自慰", "自慰"),
            FlexKeyValue("腋交", "腋交"),
            FlexKeyValue("舔腋下", "舔腋下"),
            FlexKeyValue("內射", "內射"),
            FlexKeyValue("顏射", "顏射"),
            FlexKeyValue("雙洞齊下", "雙洞齊下"),
            FlexKeyValue("懷孕", "懷孕"),
            FlexKeyValue("噴奶", "噴奶"),
            FlexKeyValue("放尿", "放尿"),
            FlexKeyValue("排便", "排便"),
            FlexKeyValue("顏面騎乘", "顏面騎乘"),
            FlexKeyValue("車震", "車震"),
            FlexKeyValue("性玩具", "性玩具"),
            FlexKeyValue("毒龍鑽", "毒龍鑽"),
            FlexKeyValue("觸手", "觸手"),
            FlexKeyValue("頸手枷", "頸手枷"),
            FlexKeyValue("著衣", "著衣")
        )

    internal val sort: List<FlexKeyValue>
        get() = listOf(
            FlexKeyValue("排序方式", "排序方式"),
            FlexKeyValue("最新上市", "最新上市"),
            FlexKeyValue("最新上傳", "最新上傳"),
            FlexKeyValue("本日排行", "本日排行"),
            FlexKeyValue("本週排行", "本週排行"),
            FlexKeyValue("本月排行", "本月排行"),
            FlexKeyValue("觀看次數", "觀看次數"),
            FlexKeyValue("他們在看", "他們在看")
        )

    internal val years: List<FlexKeyValue>
        get() {
            val values = mutableListOf<FlexKeyValue>()
            for (i in currentYear downTo 1990) {
                values.add(FlexKeyValue(i.toString(), "$i 年"))
            }
            return values
        }

    internal val months: List<FlexKeyValue>
        get() {
            val values = mutableListOf<FlexKeyValue>()
            for (i in 1..12) {
                values.add(FlexKeyValue(i.toString(), "$i 月"))
            }
            return values
        }
}