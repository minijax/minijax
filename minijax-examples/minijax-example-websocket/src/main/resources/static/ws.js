var output;
var input;
var button;
var ws;

function init() {
    output = document.querySelector('.output');
    input = document.querySelector('input');
    button = document.querySelector('button');

    input.onkeydown = function(e) {
    	if (event.keyCode === 13) {
    		submit();
    	}
    }

    button.onclick = function(e) {
		submit();
    };

    ws = new WebSocket('ws://localhost:8080/echo');

    ws.onopen = function(e) {
        log('WebSocket opened');
    };

    ws.onmessage = function(e) {
        log('<span style="color:blue">RESPONSE:</span> ' + e.data);
    };

    ws.onclose = function(e) {
        log('WebSocket closed');
    };

    ws.onerror = function(e) {
        log('<span style="color:red">ERROR:</span> ' + e.data);
    };
}

function submit() {
	var message = input.value;
    log('<span style="color:green">SENT:</span> ' + message);
    ws.send(message);
    input.value = '';
    input.focus();
}

function log(message) {
    var p = document.createElement('p');
    p.innerHTML = message;
    output.appendChild(p);
    output.scrollTop = output.scrollHeight;
}

window.addEventListener('load', init, false);
