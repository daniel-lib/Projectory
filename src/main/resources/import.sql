-- INSERT USERS
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (1, 'testuser1', 'testuser123', 'Test', 'User1', 'test1@user.com');
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (2, 'testuser2', 'testuser123', 'gebru', 'User2', 'gebru@user.com');
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (3, 'testuser3', 'testuser123', 'semhal', 'User3', 'semhal@user.com');

	
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1000, 'Project Management System', 'implement a web app to manage projects', 'Not started', 1);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1001, 'File Sharing Web App', 'implement a web app to share files', 'Not started', 1);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1002, 'Human Resource Management System', 'implement a web app to manage userloyees', 'Not started', 2);

-- INSERT PROJECTS
insert into PROJECT_MEMBERS (user_id, project_id) values (1,1002);
insert into PROJECT_MEMBERS (user_id, project_id) values (2,1000);
insert into PROJECT_MEMBERS (user_id, project_id) values (2,1001);
insert into PROJECT_MEMBERS (user_id, project_id) values (3,1000);
insert into PROJECT_MEMBERS (user_id, project_id) values (3,1001);
insert into PROJECT_MEMBERS (user_id, project_id) values (3,1002);

--INSERT PROJECT TASKS
insert into PROJECT_TASKS (TASK_ID, DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values (10, '', 'not started', '', 'user login feature', 1000);
insert into PROJECT_TASKS (TASK_ID, DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values (11, '', 'not started', '', 'change password feature', 1000);
insert into PROJECT_TASKS (TASK_ID, DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values (12, '', 'not started', '', 'project display feature', 1000);
insert into PROJECT_TASKS (TASK_ID, DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values (13, '', 'not started', '', 'task metrics feature', 1000);

insert into PROJECT_TASKS (TASK_ID, DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values (14, '', 'not started', '', 'file upload ui', 1001);

insert into PROJECT_TASKS (TASK_ID, DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values (15, '', 'not started', '', 'employee personal history form', 1002);
insert into PROJECT_TASKS (TASK_ID, DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values (16, '', 'not started', '', 'leave request form', 1002);


--INSERT Todo collections
insert into TODO_LIST_COLLECTION (TODO_COLLECTION_ID, COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE) values (1, 'Groceries', '01/07/2023', '01/07/2023');

insert into TODO_LIST_COLLECTION (TODO_COLLECTION_ID, COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE) values (2, 'today schedule', '01/07/2023', '01/07/2023');

--INSERT Todo list
insert into TODO (todo_item_id, title, COLLECTION_ID) values (1, 'onions', 1);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (2, 'water melon', 1);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (3, 'tomatoes', 1);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (4, '5 cloves of garlic', 1);

insert into TODO (todo_item_id, title, COLLECTION_ID) values (5, 'call hr', 2);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (6, 'talk to dave about chair', 2);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (7, 'get new mouse from inventory', 2);
