/**
 * 
 */

const roomId = "room123";
const sender = sessionStorage.getItem("username") || "익명";
sessionStorage.setItem("username", sender);

const socket = new SockJS("/ws/chat");
const stompClient = Stomp.over(socket);

let latestParticipants = [];
let debateTimer = null;
let debateEndTime = null;
const timeSpan = document.getElementById("time");

stompClient.connect({}, () => {
	stompClient.subscribe("/topic/chat/" + roomId, (msg) => {
		const data = JSON.parse(msg.body);
		const isMe = sender === data.sender;

		console.log("수신 메시지:", data);

		if (data.type === "TALK") {
			displayMessage(data.sender, data.message, isMe);
		} else if (data.type === "ENTER" || data.type === "LEAVE") {
			displayNotice(data.message);
		} else if (data.type === "END_TIME") {
			displayNotice(data.message);
			clearInterval(debateTimer);
			timeSpan.textContent = 0;
			document.getElementById("end-timer-btn").disabled = true;
			showVoteModal(latestParticipants);
		} else if (data.type === "START_TIME") {
			displayNotice(data.message);
			handleStartTime(data.startTime);

		}

		if (data.participants) {
			updateParticipantList(data.participants);
		}
	});


	stompClient.send("/app/chat.send/" + roomId, {}, JSON.stringify({
		roomId: roomId,
		sender: sender,
		message: "",
		type: "ENTER"
	}));
});

window.addEventListener("beforeunload", () => {
	stompClient.send("/app/chat.send/" + roomId, {}, JSON.stringify({
		roomId: roomId,
		sender: sender,
		message: "",
		type: "LEAVE"
	}));
});

function sendMessage() {
	const message = document.getElementById("message").value;
	if (!message.trim()) return;

	stompClient.send("/app/chat.send/" + roomId, {}, JSON.stringify({
		roomId: roomId,
		sender: sender,
		message: message,
		type: "TALK"
	}));
	document.getElementById("message").value = '';
}

function displayMessage(sender, message, isMe) {
	const chatBox = document.getElementById("chat-box");
	const msgDiv = document.createElement("div");
	msgDiv.className = "message " + (isMe ? "right" : "left");
	msgDiv.innerHTML = `<div class="sender">${sender}</div><div class="message-content">${message
		.replace(/&/g, '&amp;')
		.replace(/</g, '&lt;')
		.replace(/>/g, '&gt;')
		.replace(/\n/g, '<br>&ZeroWidthSpace;')}</div>`;
	chatBox.appendChild(msgDiv);
	chatBox.scrollTop = chatBox.scrollHeight;
}

function displayNotice(message) {
	if (!message || message.trim() === "") return;
	const chatBox = document.getElementById("chat-box");
	const msgDiv = document.createElement("div");
	msgDiv.className = "notice";
	msgDiv.innerHTML = `<div class="message-notice">${message}</div>`;
	chatBox.appendChild(msgDiv);
	chatBox.scrollTop = chatBox.scrollHeight;
}

function toggleRulebook(show) {
	const modal = document.getElementById("rulebook-modal");
	const overlay = document.getElementById("rulebook-overlay");
	modal.style.display = show ? 'block' : 'none';
	overlay.style.display = show ? 'block' : 'none';
}

function updateParticipantList(participants) {
	latestParticipants = participants;
	const list = document.getElementById("participant-list");
	list.innerHTML = '';
	participants.forEach(p => {
		const item = document.createElement("li");
		item.textContent = p;
		list.appendChild(item);
	});
}

function showVoteModal(participants) {
	const voteList = document.getElementById("vote-list");
	voteList.innerHTML = '';
	participants.forEach(name => {
		if (name === sender) return;
		const li = document.createElement("li");
		li.textContent = name;
		li.onclick = () => {
			document.querySelectorAll("#vote-list li").forEach(el => el.classList.remove("selected"));
			li.classList.add("selected");
		};
		voteList.appendChild(li);
	});
	document.getElementById("vote-modal").style.display = "flex";
}

function submitVote() {
	const selected = document.querySelector("#vote-list li.selected");
	if (!selected) {
		alert("투표 대상을 선택하세요.");
		return;
	}

	const votedUser = selected.textContent;
	stompClient.send("/app/chat.vote/" + roomId, {}, JSON.stringify({
		sender: sender,
		voted: votedUser,
		type: "VOTE"
	}));

	document.getElementById("vote-modal").style.display = "none";
}

function endTimerNow() {
	stompClient.send("/app/chat.send/" + roomId, {}, JSON.stringify({
		roomId: roomId,
		sender: sender,
		message: "시간 종료",
		type: "END_TIME"
	}));
}

function startDebate() {
	document.getElementById("end-timer-btn").disabled = false;

	stompClient.send("/app/chat.send/" + roomId, {}, JSON.stringify({
		roomId: roomId,
		sender: sender,
		message: "",
		type: "START_TIME"
	}));
}


function handleStartTime(startTimeMillis) {
	const duration = 120 * 1000;

	debateEndTime = startTimeMillis + duration;

	if (debateTimer) clearInterval(debateTimer);

	debateTimer = setInterval(() => {
		const now = Date.now();
		const remaining = Math.max(0, Math.floor((debateEndTime - now) / 1000));
		timeSpan.textContent = remaining;
		if (remaining <= 0) {
			clearInterval(debateTimer);
			document.getElementById("end-timer-btn").disabled = true;
			showVoteModal(latestParticipants);
		}

	}, 500);
}
