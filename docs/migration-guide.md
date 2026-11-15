# Vue.js → HTMX + Alpine.js Migration Guide

> **Purpose:** Step-by-step instructions for an AI agent (or developer) performing the migration.
> Read `project-reference.md` first to understand the existing code before touching anything.

---

## 1. Why This Migration

The current Vue 3 usage is non-standard: Vue is loaded as a CDN global, apps are mounted on arbitrary DOM elements, and each page fragment can include its own `<script>` tag that creates a new Vue app. There is no build step, no components, no SFCs — it is essentially jQuery-style DOM manipulation but wrapped in Vue's reactivity system. The result is fragile, hard to debug, and an uncomfortable combination with Thymeleaf's server-side rendering.

**HTMX** fits this stack perfectly: the server renders HTML, and HTMX swaps fragments of that HTML in response to user actions. No JSON → JS → DOM pipeline. No client-side state management. The server is the source of truth.

**Alpine.js** handles the remaining pure-client concerns that HTMX cannot:
- UI state that does not require a server round trip (expand/collapse, show/hide, drag-and-drop highlight).
- The Kanban drag-and-drop (the status update still goes to the server via HTMX; Alpine handles the drag events client-side before triggering the HTMX request).

---

## 2. Tool Decisions

### HTMX — primary tool
- Replace every `fetch()` call that loads or mutates server data.
- Attributes like `hx-get`, `hx-post`, `hx-swap`, `hx-target` go directly in the HTML.
- The server controllers will need new endpoints that return **HTML fragments** instead of JSON for the cases where HTMX triggers a re-render.

### Alpine.js — secondary tool (UI-only state)
Use Alpine only when behaviour is purely client-side:
- Expand/collapse animations (project boxes, add-task forms).
- Drag-and-drop visual feedback (highlight drop zones, track dragged task id).
- Show/hide toggling that is instant (no server round trip).
- The blog expand/collapse.

### Vanilla JS — keep for existing utilities
Keep `mainScript.js`, `dashboard-script.js`, `todoListScript.js`, `project-script.js` etc. untouched. Only remove the Vue script tags and files.

---

## 3. Backend Changes Required

> Existing JSON read endpoints can stay as-is for future API consumers. HTMX fragment reads use GET; state-changing actions use POST and include Spring Security's CSRF header.
> Add new Thymeleaf-fragment-returning endpoints alongside the existing JSON ones. Do not delete the JSON endpoints.

### New controller responsibilities

For each section below, the controller method must:
1. Accept the same parameters as the existing JSON endpoint.
2. Build the model.
3. Return a Thymeleaf template path + fragment name rather than `@ResponseBody`.

The fragment HTML files should live in the same template directories as the existing layouts.

**New endpoints to add:**

| Feature | New endpoint | Returns fragment |
|---|---|---|
| Project list (reload) | `GET /project/fragment/list` | `project-layouts :: project-list-table` |
| Project count | `GET /project/fragment/count` | `project-layouts :: project-count-info` |
| Project tasks (per project) | `GET /project/fragment/tasks?project=<id>` | `project-layouts :: project-task-list` |
| Project members (per project) | `GET /project/fragment/members?project=<id>` | `project-layouts :: project-member-list` |
| Add project (submit) | `POST /project/add-project?...` | refresh project list and count |
| Delete project | `POST /project/delete?project=<id>` | refresh project list and count |
| Add member | `POST /project/addProjectMember?...` | refresh project members |
| Remove member | `POST /project/removeProjectMember?...` | refresh project members |
| Add task | `POST /project/add-project-task?...` | refresh project tasks |
| Delete task | `POST /project/task/delete/<id>` | refresh project tasks |
| Add/delete todo item | `POST /todo/fragment/add-item` and `POST /todo/fragment/delete-item` | refresh the collection's item list |
| Add todo collection | `POST /todo-list-collection/add-collection` | refresh the collection list |
| Connection actions | `POST /user/connection/...` | refresh connection state |
| Agile board project list | `GET /project/fragment/board-projects` | `board-layouts :: project-selector` |
| Agile board tasks | `GET /project/fragment/board-tasks?project=<id>` | `board-layouts :: board-columns` |
| Update task status (drop) | `POST /project/task/update/status?...` | swap `board-layouts :: board-columns` for that project |
| Assign task | `POST /project/task/update/assignee?...` | swap the specific task card |
| User detail | `GET /user/fragment/detail` | `profile-layouts :: profile-top-user-info` |
| User projects | `GET /user/fragment/projects?username=` | `profile-layouts :: user-project-list` |
| Connections list | `GET /user/fragment/connections?username=` | `profile-layouts :: connections-tabs-content` |
| Sent requests | `GET /user/fragment/connections/sent` | `profile-layouts :: sentConnectionRequest` |
| Received requests | `GET /user/fragment/connections/received` | `profile-layouts :: receivedConnectionRequest` |
| Search results | `GET /search/fragment/all/<keyword>` | a new `search-results` fragment |
| Connection actions (send/cancel/accept/reject/unfriend) | `POST /user/connection/...` — HTMX triggers them and refreshes the connection fragments | |

> **Important:** Connection status buttons on the profile page (Connect / Cancel / Accept / Reject / Unfriend) use POST and refresh only the button group area, not the whole page. The action endpoints continue returning integers and emit HTMX events to refresh connection fragments.

---

## 4. Migration Plan by Feature Area

### 4.1 Navbar Search
**Current:** `search-vue.js` mounts on `#search-bar`, listens `@keyup`, fetches JSON, renders `<li>` items via `v-for`.

**After migration:**
1. Add `hx-get="/search/fragment/all/{keyword}"` on the search input (use `hx-trigger="keyup changed delay:300ms"` to debounce).
2. Create `GET /search/fragment/all/{keyword}` controller method returning a Thymeleaf fragment.
3. Fragment file: `search-results.html` (new file) containing `<li>` items in a `th:fragment="search-results"`.
4. HTMX target: `#search-dropdown-results` (a `<ul>` inside the existing dropdown).
5. Remove `search-vue.js` script tag from `user-layouts.html`.
6. Alpine.js: not needed for search.

**Thymeleaf model attributes needed:** `List<SearchResultDto> searchResults`

---

### 4.2 Dashboard — Tasks-on-Board Peek Card
**Current:** `dashboard-vue.js` mounts on `#projectTaskOnBoardPeekView`, reads `localStorage`, fetches JSON.

**After migration:**
1. The selected board project is now tracked via a cookie or URL parameter rather than `localStorage`. Recommend a simple cookie `agile-board-project` set by the board page when user selects a project.
2. Add a controller method `GET /project/fragment/board-peek` that reads the cookie from `HttpServletRequest` and returns a Thymeleaf fragment with the task list.
3. On dashboard load, the card uses `hx-get="/project/fragment/board-peek"` with `hx-trigger="load"` to populate itself.
4. Remove `dashboard-vue.js` script tag.

**Template change:** `project-layouts.html` — `project-task-dashboard-card` fragment:
- Remove `v-if`, `v-for`, `{{...}}` Vue expressions.
- Add an `hx-get` to a `<div id="board-peek-content">` placeholder.
- The returned fragment renders the task list items server-side.

---

### 4.3 Project Modal (most complex)
**Current:** `dashboard-project-vue.js` — a large Vue app handling project list, project CRUD, member management, task management.

**After migration strategy:**
The modal currently has a single Vue app doing everything. With HTMX, each action targets its own fragment.

**Step-by-step:**

1. **Initial load** — when the modal opens (`displayViewingModal(1)` is called), trigger `hx-get="/project/fragment/list"` to load the project list into `#projects-holder-container`. Use HTMX's JS API: `htmx.ajax('GET', '/project/fragment/list', '#projects-holder-container')` called from inside `displayViewingModal()`.

2. **Project count area** — a separate `<div id="project-count-info">` loads from `GET /project/fragment/count`. Load it at the same time as the project list (two `htmx.ajax` calls, or one endpoint that returns both wrapped in a fragment).

3. **Add project form** — `hx-post="/project/add-project"` with a CSRF header. After submission, refresh the project list and count.

4. **Delete project** — delete button uses `hx-post="/project/delete?project=${projectId}"`; refresh the project list and count.

5. **Members dropdown per project** — the member count button: `hx-get="/project/fragment/members?project=${projectId}"`, `hx-target="#member-list-${projectId}"`, `hx-trigger="click"`. Alpine `x-data` on the button group for show/hide toggling of the dropdown.

6. **Add member** — checkbox uses `hx-post="/project/addProjectMember?project=${projectId}&user=${username}"`. Disabling the checkbox during request: use Alpine `x-data="{ loading: false }"` and `@click="loading = true"` with `:disabled="loading"`.

7. **Remove member** — same pattern as add member.

8. **Connections list (for add-member dropdown)** — when the add-member button is clicked: `hx-get="/user/getUserConnectionList"` → but this returns JSON currently. Add a new Thymeleaf endpoint `GET /user/fragment/connection-list` that accepts a `projectId` param and renders the checkbox list (filtering out existing members server-side). Target `#add-member-list-container-${projectId}`.

9. **Add task form** — toggle with Alpine `x-data="{ open: false }"` on the form wrapper; `x-show="open"` on the form div; `@click="open = !open"` on the button. No server call for open/close.

10. **Submit add task** — submit the form with `hx-post="/project/add-project-task"`; the CSRF header is added globally.

11. **Delete task** — use `hx-post="/project/task/delete/${taskId}"`.

12. **Expand/collapse project box** — Alpine only: `x-data="{ expanded: false }"`, `:class="expanded ? 'project-modal-boxes-expanded' : 'project-modal-boxes-default'"`, `@click="expanded = !expanded"` on the expand button.

---

### 4.4 Agile Board
**Current:** `agile-board-vue.js` — project selector, tasks, drag-and-drop.

**After migration strategy:**

1. **Project selector** — `<select>` with `hx-get="/project/fragment/board-tasks"`, `hx-target="#board-columns"`, `hx-trigger="change"`, `hx-include="this"` (passes the selected project value). The server renders all three Kanban columns with tasks filtered by status.

2. **Initial load** — on page load, check for cookie `agile-board-project`; if present, use `hx-trigger="load"` on the board columns container to auto-load the saved project.

3. **Setting the cookie** — when user selects a project from the dropdown, a small Alpine or vanilla JS snippet sets `document.cookie = 'agile-board-project=<id>'` in an `hx-on::after-request` attribute.

4. **Drag-and-drop** — this is the hardest part. Alpine.js handles the drag events. Only the task assignee should be allowed to drag a card; render other tasks as non-draggable. The backend must independently verify that `project_tasks.assignee` matches the authenticated user and that the user remains authorized for the project, so unassigned tasks and tasks assigned to someone else cannot be moved by crafting a request:
   ```html
   <!-- Drop zone (column) -->
   <div x-data="kanbanColumn()"
        @dragover.prevent="highlight = true"
        @dragleave="highlight = true"
        @drop.prevent="onDrop($event, 'in progress')">
   ```
   ```js
   // Alpine component
   function kanbanColumn() {
     return {
       highlight: false,
       onDrop(event, newStatus) {
         const taskId = event.dataTransfer.getData('taskId');
         this.highlight = false;
         // trigger HTMX request
         htmx.ajax('POST',
           `/project/task/update/status?status=${newStatus}&task=${taskId}`,
           { target: '#board-columns', swap: 'innerHTML' }
         );
       }
     }
   }
   ```
   ```html
   <!-- Enable dragging only when the current user is the task assignee. -->
   <div th:attr="draggable=${task.assigneeId == currentUserId}"
        @dragstart="$event.dataTransfer.setData('taskId', '${task.taskId}')">
   ```

5. **Assign task** — the assign dropdown renders members server-side. On select change it uses `hx-post="/project/task/update/assignee"` and swaps the board fragment.

6. **Full-screen mode** — Alpine only: `x-data="{ fullscreen: false }"`, toggle CSS classes. No server call needed.

7. **Remove `agile-board-vue.js` script tag** from `board.html`.

---

### 4.5 Profile Page
**Current:** `user-detail-vue.js` — large Vue app, mounts on `#user-details`. Thymeleaf passes `ownAccount` and `profileUsernameValue` via inline `<script>` tags.

**After migration:**

1. **User info (name, avatar, online status)** — the server already knows who is being viewed (it's a path variable). Just render this in Thymeleaf directly on page load. No HTMX needed. The controller already has `auth` and `username` from the path; pass `PublicUserPersonalDetailDto` to the model and render it.

2. **Connection action buttons (Connect / Cancel / Accept / Reject / Unfriend)** — these depend on live connection state. Strategy:
   - Render a placeholder `<div id="connection-action-buttons">` on page load.
   - Use `hx-get="/user/fragment/connection-status?user=${viewedUserId}"`, `hx-trigger="load"` to populate it with the correct button for the current state.
   - Each button uses `hx-post="<action endpoint>"`, `hx-target="#connection-action-buttons"`. After the action, HTMX events refresh the connection state.
   - This eliminates the need for `connectionStatus`, `connectionSenderReceiverId` state.

3. **User's public projects list** — render server-side in the initial page load (the controller already queries this). Add `hx-get="/user/fragment/projects/${username}"`, `hx-trigger="load"` if you want lazy loading, or just pass the list to the model.

4. **Connections tabs** — render all three tabs server-side:
   - Approved: query on page load.
   - Sent/Received: either load on tab click via `hx-get` with `hx-trigger="click"` on the tab button, or render on page load (simpler).
   - Unfriend / cancel / accept / reject buttons inside connection items use POST and HTMX events refresh the lists.

5. **Profile picture fallback** — this logic was in Vue (`conn.profilePicture.length == 1`). Move it to Thymeleaf: `th:if="${#strings.length(user.profilePicture) == 1}"`.

6. **Remove `user-detail-vue.js` script tag and inline `<script>` with `ownAccount` / `profileUsernameValue`** from `profile.html`.

---

### 4.6 Blog
**Current:** `blog-vue.js` — expand/collapse card, fetch full body.

**After migration:**
- Expand/collapse: Alpine `x-data="{ expanded: false }"`, `x-show` / class toggle.
- Load full content on expand: `hx-get="/blog/fragment/content/${blogId}"`, `hx-trigger="click"`, `hx-target="#blog-full-content-${blogId}"`. (The `/link` endpoint is a stub anyway; create a proper one when blog content is implemented.)
- Remove `blog-vue.js`.

---

### 4.7 Todo
Todo collection and item creation/deletion use HTMX POST requests; bulk deletion also uses POST. CSRF is supplied by the shared HTMX request hook. Read-only collection and item fragments continue to use GET.

---

## 5. Adding HTMX and Alpine.js to the Project

### Where to add the script tags
Both scripts go into `user-layouts.html` inside the `<head th:fragment="head">` section, alongside the existing Bootstrap and Chart.js imports. Use CDN links (no build step required).

```html
<!-- HTMX -->
<script src="https://unpkg.com/htmx.org@1.9.12" 
        integrity="sha384-..." crossorigin="anonymous"></script>

<!-- Alpine.js -->
<script defer src="https://cdn.jsdelivr.net/npm/alpinejs@3.14.1/dist/cdn.min.js"></script>
```

> Alpine **must** be loaded with `defer` and before the page body is parsed. HTMX does not need `defer`.

### CSRF
Spring Security CSRF protection is enabled. `user-layouts.html` exposes the token and header name in meta tags, and `mainScript.js` adds the header to every HTMX request:
```html
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
```
The global request hook is:
```js
document.addEventListener('htmx:configRequest', (event) => {
  const token = document.querySelector('meta[name="_csrf"]');
  const header = document.querySelector('meta[name="_csrf_header"]');
  if (token && header) {
    event.detail.headers[header.content] = token.content;
  }
});
```

### Remove Vue
Remove from `user-layouts.html`:
```html
<!-- Remove this -->
<script src="https://unpkg.com/vue@3/dist/vue.global.prod.js"></script>
```
Also remove from `mainLayouts.html`:
```html
<!-- Remove this -->
<script src="/javascript/vue.global.js.js"></script>
```
Delete from `static/javascript/`:
- `vue-script.js` (root-level old file)
- `vue-scripts/collection-vue.js` (empty)
- `vue.global.js.js` (local Vue bundle)
- `vue3.3.4.js` (local Vue bundle)
- Each `vue-scripts/*.js` file as its feature is migrated.

---

## 6. HTMX Patterns to Know

### Loading a fragment on page load
```html
<div hx-get="/project/fragment/list"
     hx-trigger="load"
     hx-swap="innerHTML">
  <!-- Loading spinner or placeholder here -->
</div>
```

### Swapping after a mutation
```html
<button hx-post="/project/delete?project=5"
        hx-target="#projects-holder-container"
        hx-swap="innerHTML">
  Delete
</button>
```

### Debounced live search
```html
<input type="text"
       hx-get="/search/fragment/all"
       hx-trigger="keyup changed delay:300ms"
       name="keyword"
       hx-target="#search-results"
       hx-swap="innerHTML">
```

### Form submission
```html
<form hx-post="/project/add-project" method="post"
      hx-target="#projects-holder-container"
      hx-swap="innerHTML"
      hx-on::after-request="this.reset()">
  ...
</form>
```

### Show notification after any HTMX request
Add a global listener in `mainScript.js`:
```js
document.addEventListener('htmx:afterRequest', function(e) {
  const header = e.detail.xhr.getResponseHeader('HX-Trigger');
  if (header) {
    const triggers = JSON.parse(header);
    if (triggers.notify) {
      toggleNotification(triggers.notify.type, triggers.notify.message);
    }
  }
});
```
In controller methods, return notifications via the `HX-Trigger` response header:
```java
response.setHeader("HX-Trigger", "{\"notify\":{\"type\":\"success\",\"message\":\"Project deleted!\"}}");
```

### HTMX loading indicator
Use the `htmx-indicator` class:
```html
<div id="loader" class="loader-container htmx-indicator">...</div>
<button hx-get="..." hx-indicator="#loader">Action</button>
```

### Programmatic HTMX trigger (from JS, e.g. from `displayViewingModal`)
```js
htmx.ajax('GET', '/project/fragment/list', {target: '#projects-holder-container', swap: 'innerHTML'});
```

---

## 7. Alpine.js Patterns to Know

### Toggle open/close
```html
<div x-data="{ open: false }">
  <button @click="open = !open">Toggle</button>
  <div x-show="open" x-transition>Content</div>
</div>
```

### Conditional classes
```html
<div :class="expanded ? 'project-modal-boxes-expanded' : 'project-modal-boxes-default'">
```

### Disabling a button during async
```html
<div x-data="{ loading: false }">
  <button :disabled="loading"
          @click="loading = true"
          hx-get="/some/endpoint"
          hx-on::after-request="loading = false">
    Submit
  </button>
</div>
```

### Drag and drop (Kanban)
See Section 4.4 above.

### Collapse all siblings (like the project member dropdowns)
Alpine does not have a built-in "close all siblings" mechanism. Use a lightweight approach: a shared reactive store via `Alpine.store('board', { openDropdown: null })` and check `$store.board.openDropdown === projectId` to determine if a dropdown is open.
```js
document.addEventListener('alpine:init', () => {
  Alpine.store('projectDropdowns', { open: null });
});
```
```html
<div x-data
     @click="$store.projectDropdowns.open = $store.projectDropdowns.open === '${projectId}' ? null : '${projectId}'">
  <div x-show="$store.projectDropdowns.open === '${projectId}'">...</div>
</div>
```

---

## 8. Migration Sequence (recommended order)

Do these in order to minimise broken states between steps:

1. **Add HTMX + Alpine to `user-layouts.html`** (they are inert until used).
2. **Navbar search** — isolated, contained, easy win. Migrate `search-vue.js`.
3. **Tasks-on-board peek card** — simple single-fetch. Migrate `dashboard-vue.js`.
4. **Blog expand/collapse** — purely Alpine, no backend change needed.
5. **Profile page** — migrate `user-detail-vue.js`. Requires new Thymeleaf endpoints for connection-status fragment and connection-tabs content.
6. **Project modal** — largest and most complex. Migrate `dashboard-project-vue.js`. Requires several new fragment endpoints.
7. **Agile board** — complex due to drag-and-drop. Migrate `agile-board-vue.js`. Requires new fragment endpoints.
8. **Remove Vue CDN tag** from `user-layouts.html` once all 7 areas are migrated.
9. **Delete dead Vue files** (`vue-script.js`, `collection-vue.js`, `vue.global.js.js`, `vue3.3.4.js`, then each `vue-scripts/*.js` as its area is done).

---

## 9. What Changes Where — File-by-File Summary

| File | Action |
|---|---|
| `user-layouts.html` | Add HTMX + Alpine `<script>` tags; remove Vue CDN tag; remove `search-vue.js` `<script>` tag |
| `project-layouts.html` | Replace all `v-for`, `v-if`, `{{...}}`, `@click`, `:id` etc. with `th:each`, `th:if`, `th:text`, `hx-*`, `x-*` |
| `board.html` | Remove `agile-board-vue.js` script tag |
| `board-layouts.html` | Replace Vue directives with HTMX + Alpine; keep drag/drop events using Alpine |
| `profile.html` | Remove inline `<script>` with `ownAccount`/`profileUsernameValue`; remove `user-detail-vue.js` script tag |
| `profile-layouts.html` | Replace Vue directives with Thymeleaf + Alpine; connection buttons become HTMX |
| `dashboard-content-layout.html` | No Vue here currently; ensure board-peek card is HTMX `hx-trigger="load"` |
| `ProjectController.java` | Add fragment-returning `@GetMapping` methods |
| `UserDashboardController.java` | Add fragment-returning `@GetMapping` methods for profile and connections |
| `SearchController.java` | Add `GET /search/fragment/all/{keyword}` returning a Thymeleaf fragment |
| New: `src/main/resources/templates/fragments/search-results.html` | Search result `<li>` items fragment |
| New: `src/main/resources/templates/fragments/connection-status.html` | Connection action buttons fragment |
| `static/javascript/vue-scripts/*.js` | Delete each file as its feature is migrated |
| `static/javascript/vue-script.js` | Delete (old/unused) |
| `static/javascript/vue.global.js.js` | Delete |
| `static/javascript/vue3.3.4.js` | Delete |

---

## 10. Testing Checklist

Before calling the migration complete, verify each item:

- [ ] Navbar search returns results on keyup; dropdown updates without page reload.
- [ ] Dashboard loads; Tasks-on-Board card shows tasks for the last-selected project (cookie-based).
- [ ] Project modal opens and shows project list with member counts and task counts.
- [ ] Creating a project refreshes the list and count without closing the modal.
- [ ] Deleting a project refreshes the list; notification shown.
- [ ] Add task to project refreshes only that project's task list.
- [ ] Delete task from project refreshes only that project's task list.
- [ ] Add member shows the connection list; checking adds the member; member count updates.
- [ ] Remove member updates the member list.
- [ ] Agile board: selecting a project loads the three columns with tasks.
- [ ] Agile board: dragging a task card to a different column updates its status.
- [ ] Agile board: assigned task username visible; assigning a task via dropdown works.
- [ ] Agile board: full-screen button works (Alpine toggle).
- [ ] Agile board: selected project survives page refresh (cookie).
- [ ] Profile (own): name, avatar, online status, projects, connections all display.
- [ ] Profile (own): all three connection tabs load correctly.
- [ ] Profile (other): Connect button visible; clicking sends request; button changes to Cancel.
- [ ] Profile (other): Cancel request → button reverts to Connect.
- [ ] Profile (other): Accept / Reject received request works; list updates.
- [ ] Profile (other): Unfriend works; button reverts to Connect.
- [ ] No `{{...}}` or Vue template syntax visible in page source after migration.
- [ ] No JS errors in browser console related to Vue or missing variables.
- [ ] Notification bar (`toggleNotification`) still works for all actions.
- [ ] Theme switcher still works.
- [ ] Side menu expand/collapse still works.
- [ ] Dashboard modal open/close still works.
