function scrollToSpecificCollection(collectionId){
	
	//document.getElementById(collectionId).scrollIntoView();
	
	window.setTimeout(test, 300);
	
	function test(){
		const collection = document.getElementById(collectionId);
	collection.scrollIntoView();
	//alert(collectionId);
	}
	
}

document.addEventListener("click", (event) => {
	const target = event.target instanceof Element ? event.target : null;
	if (!target) {
		return;
	}

	const editItemButton = target.closest(".edit-item-btn");
	if (editItemButton) {
		editItem(editItemButton);
		return;
	}

	const editCollectionButton = target.closest(".edit-todo-collection-btn");
	if (editCollectionButton) {
		editTodoCollection(editCollectionButton);
		return;
	}

	const deleteCollectionButton = target.closest(".delete-todo-collection-btn");
	if (deleteCollectionButton) {
		openDeleteTodoCollectionConfirmation(deleteCollectionButton);
	}
});

function editTodoCollection(button) {
	const collection = button.closest(".todo-collection");
	const titleContent = collection && collection.querySelector(".todo-collection-title-content");
	const titleElement = titleContent && titleContent.querySelector(".todo-collection-name");
	if (!titleContent || !titleElement || !window.htmx) {
		return;
	}

	const collectionId = titleContent.dataset.collectionId;
	const originalTitle = titleElement.textContent.trim();
	const form = document.createElement("form");
	form.className = "collection-edit-form";
	form.setAttribute("hx-post",
		`/todo-list-collection/fragment/update-collection?collectionId=${encodeURIComponent(collectionId)}`);
	form.setAttribute("hx-target", "#todo-collection-list-container");
	form.setAttribute("hx-swap", "innerHTML");

	const input = document.createElement("input");
	input.type = "text";
	input.name = "title";
	input.value = originalTitle;
	input.maxLength = 40;
	input.required = true;
	input.className = "form-control collection-edit-input";
	input.setAttribute("aria-label", "Collection name");

	const actions = document.createElement("span");
	actions.className = "collection-edit-actions";
	const saveButton = document.createElement("button");
	saveButton.type = "submit";
	saveButton.className = "collection-action-btn collection-action-edit";
	saveButton.textContent = "Save";
	const cancelButton = document.createElement("button");
	cancelButton.type = "button";
	cancelButton.className = "collection-action-btn collection-action-cancel";
	cancelButton.textContent = "Cancel";
	cancelButton.addEventListener("click", () => renderTodoCollectionName(titleContent, collectionId, originalTitle));
	actions.append(saveButton, cancelButton);

	input.addEventListener("keydown", (event) => {
		if (event.key === "Escape") {
			event.preventDefault();
			renderTodoCollectionName(titleContent, collectionId, originalTitle);
		}
	});

	form.append(input, actions);
	titleContent.replaceChildren(form);
	htmx.process(form);
	input.focus();
	input.select();
}

function renderTodoCollectionName(titleContent, collectionId, title) {
	const titleElement = document.createElement("span");
	titleElement.className = "todo-collection-name align-self-start ps-3 pe-3";
	titleElement.textContent = title;
	titleContent.dataset.collectionId = collectionId;
	titleContent.replaceChildren(titleElement);
}

function openDeleteTodoCollectionConfirmation(button) {
	const collectionId = button.dataset.collectionId;
	if (!collectionId || !window.htmx) {
		return;
	}

	openAppConfirmation({
		title: `Delete "${button.dataset.collectionTitle}"?`,
		message: "This will permanently delete the collection and all of its todo items.",
		confirmLabel: "Delete collection",
		onConfirm: function() {
			htmx.ajax("POST",
				`/todo-list-collection/fragment/delete-collection?collectionId=${encodeURIComponent(collectionId)}`,
				{ target: "#todo-collection-list-container", swap: "innerHTML" });
		}
	});
}

function editItem(button) {
	const itemContent = button.closest(".todo-item-content");
	const itemRow = button.closest(".list-items");
	const title = itemContent && itemContent.querySelector(".todo-item-title");
	if (!itemContent || !itemRow || !title || !window.htmx) {
		return;
	}

	const itemId = itemContent.dataset.todoItemId;
	const originalTitle = title.textContent;
	const form = document.createElement("form");
	form.className = "todo-item-edit-form d-inline-flex align-items-center gap-2";
	form.setAttribute("hx-post", `/todo/fragment/update-item?itemId=${encodeURIComponent(itemId)}`);
	form.setAttribute("hx-target", "closest .list-items");
	form.setAttribute("hx-swap", "outerHTML");

	const input = document.createElement("input");
	input.type = "text";
	input.name = "title";
	input.required = true;
	input.value = originalTitle;
	input.className = "form-control edit-element-title-input";
	input.setAttribute("aria-label", "Todo item title");

	const saveButton = document.createElement("button");
	saveButton.type = "submit";
	saveButton.className = "btn btn-sm btn-primary";
	saveButton.textContent = "Save";

	const cancelButton = document.createElement("button");
	cancelButton.type = "button";
	cancelButton.className = "btn btn-sm btn-secondary";
	cancelButton.textContent = "Cancel";
	cancelButton.addEventListener("click", () => renderTodoTitle(itemContent, itemId, originalTitle));

	input.addEventListener("keydown", (event) => {
		if (event.key === "Escape") {
			event.preventDefault();
			renderTodoTitle(itemContent, itemId, originalTitle);
		}
	});

	form.append(input, saveButton, cancelButton);
	itemContent.replaceChildren(form);
	htmx.process(form);
	input.focus();
	input.select();
}

function renderTodoTitle(itemContent, itemId, title) {
	const titleElement = document.createElement("span");
	titleElement.className = "todo-item-title";
	titleElement.textContent = title;

	const editButton = document.createElement("button");
	editButton.type = "button";
	editButton.className = "edit-item-btn fa-solid fa-pen-to-square";
	editButton.title = "Edit Item";
	editButton.setAttribute("aria-label", "Edit todo item");
	const editButtonLabel = document.createElement("span");
	editButtonLabel.className = "visually-hidden";
	editButtonLabel.textContent = "Edit todo item";
	editButton.appendChild(editButtonLabel);

	itemContent.dataset.todoItemId = itemId;
	itemContent.replaceChildren(titleElement, editButton);
}
/*						</script>
						
		
		<script th:fragment = "ready-delete-button-script">*/
function readyDeleteButton(specificCollectionId) {
	const checkboxes = document.getElementsByClassName("checkbox-for-deletion-"+specificCollectionId);
	const delBtn = document.getElementById("delete-selected-btn-"+specificCollectionId);
	const numberOfItems = document.getElementById("number-of-items-badge-"+specificCollectionId);
	const deleteSingleItemBtns = document.getElementsByClassName("delete-single-item-btn-"+specificCollectionId);
	const deleteSingleItemLnk = document.getElementsByClassName("delete-single-item-link-"+specificCollectionId);
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
			//i.setAttribute("class", "badge delete-single-item-btn");
			i.classList.remove("text-bg-danger");
		}
	}
	else {
		delBtn.setAttribute("disabled", true);
		for (let l of deleteSingleItemLnk) {
			//l.style="pointer-events: none !important";
			l.style.pointerEvents = "auto";
		}
		for (let i of deleteSingleItemBtns) {
			//i.setAttribute("class", "badge text-bg-danger delete-single-item-btn");
			i.classList.add("text-bg-danger");
			i.style.opacity = "1";
		}
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




//add new collection script
// NOTE: replaced by HTMX (hx-get on the Add Collection button in todoLayouts.html)
// function addNewCollection() { ... }


//add Todo Item script
// NOTE: replaced by HTMX (hx-get on the Add Item button in todoLayouts.html)
// function addTodo(collectionId) { ... }
