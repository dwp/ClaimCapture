node ('master') {
    stage ('Checkout') {
        checkout scm
    }
    stage ('Start memcached') {
        sh '/opt/memcached-1.4.33/bin/memcached -u jenkins -p 11211 &'
        sh '/opt/memcached-1.4.33/bin/memcached -u jenkins -p 11212 &'
    }
    stage ('Build') {
        def sbttool = tool name: 'sbt137', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'
        def jvmargs = '-Xms512M -Xmx3072M -XX:MaxHeapSize=3072M'
        def sbtflags = '-Dsbt.log.noformat=true -Dtestserver.port=31121 -Dinclude=unit -Dcarers.keystore=/opt/carers-keystore/carerskeystore'
        def buildargs = 'clean update reload compile test'
        sh "cd c3; java ${jvmargs} ${sbtflags} -jar ${sbttool}/bin/sbt-launch.jar ${buildargs}"
    }
    stage ('Test') {
        def sbttool = tool name: 'sbt137', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'
        def jvmargs = '-Xms512M -Xmx3072M -XX:MaxHeapSize=3072M'
        def sbtflags = '-Dsbt.log.noformat=true -Dtestserver.port=31121 -DwaitSeconds=1000 -Dinclude=integration,functional,preview -Dcarers.keystore=/opt/carers-keystore/carerskeystore'
        def buildargs = 'test dist publish'
        sh "cd c3; java ${jvmargs} ${sbtflags} -jar ${sbttool}/bin/sbt-launch.jar ${buildargs}"
    }
    stage ('Stop memcached') {
        sh 'fuser -k 11211/tcp'
        sh 'fuser -k 11212/tcp'
    }
    stage ('Deploy to lab') {
        sshagent(['8b4a081b-f1d6-424d-959f-ae9279d08b3b']) {
            sh 'scp c3/target/universal/*.zip' c3lab@37.26.89.68:c3-latest-SNAPSHOT.zip'
            sh 'ssh c3lab@37.26.89.68 "rm -rf ~/c3-latest/* && unzip -q -d c3-latest c3-latest-SNAPSHOT.zip"'
            sh 'ssh c3lab@37.26.89.68 "./deploy.sh restart > output.log 2>&1 &"'
        }

    }
}

