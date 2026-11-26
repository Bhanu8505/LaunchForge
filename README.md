# LaunchForge

**LaunchForge** is an AI-powered application builder that turns
natural-language prompts into editable web application code and provides
a live preview. It combines an AI-assisted generation workflow with
project file storage and Kubernetes-based preview environments.

> This README describes the expected local development workflow. Check
> the project's `application.yml`  and
> frontend environment files for the exact configuration property names
> used by your current code.

## Features

-   **AI-powered code generation** --- describe a change in natural
    language and generate or update project files.
-   **Streaming responses** --- receive AI generation output
    progressively through Server-Sent Events (SSE).
-   **Project workspace** --- keep generated files organized by project.
-   **Object storage** --- store project files in MinIO.
-   **Live preview** --- run generated applications in isolated
    Kubernetes runner pods.
-   **Dynamic routing** --- route project preview domains to the
    corresponding runner.
-   **Dependency-aware builds** --- install project dependencies and
    start the development server.
-   **Chat memory and retrieval foundations** --- use PostgreSQL with
    pgvector and persistent chat history.
-   **Infrastructure integrations** --- Redis for routing/state,
    Kubernetes for runtime orchestration, and OpenRouter for LLM access.

## Tech stack

  Layer                   Technology
  ----------------------- ------------------------------------------------------
  Backend                 Java 21, Spring Boot, Spring AI
  Frontend                React, JavaScript, Vite
  AI provider             OpenRouter
  Database                PostgreSQL, pgvector
  Cache / routing         Redis
  Object storage          MinIO
  Runtime orchestration   Kubernetes
  Container tooling       Docker
  Build tools             Maven or the wrapper included in the repository; npm

## Architecture overview

1.  The user submits a prompt through the React frontend.
2.  The frontend sends the request to the Spring Boot backend.
3.  Spring AI communicates with the configured LLM provider.
4.  Generated file changes are saved under the relevant project in
    MinIO.
5.  When a preview is requested, the backend claims an available runner
    pod.
6.  The runner downloads the project's files, installs dependencies, and
    starts the Vite development server.
7.  Redis stores the project-domain-to-runner route, which the reverse
    proxy uses to serve the preview.

## Prerequisites

Install the following before running the application:

-   Java 21
-   Node.js 20 or later and npm
-   Maven (if the repository does not include `mvnw`)
-   Docker Desktop
-   A local Kubernetes cluster, such as Minikube or kind
-   `kubectl`
-   Access to an OpenRouter API key
-   PostgreSQL with the pgvector extension
-   Redis
-   MinIO

If your repository contains Docker Compose configuration for
infrastructure services, use that configuration instead of starting
PostgreSQL, Redis, and MinIO individually.

## Configuration

Configure the backend using the environment variables or Spring
properties expected by your application. Do not commit real API keys,
passwords, or access credentials.

Create a local environment file or set the values in your IDE's run
configuration. The following is a checklist of the required settings;
map the names to the exact properties used in your configuration files.

  Setting                        Purpose
  ------------------------------ ---------------------------------------
  OpenRouter API key             Authenticates AI model requests
  OpenRouter model               Selects the model used for generation
  PostgreSQL URL                 Database connection
  PostgreSQL username/password   Database credentials
  Redis host/port                Cache and preview-route storage
  MinIO endpoint                 S3-compatible object-storage endpoint
  MinIO access key/secret key    Object-storage credentials
  MinIO bucket                   Bucket used for project files
  Kubernetes namespace           Namespace containing runner pods
  Frontend API base URL          Backend URL used by the React app

For a local Kubernetes deployment, the backend and runner pods must be
able to resolve and reach the MinIO and Redis Kubernetes Services. The
runner image must include Node.js and npm; the syncer image must include
the AWS CLI.

### MinIO

Create the bucket configured in the backend (the project setup in this
repository uses `projects`). Project files are stored under a
project-specific prefix:

``` text
s3://projects/<project-id>/
```

Make sure the MinIO credentials used by the backend and runner syncer
have permission to list, read, and write the required objects.

### PostgreSQL

Create the application database and enable pgvector if the application
uses vector search:

``` sql
CREATE EXTENSION IF NOT EXISTS vector;
```

The application may create its tables automatically, depending on the
configured JPA and database initialization settings.

### OpenRouter

Create an API key in OpenRouter and configure it in the backend. Ensure
the selected model is available to your account. Model identifiers and
availability can change, so use the model configured or supported by the
current application.

## Running locally

The commands below assume a split backend/frontend project. Run them
from the relevant directories.

### 1. Start infrastructure

Start PostgreSQL, Redis, and MinIO using the repository's Docker Compose
file, if one is provided:

``` bash
docker compose up -d
```

If there is no Compose file, start these services separately and update
the backend configuration with their connection details.

### 2. Start the backend

From the backend directory (the directory containing `pom.xml`):

**Windows:**

``` powershell
.\mvnw.cmd spring-boot:run
```

If the Maven wrapper is not present:

``` powershell
mvn spring-boot:run
```

**macOS / Linux:**

``` bash
./mvnw spring-boot:run
```

Or, if no wrapper is present:

``` bash
mvn spring-boot:run
```

The backend should start on the port configured in `application.yml` or
`application.properties`.

### 3. Start the frontend

From the frontend directory (the directory containing `package.json`):

``` bash
npm install
npm run dev
```

If the frontend uses a Vite environment variable for the backend URL,
configure it in the frontend's local environment file (commonly
`.env.local`) before starting the dev server.

Open the local URL printed by Vite in your browser.

## Running the preview infrastructure on Kubernetes

The live preview workflow requires a Kubernetes cluster with the runner
and reverse-proxy resources deployed.

### 1. Start your local cluster

For Minikube:

``` bash
minikube start
kubectl config use-context minikube
```

For kind, create or start your configured kind cluster and ensure
`kubectl` is using the correct context.

### 2. Create the application namespace

``` bash
kubectl create namespace web-apps
```

If the namespace already exists, this command may report that it already
exists; that is fine.

### 3. Deploy the preview resources

Apply the Kubernetes manifests from the repository. Update the paths to
match your project:

``` bash
kubectl apply -f k8s/
```

The manifests should configure the resources required by the preview
system, including:

-   Runner pool Deployment
-   Runner and syncer containers
-   Shared workspace volume mounted by both containers
-   MinIO and Redis Services, or connectivity to those Services
-   Reverse-proxy Deployment and Service

The runner pool should have at least one ready, idle pod before
requesting a preview.

Check the resources:

``` bash
kubectl get pods -n web-apps
kubectl get deployments -n web-apps
kubectl get services -n web-apps
```

Check runner logs:

``` bash
kubectl logs <runner-pod-name> -n web-apps -c runner
kubectl logs <runner-pod-name> -n web-apps -c syncer
```

### 4. Configure preview routing

The preview service uses a project-specific hostname, for example:

``` text
project-<project-id>.app.domain.com
```

Configure local DNS or a suitable wildcard DNS/hosts setup so that
preview hostnames resolve to the reverse proxy. The reverse proxy must
be reachable from your browser, and it must be able to read the
corresponding route from Redis.

For local development, use the hostname and port configured by your
reverse proxy. If the application returns a preview URL, open that URL
in your browser.

## Useful commands

``` bash
# View pods in the preview namespace
kubectl get pods -n web-apps -o wide

# Follow backend logs
kubectl logs -f <pod-name> -n web-apps

# View runner container logs
kubectl logs -f <runner-pod-name> -n web-apps -c runner

# View syncer container logs
kubectl logs -f <runner-pod-name> -n web-apps -c syncer

# Describe a pod when it is not starting
kubectl describe pod <pod-name> -n web-apps

# Check Redis route entries
kubectl exec -it <redis-pod-name> -n web-apps -- redis-cli
```

## Troubleshooting

  -----------------------------------------------------------------------
  Problem                             Things to check
  ----------------------------------- -----------------------------------
  Backend cannot connect to           Verify service availability,
  PostgreSQL or Redis                 hostnames, ports, credentials, and
                                      active Spring profile.

  AI requests fail                    Check the OpenRouter API key,
                                      selected model identifier, account
                                      limits, and backend logs.

  Project files are missing from      Verify the bucket, project prefix,
  MinIO                               credentials, and object-storage
                                      endpoint.

  Runner pod is not available         Check runner Deployment replicas,
                                      pod readiness, image-pull errors,
                                      and namespace.

  Preview shows an old version        Check that the latest files reached
                                      MinIO and that the active runner
                                      workspace has synced them.

  Preview hostname does not resolve   Check wildcard DNS/hosts
                                      configuration and reverse-proxy
                                      reachability.

  Preview returns a routing error     Verify the `route:<hostname>` entry
                                      in Redis and confirm that it points
                                      to the active runner.

  Vite server does not start          Inspect the runner logs and
                                      `/app/dev.log`; check that
                                      dependencies installed successfully
                                      and port 5173 is listening.
  -----------------------------------------------------------------------

## Future scope

Potential improvements planned for future iterations:

-   **Multi-user authentication and authorization** --- user accounts,
    project ownership, roles, and access controls.
-   **Project history and versioning** --- snapshots, rollback, change
    diffs, and restore points.
-   **GitHub integration** --- import repositories, connect accounts,
    commit generated changes, and push updates.
-   **More frameworks and templates** --- support additional frontend
    frameworks, backend stacks, and starter templates.
-   **Improved code editing experience** --- richer editor, file
    explorer, syntax highlighting, and direct manual edits.
-   **Reliable generation workflows** --- resumable streams,
    cancellation, retries, validation, and clearer generation status.
-   **Automated quality checks** --- linting, type checking, tests,
    build verification, and AI-assisted error repair.
-   **Preview lifecycle management** --- idle timeouts, automatic
    cleanup, runner autoscaling, resource quotas, and better isolation.
-   **Deployment to production** --- publish generated applications to
    configurable hosting providers with custom domains and HTTPS.
-   **Collaboration** --- shared projects, team workspaces, comments,
    and real-time editing.
-   **Observability and cost controls** --- structured logs, metrics,
    tracing, token usage tracking, and per-project limits.
-   **Security hardening** --- secret management, network policies,
    container resource limits, and stronger isolation between user
    workloads.

## Contributing

Contributions and suggestions are welcome. Before opening a pull
request:

1.  Create a branch for your change.
2.  Keep secrets and local environment files out of version control.
3.  Run the relevant backend and frontend checks.
4.  Describe the change and include reproduction steps where applicable.

## License

Add the license and copyright information applicable to this project
before publishing or distributing it.
