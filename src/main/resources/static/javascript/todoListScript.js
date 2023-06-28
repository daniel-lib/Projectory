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

		}
	}
}
/*</script>
	
	
<script th:fragment="display-item-count-script">*/
function displayItemCount() {
	//let items = document.getElementsByClassName("itemNumber");
	const element1 = document.querySelectorAll(".card-body #itemNumber");
	const element2 = document.querySelectorAll(".item-display-table #itemNumber");
	let itemElement = [element1, element2];
	for (let items of itemElement) {
		var counter = 0;
		for (let item of items) {
			item.innerHTML = ++counter;
		}
	}
}
	/*	</script>*/