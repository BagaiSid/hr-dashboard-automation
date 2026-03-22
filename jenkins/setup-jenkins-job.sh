#!/bin/bash
# =============================================================================
# Jenkins Pipeline Job Setup Script
# Creates the HR Dashboard Automation pipeline job via Jenkins REST API
#
# Jenkins URL:  http://localhost:8080
# Username:     Admin
# Password:     Password
# =============================================================================

set -euo pipefail

JENKINS_URL="http://localhost:8080"
JENKINS_USER="Admin"
JENKINS_PASS="Password"
JOB_NAME="hr-dashboard-automation"

echo "============================================"
echo " HR Dashboard - Jenkins Pipeline Setup"
echo "============================================"

# ---- Step 1: Check Jenkins is reachable ----
echo ""
echo "[1/5] Checking Jenkins is reachable at ${JENKINS_URL}..."
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${JENKINS_USER}:${JENKINS_PASS}" \
    "${JENKINS_URL}/api/json")

if [ "$HTTP_CODE" -ne 200 ]; then
    echo "ERROR: Jenkins not reachable (HTTP ${HTTP_CODE})"
    echo "  - Verify Jenkins is running at ${JENKINS_URL}"
    echo "  - Verify credentials (User: ${JENKINS_USER})"
    exit 1
fi
echo "  Jenkins is running."

# ---- Step 2: Get CSRF crumb ----
echo ""
echo "[2/5] Fetching CSRF crumb..."
CRUMB_JSON=$(curl -s -u "${JENKINS_USER}:${JENKINS_PASS}" \
    "${JENKINS_URL}/crumbIssuer/api/json")

CRUMB_FIELD=$(echo "$CRUMB_JSON" | python3 -c "import sys,json; print(json.load(sys.stdin)['crumbRequestField'])")
CRUMB_VALUE=$(echo "$CRUMB_JSON" | python3 -c "import sys,json; print(json.load(sys.stdin)['crumb'])")
echo "  CSRF crumb obtained."

# ---- Step 3: Check if job already exists ----
echo ""
echo "[3/5] Checking if job '${JOB_NAME}' already exists..."
JOB_CHECK=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${JENKINS_USER}:${JENKINS_PASS}" \
    "${JENKINS_URL}/job/${JOB_NAME}/api/json")

if [ "$JOB_CHECK" -eq 200 ]; then
    echo "  Job already exists. Updating configuration..."
    ACTION="update"
else
    echo "  Job does not exist. Creating new job..."
    ACTION="create"
fi

# ---- Step 4: Create/Update the job ----
echo ""
echo "[4/5] $(echo "${ACTION}" | awk '{print toupper(substr($0,1,1)) substr($0,2)}')ing pipeline job '${JOB_NAME}'..."

JOB_CONFIG_XML=$(cat <<'XMLEOF'
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job">
  <description>Selenium automation tests for HR Dashboard (TestNG + Maven)</description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
      <triggers/>
    </org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty>
    <hudson.model.ParametersDefinitionProperty>
      <parameterDefinitions>
        <hudson.model.ChoiceParameterDefinition>
          <name>BROWSER</name>
          <description>Browser to run tests on</description>
          <choices class="java.util.Arrays$ArrayList">
            <a class="string-array">
              <string>chrome</string>
              <string>firefox</string>
            </a>
          </choices>
        </hudson.model.ChoiceParameterDefinition>
        <hudson.model.StringParameterDefinition>
          <name>BASE_URL</name>
          <description>HR Dashboard application URL</description>
          <defaultValue>http://localhost:8080/hr-dashboard</defaultValue>
          <trim>true</trim>
        </hudson.model.StringParameterDefinition>
        <hudson.model.ChoiceParameterDefinition>
          <name>SUITE</name>
          <description>TestNG suite XML file</description>
          <choices class="java.util.Arrays$ArrayList">
            <a class="string-array">
              <string>testng.xml</string>
            </a>
          </choices>
        </hudson.model.ChoiceParameterDefinition>
      </parameterDefinitions>
    </hudson.model.ParametersDefinitionProperty>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps">
    <script>
pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK17'
    }

    environment {
        HEADLESS = 'true'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Compiling project...'
                sh 'mvn clean compile test-compile -q'
            }
        }

        stage('Test') {
            steps {
                echo "Running tests: browser=${params.BROWSER}, url=${params.BASE_URL}"
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
                    junit allowEmptyResults: true,
                         testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/screenshots/**/*.png', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
        }
        success { echo 'All tests passed!' }
        failure { echo 'Some tests failed. Check reports and screenshots.' }
        cleanup { cleanWs() }
    }
}
    </script>
    <sandbox>true</sandbox>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>
XMLEOF
)

if [ "$ACTION" = "create" ]; then
    HTTP_RESULT=$(curl -s -o /dev/null -w "%{http_code}" \
        -u "${JENKINS_USER}:${JENKINS_PASS}" \
        -H "${CRUMB_FIELD}:${CRUMB_VALUE}" \
        -H "Content-Type: application/xml" \
        -d "${JOB_CONFIG_XML}" \
        "${JENKINS_URL}/createItem?name=${JOB_NAME}")
else
    HTTP_RESULT=$(curl -s -o /dev/null -w "%{http_code}" \
        -u "${JENKINS_USER}:${JENKINS_PASS}" \
        -H "${CRUMB_FIELD}:${CRUMB_VALUE}" \
        -H "Content-Type: application/xml" \
        -d "${JOB_CONFIG_XML}" \
        "${JENKINS_URL}/job/${JOB_NAME}/config.xml")
fi

if [ "$HTTP_RESULT" -eq 200 ] || [ "$HTTP_RESULT" -eq 201 ]; then
    echo "  Job '${JOB_NAME}' ${ACTION}d successfully!"
else
    echo "  ERROR: Failed to ${ACTION} job (HTTP ${HTTP_RESULT})"
    exit 1
fi

# ---- Step 5: Trigger first build ----
echo ""
echo "[5/5] Triggering first build..."
BUILD_RESULT=$(curl -s -o /dev/null -w "%{http_code}" \
    -u "${JENKINS_USER}:${JENKINS_PASS}" \
    -H "${CRUMB_FIELD}:${CRUMB_VALUE}" \
    -X POST \
    "${JENKINS_URL}/job/${JOB_NAME}/buildWithParameters?BROWSER=chrome&BASE_URL=http://localhost:8080/hr-dashboard&SUITE=testng.xml")

if [ "$BUILD_RESULT" -eq 201 ]; then
    echo "  Build triggered successfully!"
else
    echo "  WARNING: Build trigger returned HTTP ${BUILD_RESULT} (may need parameterized first run)"
fi

echo ""
echo "============================================"
echo " Setup Complete!"
echo "============================================"
echo ""
echo " Job URL:    ${JENKINS_URL}/job/${JOB_NAME}/"
echo " Console:    ${JENKINS_URL}/job/${JOB_NAME}/lastBuild/console"
echo " Parameters: ${JENKINS_URL}/job/${JOB_NAME}/build"
echo ""
echo " Required Jenkins Plugins:"
echo "   - Pipeline (workflow-aggregator)"
echo "   - Git"
echo "   - JUnit (included by default)"
echo "   - Timestamper"
echo "   - Workspace Cleanup"
echo ""
