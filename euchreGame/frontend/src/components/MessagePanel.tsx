"use client";

import { useSocket } from "@/lib/useSocket";
import React, { useCallback, useEffect, useState } from "react";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";

const MessagePanel = () => {
  const socket = useSocket();

  const [isPanelOpen, setIsPanelOpen] = useState(false);
  const [message, setMessage] = useState("");
  const [chat, setChat] = useState<String[]>([]);
  const [unreadMessage, setUnreadMessage] = useState(false);

  const onMessage = useCallback((message: MessageEvent) => {
    const json_response = JSON.parse(message.data);
    if (json_response.header === "chat") {
      const messages: string[] = json_response.content
        .split(",")
        .map((message: string) => message.replace(/\[|\]/g, ""));
      setChat(messages);
      setUnreadMessage(true);
    }
  }, []);

  useEffect(() => {
    socket.addEventListener("message", onMessage);
    return () => {
      socket.removeEventListener("message", onMessage);
    };
  }, [socket, onMessage]);

  const togglePanel = () => {
    setIsPanelOpen(!isPanelOpen);
    setUnreadMessage(false);
  };

  const sendMessage = () => {
    console.log(message);
    socket.send(JSON.stringify({ header: "message", message: message }));
    setMessage("");
  };

  return (
    <div
      className={`absolute top-0 right-0 ${
        isPanelOpen ? "w-full" : ""
      } h-full flex items-center justify-end`}
    >
      <div
        className={`absolute flex flex-col h-screen w-1/4 bg-black/90 rounded-md shadow-5xl ${
          isPanelOpen ? "translate-x-0" : "translate-x-full hidden"
        }`}
      >
        <div>
          {/*Add messages here*/}
          {chat.map((message, index) => (
            <p key={index} className="text-white p-2 overflow-hidden">
              {message}
            </p>
          ))}
        </div>
        <div className="absolute bottom-0 w-[100%] px-3 py-3">
          <div className="flex items-center justify-center">
            <input
              className="w-4/5 mr-3 rounded-md px-1"
              placeholder="Message"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
            />
            <button className="text-center text-white" onClick={sendMessage}>
              Send
            </button>
          </div>
        </div>
      </div>
      <div
        className={`absolute text-center justify-center py-8 px-1 bg-gray-700/90 rounded-l-md shadow-lg ${
          isPanelOpen ? "right-1/4" : "right-0"
        }`}
        onClick={togglePanel}
      >
        {isPanelOpen ? (
          <FaChevronRight className="text-white" style={{ zoom: 2 }} />
        ) : (
          <FaChevronLeft className="text-white" style={{ zoom: 2 }} />
        )}
        {unreadMessage ? (
          <div className="absolute left-[-10px] top-[-10px] px-3 py-[0.125rem] rounded-full bg-red-500 text-white font-bold shadow-xl">
            !
          </div>
        ) : null}
      </div>
    </div>
  );
};

export default MessagePanel;
