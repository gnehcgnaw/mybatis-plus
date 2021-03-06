apply(plugin = "org.jetbrains.kotlin.jvm")

dependencies {
    val lib: Map<String, Any> by rootProject.extra

    api(project(":mybatis-plus-core"))
    api("${lib["mybatis-spring"]}")
    api("${lib["mybatis"]}")

    implementation("${lib["kotlin-stdlib-jdk8"]}")
    implementation("${lib["kotlin-reflect"]}")
    implementation("${lib["spring-context-support"]}")
    implementation("${lib["spring-jdbc"]}")
    implementation("${lib["slf4j-api"]}")

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
    testCompile("${lib["logback-classic"]}")
}
