# Lab: Birthday-Paradox
Welcome to the ACE OpenShift lab! Today, you'll build a CI/CD pipeline on OpenShift to deploy a simple Spring-Boot application called `birthday-paradox` (https://en.wikipedia.org/wiki/Birthday_problem).

## Preparation
### Fork Repository
This lab will require you to reference your own fork using a `git source` BuildConfig. For this reason **please `fork` this repository** so you have your own copy.

Go ahead and clone your fork once you have forked this repository:
```bash
export GITLAB_USERNAME=<username>
git clone ssh://git@gitlab.consulting.redhat.com:2222/$GITLAB_USERNAME/ACE-OpenShift-Lab.git
```

You should also navigate to your forked repository on your browser.

### Request Access to OpenTLC Cluster
Go to https://labs.opentlc.com. Navigate to Services->Catalogs->OpenTLC OpenShift 4 Labs. Click on the catalog item called OPENTLC OpenShift 4 Shared Access and click "order". Check the checkbox that says you understand the entry's runtime and expiration dates, and click "submit".

In a few minutes, you'll receive an email from Red Hat OPENTLC. The first email will notify you that the provisioning has started. Soon, you'll receive a second email that says provisioning has completed. When you have received this email, you can continue to the next section.

## Log into OpenShift
The credentials for the below items are the same as your Red Hat OPENTLC credentials.

Log into the OpenShift 4 shared cluster on your browser using this URL:
```
https://console-openshift-console.apps.shared.na.openshift.opentlc.com
```

Log into the OpenShift 4 shared cluster on the command line with `oc`.
```bash
oc login https://api.shared.na.openshift.opentlc.com:6443
```

## Configure OpenShift Environments
In this section, you will create and configure three OpenShift projects. The projects will serve as `dev`, `test`, and `prod` environments for the CI/CD pipeline.

### 1) Create OpenShift projects
Create three projects with the following names in OpenShift:
1. \<firstinitial>\<lastname>-dev
2. \<firstinitial>\<lastname>-test
3. \<firstinitial>\<lastname>-prod

For example, my first initial is "a" and my last name is "dewey", so my dev project would be called `adewey-dev`.

### 2) Create Jenkins Server in Dev Project
The Jenkins server in your Dev project will serve as the CI/CD orchestrator. Using the `oc` command, create a Jenkins server in your Dev project.

I recommend the following for this step:
- Use the `jenkins-ephemeral` template provided by the `openshift` project
- Use the `new-app` subcommand to deploy Jenkins
- Give the Jenkins server `2Gi` of memory and `disable administrative monitors` to speed up Jenkins initialization (**hint:** What command can you use to view the parameters of a template? How can you set those parameters during `oc new-app`?)

### 3) Grant Jenkins SA Proper Permissions
#### Grant Permission to Modify Test and Prod Projects
The `jenkins-ephemeral` template created a service account called `jenkins`. It gave the `jenkins` service account permission to modify the Dev project by default because that's the project where Jenkins was deployed.

You'll also need to give the `jenkins` service account permission to modify the Test and Prod projects.

Hints:
- What `oc` command can be used to grant permissions to a user or service account?
- Jenkins will need the `edit` role in the Test and Prod projects
- The `jenkins` service account will be referenced as `system:serviceaccount:<dev-project>:jenkins`

#### Grant Permission to Push Images
In the pipeline you will create later, you'll find that the `jenkins` service account will need to be able to push images to the Test and Prod projects.

Grant the `jenkins` service account permission to push images to Test and Prod.

Hints:
- You'll use the same command as above, but with the `registry-editor` role instead

### 4) Create `git-auth` Secret
You will need to create an OpenShift secret that contains your GitLab credentials. These credentials will be used by the BuildConfig in step 5 to authenticate with your forked repository.

Using the `oc` tool, create a `kubernetes.io/basic-auth` OpenShift secret that contains your GitLab username and password.

Hints:
- Where can you go to find information on how to create this secret?

### 5) Create `birthday-paradox-pipeline` BuildConfig
BuildConfigs of type `JenkinsPipeline` are interpreted by OpenShift as Jenkins Pipelines. When a BuildConfig of this type is created, OpenShift will automatically create a new pipeline job in Jenkins.

The file `pipeline.yaml` is an OpenShift template that contains the `birthday-paradox-pipeline` BuildConfig. It uses a `git` source, which points to a git repository containing a `Jenkinsfile`.

Using the `oc` tool, process the `pipeline.yaml` OpenShift template and create the pipeline BuildConfig in the Dev project. Be sure to provide the following parameters to the template:
| Parameter | Value |
| --------- | ----- |
| APPLICATION_NAME | birthday-paradox |
| REPOSITORY_URI | \<The URL pointing to your forked repository> |
| SOURCE_REF | master |
| SOURCE_SECRET | git-auth |

### 6) Create `jenkins-agent-skopeo` ImageStream
The CI/CD pipeline to build and deploy `birthday-paradox` will require the `skopeo` tool to copy the built image to the Test and Prod environments. 

The `openshift/agents/jenkins-agent-skopeo.yaml` file contains an ImageStream with a label `role=jenkins-slave`. When an ImageStream with this label is created, Jenkins interprets this as a new Jenkins agent. It will automatically configure Jenkins to use this ImageStream when the label `jenkins-agent-skopeo` is referenced in a Jenkins pipeline.

Use the `oc` tool to create the Jenkins agent to the Dev project.

## Log into Jenkins
In the next section, you will develop the CI/CD pipeline for building and deploying `birthday-paradox`. For the development of this pipeline, it will be helpful to be logged into Jenkins so you can easily view your build logs.

Locate your Jenkins route:
```bash
oc get route jenkins
```

Copy and paste the Jenkins URL in your browser. Your login credentials for Jenkins are the same as your OpenTLC credentials. Once inside Jenkins, select the folder corresponding with your Dev project and select the pipeline inside that folder. You should see a fairly empty screen since you haven't run any builds yet. This screen will begin to show build statuses as you trigger builds in the next section.

## Create Jenkins Pipeline
Locate the `Jenkinsfile` in this repo. A Jenkinsfile contains the pipeline code that will execute your CI/CD pipeline. A Jenkinsfile is a Groovy-based DSL (Domain-Specific Language) that is understood by Jenkins and operates almost identically to vanilla Groovy, which is based on Java.

The `Jenkinsfile` contains unfinished skeleton code. Your task is to complete each section marked as `TODO`. When you believe you have fulfilled each TODO, or just want to simply test your pipeline thus far, trigger the build using `oc`:
```bash
oc start-build birthday-paradox-pipeline
```

You will have completed this section of the lab when you have completed each TODO and have an end-to-end working pipeline that can:
1. Build the `birthday-paradox` project
2. Execute unit tests
3. Create an immutable container image
4. Deploy the application Dev, Test, and Prod

## Create Git Webhook
Gitlab blocks access to port 6443! (probably)

This could be done in github instead?
