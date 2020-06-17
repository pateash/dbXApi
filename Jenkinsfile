node('maven') {
  stage('Build') {
    checkout scm
    sh "mvn clean fabric8:deploy"
    // sh "oc start-build dbxApi --from-file=target/dbxApi.jar --follow"
  }
  stage('Deploy') {
    openshiftDeploy depCfg: 'dbx'
    openshiftVerifyDeployment depCfg: 'dbx', replicaCount: 1, verifyReplicaCount: true
  }
}