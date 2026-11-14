-- Seed data for development/testing.
-- All foreign key references use subqueries on natural keys (username, title) instead of hardcoded IDs.
-- ON CONFLICT DO NOTHING makes every insert idempotent — safe to re-run on startup.
-- After the first successful seed, set spring.sql.init.mode=never in application.properties.

-- Backfill the completion flag for databases created before todo completion was added.
ALTER TABLE IF EXISTS todo
  ADD COLUMN IF NOT EXISTS completed boolean NOT NULL DEFAULT false;

-- ── USERS ──────────────────────────────────────────────────────────────────
-- password for all accounts: 123
insert into USERS (username, password, first_name, last_name, email_address, enabled, role, profile_picture, online_status)
  values ('testuser1', '$2a$12$e.6kz/8eP/3OLMNc6CA1su.SzSff6piMX.nKiwHCnC3YOahoOhdv6', 'Test',  'User1',  'test1@user.com',    true, 'ROLE_USER', null, false)
  ON CONFLICT (username) DO NOTHING;

insert into USERS (username, password, first_name, last_name, email_address, enabled, role, profile_picture, online_status)
  values ('testuser2', '$2a$12$e.6kz/8eP/3OLMNc6CA1su.SzSff6piMX.nKiwHCnC3YOahoOhdv6', 'gebru',  'User2',  'gebru@user.com',    true, 'ROLE_USER', null, false)
  ON CONFLICT (username) DO NOTHING;

insert into USERS (username, password, first_name, last_name, email_address, enabled, role, profile_picture, online_status)
  values ('testuser3', '$2a$12$e.6kz/8eP/3OLMNc6CA1su.SzSff6piMX.nKiwHCnC3YOahoOhdv6', 'semhal', 'User3',  'semhal@user.com',   true, 'ROLE_USER', null, false)
  ON CONFLICT (username) DO NOTHING;

insert into USERS (username, password, first_name, last_name, email_address, enabled, role, profile_picture, online_status)
  values ('hena',      '$2a$12$e.6kz/8eP/3OLMNc6CA1su.SzSff6piMX.nKiwHCnC3YOahoOhdv6', 'henok',  'sertse', 'hena_s@user.com',   true, 'ROLE_USER', null, false)
  ON CONFLICT (username) DO NOTHING;

insert into USERS (username, password, first_name, last_name, email_address, enabled, role, profile_picture, online_status)
  values ('hawa2',     '$2a$12$e.6kz/8eP/3OLMNc6CA1su.SzSff6piMX.nKiwHCnC3YOahoOhdv6', 'hewan',  'john',   'hewi2@user.com',    true, 'ROLE_USER', null, false)
  ON CONFLICT (username) DO NOTHING;

insert into USERS (username, password, first_name, last_name, email_address, enabled, role, profile_picture, online_status)
  values ('markos55',  '$2a$10$Q4lmSuqzTrxsx1v2J8FrHuw0coBW85Z3idtylVHs6Ld.KElrcVO46', 'mark',   'os',     'mark_os2@user.com', true, 'ROLE_USER', null, false)
  ON CONFLICT (username) DO NOTHING;


-- ── CONNECTIONS ─────────────────────────────────────────────────────────────
insert into CONNECTIONS (REQUEST_SENDER_USER, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS)
  select (select user_id from users where username = 'testuser1'),
         (select user_id from users where username = 'testuser2'),
         '2023-07-01', 'approved'
  where not exists (
    select 1 from connections
    where request_sender_user   = (select user_id from users where username = 'testuser1')
      and request_receiver_user = (select user_id from users where username = 'testuser2')
  );

insert into CONNECTIONS (REQUEST_SENDER_USER, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS)
  select (select user_id from users where username = 'testuser1'),
         (select user_id from users where username = 'testuser3'),
         '2023-07-01', 'approved'
  where not exists (
    select 1 from connections
    where request_sender_user   = (select user_id from users where username = 'testuser1')
      and request_receiver_user = (select user_id from users where username = 'testuser3')
  );

insert into CONNECTIONS (REQUEST_SENDER_USER, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS)
  select (select user_id from users where username = 'testuser1'),
         (select user_id from users where username = 'hena'),
         '2023-07-01', 'approved'
  where not exists (
    select 1 from connections
    where request_sender_user   = (select user_id from users where username = 'testuser1')
      and request_receiver_user = (select user_id from users where username = 'hena')
  );

insert into CONNECTIONS (REQUEST_SENDER_USER, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS)
  select (select user_id from users where username = 'testuser1'),
         (select user_id from users where username = 'hawa2'),
         '2023-07-01', 'approved'
  where not exists (
    select 1 from connections
    where request_sender_user   = (select user_id from users where username = 'testuser1')
      and request_receiver_user = (select user_id from users where username = 'hawa2')
  );

insert into CONNECTIONS (REQUEST_SENDER_USER, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS)
  select (select user_id from users where username = 'testuser1'),
         (select user_id from users where username = 'markos55'),
         '2023-07-01', 'pending'
  where not exists (
    select 1 from connections
    where request_sender_user   = (select user_id from users where username = 'testuser1')
      and request_receiver_user = (select user_id from users where username = 'markos55')
  );


-- ── PROJECTS ────────────────────────────────────────────────────────────────
insert into project (title, description, status, PROJECT_OWNER_USER_ID)
  select 'Project Management System', 'implement a web app to manage projects', 'In Progress',
         (select user_id from users where username = 'testuser1')
  where not exists (select 1 from project where title = 'Project Management System');

insert into project (title, description, status, PROJECT_OWNER_USER_ID)
  select 'File Sharing Web App', 'implement a web app to share files', 'In Progress',
         (select user_id from users where username = 'testuser1')
  where not exists (select 1 from project where title = 'File Sharing Web App');

insert into project (title, description, status, PROJECT_OWNER_USER_ID)
  select 'Human Resource Management System', 'implement a web app to manage employees', 'Not started',
         (select user_id from users where username = 'testuser2')
  where not exists (select 1 from project where title = 'Human Resource Management System');

insert into project (title, description, status, PROJECT_OWNER_USER_ID)
  select 'Test Project 4', 'implement Test project 4', 'In Progress',
         (select user_id from users where username = 'testuser1')
  where not exists (select 1 from project where title = 'Test Project 4');

insert into project (title, description, status, PROJECT_OWNER_USER_ID)
  select 'Project for Testing 5', 'implement an API gateway', 'On Hold',
         (select user_id from users where username = 'testuser1')
  where not exists (select 1 from project where title = 'Project for Testing 5');

insert into project (title, description, status, PROJECT_OWNER_USER_ID)
  select 'Seeded Project for Testing 6', 'implement a business software', 'Not started',
         (select user_id from users where username = 'testuser2')
  where not exists (select 1 from project where title = 'Seeded Project for Testing 6');

insert into project (title, description, status, PROJECT_OWNER_USER_ID)
  select 'Yet Another Test Project', 'implement a web app', 'Completed',
         (select user_id from users where username = 'testuser1')
  where not exists (select 1 from project where title = 'Yet Another Test Project');


-- ── PROJECT MEMBERS ─────────────────────────────────────────────────────────
insert into PROJECT_MEMBERS (user_id, project_id)
  select (select user_id from users where username = 'testuser1'),
         (select project_id from project where title = 'Human Resource Management System')
  where not exists (
    select 1 from project_members
    where user_id    = (select user_id from users where username = 'testuser1')
      and project_id = (select project_id from project where title = 'Human Resource Management System')
  );

insert into PROJECT_MEMBERS (user_id, project_id)
  select (select user_id from users where username = 'testuser2'),
         (select project_id from project where title = 'Project Management System')
  where not exists (
    select 1 from project_members
    where user_id    = (select user_id from users where username = 'testuser2')
      and project_id = (select project_id from project where title = 'Project Management System')
  );

insert into PROJECT_MEMBERS (user_id, project_id)
  select (select user_id from users where username = 'testuser2'),
         (select project_id from project where title = 'File Sharing Web App')
  where not exists (
    select 1 from project_members
    where user_id    = (select user_id from users where username = 'testuser2')
      and project_id = (select project_id from project where title = 'File Sharing Web App')
  );

insert into PROJECT_MEMBERS (user_id, project_id)
  select (select user_id from users where username = 'testuser3'),
         (select project_id from project where title = 'Project Management System')
  where not exists (
    select 1 from project_members
    where user_id    = (select user_id from users where username = 'testuser3')
      and project_id = (select project_id from project where title = 'Project Management System')
  );

insert into PROJECT_MEMBERS (user_id, project_id)
  select (select user_id from users where username = 'testuser3'),
         (select project_id from project where title = 'File Sharing Web App')
  where not exists (
    select 1 from project_members
    where user_id    = (select user_id from users where username = 'testuser3')
      and project_id = (select project_id from project where title = 'File Sharing Web App')
  );

insert into PROJECT_MEMBERS (user_id, project_id)
  select (select user_id from users where username = 'testuser3'),
         (select project_id from project where title = 'Human Resource Management System')
  where not exists (
    select 1 from project_members
    where user_id    = (select user_id from users where username = 'testuser3')
      and project_id = (select project_id from project where title = 'Human Resource Management System')
  );


-- ── PROJECT TASKS ───────────────────────────────────────────────────────────
insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, ASSIGNEE, PROJECT_ID)
  select '', 'not started', '', 'user login feature', 0,
         (select project_id from project where title = 'Project Management System')
  where not exists (select 1 from project_tasks where task_name = 'user login feature'
    and project_id = (select project_id from project where title = 'Project Management System'));

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, ASSIGNEE, PROJECT_ID)
  select '', 'not started', '', 'change password feature', 0,
         (select project_id from project where title = 'Project Management System')
  where not exists (select 1 from project_tasks where task_name = 'change password feature'
    and project_id = (select project_id from project where title = 'Project Management System'));

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, ASSIGNEE, PROJECT_ID)
  select '', 'not started', '', 'project display feature', 0,
         (select project_id from project where title = 'Project Management System')
  where not exists (select 1 from project_tasks where task_name = 'project display feature'
    and project_id = (select project_id from project where title = 'Project Management System'));

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, ASSIGNEE, PROJECT_ID)
  select '', 'not started', '', 'task metrics feature', 0,
         (select project_id from project where title = 'Project Management System')
  where not exists (select 1 from project_tasks where task_name = 'task metrics feature'
    and project_id = (select project_id from project where title = 'Project Management System'));

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, ASSIGNEE, PROJECT_ID)
  select '', 'not started', '', 'file upload ui', 0,
         (select project_id from project where title = 'File Sharing Web App')
  where not exists (select 1 from project_tasks where task_name = 'file upload ui'
    and project_id = (select project_id from project where title = 'File Sharing Web App'));

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, ASSIGNEE, PROJECT_ID)
  select '', 'not started', '', 'employee personal history form', 0,
         (select project_id from project where title = 'Human Resource Management System')
  where not exists (select 1 from project_tasks where task_name = 'employee personal history form'
    and project_id = (select project_id from project where title = 'Human Resource Management System'));

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, ASSIGNEE, PROJECT_ID)
  select '', 'not started', '', 'leave request form', 0,
         (select project_id from project where title = 'Human Resource Management System')
  where not exists (select 1 from project_tasks where task_name = 'leave request form'
    and project_id = (select project_id from project where title = 'Human Resource Management System'));


-- ── TODO COLLECTIONS ────────────────────────────────────────────────────────
insert into TODO_LIST_COLLECTION (COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE, USER_ID)
  select 'Groceries', '2023-07-01', '2023-07-01',
         (select user_id from users where username = 'testuser1')
  where not exists (
    select 1 from todo_list_collection
    where collection_title = 'Groceries'
      and user_id = (select user_id from users where username = 'testuser1')
  );

insert into TODO_LIST_COLLECTION (COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE, USER_ID)
  select 'today schedule', '2023-07-01', '2023-07-01',
         (select user_id from users where username = 'testuser1')
  where not exists (
    select 1 from todo_list_collection
    where collection_title = 'today schedule'
      and user_id = (select user_id from users where username = 'testuser1')
  );

insert into TODO_LIST_COLLECTION (COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE, USER_ID)
  select 'for the hr project', '2023-07-01', '2023-07-01',
         (select user_id from users where username = 'testuser1')
  where not exists (
    select 1 from todo_list_collection
    where collection_title = 'for the hr project'
      and user_id = (select user_id from users where username = 'testuser1')
  );


-- ── TODO ITEMS ──────────────────────────────────────────────────────────────
insert into TODO (title, COLLECTION_ID)
  select 'onions', (select todo_collection_id from todo_list_collection
    where collection_title = 'Groceries'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'onions'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'Groceries'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'water melon', (select todo_collection_id from todo_list_collection
    where collection_title = 'Groceries'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'water melon'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'Groceries'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'tomatoes', (select todo_collection_id from todo_list_collection
    where collection_title = 'Groceries'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'tomatoes'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'Groceries'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select '5 cloves of garlic', (select todo_collection_id from todo_list_collection
    where collection_title = 'Groceries'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = '5 cloves of garlic'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'Groceries'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'call hr', (select todo_collection_id from todo_list_collection
    where collection_title = 'today schedule'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'call hr'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'today schedule'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'talk to dave about chair', (select todo_collection_id from todo_list_collection
    where collection_title = 'today schedule'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'talk to dave about chair'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'today schedule'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'get new mouse from inventory', (select todo_collection_id from todo_list_collection
    where collection_title = 'today schedule'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'get new mouse from inventory'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'today schedule'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'find form validation react component', (select todo_collection_id from todo_list_collection
    where collection_title = 'for the hr project'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'find form validation react component'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'for the hr project'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'get employee required information for registration from hr',
         (select todo_collection_id from todo_list_collection
           where collection_title = 'for the hr project'
             and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo
    where title = 'get employee required information for registration from hr'
      and collection_id = (select todo_collection_id from todo_list_collection
        where collection_title = 'for the hr project'
          and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'get employee data policy from hr', (select todo_collection_id from todo_list_collection
    where collection_title = 'for the hr project'
      and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo where title = 'get employee data policy from hr'
    and collection_id = (select todo_collection_id from todo_list_collection
      where collection_title = 'for the hr project'
        and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'prepare value proposition canvas presentation',
         (select todo_collection_id from todo_list_collection
           where collection_title = 'for the hr project'
             and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo
    where title = 'prepare value proposition canvas presentation'
      and collection_id = (select todo_collection_id from todo_list_collection
        where collection_title = 'for the hr project'
          and user_id = (select user_id from users where username = 'testuser1')));

insert into TODO (title, COLLECTION_ID)
  select 'how many employees? ask hr.',
         (select todo_collection_id from todo_list_collection
           where collection_title = 'for the hr project'
             and user_id = (select user_id from users where username = 'testuser1'))
  where not exists (select 1 from todo
    where title = 'how many employees? ask hr.'
      and collection_id = (select todo_collection_id from todo_list_collection
        where collection_title = 'for the hr project'
          and user_id = (select user_id from users where username = 'testuser1')));
