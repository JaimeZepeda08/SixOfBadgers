"use client";

import React, { useState } from "react";
import WebSocketComponent from "@/lib/WebSocketComponent";

export default function Page() {
  const socket = new WebSocket("ws://localhost:8080/ws");

  function handleCreateGame() {
    socket.send(JSON.stringify({ type: "create" }));
  }

  return (
    <div className="flex flex-col items-center justify-center mt-10">
      <div className="bg-red-500 text-white text-4xl p-4 rounded-md mb-4">
        <h1>Multiplayer</h1>
      </div>
      <WebSocketComponent socket={socket} />
      <div className="flex flex-col justify-center items-center mt-10">
        <div
          onClick={handleCreateGame}
          className="mr-4 p-2 bg-red-500 text-white text-xl rounded-md mb-2 bottom-0"
        >
          Create New Game
        </div>
        <div className="p-2 bg-red-500 text-white text-xl rounded-md bottom-0">
          Join Game
        </div>
      </div>
    </div>
  );
}
