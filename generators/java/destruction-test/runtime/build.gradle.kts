description = "oas-gen - java destruction-test runtime support classes"

plugins {
    id("java-library-publishing-conventions")
}

dependencies {
    api(project(":generators:java:dto:runtime")) {
        exclude("com.fasterxml.jackson.core", "jackson-core")
    }
    api("com.fasterxml.jackson.core","jackson-core", JACKSON_VERSION)
    api("org.junit.jupiter:junit-jupiter:5.7.1") {
        exclude("com.fasterxml.jackson.core", "jackson-core")
    }
    api("uk.co.jemos.podam:podam:7.2.7.RELEASE") {
        exclude("com.fasterxml.jackson.core", "jackson-core")
    }
    compileOnly(group = "io.rest-assured", name = "rest-assured", version = "4.4.0") {
        exclude("com.fasterxml.jackson.core", "jackson-core")
    }
    compileOnlyApi("org.apache.tomcat.embed:tomcat-embed-core:9.0.44")
}
