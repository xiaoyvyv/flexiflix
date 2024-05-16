
# 禁止混淆泛型，项目会读取信息初始化
-keepattributes Signature
-keepattributes Exceptions

# seeds.txt 列出未混淆的类和成员
-printseeds build/proguard/seeds.txt
# usage.txt 列出从apk中删除的代码
-printusage build/proguard/unused.txt
# mapping.txt 列出混淆前后的映射
-printmapping build/proguard/mapping.txt