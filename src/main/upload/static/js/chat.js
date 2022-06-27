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
    displaySentMessage(content);
    sendMessageSocket(senderId, receiverId, content);
    saveMessageToDB(receiverId, content);
    scrollDown();
}

let processData = (data) => {
    let {senderId, content} = JSON.parse(data);
    let currentTarget = $("#receiver_id").val();
    addNotification(senderId);
    if (senderId === currentTarget) {
        displayReceivedMessage(content);
    }
};

$("form").submit(e => e.preventDefault());

$("#content").on("keyup", (e) => {
    // check enter
    if (e.keyCode === 13) send();
});

//<editor-fold desc="Utilities">
function createMessage(content, fromSelf) {
    let div = document.createElement('div');
    let direction = fromSelf ? 'flex-row-reverse' : 'flex-row';
    let bg = fromSelf ? 'bg-blue-600' : 'bg-gray-700';
    div.innerHTML = `<div class='p-4 flex ${direction}'><div class='p-2 rounded-xl ${bg}'>${content}</div></div>`;
    return div.firstChild;
}

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

let saveMessageToDB = (receiverId, content) => {
    $.ajax({
        url: 'http://localhost:8080/sendMessage',
        method: 'GET',
        data: {
            receiverId: receiverId,
            content: content
        },
    })
}

let displaySentMessage = (content) => {
    $("#dummy").before(createMessage(content, true));
}
let displayReceivedMessage = (content) => {
    if (content === "") return;
    $("#dummy").before(createMessage(content, false));
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
