"use client";

import React, { useState } from "react";

interface Props {
  socket: WebSocket;
}

const WebSocketComponent: React.FC<Props> = ({ socket }) => {
  const [status, setStatus] = useState("Waiting for connection...");
  const [username, setUsername] = useState("");
  const [users, setUsers] = useState([]);
  const [disableButton, setDisableButton] = useState(false);

  // Add event listener for the 'open' event
  socket.onopen = () => {
    console.log("WebSocket connection established");
    setStatus("Connection established successfully");
  };

  // Add event listener for the 'message' event to receive messages
  socket.onmessage = (event) => {
    console.log("Received message:", event.data);
    // Handle incoming messages as needed
    setUsers(JSON.parse(event.data));
  };

  // Add event listener for the 'close' event
  socket.onclose = () => {
    console.log("WebSocket connection closed");
  };

  // Add event listener for the 'error' event
  socket.onerror = (error) => {
    console.error("WebSocket error:", error);
    setStatus("Failed to establish a connection with the server");
  };

  // Function to send a message
  const sendMessage = () => {
    if (!username.trim()) return; // Ignore if message is empty or whitespace only
    socket.send(username); // Sending the message
    setDisableButton(true); // Disable the button
  };

  // Return any JSX or other content if needed
  return (
    <div>
      <h1>{status}</h1>
      <input
        type="text"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        disabled={disableButton}
        placeholder="Enter your username"
        style={{ margin: "20px" }}
      />
      <button onClick={sendMessage} disabled={disableButton}>
        Enter
      </button>
      <h1>
        <b>Players Online:</b>
      </h1>
      <div>
        <table className="mx-auto">
          <tbody>
            {users.map((user, index) => (
              <tr
                key={index}
                style={
                  user === username
                    ? {
                        backgroundColor: "#d1d5db",
                      }
                    : {}
                }
              >
                <td className="py-2 px-4">{user}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default WebSocketComponent;
