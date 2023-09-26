--Create Authority table


-- INSERT USERS
insert into USERS (username, password, first_name, last_name, email_address, enabled) values ('testuser1', 'testuser123', 'Test', 'User1', 'test1@user.com', true);
insert into USERS (username, password, first_name, last_name, email_address, enabled) values ('testuser2', 'testuser123', 'gebru', 'User2', 'gebru@user.com', true);
insert into USERS (username, password, first_name, last_name, email_address, enabled) values ('testuser3', 'testuser123', 'semhal', 'User3', 'semhal@user.com', true);
insert into USERS (username, password, first_name, last_name, email_address, enabled) values ('hena', '123456', 'henok', 'sertse', 'hena_s@user.com', true);
insert into USERS (username, password, first_name, last_name, email_address, enabled) values ('hawa2', 'pasw1', 'hewan', 'john', 'hewi2@user.com', true);
insert into USERS (username, password, first_name, last_name, email_address, enabled) values ('markos55', 'inthe24', 'mark', 'os', 'mark_os2@user.com', true);

-- INSERT CONNECTIONS
insert into CONNECTIONS (REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (2, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (3, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (4, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (5, '01/07/2023', 'approved', 1);
insert into CONNECTIONS (REQUEST_RECEIVER_USER, CONNECTION_DATE, CONNECTION_STATUS, REQUEST_SENDER_USER) values (6, '01/07/2023', 'pending', 1);
	
-- INSERT PROJECTS	
insert into project (title, description, status, PROJECT_OWNER_USER_ID) values ('Project Management System', 'implement a web app to manage projects', 'In Progress', 1);
insert into project (title, description, status, PROJECT_OWNER_USER_ID) values ('File Sharing Web App', 'implement a web app to share files', 'In Progress', 1);
insert into project (title, description, status, PROJECT_OWNER_USER_ID) values ('Human Resource Management System', 'implement a web app to manage employees', 'Not started', 2);
insert into project (title, description, status, PROJECT_OWNER_USER_ID) values ('Test Project 4', 'implement Test project 4', 'In Progress', 1);
insert into project (title, description, status, PROJECT_OWNER_USER_ID) values ('Project for Testomg 5', 'implement an API gateway', 'On Hold', 1);
insert into project (title, description, status, PROJECT_OWNER_USER_ID) values ('Seeded Project for Testing 6', 'implement a business software', 'Not started', 2);
insert into project (title, description, status, PROJECT_OWNER_USER_ID) values ('Yet Another Test Project', 'implement a web app', 'Completed', 1);

-- INSERT PROJECTS MEMBERS
insert into PROJECT_MEMBERS (user_id, project_id) values (1,3);
insert into PROJECT_MEMBERS (user_id, project_id) values (2,1);
insert into PROJECT_MEMBERS (user_id, project_id) values (2,2);
insert into PROJECT_MEMBERS (user_id, project_id) values (3,1);
insert into PROJECT_MEMBERS (user_id, project_id) values (3,2);
insert into PROJECT_MEMBERS (user_id, project_id) values (3,3);

--INSERT PROJECT TASKS
insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values ('', 'not started', '', 'user login feature', 1);
insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values ('', 'not started', '', 'change password feature', 1);
insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values ('', 'not started', '', 'project display feature', 1);
insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values ('', 'not started', '', 'task metrics feature', 1);

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values ('', 'not started', '', 'file upload ui', 2);

insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values ('', 'not started', '', 'employee personal history form', 3);
insert into PROJECT_TASKS (DEADLINE, STATUS, TASK_DESCRIPTION, TASK_NAME, PROJECT_ID) values ('', 'not started', '', 'leave request form', 3);


--INSERT Todo collections
insert into TODO_LIST_COLLECTION (COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE) values ('Groceries', '01/07/2023', '01/07/2023');
insert into TODO_LIST_COLLECTION (COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE) values ('today schedule', '01/07/2023', '01/07/2023');
insert into TODO_LIST_COLLECTION (COLLECTION_TITLE, CREATION_DATE, MODIFICATION_DATE) values ('for the hr project', '01/07/2023', '01/07/2023');

--INSERT Todo list
insert into TODO (title, COLLECTION_ID) values ('onions', 1);
insert into TODO (title, COLLECTION_ID) values ('water melon', 1);
insert into TODO (title, COLLECTION_ID) values ('tomatoes', 1);
insert into TODO (title, COLLECTION_ID) values ('5 cloves of garlic', 1);

insert into TODO (title, COLLECTION_ID) values ('call hr', 2);
insert into TODO (title, COLLECTION_ID) values ('talk to dave about chair', 2);
insert into TODO (title, COLLECTION_ID) values ('get new mouse from inventory', 2);

insert into TODO (title, COLLECTION_ID) values ('find form validation react component', 3);
insert into TODO (title, COLLECTION_ID) values ('get employee required information for registration from hr', 3);
insert into TODO (title, COLLECTION_ID) values ('get employee data policy from hr', 3);
insert into TODO (title, COLLECTION_ID) values ('prepare value proposition canvas presentation', 3);
insert into TODO (title, COLLECTION_ID) values ('how many employees? ask hr.', 3);
