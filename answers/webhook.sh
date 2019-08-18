## 1) Create GitHub Trigger on birthday-paradox-pipeline BuildConfig
oc process -f pipeline-with-trigger.yaml -p APPLICATION_NAME=birthday-paradox -p REPOSITORY_URI=https://github.com/${GITHUB_USERNAME}/OpenShift-Jenkins-Lab -p SOURCE_REF=master -p SOURCE_SECRET=git-auth | oc apply -f -

## 2) Create the webhook-secret secret
oc create secret generic webhook-secret --from-literal=WebHookSecretKey=openshift123
