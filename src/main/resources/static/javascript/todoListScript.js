function scrollToSpecificCollection(collectionId){
	
	//document.getElementById(collectionId).scrollIntoView();
	
	window.setTimeout(test, 300);
	
	function test(){
		const collection = document.getElementById(collectionId);
	collection.scrollIntoView();
	//alert(collectionId);
	}
	
}

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
function addNewCollection() {
	const title = document.getElementById("collection-name-input").value;
	fetch('/todo-list-collection/add-collection?title=' + title)
		.then(response => response.json())
		.then(data => {
			let msg, notificationType;
			if (data == "1") {
				msg = "New Collection Created!";
				notificationType = "success";
			}
			else {
				msg = "Couldn't Create New Collection. Please try again.";
				notificationType = "error";
			}
			toggleNotification(notificationType, msg);
			
		})
}




//add Todo Item script
function addTodo(collectionId) {
	const title = document.getElementById("col-"+collectionId+"-todo-title-input").value;
	fetch('/todo/add-item-js?title=' + title + '&collectionId=' + collectionId)
		.then(response => response.json())
		.then(data => {
			let msg, notificationType;
			if (data == "1") {
				msg = "Todo Item Added!";
				notificationType = "success";
			}
			else {
				msg = "Couldn't add todo item. Please try again.";
				notificationType = "error";
			}
			/*alert(msg);*/
			toggleNotification(notificationType, msg);
			
		})
}

