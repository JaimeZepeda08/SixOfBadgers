"use client";

import React, { useEffect, useState } from "react";

export default function WebSocketComponent() {
  const [status, setStatus] = useState("Waiting for connection...");

  useEffect(() => {
    const socket = new WebSocket(`ws://localhost:8080/ws`);

    // Add event listener for the 'open' event
    socket.onopen = () => {
      console.log("WebSocket connection established");
      setStatus("Connection established succesfully");
    };

    // Add event listener for the 'message' event to receive messages
    socket.onmessage = (event) => {
      console.log("Received message:", event.data);
      // Handle incoming messages as needed
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

    // Cleanup function to close the WebSocket connection when the component unmounts
    return () => {
      socket.close();
    };
  }, []); // Empty dependency array ensures this effect runs only once on mount

  // Return any JSX or other content if needed
  return <h1>{status}</h1>;
}
