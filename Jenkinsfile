node('maven') {
  stage('Build') {
    checkout scm
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