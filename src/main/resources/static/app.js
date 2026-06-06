const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/games', (greeting) => {
        const message = JSON.parse(greeting.body);
        if(message.type == "TABLE_UPDATE") {
            console.log(message)
            console.log(message.content)
            showGreeting(message.content);
        } else if(message.type == "GAME_FINISH") {
            renderSocketData(message.content);
        }
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': $("#name").val()})
    });
}

function showResult(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});

// Result
function renderSocketData(jsonData) {
    // 1. Parse the incoming JSON if it's still a string
    const data = typeof jsonData === 'string' ? JSON.parse(jsonData) : jsonData;

    // 2. Extract headers (names) and row data (numbers)
    const headers = Object.keys(data);
    const rowValues = Object.values(data);

    // 3. Create the table elements
    const table = document.createElement('table');
    table.border = "1";
    table.className = "animatedTable";// Optional: just for basic visibility, use CSS instead!

    const thead = document.createElement('thead');
    const tbody = document.createElement('tbody');

    // 4. Build the Header Row
    const headerRow = document.createElement('tr');
    headers.forEach(name => {
        const th = document.createElement('th');
        th.className = 'player-header';
        th.textContent = name;
        headerRow.appendChild(th);
    });
    thead.appendChild(headerRow);


    // 5. Build the Data Row

    const highestResult = Math.max(...rowValues);
    console.log(highestResult);
    const dataRow = document.createElement('tr');
    rowValues.forEach(value => {
        const td = document.createElement('td');
        td.textContent = value;
        if(highestResult == value) {
            td.className = 'winner-score';
        }
        dataRow.appendChild(td);
    });
    tbody.appendChild(dataRow);

    // 6. Assemble and inject into the DOM
    table.appendChild(thead);
    table.appendChild(tbody);

    const container = document.getElementById('result-container');
    container.innerHTML = ''; // Clear previous table if you only want the latest message
    container.appendChild(table);
}

// Scoreboard

function showGreeting(jsonString) {
            const data = JSON.parse(jsonString);
            const layers = Object.keys(data); // ["1", "2", "3", ...]

            if (layers.length === 0) return;

            // 1. Extract player names dynamically from the very first layer
            const firstLayerKey = layers[0];
            const playerNames = Object.keys(data[firstLayerKey]); // ["Nezka", "Vito", "Petra", ...]

            // 2. Start building the HTML Table string
            let tableHtml = `<table><thead>`;

            // First header row: Player Names spanning 3 columns each
            tableHtml += `<tr><th>Round</th>`;
            playerNames.forEach(name => {
                tableHtml += `<th colspan="3" class="player-header">${name}</th>`;
            });
            tableHtml += `</tr>`;

            // Second header row: Prediction, Result, Score sub-headers
            tableHtml += `<tr><th class="sub-header"></th>`;
            playerNames.forEach(() => {
                tableHtml += `
                    <th class="sub-header">P</th>
                    <th class="sub-header">R</th>
                    <th class="sub-header">S</th>
                `;
            });
            tableHtml += `</tr></thead><tbody>`;

            // 3. Build the data rows for each layer
            layers.forEach(layerId => {
                tableHtml += `<tr><td><strong>${layerId}</strong></td>`;

                const layerData = data[layerId];

                // Loop through players in the exact order established by the headers
                playerNames.forEach(name => {
                    const playerData = layerData[name];

                    if (playerData) {
                        if(playerData.result == null) {
                            tableHtml += `
                            <td>${playerData.prediction}</td>
                            <td></td>
                            <td></td>
                            `;  
                        } else {
                          tableHtml += `
                            <td>${playerData.prediction}</td>
                            <td>${playerData.result}</td>
                            <td class="score-data">${playerData.score}</td>
                            `;  
                        }
                    } else {
                        // Fallback empty cells if a player is missing in this particular layer
                        tableHtml += `<td>-</td><td>-</td><td>-</td>`;
                    }
                });

                tableHtml += `</tr>`;
            });

            tableHtml += `</tbody></table>`;

            // 4. Inject the generated table into the DOM
//            $("#greetings").append(tableHtml);
            document.getElementById('table-container').innerHTML = tableHtml;
        }