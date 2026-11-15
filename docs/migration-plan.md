# Migration Plan — Vue.js → HTMX + Alpine.js

> This is the working plan file. The agent should update task statuses as work progresses.
> See `migration-guide.md` for detailed how-to instructions per feature.
> See `project-reference.md` for the complete current-state reference.

---

## Status Legend
- `[ ]` Not started
- `[~]` In progress
- `[x]` Done
- `[!]` Blocked / needs attention

---

## Phase 0 — Setup (no breaking changes)

- [x] **0.1** Add HTMX CDN `<script>` to `user-layouts.html` `<head>` fragment.
- [x] **0.2** Add Alpine.js CDN `<script defer>` to `user-layouts.html` `<head>` fragment.
- [x] **0.3** Verify the app still starts and both libraries load in the browser (check DevTools Network tab). No functionality should change yet.

---

## Phase 1 — Easy Wins (no new backend endpoints)

### 1.1 Blog expand/collapse
- [x] Find where the blog template is loaded and locate any `<script src=".../blog-vue.js">` tag.
- [x] Add Alpine `x-data="{ expanded: false }"` to the blog post card container.
- [x] Replace `@click="expandBlogPost($event)"` with `@click="expanded = !expanded"`.
- [x] Replace `v-if` / class binding with Alpine `:class` and `x-show`.
- [x] The full content fetch (`/link`) is a stub — leave an `hx-get` placeholder with a comment, do not remove the existing UI behaviour.
- [x] Delete `static/javascript/vue-scripts/blog-vue.js`.

### 1.2 Project box expand/collapse (inside Project Modal)
- [!] **Deferred to Phase 6.** The expand/collapse button is embedded inside the same Vue app (`dashboard-project-vue.js`) that drives the entire project list. Pulling it out early would create an Alpine + Vue conflict on the same DOM elements. This will be converted to Alpine as part of Phase 6 when the whole Vue app is replaced.

---

## Phase 2 — Todo / Notes Section

**Goal:** Replace `fetch()` calls in `todoListScript.js` and form-based deletes with HTMX. Keep the existing server-rendered Thymeleaf structure — these pages are already server-rendered, so the backend work is minimal.

**New backend endpoints:**
- `GET /todo-list-collection/fragment/list` → returns `todoLayouts :: todo-collection-list` (the full collection list body).
- `GET /todo/fragment/items?collection=<id>` → returns `todoLayouts :: displayItems` for a single collection.

**New fragment to create:** `todoLayouts :: todo-collection-list` — wraps the `th:each="collection : ${TodoCollections}"` loop so it can be swapped independently.

- [x] **2.1** Create `GET /todo-list-collection/fragment/list` in the controller.
- [x] **2.2** Create `POST /todo/fragment/add-item?collectionId=` in `TodoController`.
- [x] **2.3** Create `POST /todo/fragment/delete-item?itemId=&collectionId=` in `TodoController`.
- [x] **2.4** Create `POST /todo/fragment/delete-selected` in `TodoController`.
- [x] **2.5** In `todoLayouts.html`, create `th:fragment="todo-collection-list"` wrapping the `th:each` collection loop.
- [x] **2.6** Give the collection list wrapper stable id `todo-collection-list-container`.
- [x] **2.7** Give each collection's item list stable `th:id="'todo-items-' + ${collection.todoCollectionId}"`.
- [x] **2.8** Replace **Add Collection** button with HTMX `hx-post`.
- [x] **2.9** Replace **Add Todo Item** span with HTMX `hx-post`.
- [x] **2.10** Replace delete single item `<a>` link with HTMX `hx-post`.
- [x] **2.11** Migrate delete-selected form to HTMX `hx-post`.
- [x] **2.12** Remove `addNewCollection()` and `addTodo()` from `todoListScript.js`.
- [ ] **2.13** Test: add collection, add item, delete item, delete selected.

---

## Phase 3 — Navbar Search

**New backend:** `GET /search/fragment/all/{keyword}` → returns `fragments/search-results :: search-results`

**New template:** `src/main/resources/templates/fragments/search-results.html`

- [x] **3.1** Add `searchFragment` to `SearchController.java`.
- [x] **3.2** Create `templates/fragments/search-results.html`.
- [x] **3.3** Replace search `<input>` with HTMX attributes.
- [x] **3.4** Give `<ul>` an `id="search-dropdown-results"` and clear Vue children.
- [x] **3.5** Remove `<script src="/javascript/vue-scripts/search-vue.js">` tag.
- [x] **3.6** Delete `static/javascript/vue-scripts/search-vue.js`.
- [ ] **3.7** Test: type in the search box; results appear in the dropdown without a page reload.

---

## Phase 4 — Tasks-on-Board Peek Card (Dashboard)

**New backend:** `GET /project/fragment/board-peek` → returns `project-layouts :: board-peek-content`

- [x] **4.1** Add `boardPeekFragment` to `ProjectController`.
- [x] **4.2** Create `th:fragment="board-peek-content"` in `project-layouts.html`.
- [x] **4.3** Replace `project-task-dashboard-card` inner content with HTMX `hx-trigger="load"` div.
- [x] **4.4** Remove Vue template syntax from `project-task-dashboard-card`.
- [x] **4.5** Remove `<script src="/javascript/vue-scripts/dashboard-vue.js">`.
- [x] **4.6** Delete `static/javascript/vue-scripts/dashboard-vue.js`.
- [ ] **4.7** Test: dashboard loads; card shows tasks or the "no items" message.

---

## Phase 5 — User Profile Page

**New backend endpoints:**
- `GET /user/fragment/connection-status?user=<userId>` → returns `fragments/connection-status :: connection-buttons`
- Connection action endpoints (send/cancel/accept/reject/unfriend) already exist as JSON — after each action, re-fetch connection-status fragment to update the buttons.

**New template:** `templates/fragments/connection-status.html`

- [x] **5.1** Update `displayUserProfile` and `displayUserProfileWithUsername` in `UserDashboardController` to pass the viewed user's full detail object to the model (so name, avatar, online status can be rendered by Thymeleaf directly).
- [x] **5.2** In `profile-layouts.html`, convert `profile-top-user-info` fragment: replace `{{authFirstName}}`, `{{authLastName}}`, `{{authUsername}}`, `{{authOnlineStatus}}` with `th:text` and `th:class` using model attributes.
- [x] **5.3** Create `GET /user/fragment/connection-status?user=<userId>` endpoint: query connection status and sender/receiver IDs server-side, pass to model, return `"fragments/connection-status :: connection-buttons"`.
- [x] **5.4** Create `templates/fragments/connection-status.html` with `th:fragment="connection-buttons"` — Thymeleaf renders the correct action for the current state. State-changing actions use POST; HTMX events refresh connection fragments.
- [x] **5.5** In `profile.html`, remove the inline `<script>` block (ownAccount / profileUsernameValue). Add `<div id="connection-action-buttons" th:if="${!ownAccount}" hx-get="/user/fragment/connection-status?user=${viewedUserId}" hx-trigger="load" hx-swap="outerHTML">`.
- [x] **5.6** Pass `viewedUserId` to the model in the profile controllers.
- [x] **5.7** Update `displayUserProfile` and `displayUserProfileWithUsername` to query and add `userProjectList`, `userConnectionsList`, `sentConnections`, `receivedConnections` to the model (so tabs render on page load with no HTMX).
- [x] **5.8** Convert `user-project-list` fragment from Vue `v-for` to Thymeleaf `th:each`.
- [x] **5.9** Convert `approvedConnections`, `sentConnectionRequest`, `receivedConnectionRequest` fragments from Vue `v-for` to Thymeleaf `th:each`. Unfriend / cancel / accept / reject buttons inside become HTMX (each targets `#connection-action-buttons` or the tab content).
- [x] **5.10** Profile picture fallback: move from Vue (`connect.profilePicture.length == 1`) to Thymeleaf `th:if`.
- [x] **5.11** Remove `<script src="/javascript/vue-scripts/user-detail-vue.js">` from `profile.html`.
- [x] **5.12** Delete `static/javascript/vue-scripts/user-detail-vue.js`.
- [ ] **5.13** Test all profile scenarios: own profile, other profile, connect, cancel, accept, reject, unfriend.

---

## Phase 6 — Project Modal (most complex)

**New backend endpoints:**
- `GET /project/fragment/list` → `project-layouts :: project-list-table`
- `GET /project/fragment/count` → `project-layouts :: project-count-info`
- `GET /project/fragment/tasks?project=<id>` → `project-layouts :: project-task-list`
- `GET /project/fragment/members?project=<id>` → `project-layouts :: project-member-list`
- `GET /user/fragment/connection-list?project=<id>` → `project-layouts :: add-member-checkbox-list`
- Mutation endpoints (`add-project`, `delete`, `addProjectMember`, etc.) use POST and retain their existing response bodies; HTMX events trigger fragment refreshes.

- [x] **6.1** Add `projectListFragment(Authentication auth, Model model)` to `ProjectController`: query projects, members per project, tasks per project; build model; return `"user/project/project-layouts :: project-list-table"`.
- [x] **6.2** Add `projectCountFragment(Authentication auth, Model model)` to `ProjectController`.
- [x] **6.3** Add `projectTasksFragment(@RequestParam long project, Authentication auth, Model model)` to `ProjectController`.
- [x] **6.4** Add `projectMembersFragment(@RequestParam long project, Authentication auth, Model model)` to `ProjectController`.
- [x] **6.5** Add `connectionListForProject(@RequestParam long project, Authentication auth, Model model)` to `UserDashboardController`: get user's connections, filter out existing project members, return `"user/project/project-layouts :: add-member-checkbox-list"`.
- [x] **6.6** Convert `project-layouts.html` → `project-list-table` fragment:
  - `v-for="project in projects"` → `th:each="project : ${projects}"`.
  - All `{{...}}` → `th:text`.
  - All `:id` → `th:id`.
  - All `:class` → `th:class` / `th:classappend`.
  - `@click` on expand button → Alpine `x-data="{ expanded: false }"`, `:class`, `@click`.
  - Add-task form toggle → Alpine `x-data="{ taskFormOpen: false }"`, `x-show`, `@click`.
  - Add-member list toggle → Alpine `x-data` + `hx-get="/user/fragment/connection-list?project=${project.projectId}"` on button click to load the checkbox list.
  - Task list → `th:each="task : ${project.tasks}"` (tasks loaded server-side per project in the fragment endpoint).
  - Delete task button → `hx-post="/project/task/delete/${task.taskId}"`.
  - Add task submit → `hx-post="/project/add-project-task"` and include the form fields.
  - Delete project button → `hx-post="/project/delete?project=${project.projectId}"`.
  - Add member checkbox → `hx-post="/project/addProjectMember"` with project id + username.
  - Remove member button → `hx-post="/project/removeProjectMember"` with project id + username.
- [x] **6.7** Create `project-count-info`, `project-task-list`, `project-member-list`, `add-member-checkbox-list` as named fragments in `project-layouts.html`.
- [x] **6.8** In `dashboard-script.js` `displayViewingModal()`: add `htmx.ajax('GET', '/project/fragment/list', {target: '#projects-holder-container', swap: 'innerHTML'})` after modal visibility is set, and similarly for the count area.
- [x] **6.9** Add `HX-Trigger` response headers to mutation endpoints to fire `toggleNotification`.
- [x] **6.10** Remove `<script src="/javascript/vue-scripts/dashboard-project-vue.js">` from `project-layouts.html`.
- [x] **6.11** Delete `static/javascript/vue-scripts/dashboard-project-vue.js`.
- [x] **6.12** Delete `static/javascript/vue-script.js` (root-level old file).
- [ ] **6.13** Test all project modal scenarios (see testing checklist in `migration-guide.md`).

---

## Phase 7 — Agile Board

**New backend endpoints:**
- `GET /project/fragment/board-projects` → `board-layouts :: project-selector-options`
- `GET /project/fragment/board-tasks?project=<id>` → `board-layouts :: board-columns`
- `POST /project/task/update/status?status=&task=&project=` → accepts `project` param and returns `board-layouts :: board-columns`.
- `GET /project/fragment/board-assign?user=&task=&project=` → returns updated task card or board-columns.

- [x] **7.1** Add `boardProjectsFragment(Authentication auth, Model model)` to `ProjectController`: return project `<option>` elements fragment.
- [x] **7.2** Add `boardTasksFragment(@RequestParam long project, Authentication auth, Model model)` to `ProjectController`: query tasks, pass `projectId` and task lists by status to model, return `"user/board/board-layouts :: board-columns"`.
- [x] **7.3** `updateProjectTaskStatus` accepts optional `project` param; status changes are allowed only when the authenticated user is the task assignee and remains authorized for its project. Return the board fragment when requested and trigger a notification.
- [x] **7.4** Convert `board-layouts.html` `board-container` fragment:
  - Project selector `<select>`: add `hx-get="/project/fragment/board-tasks"`, `hx-trigger="change"`, `hx-include="this"` (the select must have `name="project"`), `hx-target="#board-columns"`.
  - Add `hx-get="/project/fragment/board-projects"` on the select with `hx-trigger="load"` to populate options.
  - On project selection, set cookie: `hx-on::after-request="document.cookie='agile-board-project='+this.value+';path=/';"`.
  - Loader: add `htmx-indicator` class to the spinner element; add `hx-indicator="#loader"` to triggers.
  - Full-screen button: Alpine `x-data="{ fs: false }"` on outer container, `@click="fs = !fs"`, `:class` bindings.
- [x] **7.5** Convert `board-layouts.html` `task-stage-categories` fragment → rename to `board-columns`:
  - `v-for` on the outer template → `th:each` on a wrapper or use separate server-rendered sections per status (simpler: three `th:each` sections each filtering by status server-side: `th:if="${task.status == 'not started'}"`).
  - `v-if` conditions on task cards → `th:each` with `th:if`.
  - `{{task.xxx}}` → `th:text`.
  - `:id` → `th:id`.
  - Alpine `x-data` on each column `<div>` for drag-and-drop:
    ```html
    x-data="{ highlight: false }"
    @dragover.prevent="highlight = true"
    @dragleave="highlight = false"
    @drop.prevent="onDrop($event, 'not started')"
    :class="highlight ? 'highlighted' : ''"
    ```
  - Alpine `x-init` on the board container to define `onDrop`:
    ```html
    x-data="{ onDrop(e, status) {
      const taskId = e.dataTransfer.getData('taskId');
      this.highlight = false;
      htmx.ajax('POST', '/project/task/update/status?status='+status+'&task='+taskId+'&project=${selectedProjectId}',
        {target:'#board-columns', swap:'innerHTML'});
    }}"
    ```
  - Task card drag start: only enable dragging when `task.assigneeId == currentUserId`; backend independently enforces the assignee and project-membership checks.
  - Assign dropdown: render members server-side in the board-tasks fragment (pass members per project). On select change: `hx-post="/project/task/update/assignee"` with user + task ids.
- [x] **7.6** In `ProjectController.serveBoard()`, read cookie `agile-board-project` and add `selectedProjectId` to model (or null if not set).
- [x] **7.7** In `board-layouts.html`, if `selectedProjectId` is not null, add `hx-trigger="load"` with `hx-get="/project/fragment/board-tasks?project=${selectedProjectId}"` on `#board-columns` to auto-load on page open.
- [x] **7.8** Remove `<script src="/javascript/vue-scripts/agile-board-vue.js">` from `board.html`.
- [x] **7.9** Delete `static/javascript/vue-scripts/agile-board-vue.js`.
- [ ] **7.10** Test all board scenarios (see testing checklist).

---

## Phase 8 — Final Cleanup

- [x] **8.1** Remove Vue CDN `<script>` tag from `user-layouts.html`.
- [x] **8.2** Remove Vue CDN `<script src="/javascript/vue.global.js.js">` from `mainLayouts.html`.
- [x] **8.3** Delete `static/javascript/vue.global.js.js`.
- [x] **8.4** Delete `static/javascript/vue3.3.4.js`.
- [x] **8.5** Delete `static/javascript/vue-scripts/collection-vue.js` (empty/unused).
- [x] **8.6** Delete `static/javascript/vue-scripts/charts-main-vue.js` (commented out in template — also remove the commented-out HTML block from `dashboard-content-layout.html`).
- [ ] **8.7** Run the full testing checklist from `migration-guide.md` Section 10.
- [ ] **8.8** Check browser console for zero JS errors on each page.
- [ ] **8.9** Confirm no visible `{{...}}` text in the rendered pages.

---

## Notes / Decisions

| Date | Note |
|---|---|
| Migration complete | State-changing routes in `ProjectController`, `UserDashboardController`, `TodoListCollectionController`, and `TodoController` use POST. HTMX includes Spring Security's CSRF header from the token metadata in `user-layouts.html`; read-only routes remain GET. |
| Pre-migration | JSON endpoints stay; new HTML fragment endpoints are additive. Do not delete existing JSON endpoints. |
| Pre-migration | `localStorage` → **cookie** for board project selection. Cookie name: `agile-board-project`. Path `/`. Agreed by user. |
| Pre-migration | HTMX + Alpine loaded from CDN, no build step, consistent with existing approach. |
| Pre-migration | Notification bar (`toggleNotification`) kept as vanilla JS; hooked into HTMX via `htmx:afterRequest` event and `HX-Trigger` response headers. |
| Pre-migration | Todo section **is included** in this migration for consistency. |
| Pre-migration | Blog: expand/collapse → Alpine; full-content fetch endpoint is a stub — leave `hx-get` placeholder with comment, do not break existing behaviour. |
| Pre-migration | DB: PostgreSQL at `127.0.0.1:5432`, db `projectory-db`, user `postgres`, pass `pg123`. Start with `./mvnw spring-boot:run`. Test at `http://localhost:8080`. |
