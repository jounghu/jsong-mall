## mall-gen mybatis代码生成工具

可以自动生成mapper，pojo，dao

## quick start

1. 修改mall-gen\src\main\resources\db-mysql.properties 数据库连接地址，生成位置
2. 修改mall-gen\src\main\resources\generatorConfig.xml 生成的表名


**注意**

build的时候需要先注释 `task mybatisGenerate`

``` build.gradle
task mybatisGenerate {}
```

然后build完毕之后，打开这个task执行，就会生成对应的mapper