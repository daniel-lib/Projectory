# Agent Handoff

## Read first
- `docs/project-reference.md` — full app inventory (features, APIs, templates, JS files)
- `docs/migration-guide.md` — how-to for every feature area (HTMX patterns, backend changes needed)
- `docs/migration-plan.md` — phase checklist; tick items `[x]` as you complete them

## Stack
Spring Boot 2.7.12 + Thymeleaf + PostgreSQL. HTMX 1.9.12 and Alpine.js 3.14.1 are already added to `user-layouts.html`. Run with `./mvnw spring-boot:run`. App at `http://localhost:8080`.

## What is done
Phases 0–4 complete (all marked `[x]` in migration-plan.md):
- HTMX + Alpine added to layouts
- Blog expand/collapse → Alpine (`blog.html`)
- Navbar search → HTMX (`SearchController.searchFragment`, `templates/fragments/search-results.html`)
- Todo section → HTMX (`TodoController` + `TodoListCollectionController` have fragment endpoints; `todoLayouts.html` rewritten)
- Dashboard board-peek card → HTMX (`ProjectController.boardPeekFragment`, `board-peek-content` fragment in `project-layouts.html`)

## What remains
Phases 5–8 — in order:
1. **Phase 5** — User profile page (`user-detail-vue.js` → Thymeleaf + HTMX)
2. **Phase 6** — Project modal (`dashboard-project-vue.js` → HTMX + Alpine; most complex)
3. **Phase 7** — Agile board (`agile-board-vue.js` → HTMX + Alpine; drag-and-drop via Alpine)
4. **Phase 8** — Remove Vue CDN tags, delete remaining dead Vue files

## Key rules
- All mutations use `GET` — do not change to POST during this migration
- JSON endpoints stay; add new HTML fragment endpoints alongside them
- Board project selection: use cookie `agile-board-project` (not localStorage)
- Notifications: keep `toggleNotification()` in `mainScript.js`; fire it via `HX-Trigger` response header
- Compile check: `./mvnw compile --no-transfer-progress` after each phase
