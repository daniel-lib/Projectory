(function() {
	const storageKey = "_p1";
	const uuidPattern = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

	function createVisitorId() {
		if (window.crypto && typeof window.crypto.randomUUID === "function") {
			return window.crypto.randomUUID();
		}
		if (!window.crypto || typeof window.crypto.getRandomValues !== "function") {
			throw new Error("Secure random number generation is unavailable.");
		}

		const bytes = new Uint8Array(16);
		window.crypto.getRandomValues(bytes);
		bytes[6] = (bytes[6] & 0x0f) | 0x40;
		bytes[8] = (bytes[8] & 0x3f) | 0x80;
		const hex = Array.from(bytes, function(byte) {
			return byte.toString(16).padStart(2, "0");
		}).join("");
		return hex.slice(0, 8) + "-" + hex.slice(8, 12) + "-" + hex.slice(12, 16)
			+ "-" + hex.slice(16, 20) + "-" + hex.slice(20);
	}

	let visitorId;
	try {
		visitorId = window.localStorage.getItem(storageKey);
		if (!visitorId || !uuidPattern.test(visitorId)) {
			visitorId = createVisitorId();
			window.localStorage.setItem(storageKey, visitorId);
		}
	} catch (error) {
		console.error("Unable to initialize anonymous visitor tracking.", error);
		return;
	}

	const csrfToken = document.querySelector('meta[name="_csrf"]');
	const csrfHeader = document.querySelector('meta[name="_csrf_header"]');
	if (!csrfToken || !csrfHeader) {
		console.error("Unable to track visitor because the CSRF token is unavailable.");
		return;
	}

	const headers = { "X-Visitor-Id": visitorId };
	headers[csrfHeader.content] = csrfToken.content;
	fetch("/system-access/visit", {
		method: "POST",
		credentials: "same-origin",
		headers: headers
	}).then(function(response) {
		if (!response.ok) {
			throw new Error("Visitor tracking request failed with status " + response.status + ".");
		}
	}).catch(function(error) {
		console.error("Unable to record anonymous visitor access.", error);
	});
})();
