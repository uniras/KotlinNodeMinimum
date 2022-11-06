plugins {
    id("org.jetbrains.kotlin.js") version "1.7.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")

    //使用するNodeモジュールがある場合はこのようにして追加
    //implementation(npm("express", "4.18.2"))
}

kotlin {
    js {
        tasks {
            //ビルドオプションの設定
            compilations.all {
                kotlinOptions.moduleKind = "commonjs"  //モジュールシステムの設定
                kotlinOptions.sourceMap = false        //ソースマップの生成
                kotlinOptions.metaInfo = false         //metaファイルの生成
            }

            //出力ファイルのコピー
            val jsMove by creating(Copy::class) {
                dependsOn(build)
                from("build/js/packages/${project.name}/kotlin/${project.name}.js")
                //出力先フォルダの設定
                into("output")
                //出力先ファイル名の設定
                rename { it.replace("${project.name}.js", "${project.name}.js") }
                mustRunAfter(build)  //これを入れておかないと順番が保証されないらしい。
            }

            //トランスパイルしてコピーするタスクをまとめて実行
            register("jsBuild") {
                dependsOn(build, jsMove)
            }
        }

        nodejs()
    }
}