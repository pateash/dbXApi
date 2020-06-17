node('maven') {
  stage('Build') {
    git url: "https://github.com/ashishpatel0720/dbXApi.git"
    sh "mvn package"
    stash name:"jar", includes:"target/dbxApi.jar"
  }
  stage('Build Image') {
    unstash name:"jar"
    sh "oc start-build dbxApi --from-file=target/dbxApi.jar --follow"
  }
  stage('Deploy') {
    openshiftDeploy depCfg: 'dbx'
    openshiftVerifyDeployment depCfg: 'dbx', replicaCount: 1, verifyReplicaCount: true
  }
}