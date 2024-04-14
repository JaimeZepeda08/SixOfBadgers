"use client";

import { useSocket } from "@/lib/useSocket";
import React, { useCallback, useEffect, useState } from "react";
import { SimpleButtonRed, SimpleButtonGreen } from "@/components/SimpleButton";
import { useRouter } from "next/navigation";

export default function Page() {
  const socket = useSocket();
  const router = useRouter();

  const [gameID, setGameID] = useState("");
  const [joinID, setJoinID] = useState("");
  const [players, setPlayers] = useState([]);
  const [message, setMessage] = useState("");

  const errorWaitTime = 3000;

  const onMessage = useCallback(
    (message: MessageEvent) => {
      const json_response = JSON.parse(message.data);
      if (json_response.header === "id") {
        setGameID(json_response.content);
      }
      if (json_response.header === "players") {
        let players = json_response.content;
        players = players.substring(1, players.length - 1).split(",");
        setPlayers(players);
      }
      if (json_response.header === "leave") {
        setPlayers([]);
        setGameID("");
      }
      if (json_response.header === "error") {
        setMessage(json_response.content);
        setTimeout(() => {
          setMessage("");
        }, errorWaitTime);
      }
      if (json_response.header === "started") {
        router.push("/game");
        console.log(json_response.content);
      }
    },
    [router]
  );

  useEffect(() => {
    socket.addEventListener("message", onMessage);
    return () => {
      socket.removeEventListener("message", onMessage);
    };
  }, [socket, onMessage]);

  return (
    <div
      className="flex flex-col items-center justify-center mt-5"
      style={{ zoom: 0.8 }}
    >
      <div className="bg-red-500 text-white text-4xl p-4 rounded-md mb-4">
        <h1>Multiplayer</h1>
      </div>
      <div className="text-3xl font-medium my-5">
        Code: <span className="text-4xl font-bold">{gameID}</span>
      </div>
      <div className="flex flex-col justify-center items-center">
        <SimpleButtonRed
          text="Create New Game"
          onClick={() => {
            socket.send(JSON.stringify({ header: "create" }));
          }}
        />
        <div className="flex justify-center items-center my-8">
          <input
            type="text"
            value={joinID}
            onChange={(e) => setJoinID(e.target.value)}
            placeholder="Enter Code"
            className="border-2 border-slate-500 mx-5 py-1 px-2 rounded-md shadow-md"
          />
          <SimpleButtonRed
            text="Join Game"
            onClick={() => {
              socket.send(JSON.stringify({ header: "join", gameID: joinID }));
            }}
          />
        </div>
      </div>
      <div className="w-5/6 my-2">
        <h2 className="text-md font-light text-slate-500">
          {players.length} out of 4 Players
        </h2>
        <div className="border color-slate-900" />
      </div>
      <div>
        {players.map((player, index) => (
          <div
            key={index}
            className="flex justify-center items-center bg-blue-500/15 mx-5 py-3 px-4 mt-5 rounded-md shadow-md font-bold text-2xl text-blue-600"
          >
            {player}
          </div>
        ))}
      </div>
      <div className="w-full flex justify-end">
        <SimpleButtonRed
          text="Leave Game"
          className="my-10 mx-2"
          onClick={() => {
            socket.send(JSON.stringify({ header: "leave" }));
          }}
        />
        <SimpleButtonGreen
          text="Start Game"
          className="my-10 mx-2"
          onClick={() => {
            socket.send(JSON.stringify({ header: "start" }));
          }}
        />
      </div>
      <h2
        className={`absolute bottom-10 text-white font-medium text-lg bg-orange-500/50 shadow-md rounded-md ${
          message !== "" ? "py-2 px-3" : ""
        }`}
      >
        {message}
      </h2>
    </div>
  );
}
