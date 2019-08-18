#!/bin/bash

## 1) Create OpenShift Environments
oc new-project ${GITHUB_USERNAME}-dev
oc new-project ${GITHUB_USERNAME}-test
oc new-project ${GITHUB_USERNAME}-prod
oc project ${GITHUB_USERNAME}-dev

## 2) Create Jenkins Server in Dev Project
oc new-app jenkins-ephemeral -p MEMORY_LIMIT=2Gi -p DISABLE_ADMINISTRATIVE_MONITORS=true -n ${GITHUB_USERNAME}-dev

## 3) Grant Jenkins SA Proper Permissions
oc policy add-role-to-user edit system:serviceaccount:${GITHUB_USERNAME}-dev:jenkins -n ${GITHUB_USERNAME}-test
oc policy add-role-to-user edit system:serviceaccount:${GITHUB_USERNAME}-dev:jenkins -n ${GITHUB_USERNAME}-prod
oc policy add-role-to-user registry-editor system:serviceaccount:${GITHUB_USERNAME}-dev:jenkins -n ${GITHUB_USERNAME}-test
oc policy add-role-to-user registry-editor system:serviceaccount:${GITHUB_USERNAME}-dev:jenkins -n ${GITHUB_USERNAME}-prod

## 4) Create git-auth secret
oc create secret generic git-auth --from-literal=username=${GITHUB_USERNAME} --from-literal=password=${GITHUB_PASSWORD} --type=kubernetes.io/basic-auth -n ${GITHUB_USERNAME}-dev

## 5) Create birthday-paradox-pipeline BuildConfig
oc process -f ../pipeline.yaml -p APPLICATION_NAME=birthday-paradox -p REPOSITORY_URI=https://github.com/${GITHUB_USERNAME}/OpenShift-Jenkins-Lab -p SOURCE_REF=master -p SOURCE_SECRET=git-auth | oc apply -f - -n ${GITHUB_USERNAME}-dev

## 6) Create jenkins-agent-skopeo ImageStream
oc create -f ../openshift/agents/jenkins-agent-skopeo.yaml -n ${GITHUB_USERNAME}-dev
