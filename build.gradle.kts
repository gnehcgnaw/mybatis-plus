buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.10")
        classpath("com.netflix.nebula:gradle-extra-configurations-plugin:3.0.3")
    }
}

plugins {
    `java-library`
    signing
    `maven-publish`
}

// versions
val javaVersion = JavaVersion.VERSION_1_8
val mybatisSpringVersion = "2.0.0"
val mybatisVersion = "3.5.0"
val springVersion = "5.1.4.RELEASE"
val jsqlparserVersion = "1.3"
val junitVersion = "5.4.0-RC1" // TODO 5.4.0 预计将与 2019-02-06 发布，届时应替换成正式版本
val lombokVersion = "1.18.4"
val cglibVersion = "3.2.6"

// libs
val lib = mapOf(
    "kotlin-reflect"             to "org.jetbrains.kotlin:kotlin-reflect:1.3.10",
    "kotlin-stdlib-jdk8"         to "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.10",
    "jsqlparser"                 to "com.github.jsqlparser:jsqlparser:$jsqlparserVersion",
    "mybatis-spring"             to "org.mybatis:mybatis-spring:$mybatisSpringVersion",
    "mybatis"                    to "org.mybatis:mybatis:$mybatisVersion",
    "spring-context-support"     to "org.springframework:spring-context-support:$springVersion",
    "spring-jdbc"                to "org.springframework:spring-jdbc:$springVersion",
    "spring-tx"                  to "org.springframework:spring-tx:$springVersion",
    "spring-web"                 to "org.springframework:spring-web:$springVersion",
    "spring-aop"                 to "org.springframework:spring-aop:$springVersion",
    "cglib"                      to "cglib:cglib:$cglibVersion",
    "lombok"                     to "org.projectlombok:lombok:$lombokVersion",

    "javax.servlet-api"          to "javax.servlet:javax.servlet-api:4.0.1",
    "aspectjweaver"              to "org.aspectj:aspectjweaver:1.8.9",
    "mockito"                    to "org.mockito:mockito-core:2.13.0",
    "mybatis-ehcache"            to "org.mybatis.caches:mybatis-ehcache:1.1.0",
    "slf4j-api"                  to "org.slf4j:slf4j-api:1.7.25",
    "logback-classic"            to "ch.qos.logback:logback-classic:1.2.3",
    // test
    "spring-test"                to "org.springframework:spring-test:$springVersion",
    "junit-jupiter-api"          to "org.junit.jupiter:junit-jupiter-api:$junitVersion",
    "junit-jupiter-engine"       to "org.junit.jupiter:junit-jupiter-engine:$junitVersion",
    "mockito-all"                to "org.mockito:mockito-all:1.10.19",
    "fastjson"                   to "com.alibaba:fastjson:1.2.49",
    "jackson"                    to "com.fasterxml.jackson.core:jackson-databind:2.9.6",
    "tomcatjdbc"                 to "org.apache.tomcat:tomcat-jdbc:9.0.2",
    // datasource
    "hikaricp"                   to "com.zaxxer:HikariCP:2.7.0",
    "druid"                      to "com.alibaba:druid:1.0.29",
    "commons-dbcp2"              to "org.apache.commons:commons-dbcp2:2.1.1",
    "sqlserver"                  to "com.microsoft.sqlserver:sqljdbc4:4.0",
    "postgresql"                 to "org.postgresql:postgresql:9.4.1212",
    "oracle"                     to fileTree("libs/ojdbc-11.2.0.3-jdk16.jar"),
    "h2"                         to "com.h2database:h2:1.4.194",
    "mysql"                      to "mysql:mysql-connector-java:5.1.38",
    // code generator
    "velocity"                   to "org.apache.velocity:velocity-engine-core:2.0",
    "freemarker"                 to "org.freemarker:freemarker:2.3.9",
    "beetl"                      to "com.ibeetl:beetl:2.9.6"
)
// ext
extra["lib"] = lib

allprojects {
    group = "com.baomidou"
    version = "3.0.8.3-SNAPSHOT"
}

description = "Mybatis 增强工具包 - 只做增强不做改变，简化CRUD操作"

subprojects {
    // 插件
    apply(plugin = "org.gradle.java-library")
    apply(plugin = "org.gradle.maven-publish")
    apply(plugin = "org.gradle.signing")

    // Java 版本
    configure<JavaPluginConvention> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    // 编译器配置
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.isDeprecation = true
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Jar> {
        afterEvaluate {
            manifest {
                attributes["Implementation-Version"] = version
            }
        }
    }

    repositories {
        mavenLocal()
        maven("http://maven.aliyun.com/nexus/contencommons-dbcpt/groups/public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("http://www.cameliatk.jp/maven2/repository/thirdparty")
        jcenter()
    }

    dependencies {
        annotationProcessor("${lib["lombok"]}")
        compileOnly("${lib["lombok"]}")

        testAnnotationProcessor("${lib["lombok"]}")
        testCompileOnly("${lib["mockito-all"]}")
        testCompile("${lib["junit-jupiter-api"]}")
        testRuntime("${lib["junit-jupiter-engine"]}")
        testCompile("org.mockito:mockito-junit-jupiter:2.23.4")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
        archiveClassifier.set("source")
        from(sourceSets["main"].allJava)
    }

    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
        isFailOnError = false
        (options as? StandardJavadocDocletOptions)?.also {
            it.charSet = "UTF-8"
            it.isAuthor = true
            it.isVersion = true
            it.links = listOf("https://docs.oracle.com/javase/8/docs/api")
            if (JavaVersion.current().isJava9Compatible) {
                it.addBooleanOption("html5", true)
            }
        }
    }

    val javadocJar by tasks.registering(Jar::class) {
        dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
        archiveClassifier.set("javadoc")
        from(tasks["javadoc"])
    }

    tasks.whenTaskAdded {
        if (this.name.contains("signMavenJavaPublication")) {
            this.enabled = File(project.property("signing.secretKeyRingFile") as String).isFile
        }
    }

    publishing {
        repositories {
            maven {
                val userName = System.getProperty("un")
                val passWord = System.getProperty("ps")
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"
                setUrl(if (version.toString().endsWith("RELEASE")) releasesRepoUrl else snapshotsRepoUrl)

                credentials {
                    username = userName
                    password = passWord
                }
            }
        }
        publications {
            register("mavenJava", MavenPublication::class) {
                pom {
                    name.set("mybatis-plus")
                    description.set("An enhanced toolkit of Mybatis to simplify development.")
                    inceptionYear.set("2016")
                    url.set("https://github.com/baomidou/mybatis-plus")

                    artifactId = project.name
                    groupId = "${project.group}"
                    version = "${project.version}"
                    packaging = "jar"

                    organization {
                        name.set("baomidou")
                    }

                    scm {
                        connection.set("scm:git@github.com:Codearte/gradle-nexus-staging-plugin.git")
                        developerConnection.set("scm:git@github.com:Codearte/gradle-nexus-staging-plugin.git")
                        url.set("https://github.com/baomidou/mybatis-plus")
                    }

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("baomidou")
                            name.set("hubin")
                            email.set("jobob@qq.com")
                        }
                    }
                }

                from(components["java"])
                artifact(sourcesJar.get())
                artifact(javadocJar.get())
            }
        }
    }

    signing {
        sign(publishing.publications.getByName("mavenJava"))
    }
}
