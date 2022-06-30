let ws;
const END_POINT = "ws://localhost:8080/chatSocket";

$(document).ready(function () {
    let senderId = $("#sender_id").val();
    let receiverId = $("#receiver_id").val();
    ws = new WebSocket(END_POINT);
    ws.onmessage = (data) => {
        processData(data.data);
        scrollDown();
    }
    setTimeout(() => establishConnection(senderId, receiverId), 100);
    scrollDown();
    removeNotification();
});

function send() {
    let senderId = $("#sender_id").val();
    let receiverId = $("#receiver_id").val();
    let content = $("#content").val();

    emptyInput();
    sendMessageSocket(senderId, receiverId, content);
    scrollDown();
}

let processData = (data) => {
    let {senderId, receiverId, content, image, title, link} = JSON.parse(data);
    let currentTarget = $("#receiver_id").val();
    addNotification(senderId);
    if (senderId === currentTarget) {
        $("#dummy").before(createMessage({content, image, title, link}, false));
    }
    if (senderId === $("#sender_id").val()) {
        $("#dummy").before(createMessage({content, image, title, link}, true));
    }
};

$("form").submit(e => e.preventDefault());

$("#content").on("keyup", (e) => {
    // check enter
    if (e.keyCode === 13) send();
});

//<editor-fold desc="Utilities">
let scrollDown = () => $("#dummy")[0].scrollIntoView();
let emptyInput = () => $("#content").val("");

let removeNotification = () => {
    [...$('.connection_button')].forEach(sender => {
        if (sender.href === window.location.href) {
            $(sender).find('p').removeClass('font-semibold');
            $(sender).find('p').addClass('font-medium');
            $(sender).removeClass('bg-blue-800/50');
        }
    });
}

let addNotification = (senderId) => {
    [...$('.connection_button')].forEach(sender => {
        if (sender.id === "connection_" + senderId && sender.href !== window.location.href) {
            $(sender).find('p').removeClass('font-medium');
            $(sender).find('p').addClass('font-semibold');
            $(sender).addClass('bg-blue-800/50');
        }
    });
}

function createMessage(content, fromSelf) {
    let div = document.createElement('div');
    let direction = fromSelf ? 'flex-row-reverse' : 'flex-row';
    let bg = fromSelf ? 'bg-blue-600' : 'bg-gray-700';
    let ogInfo = content.image ? `<a href="${content.link}" target="_blank">
            <img src="${content.image}" alt="${content.title}">
            <p class="font-medium p-3">${content.title}</p>
        </a>` : "";
    div.innerHTML = `<div class='p-4 flex ${direction}'>
            <div class='py-2 rounded-xl ${bg} max-w-[40vmin]'>
                <p class="px-2 break-all">${content.content}</p>
                ${ogInfo}
            </div>
        </div>`;
    return div.firstChild;
}

let sendMessageSocket = (senderId, receiverId, content) => {
    ws.send(JSON.stringify({
        senderId: senderId,
        receiverId: receiverId,
        content: content
    }));
}

let establishConnection = (senderId, receiverId) => {
    ws.send(JSON.stringify({
        senderId: senderId,
        receiverId: receiverId,
    }));
}
//</editor-fold>
