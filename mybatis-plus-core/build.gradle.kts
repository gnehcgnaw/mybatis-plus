dependencies {
    val lib: Map<String, Any> by rootProject.extra

    api(project(":mybatis-plus-annotation"))
    api("${lib["mybatis"]}")
    api("${lib["jsqlparser"]}")

    implementation("${lib["cglib"]}")
    implementation("${lib["spring-aop"]}")

    testCompile("${lib["mybatis-ehcache"]}")
    testCompile("${lib["logback-classic"]}")
    testCompile("${lib["commons-dbcp2"]}") {
        exclude(module = "commons-logging")
    }
    testCompile("${lib["aspectjweaver"]}")
    testCompile("${lib["hikaricp"]}")
    testCompile("${lib["druid"]}")
    testCompile("${lib["fastjson"]}")
    testCompile("${lib["tomcatjdbc"]}")
}
