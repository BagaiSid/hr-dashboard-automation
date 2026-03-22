// Jenkins Pipeline Job Configuration Script
// Run this in Jenkins Script Console (Manage Jenkins > Script Console)
// URL: http://localhost:8080/script
//
// Prerequisites:
//   - Pipeline plugin installed
//   - Git plugin installed
//   - TestNG Results plugin installed (optional, for test reports)

import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition

def jobName = 'hr-dashboard-automation'
def jenkins = Jenkins.instance

// Remove existing job if present
def existingJob = jenkins.getItem(jobName)
if (existingJob != null) {
    existingJob.delete()
    println "Deleted existing job: ${jobName}"
}

// Create new Pipeline job
def job = jenkins.createProject(WorkflowJob, jobName)

// Set the pipeline script (inline definition)
def pipelineScript = '''
pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        BROWSER     = 'chrome'
        HEADLESS    = 'true'
        BASE_URL    = 'http://localhost:8080/hr-dashboard'
    }

    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser to run tests')
        choice(name: 'SUITE', choices: ['testng.xml'], description: 'TestNG suite file')
        string(name: 'BASE_URL', defaultValue: 'http://localhost:8080/hr-dashboard', description: 'Application URL')
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
                    mvn test \\
                        -Dbrowser=${params.BROWSER} \\
                        -Dheadless=true \\
                        -Dbase.url=${params.BASE_URL} \\
                        -DsuiteXmlFile=${params.SUITE}
                """
            }
            post {
                always {
                    junit allowEmptyResults: true,
                         testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/screenshots/**/*.png',
                             allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/surefire-reports/**',
                             allowEmptyArchive: true
        }
        success { echo 'All tests passed!' }
        failure { echo 'Some tests failed. Check reports and screenshots.' }
        cleanup { cleanWs() }
    }
}
'''

job.setDefinition(new CpsFlowDefinition(pipelineScript, true))
job.save()

println "Pipeline job '${jobName}' created successfully!"
println "Access it at: http://localhost:8080/job/${jobName}/"

jenkins.reload()
