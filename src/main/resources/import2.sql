-- INSERT USERS
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (1, 'testuser1', 'testuser123', 'Test', 'User1', 'test1@user.com');
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (2, 'testuser2', 'testuser123', 'gebru', 'User2', 'gebru@user.com');
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (3, 'testuser3', 'testuser123', 'semhal', 'User3', 'semhal@user.com');
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (4, 'hena', '123456', 'henok', 'sertse', 'hena_s@user.com');
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (5, 'hawa2', 'pasw1', 'hewan', 'john', 'hewi2@user.com');
insert into USERS (user_id, username, password, first_name, last_name, email_address) values (6, 'markos55', 'inthe24', 'mark', 'os', 'mark_os2@user.com');

-- INSERT CONNECTIONS
insert into CONNECTIONS (CONNECTION_ID, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (1, 2, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (CONNECTION_ID, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (2, 3, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (CONNECTION_ID, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (3, 4, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (CONNECTION_ID, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (4, 5, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (CONNECTION_ID, REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (5, 6, '01/07/2023', 'pending', 1);
	
-- INSERT PROJECTS	
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1000, 'Project Management System', 'implement a web app to manage projects', 'In Progress', 1);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1001, 'File Sharing Web App', 'implement a web app to share files', 'In Progress', 1);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1002, 'Human Resource Management System', 'implement a web app to manage employees', 'Not started', 2);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1003, 'Test Project 4', 'implement Test project 4', 'In Progress', 1);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1004, 'Project for Testomg 5', 'implement an API gateway', 'On Hold', 1);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1005, 'Seeded Project for Testing 6', 'implement a business software', 'Not started', 2);
insert into project (project_id, title, description, status, PROJECT_OWNER_USER_ID) values (1006, 'Yet Another Test Project', 'implement a web app', 'Completed', 1);

-- INSERT PROJECTS MEMBERS
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
insert into TODO_LIST_COLLECTION (TODO_COLLECTION_ID, COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE) values (3, 'for the hr project', '01/07/2023', '01/07/2023');

--INSERT Todo list
insert into TODO (todo_item_id, title, COLLECTION_ID) values (1, 'onions', 1);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (2, 'water melon', 1);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (3, 'tomatoes', 1);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (4, '5 cloves of garlic', 1);

insert into TODO (todo_item_id, title, COLLECTION_ID) values (5, 'call hr', 2);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (6, 'talk to dave about chair', 2);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (7, 'get new mouse from inventory', 2);

insert into TODO (todo_item_id, title, COLLECTION_ID) values (8, 'find form validation react component', 3);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (9, 'get employee required information for registration from hr', 3);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (10, 'get employee data policy from hr', 3);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (11, 'prepare value proposition canvas presentation', 3);
insert into TODO (todo_item_id, title, COLLECTION_ID) values (12, 'how many employees? ask hr.', 3);
