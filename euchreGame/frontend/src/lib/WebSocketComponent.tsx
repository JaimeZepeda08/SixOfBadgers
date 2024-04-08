"use client";

import React, { useState } from "react";

interface Props {
  socket: WebSocket;
}

const WebSocketComponent: React.FC<Props> = ({ socket }) => {
  const [status, setStatus] = useState("Waiting for connection...");
  const [gameID, setGameID] = useState("");

  // Add event listener for the 'open' event
  socket.onopen = () => {
    console.log("WebSocket connection established");
    setStatus("Connection established successfully");
  };

  // Add event listener for the 'message' event to receive messages
  socket.onmessage = (event) => {
    console.log("Received message:", event.data);
    setGameID(event.data);
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

  // Return any JSX or other content if needed
  return (
    <div style={{ textAlign: "center" }}>
      <h1>{status}</h1>
      <h1>{gameID}</h1>
    </div>
  );
};

export default WebSocketComponent;
