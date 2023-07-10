/*<script th:fragment = "edit-item-script">*/
function editItem(id) {

	document.getElementById(id).contentEditable;

	// Get the container element by its ID.
	const listContrainer = document.select(id);

	// Get the span element by its ID.
	const spanElement = document.querySelector("#" + id + " span");
	//


	//create form
	const form = document.createElement("form");
	form.setAttribute("action", "/todo/update-item?itemId=" + id);
	form.setAttribute("method", "POST");
	form.style.display = "inline";

	// Create a new text input element.
	const inputElement = document.createElement('input');
	inputElement.setAttribute("type", "text");
	inputElement.setAttribute("name", "name");

	// Set th value of the text input element to the text of the span element.
	inputElement.value = spanElement.textContent;
	inputElement.className = "form-control edit-element-title-input"

	//add input element to the form
	form.appendChild(inputElement);

	// Replace the span element with the form element.
	spanElement.replaceWith(form);
	//spanElement.replaceWith(inputElement);
	//&#10004;
}
/*						</script>
						
		
		<script th:fragment = "ready-delete-button-script">*/
function readyDeleteButton() {
	const checkboxes = document.getElementsByClassName("checkbox-for-deletion");
	const delBtn = document.getElementById("delete-selected-btn");
	const numberOfItems = document.getElementById("number-of-items-badge");
	const deleteSingleItemBtns = document.getElementsByClassName("delete-single-item-btn");
	const deleteSingleItemLnk = document.getElementsByClassName("delete-single-item-link");
	let num = 0;
	for (var checkbox of checkboxes) {
		if (checkbox.checked === true) {
			num++;
		}
	}
	numberOfItems.innerHTML = num;
	if (num > 0) {
		delBtn.removeAttribute("disabled");

		for (let l of deleteSingleItemLnk) {
			l.style.pointerEvents = "none";
		}

		for (let i of deleteSingleItemBtns) {
			i.style.backgroundColor = "grey";
			i.style.opacity = "0.1";
			i.setAttribute("class", "badge delete-single-item-btn");
		}
	}
	else {
		delBtn.setAttribute("disabled", true);
		for (let l of deleteSingleItemLnk) {
			//l.style="pointer-events: none !important";
			l.style.pointerEvents = "auto";
		}
		for (let i of deleteSingleItemBtns) {
			i.setAttribute("class", "badge text-bg-danger delete-single-item-btn");
			i.style.opacity = "1";
		}
	}
}
/*</script>
	
	
<script th:fragment="display-item-count-script">*/
function displayItemCount() {
	//let items = document.getElementsByClassName("itemNumber");
	const element1 = document.querySelectorAll(".card-body #itemNumber");
	const element2 = document.querySelectorAll(".item-display-table #itemNumber");
	const element3 = document.querySelectorAll(".card-body #projectNumber");
	const element4 = document.querySelectorAll(".project-display-table #itemNumber");
	let itemElement = [element1, element2, element3, element4];
	for (let items of itemElement) {
		var counter = 0;
		for (let item of items) {
			item.innerHTML = ++counter;
		}
	}
}


//todo list item count checker
let itemCount = document.getElementsByClassName("todo-card-item-count");
for (let ic of itemCount) {
	if (ic.textContent == 0) {
		ic.innerHTML = "<br/><br/>Todo list is empty at the moment."
	}
	else {
		ic.textContent = " ";
	}
}


/*<script th:fragment = "notificationScripts">*/
/*function showActionNotification(){*/
function showDashboardActionNotification() {
	var notificationBoard = document.getElementById("item-action-notification");
	if (notificationBoard.innerHTML !== "") {
		var color = "red";
		if (notificationBoard.innerHTML === "added") {
			notificationBoard.innerHTML = "Item added";
			color = "green";
		}
		else if (notificationBoard.innerHTML === "deleted") {
			notificationBoard.innerHTML = "1 Item deleted";
		}
		else if (notificationBoard.innerHTML === "multiple-deleted") {
			notificationBoard.innerHTML = "Selected Items deleted";
		}
		notificationBoard.style = "transition: all 1s ease-out;display: flex !important;color: " + color + " !important;";
		setTimeout(hideNotificationBoard, 1500);
	}


}
function hideNotificationBoard() {
	var notificationBoard = document.getElementById("item-action-notification");
	//notificationBoard.style="display: flex !important;height:300px !important;";
	notificationBoard.style = "color:yellow;display: none !important;";
	//notificationBoard.style = "color:yellow;display: flex !important;";

}
/*</script>*/


//show "add todo list item" form
function showAddTodoItemForm(source) {
	const addTodoItemForm = document.getElementById("add-todo-item-form");
	const AddTodoItemBtn = document.getElementsByClassName("add-todo-item-btn");
	if (source == "card") {//if the click comes from dashboard card instead of modal
		//createItemButtonToDefault();		
		//change the state of "Create Project" button to default state
		addTodoItemForm.style.height = 0;
		addTodoItemForm.style.padding = "0";
	}

	if (addTodoItemForm.style.height == "0px") {
		addTodoItemForm.style.height = "240px";
		addTodoItemForm.style.padding = "1.5rem";
		for(let btns of AddTodoItemBtn){
			btns.value = "Exit Form";
		}
	}
	else {
		addTodoItemForm.style.height = 0;
		addTodoItemForm.style.padding = "0";
		for(let btns of AddTodoItemBtn){
			btns.value = "Add Item";
		}
	}
}

