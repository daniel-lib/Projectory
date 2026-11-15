# Projectory — Project Reference Guide

> This document describes the application as it currently exists, before the Vue.js → HTMX migration.
> It is meant to give an AI agent (or a human) a complete picture of the app's features, pages,
> API endpoints, and frontend interactions so nothing is missed during the migration.

---

## 1. Tech Stack

| Layer | Technology |
|---|---|
| Backend framework | Spring Boot 2.7.12 (Java 11) |
| Persistence | Spring Data JPA + PostgreSQL (H2 for dev/test) |
| Security | Spring Security 5 |
| View engine | Thymeleaf 3 |
| Reactivity / AJAX | Vue 3 (CDN, global build) — **to be replaced** |
| CSS framework | Bootstrap 5.2.3 (CDN) |
| Charts | Chart.js (CDN) |
| Animation | AOS (Animate on Scroll), Themify Icons |
| Font icons | Font Awesome 6 (locally bundled) |
| Other JS | jQuery (locally bundled, used minimally) |

---

## 2. Application Pages & Features

### 2.1 Landing / Home (`/`)
- Static marketing page with sign-in and sign-up forms (shown/hidden via JS).
- No Vue usage. Vanilla JS only (`mainScript.js`, `landing-pages-menu-script.js`).
- Theme switcher (dark/light mode cookie-based).
- Cookie notice banner.

### 2.2 User Dashboard (`/user/dashboard`)
**Template:** `user-content-container.html` → includes `dashboard-content-layout.html`

Three dashboard cards that open a "viewing modal" overlay:
1. **Project card** — lists user's projects (server-rendered via Thymeleaf). Clicking opens the project modal.
2. **Todo / Notes card** — lists collections with their items (server-rendered). Clicking opens the todo modal.
3. **Tasks on Board card** — peek at tasks on the agile board for the currently selected project. **Vue-driven** (`dashboard-vue.js` → reads `localStorage` for selected project, fetches `/project/getProjectTasks?project=<id>`).

**Dashboard charts section:** Pie chart showing project-status counts, rendered by `charts-project.js` using data injected by Thymeleaf into a JS variable (`ProjectStatusCount` JSON string).

The viewing modal (`#viewing-modal-container`) contains two panels:
- **Todo modal** (`#todo-list-modal-content`) — rendered by Thymeleaf; add-collection and add-todo-item calls use vanilla `fetch()` in `todoListScript.js`.
- **Project modal** (`#project-list-modal-content`) — **Vue-driven** (`dashboard-project-vue.js`, mounted on `#project-list-modal-content`).

### 2.3 Project Modal (inside Dashboard)
**Vue app:** `dashboard-project-vue.js`, mounted on `#project-list-modal-content`

Reactive features:
- **List all projects** with their tasks and member count (`GET /project/getProjects`).
- **Project count** (total / owned / joined) (`GET /project/getProjectCount`, `/getOwnProjectsCount`, `/getJoinedProjectsCount`).
- **Add project** (form submission via `fetch`, `GET /project/add-project?title=&description=&status=`).
- **Delete project** (`GET /project/delete?project=<id>`).
- **Project members** — dropdown showing members per project, loaded lazily (`GET /project/members?project=<id>`).
- **Add member** — checkbox toggle from user's connection list (`GET /project/addProjectMember?project=&user=`).
- **Remove member** — (`GET /project/removeProjectMember?project=&user=`).
- **Add task to project** — inline form per project (`GET /project/add-project-task?title=&desc=&project=&status=`).
- **Delete task** (`GET /project/task/delete/<taskId>`).
- **Expand/collapse project boxes** — purely DOM manipulation (no server call).
- **Show/hide add-member dropdown** — with collapse-siblings logic.
- **Show/hide add-task form** — animated height toggle.

### 2.4 Agile Board (`/project/board`)
**Template:** `board.html` → includes `board-layouts.html`
**Vue app:** `agile-board-vue.js`, mounted on `#board-container-container`

Reactive features:
- **Project list dropdown** to pick which project to view on the board (`GET /project/getProjects`).
- **Load tasks for selected project** (`GET /project/getProjectTasks?project=<id>`).
- **Persist selected project** in `localStorage` so it survives page refresh.
- **Drag-and-drop Kanban** — three columns: *Todo* (`not started`), *In Progress* (`in progress`), *Done* (`done`).
  - `dragstart`, `drop`, `dragover`, `dragleave`, `dragenter` events all handled in Vue.
  - On drop: `GET /project/task/update/status?status=<status>&task=<taskId>`.
  - Drop area highlight CSS class toggled on drag enter/leave.
- **Assign task to member** — dropdown per card, loads members on click (`GET /project/members?project=<id>`), then `GET /project/task/update/assignee?user=<userId>&task=<taskId>`.
- **Loader spinner** shown during async operations.
- **Full-screen mode** — DOM class toggle, no server call.
- Selected project is restored from `localStorage` on page load.

### 2.5 User Profile (`/user/profile` and `/user/{username}`)
**Template:** `profile.html` + `profile-layouts.html`
**Vue app:** `user-detail-vue.js`, mounted on `#user-details`

Thymeleaf passes two values into `<script>` tags:
- `ownAccount` — `"true"` or `"false"` string
- `profileUsernameValue` — the username being viewed

Reactive features:
- **Display user detail** (name, username, online status, profile picture) — `GET /user/detail` (own) or `GET /user/detail/<username>` (other).
- **Display user's public projects** (`GET /user/projects/<username>`).
- **Display connections** (approved, sent-pending, received-pending) — three Bootstrap tab panels.
  - Approved: `GET /user/connections/<username>`
  - Sent requests: `GET /user/connection/request/sent/`
  - Received requests: `GET /user/connection/request/received/`
- **Connection status** between current user and viewed user (`GET /user/connection/status/<userId>`).
- **Connect** button: `GET /user/connection/request/send/<userId>`.
- **Cancel pending request**: `GET /user/connection/request/remove/<userId>` or `/remove-username/<username>`.
- **Accept/Reject received request**: `GET /user/connection/request/accept/<connId>` / `.../reject/<connId>`.
- **Unfriend**: `GET /user/connection/remove-by-conn-id/<connId>`.
- Profile picture fallback: first letter of first name shown as avatar.

### 2.6 Search (navbar, all pages in dashboard area)
**Vue app:** `search-vue.js`, mounted on `#search-bar`

- Live search on `keyup` from text input in nav bar.
- `GET /search/all/<keyword>` returns a list of `SearchResultDto`.
- Results shown in a Bootstrap dropdown below the input.
- Currently only "user" result type is rendered.
- Loader spinner during fetch.

### 2.7 Todo / Notes (inside Dashboard Modal)
No Vue. All interactions use vanilla `fetch()` in `todoListScript.js`:
- **Add collection**: `fetch('/todo-list-collection/add-collection?title=<title>')` → expects `"1"` for success.
- **Add todo item**: `fetch('/todo/add-item-js?title=<title>&collectionId=<id>')` → expects `"1"`.
- **Delete item (single)**: regular `<a href="/todo/delete-item?itemId=<id>">` link (full page reload).
- **Delete selected**: HTML form `POST /user/todo/delete-selected`.
- **Edit item**: inline DOM manipulation (`editItem()` function), submits as form.
- **Checkbox selection**: `readyDeleteButton()` manages the delete button enabled state and counter.

### 2.8 Blog (`/blog`)
**Vue app:** `blog-vue.js`, mounted on `#blog-post-area`

- Expand/collapse blog post cards with animated CSS class toggle.
- Fetches full blog body from server on expand (URL is `/link`, currently a stub).
- Collapse resets text content and removes classes.

### 2.9 Charts (Dashboard metrics)
- `charts-project.js` renders a Chart.js pie chart on `#projects-chart`.
- Data comes from a Thymeleaf-injected JSON string `ProjectStatusCount`.
- A Vue-based chart (`charts-main-vue.js`) exists but is commented out in the template.

---

## 3. Full API Endpoint Inventory

All endpoints return JSON unless noted. All are `GET` (the project uses GET for mutations — this is a known issue to be aware of but **not changed** in this migration).

### Project endpoints (`/project/...`)
| Endpoint | Returns | Used by |
|---|---|---|
| `GET /project/getProjects` | `List<ProjectDto>` | dashboard-project-vue, agile-board-vue, dashboard-vue |
| `GET /project/getProjectTasks?project=<id>` | `List<ProjectTasksDto>` | agile-board-vue, dashboard-vue |
| `GET /project/task?project=<id>` | `List<ProjectTasksDto>` | dashboard-project-vue (per-project tasks) |
| `GET /project/getProjectCount` | `long` | dashboard-project-vue |
| `GET /project/getOwnProjectsCount` | `long` | dashboard-project-vue |
| `GET /project/getJoinedProjectsCount` | `long` | dashboard-project-vue |
| `GET /project/status/count` | `List<ProjectStatusCount>` | charts-main-vue (not in use), charts-project.js |
| `GET /project/add-project?title=&description=&status=` | `int` (1/0) | dashboard-project-vue |
| `GET /project/delete?project=<id>` | `int` (1/0) | dashboard-project-vue |
| `GET /project/add-project-task?title=&desc=&project=&status=` | `int` (1/0) | dashboard-project-vue |
| `GET /project/task/delete/<taskId>` | `int` (1/0) | dashboard-project-vue |
| `GET /project/members?project=<id>` | `List<ProjectMembersDto>` | dashboard-project-vue, agile-board-vue |
| `GET /project/addProjectMember?project=&user=` | `int` (1/0) | dashboard-project-vue |
| `GET /project/removeProjectMember?project=&user=` | `int` (1/0) | dashboard-project-vue |
| `GET /project/task/update/status?status=&task=<id>` | `int` (1/0) | agile-board-vue |
| `GET /project/task/update/assignee?user=&task=<id>` | `int` (1/0) | agile-board-vue |

### User / connections endpoints (`/user/...`)
| Endpoint | Returns | Used by |
|---|---|---|
| `GET /user/detail` | `CurrentUserDetailDto` | dashboard-project-vue, user-detail-vue |
| `GET /user/detail/<username>` | `PublicUserPersonalDetailDto` | user-detail-vue |
| `GET /user/projects/<username>` | `List<PublicUserProjectDetailDto>` | user-detail-vue |
| `GET /user/connections/<username>` | `List<PublicUserConnectionsDetailDto>` | user-detail-vue |
| `GET /user/getUserConnectionList` | `List<PublicUserConnectionsDetailDto>` | dashboard-project-vue (add-member list) |
| `GET /user/connection/status/<userId>` | `String` (plain text) | user-detail-vue |
| `GET /user/connection/SenderReceiverId/<userId>` | `ConnectionSenderReceiverDto` | user-detail-vue |
| `GET /user/connection/request/send/<userId>` | `int` (1/0) | user-detail-vue |
| `GET /user/connection/request/remove/<userId>` | `int` (1/0) | user-detail-vue |
| `GET /user/connection/request/remove-username/<username>` | `int` (1/0) | user-detail-vue |
| `GET /user/connection/request/accept/<connId>` | `int` (1/0) | user-detail-vue |
| `GET /user/connection/request/reject/<connId>` | `int` (1/0) | user-detail-vue |
| `GET /user/connection/request/sent/` | `List<PublicUserConnectionsDetailDto>` | user-detail-vue |
| `GET /user/connection/request/received/` | `List<PublicUserConnectionsDetailDto>` | user-detail-vue |
| `GET /user/connection/remove-by-conn-id/<connId>` | `int` (1/0) | user-detail-vue |

### Todo endpoints
| Endpoint | Returns | Used by |
|---|---|---|
| `GET /todo-list-collection/add-collection?title=` | `String` ("1"/"0") | todoListScript.js |
| `GET /todo/add-item-js?title=&collectionId=` | `String` ("1"/"0") | todoListScript.js |
| `GET /todo/delete-item?itemId=` | (redirect / full reload) | Thymeleaf `<a>` link |
| `POST /user/todo/delete-selected` | (redirect) | HTML form |

### Search
| Endpoint | Returns | Used by |
|---|---|---|
| `GET /search/all/<keyword>` | `List<SearchResultDto>` | search-vue.js |

---

## 4. Frontend JS File Inventory

| File | Purpose | Status |
|---|---|---|
| `vue-scripts/dashboard-project-vue.js` | Project list + CRUD in modal | **Active Vue — migrate** |
| `vue-scripts/dashboard-vue.js` | Tasks-on-board peek card | **Active Vue — migrate** |
| `vue-scripts/agile-board-vue.js` | Kanban board full page | **Active Vue — migrate** |
| `vue-scripts/user-detail-vue.js` | Profile page (user info, connections) | **Active Vue — migrate** |
| `vue-scripts/search-vue.js` | Navbar live search | **Active Vue — migrate** |
| `vue-scripts/blog-vue.js` | Blog expand/collapse | Minor Vue (can be Alpine or vanilla) |
| `vue-scripts/charts-main-vue.js` | Vue + Chart.js chart | Commented out in template — skip for now |
| `vue-scripts/collection-vue.js` | Todo collection Vue app | Empty / unused — delete |
| `vue-script.js` (root) | Older/duplicate Vue code | Superseded by vue-scripts/ — delete |
| `vue.global.js.js` | Local Vue 3 bundle (old) | Remove once migrated |
| `vue3.3.4.js` | Another local Vue 3 bundle | Remove once migrated |
| `todoListScript.js` | Todo AJAX + UI helpers | Keep — no Vue here |
| `dashboard-script.js` | Modal open/close, side menu, notifications | Keep |
| `mainScript.js` | Notifications, sign-in form toggle, go-to-top | Keep |
| `project-script.js` | Show/hide add-project form | Keep |
| `theme-switch-script.js` | Dark/light mode | Keep |
| `anim-script.js` | AOS animation init | Keep |
| `login-info-fetch.js` | Fetches login info display | Keep |
| `charts-project.js` | Chart.js dashboard pie chart | Keep |
| `charts/` | Chart helper files | Keep |

---

## 5. Template Structure

```
templates/
├── mainLayouts.html           # Landing page nav, footer, theme switch, notification bar
├── home/                      # Landing/marketing pages
├── pageError/                 # Error pages
└── user/
    ├── user-content-container.html   # Main authenticated shell (switches between pages)
    ├── user-layouts.html             # Shared head, nav, side-menu, footer fragments
    ├── dashboard-content-layout.html # Dashboard card grid + charts section
    ├── user-content-container.html   # Page switcher (dashboard / profile / board)
    ├── project/
    │   └── project-layouts.html      # Project list table, add-project form, modal content, dashboard card
    ├── todo/
    │   └── todoLayouts.html          # Todo modal, add forms, item list, dashboard card
    ├── profile/
    │   ├── profile.html              # Profile page shell
    │   └── profile-layouts.html      # Profile banner, top info, project list, connections fragments
    └── board/
        ├── board.html                # Board page shell
        └── board-layouts.html        # Kanban columns, task cards, project selector
```

---

## 6. Data DTOs (what JSON shapes the frontend consumes)

### `ProjectDto`
```
projectId, title, description, status, creationDate, projectOwnerUsername
(+ projectTasks[] attached client-side by Vue)
```

### `ProjectTasksDto`
```
taskId, taskName, taskDescription, status, assigneeUsername, projectId
```

### `ProjectMembersDto`
```
username, firstName, lastName, projectMemberUserId
```

### `ProjectStatusCount`
```
statusLabel, labelCount
```

### `CurrentUserDetailDto`
```
userId, username, firstName, lastName, email, onlineStatus, profilePicture, authRole, Enabled
```

### `PublicUserPersonalDetailDto`
```
userId, username, firstName, lastName, onlineStatus, profilePicture
```

### `PublicUserProjectDetailDto`
```
projectId, title, description, status, creationDate, projectOwnerUsername
```

### `PublicUserConnectionsDetailDto`
```
connectionId, connectionStatus, connectedUserUsername, connectedUserFirstName,
connectedUserLastName, profilePicture
```

### `ConnectionSenderReceiverDto`
```
connectionId, requestSender, requestReceiver
```

### `SearchResultDto`
```
searchResultType, searchItemTitle, searchItemDescription
```

---

## 7. Shared JS Utilities (vanilla, used across pages)

- `toggleNotification(type, msg)` — shows the floating notification bar (`mainScript.js`).
- `showDashboardActionNotification()` / `hideNotificationBoard()` — inline action feedback.
- `scrollToSpecificCollection(collectionId)` — scroll to a todo collection.
- `readyDeleteButton(specificCollectionId)` — enable/disable bulk-delete button.
- `editItem(id)` — inline edit (replaces `<span>` with `<form>`).
- `addNewCollection()` / `addTodo(collectionId)` — fetch wrappers.
- `addTaskToProject(projId)` — legacy fetch wrapper (largely superseded by Vue).
- `showAddProjectForm(source)` — toggle CSS class on the form.
- `displayViewingModal(contentType)` / `closeViewingModal()` — modal overlay logic.
- `sideMenuToggle()` — expand/collapse side nav.
- `showTopCollapsableMenu(index, clickOrigin)` — top nav dropdown menus.
- `selectedNavIndicator()` — highlights the active side-menu link.
- `displayItemCount()` — numbered badges on project/todo items.

---

## 8. Known Issues / Oddities in the Current Code

1. **All mutations use GET** — `add-project`, `delete`, `addProjectMember`, etc. are all `@GetMapping`. Not RESTful, and technically a security concern (GET requests should be idempotent). **Tech-debt to fix after this migration:** convert every mutation endpoint to `@PostMapping` in `ProjectController`, `UserDashboardController`, `TodoListCollectionController`, and `TodoController`; switch HTMX attributes from `hx-get` to `hx-post`; add Spring Security CSRF meta tags and an `htmx:configRequest` listener in `mainScript.js`.
2. **Vue used without build tooling** — Vue 3 is loaded from a CDN `global` build; components are inline JS objects, not SFCs. Each page that needs Vue loads a separate `<script>` tag at the bottom of the template fragment.
3. **Duplicate/dead Vue files** — `vue-script.js` (root-level) and `collection-vue.js` are either superseded or empty. `charts-main-vue.js` is commented out.
4. **Multiple Vue app instances per page** — the dashboard page mounts two separate Vue apps: `projectVue` on `#project-list-modal-content` and `dashboardVue` on `#projectTaskOnBoardPeekView`. They do not share state.
5. **localStorage for board state** — the agile board persists the selected project in `localStorage`. This needs to be replicated in the HTMX migration (via a cookie or URL param, since HTMX naturally handles server state).
6. **Thymeleaf + Vue template conflict** — Thymeleaf uses `[[...]]` for inline expressions; Vue uses `{{...}}` for interpolation. These coexist because `[[...]]` is Thymeleaf server-side and `{{...}}` is Vue client-side. The templates are careful to keep them separate. HTMX removes this tension entirely.
7. **`search-vue.js` is loaded inline in the nav fragment** (`user-layouts.html`) — it is included on every authenticated page.
8. **Vue data variables passed from Thymeleaf via inline `<script>` tags** — e.g. `ownAccount = "[[${ownAccount}]]"` in `profile.html`. After migration, these values should come from HTMX endpoint responses or `hx-vars` / data attributes.
