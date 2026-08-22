# Relay

A structured shift handoff logbook for SRE/engineering teams - replaces messy group-chat handoffs with an auditable, enforced acknowledgment workflow.

An outgoing engineer writes a structured shift report and publishes it to a specific incoming engineer. That engineer must explicitly acknowledge it before the handoff is considered complete. Every state change is timestamped, attributed to a real authenticated user, and recorded in an immutable audit log — so "did the handoff actually happen" is never a matter of scrolling through chat history.

## Why

Shift handoffs over Slack/chat get buried, skipped, or misread. Relay makes the handoff a first-class, trackable action:

- **DRAFT → PUBLISHED → ACKNOWLEDGED** - a report can only move forward through this sequence, enforced server-side regardless of what the UI allows
- **Immutability after publish** - once published, a report's content can never be edited
- **Designated recipient** - every report is addressed to one specific person, and only that person can acknowledge it
- **Full audit trail** - every transition is a permanent, separate record: who did it, and exactly when
- **Live system snapshot** - at the moment of publishing, the backend automatically captures real CPU load and recent system log activity from the host it's running on, attaching it to the report

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3, Spring Security, JWT auth, Spring Data JPA |
| Frontend | React, TypeScript, Vite, React Router |
| Database | PostgreSQL (local: Docker; production: AWS RDS) |
| Infrastructure | Docker, Docker Compose, nginx (reverse proxy gateway)|
| Deployment | AWS EC2 (Ubuntu), AWS RDS |
| CI | GitHub Actions (build + test on every PR) |
| Scripting | Bash (host system snapshot, captured at publish time) |

Nginx routes `/api/*` requests to the backend and everything else to the frontend's static files, so the browser only ever talks to one origin.

## Core features

- **Auth** — registration, login, JWT-based sessions
- **Structured reports** — Active Incidents / Ongoing Investigations / Watchlist Items, severity level, tags
- **Enforced state machine** — `DRAFT → PUBLISHED → ACKNOWLEDGED`, validated server-side via an explicit transition table, with edits blocked once published
- **Designated handoff recipient** — chosen at creation; only that user can acknowledge
- **Audit log** — a separate, append-only table recording every transition independently of the report's own fields
- **Searchable history** — filter published reports by severity, tag, and date
- **In-app notifications** — a badge showing how many reports are waiting on the current user, refreshed live
- **Automated system snapshot** — a bash script run server-side at publish time, capturing real host CPU load and recent log activity
- **CI** — every PR automatically builds and tests the backend via GitHub Actions

## Running locally

Requires Docker and Docker Compose.

1. Clone the repo
2. Create a `.env` file at the repo root (see `.env.example` if present, or the Environment variables section below)
3. From the repo root:

```bash
docker compose up -d --build
```

4. Visit `http://localhost:5173`

The local Compose setup includes its own PostgreSQL container - no external database needed for local development.

## Environment variables

| Variable | Used by | Purpose |
|---|---|---|
| `POSTGRES_DB` / `POSTGRES_USER` / `POSTGRES_PASSWORD` | Postgres, backend | Local database credentials |
| `JWT_SECRET` | Backend | Signing key for JWTs |
| `JWT_EXPIRATION_MS` | Backend | Token lifetime |
| `CORS_ALLOWED_ORIGIN` | Backend | Allowed frontend origin (mainly relevant outside the Docker gateway setup) |
| `SPRING_DATASOURCE_URL` / `_USERNAME` / `_PASSWORD` | Backend (production only) | Points at the managed RDS instance instead of local Postgres |

Production uses a separate `docker-compose.prod.yml`, which omits the local Postgres container in favor of a managed RDS instance, and is deployed on an EC2 instance behind the same nginx gateway pattern.

## Project structure

```
backend/    Spring Boot API - auth, domain model, service layer, REST controllers
frontend/   React + TypeScript SPA
nginx.conf  Gateway routing config
.github/    GitHub Actions CI workflow
```

## Notable design decisions

- **Raw SQL for genuinely complex queries** (joins, multi-condition search), JPA method-name derivation for simple lookups
- **UUIDs, not auto-increment IDs**, so identifiers don't leak sequence/volume information
- **DTOs on every API boundary** - entities are never serialized directly, so a field like a password hash can never accidentally leak into a response
- **The system snapshot script runs on the host**, mounted read-only into the backend container, rather than trying to run privileged commands inside an isolated container filesystem