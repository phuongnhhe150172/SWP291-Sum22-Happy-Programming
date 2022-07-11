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
    if (content.length <= 0) return;
    emptyInput();
    sendMessageSocket(senderId, receiverId, content);
    scrollDown();
}

let processData = (data) => {
    let {senderId, receiverId, content, image, title, link, timestamp} = JSON.parse(data);
    let currentTarget = $("#receiver_id").val();
    addNotification(senderId);
    if (senderId === currentTarget) {
        $("#dummy").before(createMessage({content, image, title, link, timestamp}, false));
    }
    if (senderId === $("#sender_id").val()) {
        $("#dummy").before(createMessage({content, image, title, link, timestamp}, true));
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
    let ogInfo = content.link ? `<a href="${content.link}" target="_blank">
            <img class="w-full" src="${content.image}" alt="${content.title}">
            <p class="font-medium p-3">${content.title}</p>
        </a>` : "";
    div.innerHTML = `<div class='p-4 flex ${direction} items-center group gap-4'>
            <div class='rounded-xl ${bg} max-w-[30ch]'>
                <p class="p-2 break-words">${content.content}</p>
                ${ogInfo}
            </div>
            <p class="hidden group-hover:block text-gray-600 font-medium text-sm">${content.timestamp}</p>
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
