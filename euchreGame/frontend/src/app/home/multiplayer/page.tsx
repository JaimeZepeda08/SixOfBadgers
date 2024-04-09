"use client";

import { useSocket } from "@/lib/useSocket";
import { Message } from "postcss";
import React, { useCallback, useEffect, useState } from "react";

export default function Page() {
  const socket = useSocket();

  const [gameID, setGameID] = useState("");
  const [joinID, setJoinID] = useState("");
  const [players, setPlayers] = useState("");

  const onMessage = useCallback((message: MessageEvent) => {
    const json_response = JSON.parse(message.data);
    if (json_response.type === "id") {
      setGameID(json_response.content);
    }
    if (json_response.type === "players") {
      setPlayers(json_response.content);
    }
  }, []);

  useEffect(() => {
    socket.addEventListener("message", onMessage);
    return () => {
      socket.removeEventListener("message", onMessage);
    };
  }, [socket, onMessage]);

  return (
    <div className="flex flex-col items-center justify-center mt-5">
      <div className="bg-red-500 text-white text-4xl p-4 rounded-md mb-4">
        <h1>Multiplayer</h1>
      </div>
      <div className="text-2xl font-bold my-5">{gameID}</div>
      <div className="flex flex-col justify-center items-center">
        <div
          onClick={() => {
            socket.send(JSON.stringify({ type: "create" }));
          }}
          className="mr-4 p-2 bg-red-500 text-white text-xl rounded-md mb-2 bottom-0"
        >
          Create New Game
        </div>
        <input
          type="text"
          value={joinID}
          onChange={(e) => setJoinID(e.target.value)}
          placeholder="Enter Game ID"
          style={{ margin: "20px" }}
        />
        <div
          onClick={() => {
            socket.send(JSON.stringify({ type: "join", gameID: joinID }));
          }}
          className="p-2 bg-red-500 text-white text-xl rounded-md bottom-0"
        >
          Join Game
        </div>
      </div>
      <div>{players}</div>
    </div>
  );
}
