dependencies {
    val lib: Map<String, Any> by rootProject.extra

    api(project(":mybatis-plus-extension"))
    implementation(project(":mybatis-plus-generator"))

    testCompile("${lib["spring-web"]}")
    testCompile("${lib["javax.servlet-api"]}")

    testCompile("${lib["spring-test"]}")
    testCompile("${lib["fastjson"]}")

    testCompile("${lib["hikaricp"]}")
    testCompile("${lib["commons-dbcp2"]}")
    testCompile("${lib["druid"]}")
    testCompile("${lib["tomcatjdbc"]}")

    testCompile("${lib["h2"]}")
    testCompile("${lib["sqlserver"]}")
    testCompile("${lib["postgresql"]}")
    testCompile(lib["oracle"] as ConfigurableFileTree)
    testCompile("${lib["mysql"]}")
    testCompile("${lib["jackson"]}")
    testCompile("${lib["logback-classic"]}")

    testCompile("${lib["spring-context-support"]}")
    testCompile("${lib["spring-jdbc"]}")
}
