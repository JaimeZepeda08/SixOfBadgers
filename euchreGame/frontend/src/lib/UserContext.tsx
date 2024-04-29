'use client'
import React, { createContext, useState } from 'react';
import {gameRecord, userType} from "@/lib/Types";

const initTestData: userType = {
    userName: 'guest',
    email: '',
    image: '',
};

type userContextType = {
    userData: userType;
    setUserData: (value: userType) => void;
    savedGames: gameRecord[];
    setSavedGames: (value: gameRecord[]) => void;
}

export const UserContext = createContext<userContextType>({
    userData: initTestData,
    setUserData: () => {
    },
    savedGames: [],
    setSavedGames: () => {
    },
});

export const UserProvider = ({ children }: any) => {
    const [userData, setUserData] = useState<userType>(initTestData);
    const [savedGames, setSavedGames] =
        useState<gameRecord[]>([]);

    return (
        <UserContext.Provider value={{ userData, setUserData, savedGames, setSavedGames}}>
            {children}
        </UserContext.Provider>
    );
};
