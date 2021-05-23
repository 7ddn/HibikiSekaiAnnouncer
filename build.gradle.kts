plugins {
    val kotlinVersion = "1.4.31"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.6.4"
}

group = "org.example"
version = "0.1.0"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.alibaba:fastjson:1.2.73")
}
