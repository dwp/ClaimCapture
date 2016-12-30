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
    stage ('Deploy') {
        sshagent(['8b4a081b-f1d6-424d-959f-ae9279d08b3b']) {
            sh 'ssh -o StrictHostKeyChecking=no ubuntu@37.26.89.68 \'sudo salt "*5*" cmd.run "service carers c3lab deploy"\''
            sh 'ssh ubuntu@37.26.89.68 \'sudo salt "*5*" cmd.run "service carers c3lab start"\''
        }

    }
}

