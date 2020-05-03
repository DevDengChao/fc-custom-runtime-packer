[![Actions Status](https://github.com/XieEDeHeiShou/fc-custom-runtime-packer/workflows/Build/badge.svg)](https://github.com/XieEDeHeiShou/fc-custom-runtime-packer/actions)
[![Gradle Status](https://gradleupdate.appspot.com/XieEDeHeiShou/fc-custom-runtime-packer/status.svg)](https://gradleupdate.appspot.com/XieEDeHeiShou/fc-custom-runtime-packer/status)
![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/xieedeheishou/fc-custom-runtime-packer)

# Fc custom runtime packer

This is a gradle plugin used to automatically compress your bootJar with a bootstrap file into a zip.

After applying this plugin, it will try to search _bootstrap_ files under `${PROJECT_DIR}` and `${PROJECT_DIR}/bootstrap` dir.


# How to install

```groovy
buildscript {
  repositories {
    maven { url "https://plugins.gradle.org/m2/" }
  }
  dependencies {
    classpath "dev.dengchao:fc-custom-runtime-packer:x.y.z" // replace x.y.z with actual version of this plugin
  }
}

version 'your-project-version'

// This plugin MUST be applied below version field.
apply plugin: "dev.dengchao.fc-custom-runtime-packer"
```


# How to use

## Project structure

Let's assume you have a spring project like below:
```
project dir
+--- bootstrap
|       +--- bootstrap-pro  (1)
|       \--- dev                        (2)
+--- build
+--- src
+--- bootstrap.sh             (3)
+--- build.gradle              (plugin applyed)
```

After run `gradle :zipBootstrap`, there will be a regular bootJar, three profile-ed bootstrap zip:
```
project dir
+...
+--- build
|       \--- libs
|               +--- demo-1.0.0.jar                     (bootJar)
|               +--- demo-1.0.0-default.zip
|               |       +--- bootstrap                       (generated from 3) 
|               |       \--- demo-1.0.0.jar
|               +--- demo-1.0.0-dev.zip
|               |       +--- bootstrap                       (generated from 2) 
|               |       \--- demo-1.0.0.jar
|               \--- demo-1.0.0-pro.zip
|                       +--- bootstrap                       (generated from 1) 
|                       \--- demo-1.0.0.jar
+...
``` 
More additionally, you can run `gradle :zipBootstrapDefault` to package default profile only, so does other profiles.

## Bootstrap file spec

+ **It's content MAY have one or more place holder `archive` or `boot.jar`, which will be replaced by actual bootJar name 
       in generated bootstrap file**. eg: `java -jar archive` will result `java -jar demo-1.0.0.jar`
    + See [ReplacePlaceHolderContentInterceptor][^ReplacePlaceHolderContentInterceptor] for more details.
+ If it is placed under project dir like (3), its name MUST match regex expression `bootstrap(-[a-zA-Z0-9\-]+)?(\.sh)?`
    + If a profile `(-[a-zA-Z0-9\-]+)` is present, it will result a `${PROJECT_NAME}-${PROJECT_VERSION}-${PROFILE}.zip` zip.  
    + If the profile is not present,  it will result a `${PROJECT_NAME}-${PROJECT_VERSION}-default.zip` zip.
    + See [ProjectDirBootstrapCollector][^ProjectDirBootstrapCollector] for more details.
+ If it is placed under bootstrap dir like (1) and (2), its name MUST match regex expression `([a-zA-Z0-9\-]+)(\.sh)?`    
    + If a 'bootstrap-' prefix is present, it will be removed from profile.
    + See [BootstrapDirBootstrapCollector][^BootstrapDirBootstrapCollector] for more details.
+ It's content MAY have announced its [shebang command][^Shebang @wikipedia.org]. Accepted values are `#!/bin/bash` and 
    `#!/usr/bin/env sh`.
    + If it is missing or having an invalid shebang command, `#!/bin/bash` will be prepend into generated bootstrap.    
    + See [ShebangInterceptor][^ShebangInterceptor] for more details.
+ It MAY have granted the execution permission.
    + As the bootstrap inside the zip file is a generated file, not the original one, so the execution permission 
    the original file have is ignored, and the generated one's [file mode][^Unix file mode] is setting to 775. 
    + See [ZipBootstrap][^ZipBootstrap] for more details.
+ If there are more than one bootstrap files having the same profile, 
    a [DuplicateBootstrapProfileException][^DuplicateBootstrapProfileException] will be thrown when running.
    + See [ProjectBootstrapCollector][^ProjectBootstrapCollector] for more details.


# Contributing

Please PR to master branch.

If you want to perform integrate test on `demo` module with your modifications, 
just run `gradle :fc-custom-runtime-packer:publishToMavenLocal`.

# LICENSE

[Apache 2.0](LICENSE)


# Reference

[Spring boot gradle plugin @github.com](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-tools/spring-boot-gradle-plugin)

[How to get project version in custom gradle plugin @stackoverflow.com](https://stackoverflow.com/questions/13198358/how-to-get-project-version-in-custom-gradle-plugin)

[Shebang @wikipedia.org](https://en.wikipedia.org/wiki/Shebang_(Unix))

[Unix file mode](https://www.tutorialspoint.com/unix/unix-file-permission.htm)



[^ReplacePlaceHolderContentInterceptor]:fc-custom-runtime-packer/src/main/java/dev/dengchao/content/interceptor/ReplacePlaceHolderContentInterceptor.java
[^ProjectDirBootstrapCollector]:fc-custom-runtime-packer/src/main/java/dev/dengchao/bootstrap/collector/ProjectDirBootstrapCollector.java
[^BootstrapDirBootstrapCollector]:fc-custom-runtime-packer/src/main/java/dev/dengchao/bootstrap/collector/BootstrapDirBootstrapCollector.java
[^ShebangInterceptor]:fc-custom-runtime-packer/src/main/java/dev/dengchao/content/interceptor/ShebangInterceptor.java
[^ZipBootstrap]:fc-custom-runtime-packer/src/main/java/dev/dengchao/ZipBootstrap.java
[^Unix file mode]:https://www.tutorialspoint.com/unix/unix-file-permission.htm
[^DuplicateBootstrapProfileException]:fc-custom-runtime-packer/src/main/java/dev/dengchao/bootstrap/collector/DuplicateBootstrapProfileException.java
[^ProjectBootstrapCollector]:fc-custom-runtime-packer/src/main/java/dev/dengchao/bootstrap/collector/ProjectBootstrapCollector.java
[^Shebang @wikipedia.org]:https://en.wikipedia.org/wiki/Shebang_(Unix)
