#!groovy
pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    stages {
        stage('Initialize') {
            steps {
                script {
                    if (env.BRANCH_NAME == "master") {
                        sh "mvn -B versions:set -DnewVersion=0.1.${BUILD_NUMBER} -DgenerateBackupPoms=false"
                    } else {
                        sh "mvn -B versions:set -DnewVersion=${BRANCH_NAME}-${BUILD_NUMBER} -DgenerateBackupPoms=false"
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh "mvn -B -U" +
                    " -Dorg.xml.sax.driver=\"com.sun.org.apache.xerces.internal.parsers.SAXParser\"" +
                    " clean" +
                    " org.jacoco:jacoco-maven-plugin:prepare-agent" +
                    " verify" +
                    " site"
            }
            post {
                always {
                    junit '**/target/surefire-reports/**/*.xml'
                }
            }
        }
        stage('Sonar') {
            steps {
                script {
                    if (env.BRANCH_NAME == "master") {
                        sh "mvn -B sonar:sonar"
                    } else {
                        def PULL_REQUEST = sh(script: 'python /opt/findpr.py', returnStdout: true).trim()
                        sh "mvn -B sonar:sonar" +
                            " -Dsonar.analysis.mode=preview" +
                            " -Dsonar.github.repository=minijax/minijax" +
                            " -Dsonar.github.login=ajibotjenkins" +
                            " -Dsonar.github.oauth=ba7b372bdcb5a2d774c920c789e6a8b188b59a30" +
                            " -Dsonar.github.pullRequest=${PULL_REQUEST}"
                    }
                }
            }
        }
        stage('Deploy') {
            when { expression { env.BRANCH_NAME == "master" } }
            steps {
                sh "mvn -B -DskipTests=true deploy"
            }
        }
    }
}
