"use client";

import React from "react";
import WebSocketComponent from "@/lib/WebSocketComponent";

export default function Page() {
  // change to make sure connection is only made once
  const socket = new WebSocket("ws://localhost:8080/ws");

  return (
    <div className="flex flex-col items-center justify-center mt-10">
      <div className="bg-red-500 text-white text-4xl p-4 rounded-md mb-4">
        <h1>Multiplayer</h1>
      </div>
      <WebSocketComponent socket={socket} />
    </div>
  );
}
