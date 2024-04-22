"use client";

import { createContext, ReactNode } from "react";

const ws = new WebSocket("ws://localhost:8080/ws");

export const SocketContext = createContext(ws);

interface ISocketProvider {
  children: ReactNode;
}

export const SocketProvider = (props: ISocketProvider) => (
  <SocketContext.Provider value={ws}>{props.children}</SocketContext.Provider>
);
