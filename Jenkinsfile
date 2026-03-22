pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        BROWSER     = 'chrome'
        HEADLESS    = 'true'
        BASE_URL    = 'http://localhost:4000/hr-dashboard'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser to run tests')
        choice(name: 'SUITE', choices: ['testng.xml'], description: 'TestNG suite file')
        string(name: 'BASE_URL', defaultValue: 'http://localhost:4000', description: 'Application URL')
    }

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Validate') {
            steps {
                echo 'Validating project setup...'
                sh 'mvn validate -q'
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling project...'
                sh 'mvn clean compile test-compile -q'
            }
        }

        stage('Test') {
            steps {
                echo "Running tests with browser=${params.BROWSER}, headless=true"
                sh """
                    mvn test \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=true \
                        -Dbase.url=${params.BASE_URL} \
                        -DsuiteXmlFile=${params.SUITE}
                """
            }
            post {
                always {
                    // Publish TestNG results
                    testNG(reportFilenamePattern: '**/testng-results.xml',
                           failOnError: false)

                    // Archive surefire reports
                    junit allowEmptyResults: true,
                         testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            echo 'Archiving test artifacts...'

            // Archive screenshots on failure
            archiveArtifacts artifacts: 'target/screenshots/**/*.png',
                             allowEmptyArchive: true

            // Archive TestNG reports
            archiveArtifacts artifacts: 'target/surefire-reports/**',
                             allowEmptyArchive: true
        }

        success {
            echo 'All tests passed!'
        }

        failure {
            echo 'Some tests failed. Check reports and screenshots.'
        }

        cleanup {
            cleanWs()
        }
    }
}
