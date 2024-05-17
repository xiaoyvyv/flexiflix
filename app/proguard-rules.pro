
# 禁止混淆泛型，项目会读取信息初始化
-keepattributes Signature
-keepattributes Exceptions

# seeds.txt 列出未混淆的类和成员
-printseeds build/proguard/seeds.txt
# usage.txt 列出从apk中删除的代码
-printusage build/proguard/unused.txt
# mapping.txt 列出混淆前后的映射
-printmapping build/proguard/mapping.txt

# MediaVideoGestureView 反射了 DefaultTimeBar 几个方法，需要禁止混淆
-keep class androidx.media3.ui.DefaultTimeBar {
    private void startScrubbing(long);
    private void updateScrubbing(long);
    private void stopScrubbing(boolean);
    private void update();
}

# 快手弹幕库使用的 gdx
-dontwarn com.badlogic.gdx.backends.android.AndroidFragmentApplication
-dontwarn com.badlogic.gdx.utils.GdxBuild
-dontwarn com.badlogic.gdx.jnigen.BuildTarget*
-dontwarn com.badlogic.gdx.graphics.g2d.freetype.FreetypeBuild

# Required if using Gdx-Controllers extension
-keep class com.badlogic.gdx.controllers.android.AndroidControllers