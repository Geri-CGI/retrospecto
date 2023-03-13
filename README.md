# Retrospecto

An open source retrospective tool created as a training application for newcomers.

## Architecture

Quasar frontend + Spring backend packaged ina single uber jar.

### Modules (a.k.a Plugins)

### frontend

The quasar frontend using Vue. It is a single page application communicating mostly over websocket with the backend.

### backend

A simple spring boot backend hosting the websocket server.

## Stack

### Spring

This project uses Spring Boot framework.
We use it for dependency injection and configuration management, but also because it has battle-tested integration
packages with all of them major cloud native components.

### Quasar frontend

This application has a quasar frontend to manage the board and poker. A web domain has been registered for Retrospecto
deployment on https://retrospect.cloud/

### Jenkins & Docker

To make the process of *development -> deploy* faster, easier and automatic we use a continuous integration tool *
*Jenkins** and
containerization platform **Docker**. Both Jenkins and Docker have been installed on our remote server
which can be accessed on [https://bonnee.eu/ci/](https://bonnee.eu/ci/) or via SSH CLI.

# Building & Running the application locally

To build the application simply run:

```bash
mvn clean install
```

## Running the application

### Running the backend

```bash
mvn spring-boot:run
```

### Running the frontend

```bash
quasar dev
```

# Retrospecto live environment

The Retrospecto application is up and running as a docker container
on [https://retrospecto.cloud/](https://retrospecto.cloud/).
Retrospecto has a multibranch build pipeline in Jenkins. This pipeline is triggered by git push events and build new
artifacts of Bonnie backend and frontend.
In the last stage of the pipeline we restart Retrospecto with the new versions of backend and frontend.
You can see the build pipeline here: https://bonnee.eu/ci/job/build-retrospecto/. You find the pipeline definition in
the file names "Jenkinsfile" under retrospecto's repository.

# FAQ

**Q: What SDK should I use for Retrospecto?**
A: OpenJDK v.18 (non-licensed preferably). Intellij IDEA allows to manage different versions of JDKs for each project
so this feature is recommended rather than installing java separately.

**Q: Do I need to Install Jenkins / Docker on my local machine?**
A: There's no need to install Jenkins nor Docker. In fact, it is not allowed to install Docker on the company machines
due to licensing.
Docker and Jenkins are both installed on our remote server.

**Q: How can I contribute to the Retrospecto repository on Github?**
A: You have to be added on the collaborator list by the repository owner in order to create/merge branches.
